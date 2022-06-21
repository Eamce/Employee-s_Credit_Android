package ecandroid.ebs.ec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminHome extends AppCompatActivity {
    ListView listView_emplist;
    SQLiteDatabase mydatabase;
    ArrayList<BackupEmployees> itemlist;
    BackupEmployeesAdapter adapter;
    Msgbox msgbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        listView_emplist = (ListView) findViewById(R.id.lv_emplist);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        msgbox = new Msgbox(this);
        displaydateuploaded();
    }

    public void displaydateuploaded(){
        itemlist = new ArrayList<>();
        Cursor resultSet = mydatabase.rawQuery("SELECT eb_requestdate, sum(eb_quantity * eb_amount) total, count(DISTINCT(eb_emp_id)) total_emp FROM employeeitems_bak GROUP BY eb_requestdate",null);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);

        if(resultSet != null)
        {
            while (resultSet.moveToNext()) {
                    String dateuploaded = resultSet.getString(0);
                    String total = resultSet.getString(1);
                    String total_emp = resultSet.getString(2);

                    BackupEmployees itemrow = new BackupEmployees(dateuploaded,total,total_emp);
                    itemlist.add(itemrow);
                }
            }
            resultSet.close();

            adapter = new BackupEmployeesAdapter(this, R.layout.view_backupemployeescredit_row, itemlist);
            listView_emplist.setAdapter(adapter);
            listView_emplist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //Mo trigger if i long press ang listview....
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final TextView tv_uploadeddate = (TextView) view.findViewById(R.id.tv_uploadeddate);
                    final String str_uploadeddate = tv_uploadeddate.getText().toString();
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(AdminHome.this);

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdminHome.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("View Batch");
                    arrayAdapter.add("Retrieve Batch");
                    //arrayAdapter.add("Delete Transaction");

                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strOption = arrayAdapter.getItem(which);
                            if(strOption.equalsIgnoreCase("View Batch"))
                            {
                                //confirmViewBatch(str_uploadeddate);
                            }
                            else if(strOption.equalsIgnoreCase("Retrieve Batch"))
                            {
                                confirmRetrieveBatch(str_uploadeddate);
                            }
//                            else if(strOption.equalsIgnoreCase("Delete Transaction"))
//                            {
                                //confirmRemove(emp_tag[1], emp_name.getText().toString());
//                            }
                            else
                            {

                            }
                        }
                    });
                    builderSingle.show();
                    return false;
                }
            });
    }

    private void confirmRetrieveBatch(String uploadeddate)
    {
        Cursor row2 = mydatabase.rawQuery("SELECT * FROM employeeitems_bak e WHERE e.eb_requestdate = " +qttext(uploadeddate), null);
        while (row2.moveToNext())
        {
            String bak_id = row2.getString(0);
            String eb_emp_id = row2.getString(1);
            String eb_itemid = row2.getString(2);
            String eb_item_desc = row2.getString(3);
            String eb_quantity = row2.getString(4);
            String eb_amount = row2.getString(5);
            String eb_company = row2.getString(6);
            String eb_requestgroup = row2.getString(7);
            String eb_approvedby = row2.getString(8);
            String eb_reqstatus = row2.getString(9);
            String eb_requestdate = row2.getString(10);

            mydatabase.execSQL("INSERT INTO employeeitems(id,emp_id,itemid,item_desc,quantity,amount,company,requestgroup,approvedby,reqstatus,requestdate) VALUES(" + qttext(bak_id) + "," + qttext(eb_emp_id) + "," + qttext(eb_itemid) + "," + qttext(eb_item_desc) + "," + qttext(eb_quantity) + "," + qttext(eb_amount) + "," + qttext(eb_company) + "," + qttext(eb_requestgroup) + "," + qttext(eb_approvedby) + "," + qttext(eb_reqstatus) + ", strftime('%Y-%m-%d %H:%M:%S','now'))");
        }

    }

    private void confirmViewBatch(String uploadeddate)
    {
        final String str_emp_id = uploadeddate;
        final String[] arr_items = new String[1];

        msgbox.showyesno("Confirmation", "Do you want to view "+str_emp_id+"?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                Cursor row2 = mydatabase.rawQuery("SELECT e.itemid, e.item_desc, e.amount, e.quantity, (e.amount * e.quantity) as total_amount, ec.store FROM employeeitems e INNER JOIN ecform ec ON e.itemid = ec.id WHERE e.emp_id = " +qttext(str_emp_id), null);
                arr_items[0] = "";

                while (row2.moveToNext())
                {
                    arr_items[0] += "Itemcode: "+ row2.getString(0) + "\n" + "Desc: "+ row2.getString(1) + "\n" + "Unit Cost: "+ row2.getString(2) + "\n" + "Quantity: "+ row2.getString(3) + "\n" + "Total: "+ row2.getString(4) + "\n" + "B.U. : "+ row2.getString(5) + "\n\n";
                }

                msgbox.showyes("Item(s)", arr_items[0]);
                msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
                    @Override
                    public void onyes(){

                    }
                    @Override
                    public void onno() {
                    }
                });
            }

            @Override
            public void onno() {
            }
        });
    }
    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }
}
