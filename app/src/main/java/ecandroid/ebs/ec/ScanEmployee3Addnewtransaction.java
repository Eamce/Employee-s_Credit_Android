package ecandroid.ebs.ec;

import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ScanEmployee3Addnewtransaction extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    CustomItemsList adapter;
    Msgbox msgbox;
    int itemcount;
    double totalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanemployee3addnewtransaction);
        Button addnewitem = (Button) findViewById(R.id.btnaddnewtransactionadditem); //Add Item button...
        Button transactioncancel = (Button) findViewById(R.id.btnaddnewtransactioncancel); //Cancel button...
        Button done = (Button) findViewById(R.id.btnaddnewtransactiondone); //Done button...
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        msgbox = new Msgbox(this);
        itemcount = 0;

        addnewitem.setOnClickListener(new View.OnClickListener() {   //Trigger after clicking Add Item button...
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ScanEmployee3Addnewtransaction.this,Addtocartitems.class));
            }
        });
        transactioncancel.setOnClickListener(new View.OnClickListener() {     //Trigger after clicking Cancel button...
            @Override
            public void onClick(View v)
            {
                if(itemcount > 0 )
                {
                    canceltransaction();    //Calling canceltransaction() function...
                } else
                {
                    finish();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {        //Trigger after clicking Done button...
            @Override
            public void onClick(View v) {
                if(itemcount > 0 )
                {
                    if (checkquantity())
                    {
                        Intent doneIntent = new Intent(ScanEmployee3Addnewtransaction.this, ScanEmployee3Details.class);
                        doneIntent.putExtra("totalamount", String.valueOf(totalamount));
                        startActivity(doneIntent);
                    }
                    else
                    {
                        msgtoaster("ERROR: Item/s has 0 quantity.");
                    }
                }
                else
                {
                    msgtoaster("No item/s added yet.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        removePreviousDayOrder();    //Delete all orders if not equal 'now' and emp_id = "walapa"...
        displayemployeeitems();
    }

    public void canceltransaction(){
        final String msg = "This will remove the added items. Continue?";
        msgbox.showyesno("Cancel Transaction",msg);
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                mydatabase.execSQL("DELETE FROM employeeitems WHERE emp_id="+qttext("walapa"));
                //mydatabase.execSQL("DELETE FROM employeeitems_backup WHERE eb_emp_id="+qttext("walapa"));
                finish();
            }

            @Override
            public void onno() {
            }
        });
    }

    public void displayemployeeitems(){
        totalamount=0.0;
        ListView lstviewddeditems = (ListView) findViewById(R.id.lstviewaddnewtransactionaddeditems);
        lstviewddeditems.setFocusable(false);
        Cursor row = mydatabase.rawQuery("select ecform.description,employeeitems.quantity,ecform.cost,ecform.unit,employeeitems.id from employeeitems,ecform where employeeitems.itemid=ecform.id and employeeitems.emp_id="+qttext("walapa"),null);
        ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
        itemcount = row.getCount();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        while(row.moveToNext()){
            String itemdescription = row.getString(0);
            String theqty = row.getString(1);
            String price = row.getString(2);
            String unit = row.getString(3);
            String employeeitemsid = row.getString(4);
            double myNumber = (Double.parseDouble(theqty) * Double.parseDouble(price));

            String amount =  df.format(myNumber);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("lsttitle", itemdescription);
            map.put("lstsubtitle", "Php "+price+" / "+unit);
            map.put("qty", theqty);
            map.put("employeeitemsid",employeeitemsid);
            map.put("amount",amount);
            detailss.add(map);
            totalamount += myNumber;
        }

        TextView txtamount = (TextView) findViewById(R.id.txtviewscanemployeeitemscredittotalamount);
        txtamount.setText("Php " + df.format(totalamount)+"");

        int index = lstviewddeditems.getFirstVisiblePosition();
        View v = lstviewddeditems.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lstviewddeditems.getPaddingTop());
        //if(v == null)
        //{
        //top = 0;
        //}
        //else
        //{
        //top =  v.getTop() - lstviewddeditems.getPaddingTop()
        //}

        adapter = new CustomItemsList(this, detailss);
        lstviewddeditems.setAdapter(adapter);
        lstviewddeditems.setSelectionFromTop(index, top);

        lstviewddeditems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {    //Trigger if long press the listview...
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                TextView txTitle = (TextView) view.findViewById(R.id.txtviewscanemployeeaddedtitle);
                Button itemid = (Button) view.findViewById(R.id.btnscanemployeelistrowsetquantity);
                confirmRemove(itemid.getTag().toString(), txTitle.getText().toString());      //Remove then specific item in the listview...
                return false;
            }
        });
        registerForContextMenu(lstviewddeditems);

    }

    private void removePreviousDayOrder()
    {
        mydatabase.execSQL("DELETE FROM employeeitems WHERE strftime('%Y-%m-%d',requestdate) != strftime('%Y-%m-%d','now') AND emp_id="+qttext("walapa"));
        //mydatabase.execSQL("DELETE FROM employeeitems_backup WHERE strftime('%Y-%m-%d',eb_requestdate) != strftime('%Y-%m-%d','now') AND eb_emp_id="+qttext("walapa"));
    }

    private void confirmRemove(String id, String name){
        final String fid = id;
        final String fname = name;
        msgbox.showyesno(name, "Remove this item?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                mydatabase.execSQL("DELETE FROM employeeitems WHERE id="+fid);
                //mydatabase.execSQL("DELETE FROM employeeitems_backup WHERE id="+fid);
                msgtoaster(fname.substring(0,1).toUpperCase() + fname.substring(1).toLowerCase() +" successfully removed.");
                displayemployeeitems();
            }

            @Override
            public void onno() {
            }
        });
    }

    private String m_Text = "";
    public void myClickHandler(View v)     //Trigger if click qty button...
    {
        final Button n = (Button) v;
        final String req_id = n.getTag().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Quantity");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // | InputType.TYPE_NUMBER_FLAG_DECIMAL remove whole number only for quantity
        input.setText(n.getText().toString());
        input.selectAll();
        builder.setView(input);              //Setting EditText to AlertDialog...
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
            m_Text = input.getText().toString();
            Double maxQ = getMaxQuantity(req_id) + Double.parseDouble(n.getText().toString());
            if(maxQ >= Double.parseDouble(m_Text)) {
                mydatabase.execSQL("UPDATE employeeitems set quantity=" + m_Text + " where id=" + req_id);
                //mydatabase.execSQL("UPDATE employeeitems_backup set eb_quantity=" + m_Text + " where id=" + req_id);
            } else {
                msgtoaster(maxQ + " stock/s available.");
            }
            displayemployeeitems();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        builder.show();
    }

    public double getMaxQuantity(String id){
        Cursor resultSet = mydatabase.rawQuery("SELECT a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining, a.id " +
                "FROM ecform a " +
                "LEFT JOIN employeeitems b ON a.id = b.itemid WHERE b.itemid=(SELECT itemid FROM employeeitems WHERE id = "+id+") GROUP BY a.id",null);
        resultSet.moveToFirst();
        return Double.parseDouble(resultSet.getString(0));
    }
    public boolean checkquantity(){
        Cursor row = mydatabase.rawQuery("SELECT quantity FROM employeeitems WHERE quantity="+qttext("0")+" and emp_id="+qttext("walapa"),null);
        return row.getCount() == 0;
    }
        public void msgtoaster(String msg) {
            Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
            toast.show();
        };
    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }
}
