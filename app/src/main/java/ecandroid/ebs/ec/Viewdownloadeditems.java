package ecandroid.ebs.ec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Viewdownloadeditems extends AppCompatActivity {

    private static final String TAG = "Viewdownloadeditems";
    SQLiteDatabase mydatabase;
    Context mContext = this;
//    Downloadeditems itemtoadjust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdownloadeditems);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        Log.d(TAG, "onCreate: Started.");
        ListView mListView = (ListView) findViewById(R.id.lstviewitems);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);

//        Bundle b = getIntent().getExtras();
//        int addcart = -1;
//        if(b != null)
//            addcart = b.getInt("addtocart");

        final ArrayList<Downloadeditems> itemlist = new ArrayList<>();
        Cursor resultSet = mydatabase.rawQuery("SELECT a.code,a.id,a.description, a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining,a.unit,a.cost,a.store,a.expirydate,a.remarks " +
                "FROM ecform a " +
                "LEFT JOIN employeeitems b ON a.id = b.itemid AND b.reqstatus != 'Disapproved' WHERE date(a.enddate) >= date('now') GROUP BY a.id ORDER BY a.description ASC",null);

        if(resultSet != null)
        {
            while (resultSet.moveToNext()) {
                if(!resultSet.getString(3).equalsIgnoreCase("0"))
                    {
                        String itemcode = resultSet.getString(0);
                        String itemid = resultSet.getString(1);
                        String itemdescription = resultSet.getString(2).trim();
                        String itemqty = resultSet.getString(3);
                        String itemunit = resultSet.getString(4);
                        String itemcost = df.format(Double.parseDouble(resultSet.getString(5)));
                        String itemstore = resultSet.getString(6);
                        String[] expdate = resultSet.getString(7).split(" ");
                        String remarks = resultSet.getString(8);
                        File ecpath = mContext.getApplicationContext().getDir("ecimages", Context.MODE_PRIVATE);
                        File theimg = new File(ecpath, itemid + ".png");
                        Downloadeditems itemrow = new Downloadeditems(itemid, itemdescription, getString(R.string.item_details, itemcode, itemqty, itemunit, expdate[0], itemstore, itemcost, remarks), itemqty,
                                "file://" + theimg.toString());
                        itemlist.add(itemrow);
                }
            }
            resultSet.close();
            DownloadedListAdapter adapter = new DownloadedListAdapter(this, R.layout.view_downloaded_row, itemlist);
            adapter.viewimgclicklistener(new DownloadedListAdapter.listener() {
                @Override
                public void onimgclick(String id) {
                    Intent newintent = new Intent(getBaseContext(), ImagePreview.class);
                    newintent.putExtra("id", id);
                    startActivity(newintent);
                }
            });
            mListView.setAdapter(adapter);
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    adjustOnLongClick(itemlist.get(position).getItemid(), itemlist.get(position).getItemqty());
                    return false;
                }
            });
        }
    }

    private void adjustOnLongClick(String selectedid, String qty){
        final String id = selectedid;
        final Integer max = Integer.parseInt(qty);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(qty);
        input.selectAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Input Item Adjustment")
                .setView(input)
                .setPositiveButton("OK", null)
                .setNegativeButton("CANCEL", null)
                .setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do stuff, possibly set wantToCloseDialog to true then...
                Long tobeadj = Long.parseLong(input.getText().toString());
                if(max>=tobeadj && tobeadj != 0){
                    Cursor prevadj = mydatabase.rawQuery("SELECT COALESCE(adjqty, 0) FROM ecform WHERE id=" + qttext(id), null);
                    if(prevadj.getCount() == 1) {
                        prevadj.moveToNext();
                        tobeadj = Long.parseLong(prevadj.getString(0)) + tobeadj;
                        mydatabase.execSQL("UPDATE ecform SET adjqty=" + qttext(tobeadj) + " WHERE id=" + qttext(id));
                        msgtoaster("Adjustment Saved.");
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                    prevadj.close();
                } else if(max == 0){
                    msgtoaster("Remaining quantity is already 0.");
                } else if(tobeadj == 0){
                    msgtoaster("Invalid adjustment quantity.");
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                else {
                    msgtoaster("Maximum quantity is " + max + ".");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }
    public void msgtoaster(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
        toast.show();
    }

}
