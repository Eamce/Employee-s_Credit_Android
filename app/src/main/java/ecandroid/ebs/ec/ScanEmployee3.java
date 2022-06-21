package ecandroid.ebs.ec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ScanEmployee3 extends AppCompatActivity {
    Lstscanemployee3viewlogs adapter;   //Lstscanemployee3viewlogs.java
    SQLiteDatabase mydatabase;   //SQLiteDatabase.java
    Globalvars globalvars;    //Globalvars.java
    Msgbox msgbox;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_employee3);
        globalvars = new Globalvars((Context)this,(Activity)this);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        Button addnewtransaction = (Button) findViewById(R.id.btnscanemployee3addnewtransaction);
        LinearLayout layoutlegend = (LinearLayout) findViewById(R.id.llayoutlegend);
        msgbox = new Msgbox(context);

        addnewtransaction.setOnClickListener(new View.OnClickListener() {   //Invoked when New Transaction is clicked...
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanEmployee3.this,ScanEmployee3Addnewtransaction.class));
            }
        });

        if(globalvars.get("usertype").equals("6")) {   //Hide the Legend above the ListView...
            //Peanut Kisses...
            if(globalvars.get("company_code").equalsIgnoreCase("09") && globalvars.get("bunit_code").equalsIgnoreCase("01")){
                layoutlegend.setVisibility(View.VISIBLE);
            }
            //Noodles Factory...
            else if(globalvars.get("company_code").equalsIgnoreCase("03") && globalvars.get("bunit_code").equalsIgnoreCase("32")){
                layoutlegend.setVisibility(View.VISIBLE);
            }
            else{
                layoutlegend.setVisibility(View.GONE);
            }
        }

    }
    protected void onResume(){
        super.onResume();
        //For Red Ribbon only...
        if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03") ||
                globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04") ||
                globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05")) {
            displayemployeelogs_rb();
        }else{
            displayemployeelogs();
        }
    }

    public void displayemployeelogs_rb()
    {
        ListView lstlogs = (ListView) findViewById(R.id.lstviewscanemployee3logs);
        ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
        Cursor row = mydatabase.rawQuery("select b.name,(b.businessunit||' \n'||b.deptname) as department,b.emp_id as emp_id, a.approvedby as approvedby, a.reqstatus, b.credit_limit from employeeitems a,employeemasterfile b where b.emp_id = a.emp_id group by a.emp_id", null);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        DecimalFormat df1 = new DecimalFormat("#,##0.00");
        df1.setMaximumFractionDigits(0);
        while (row.moveToNext())
        {
            String employeename = row.getString(0);
            String department = row.getString(1);
            String emp_id = row.getString(2);
            String approvedby = row.getString(3);
            String reqstatus = row.getString(4);
            double totalamount = 0.0;
            double discount_amount = 0.0;
            double discount_percentage = 0.0;
            double crlimit = 0.0;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("employeename", employeename);
            map.put("department", department);
            Cursor row2 = mydatabase.rawQuery("select ((a.quantity * a.amount) - (a.amount_discount * a.quantity)) as totalamount, a.discount_percentage as a_discount_percentage from employeeitems a where a.emp_id=" + qttext(emp_id), null);
            while (row2.moveToNext())
            {
                totalamount += Double.parseDouble(row2.getString(0));
                discount_percentage = Double.parseDouble(row2.getString(1)) * 100.0;
            }
            crlimit = Double.parseDouble(row.getString(5)) - totalamount;
            map.put("totalamount", "Php " + df.format(totalamount));
            //map.put("totalamount", "Php " + df.format(totalamount) + "(-" + df1.format(discount_percentage) + "%)");
            map.put("status", reqstatus);
            map.put("approvedby", approvedby);
            map.put("emp_id", emp_id);
            map.put("limit", "Php " +  df.format(crlimit) + "");
            detailss.add(map);
        }

        int index = lstlogs.getFirstVisiblePosition();  //Returns the position within the adapter's data set for the first item displayed on screen...
        View v = lstlogs.getChildAt(0);  //Returns the view at the specified position in the group...
        int top = (v == null) ? 0 : (v.getTop() - lstlogs.getPaddingTop());

        adapter = new Lstscanemployee3viewlogs(this,detailss);
        lstlogs.setAdapter(adapter);
        lstlogs.setSelectionFromTop(index, top);

        lstlogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //Mo trigger if i long press ang listview....
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView emp_name = (TextView) view.findViewById(R.id.txtviewscanemployee3logsemployeename);
                final String emp_tag[] = emp_name.getTag().toString().split("\\|");

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ScanEmployee3.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScanEmployee3.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("View Items");
                arrayAdapter.add("View Supervisor");
                arrayAdapter.add("Delete Transaction");

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
                        if(strOption.equalsIgnoreCase("View Items"))
                        {
                            confirmViewItems(emp_tag[1],emp_name.getText().toString());
                        }
                        else if(strOption.equalsIgnoreCase("View Supervisor"))
                        {
                            confirmViewLeveling(emp_tag[1],emp_name.getText().toString());
                        }
                        else if(strOption.equalsIgnoreCase("Delete Transaction"))
                        {
                            confirmRemove(emp_tag[1], emp_name.getText().toString());
                        }
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

    public void displayemployeelogs()
    {
        ListView lstlogs = (ListView) findViewById(R.id.lstviewscanemployee3logs);
        ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
        Cursor row = mydatabase.rawQuery("select b.name,(b.businessunit||' \n'||b.deptname) as department,b.emp_id as emp_id, a.approvedby as approvedby, a.reqstatus, b.credit_limit from employeeitems a,employeemasterfile b where b.emp_id = a.emp_id group by a.emp_id", null);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        while (row.moveToNext())
        {
            String employeename = row.getString(0);
            String department = row.getString(1);
            String emp_id = row.getString(2);
            String approvedby = row.getString(3);
            String reqstatus = row.getString(4);
            double totalamount = 0.0;
            double crlimit = 0.0;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("employeename", employeename);
            map.put("department", department);
                Cursor row2 = mydatabase.rawQuery("select (a.quantity * a.amount) as totalamount from employeeitems a where a.emp_id=" + qttext(emp_id), null);
                while (row2.moveToNext())
                {
                    totalamount += Double.parseDouble(row2.getString(0));
                }
            crlimit = Double.parseDouble(row.getString(5)) - totalamount;
            map.put("totalamount", "Php " + df.format(totalamount) + "");
            map.put("status", reqstatus);
            map.put("approvedby", approvedby);
            map.put("emp_id", emp_id);
            map.put("limit", "Php " +  df.format(crlimit) + "");
            detailss.add(map);
        }

        int index = lstlogs.getFirstVisiblePosition();  //Returns the position within the adapter's data set for the first item displayed on screen...
        View v = lstlogs.getChildAt(0);  //Returns the view at the specified position in the group...
        int top = (v == null) ? 0 : (v.getTop() - lstlogs.getPaddingTop());
        //if(v == null)
        // {
            //top = 0;
        // }/
        //else
        //{
            //(v.getTop() - lstlogs.getPaddingTop())
        // }
        //adapter = new Lstscanemployee3viewlogs(this,detailss);

        adapter = new Lstscanemployee3viewlogs(this,detailss);
        lstlogs.setAdapter(adapter);
        lstlogs.setSelectionFromTop(index, top);

        lstlogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //Mo trigger if i long press ang listview....
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView emp_name = (TextView) view.findViewById(R.id.txtviewscanemployee3logsemployeename);
                final String emp_tag[] = emp_name.getTag().toString().split("\\|");

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ScanEmployee3.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScanEmployee3.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("View Items");
                arrayAdapter.add("View Supervisor");
                arrayAdapter.add("Delete Transaction");

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
                        if(strOption.equalsIgnoreCase("View Items"))
                        {
                            confirmViewItems(emp_tag[1],emp_name.getText().toString());
                        }
                        else if(strOption.equalsIgnoreCase("View Supervisor"))
                        {
                            confirmViewLeveling(emp_tag[1],emp_name.getText().toString());
                        }
                        else if(strOption.equalsIgnoreCase("Delete Transaction"))
                        {
                            confirmRemove(emp_tag[1], emp_name.getText().toString());
                        }
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

    private void confirmViewItems(String id, String name)
    {
        final String emp_id = id;
        final String fullname = name;
        final String[] arr_items = new String[1];

        msgbox.showyesno("Confirmation", "Do you want to view "+fullname+ "`s item(s)?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                Cursor row2 = mydatabase.rawQuery("SELECT e.itemid, e.item_desc, e.amount, e.quantity, (e.amount * e.quantity) as total_amount, ec.store FROM employeeitems e INNER JOIN ecform ec ON e.itemid = ec.id WHERE e.emp_id = " +qttext(emp_id), null);
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

    private void confirmViewLeveling(String id, String name)
    {
        final String emp_id = id;
        final String fullname = name;
        final String[] supervisor = new String[1];
        msgbox.showyesno("Confirmation", "Do you want to view "+fullname+ "`s supervisor?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                Cursor row2 = mydatabase.rawQuery("SELECT name FROM employeemasterfile as emp INNER JOIN levelingsubordinates as sub ON emp.emp_id = sub.ratee WHERE sub.subordinates_rater =" + qttext(emp_id), null);
                supervisor[0] = "";
                while (row2.moveToNext())
                {
                    supervisor[0] += row2.getString(0) + "\n";
                }
//                msgtoaster(supervisor[0]);
                msgbox.showyes("Supervisor(s)", supervisor[0]);
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

    private void confirmRemove(String id, String name)
    {
        final String fid = id;
        final String fname = name;
        msgbox.showyesno(name, "Remove this transaction?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                mydatabase.execSQL("DELETE FROM employeeitems WHERE emp_id="+qttext(fid));
                //mydatabase.execSQL("DELETE FROM employeeitems_backup WHERE eb_emp_id="+qttext(fid));
                msgtoaster(fname.toUpperCase() +"'s transaction successfully removed.");
                displayemployeelogs();
            }

            @Override
            public void onno() {
            }
        });
    }

    public void msgtoaster(String msg)
    {
        Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
        toast.show();
    };

    public String qttext(Object txt)
    {
        return '"' + txt.toString() + '"';
    }
}
