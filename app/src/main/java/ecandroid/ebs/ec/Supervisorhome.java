package ecandroid.ebs.ec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Supervisorhome extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    LstsupervisorAdapter lstadapter;
    Msgbox msgbox;
    ListView myList;
    View v;
    Globalvars globalvars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisorhome);
        msgbox = new Msgbox((Context)this);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        displayemployeelogs();

        CheckBox selectall = (CheckBox) findViewById(R.id.supervisorchkselecall);
        myList = (ListView) findViewById(R.id.lstviewsupervisor);
        Button btnapprove = (Button) findViewById(R.id.supervisorbtnapprove);
        Button btndisapprove = (Button) findViewById(R.id.supervisorbtndisapprove);
        globalvars = new Globalvars((Context)this,(Activity)this);

        selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked && myList.getVerticalScrollbarPosition() != myList.getCount()-1)
                    myList.smoothScrollToPosition(myList.getCount()-1);
                lstadapter.ischecking = true;
                lstadapter.checkAll_flag = isChecked;
                lstadapter.notifyDataSetChanged();
            }
        });
        btnapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval(globalvars.get("inchargeempid"),"Approved", lstadapter);
                displayemployeelogs();
            }
        });
        btndisapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval(globalvars.get("inchargeempid"),"Disapproved", lstadapter);
                displayemployeelogs();
            }
        });

    }
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
    public void displayemployeelogs()
    {
        globalvars = new Globalvars((Context)this,(Activity)this);
        ListView myList = (ListView) findViewById(R.id.lstviewsupervisor);
        final ArrayList<Lstsupervisormod> newmod = new ArrayList<>();
        String sqlquery = "";
        if(globalvars.get("usertype").equals("5"))
        {
            sqlquery = "select b.name," +
                    "(b.companyname||'/'||b.businessunit||'/'||b.deptname) as department," +
                    "b.eoc_date as eoc_date," +
                    "b.emp_id as emp_id," +
                    "b.credit_balance as cr_balance," +
                    "a.reqstatus " +
                    "from employeeitems a,employeemasterfile b " +
                    "where b.emp_id = a.emp_id " +
                    "group by a.emp_id";
        }
        else
        {
            sqlquery = "select b.name," +
                    "(b.companyname||'/'||b.businessunit||'/'||b.deptname) as department," +
                    "b.eoc_date as eoc_date," +
                    "b.emp_id as emp_id," +
                    "b.credit_balance as cr_balance," +
                    "a.reqstatus "+
                    "from employeeitems a,employeemasterfile b, levelingsubordinates c " +
                    "where b.emp_id = a.emp_id and trim(c.subordinates_rater) = a.emp_id and c.ratee = "+qttext(globalvars.get("inchargeempid"))+
                    " group by a.emp_id";
        }
        Cursor row = mydatabase.rawQuery(sqlquery,null);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        while(row.moveToNext())
        {
            String employeename=row.getString(0);
            String department=row.getString(1);
            String eoc_date=row.getString(2);
            String emp_id=row.getString(3);
            String cr_balance=row.getString(4);
            String reqstatus=row.getString(5);
            double totalamount=0.0;

            Cursor row2 = mydatabase.rawQuery("select (a.quantity * c.cost) as totalamount from employeeitems a, ecform c where a.itemid=c.id and a.emp_id="+qttext(emp_id),null);
            while(row2.moveToNext())
            {
                totalamount += Double.parseDouble(row2.getString(0));
            }
            Lstsupervisormod newrow = new Lstsupervisormod(emp_id, employeename, department, eoc_date, Double.toString(totalamount), cr_balance, reqstatus);
            newmod.add(newrow);
        }


        lstadapter = new LstsupervisorAdapter(this, R.layout.lstsupervisortransactionlists, newmod);
        myList.setAdapter(lstadapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final TextView emp_name = (TextView) view.findViewById(R.id.txtviewscanemployee3logsemployeename);
                final String emp_tag[] = emp_name.getTag().toString().split("\\|");
            }
        });






        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView emp_name = (TextView) view.findViewById(R.id.txtviewscanemployee3logsemployeename);
            final String emp_tag[] = emp_name.getTag().toString().split("\\|");

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(Supervisorhome.this);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Supervisorhome.this, android.R.layout.simple_list_item_1);
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
                    }
                    else if(strOption.equalsIgnoreCase("View Supervisor"))
                    {
                    }
                    else if(strOption.equalsIgnoreCase("Delete Transaction"))
                    {
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

    private void confirmView()
    {
        msgbox.showyesno("", "Are you sure you want to view this transaction?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                msgtoaster("Transaction successfully viewed.");
                displayemployeelogs();
            }

            @Override
            public void onno() {
            }
        });
    }


    public void approval(String approveby,String status, LstsupervisorAdapter lst)
    {
        int chkcount = 0;
        if (lst.getCount() > 0)
        {
            for (int i = 0; i < lst.getCount(); i++)
            {
                Lstsupervisormod v = lst.getItem(i);

                if (v.isSelected())
                {
                    String emp_id = v.getEmp_id();
                    mydatabase.execSQL("UPDATE employeeitems SET approvedby=" + qttext(approveby) + ",reqstatus=" + qttext(status) + " WHERE emp_id=" + qttext(emp_id));
                    //mydatabase.execSQL("UPDATE employeeitems SET approvedby=" + qttext(approveby) + ",reqstatus=" + qttext("Initial Approved") + " WHERE company = '020303' AND emp_id=" + qttext(emp_id));
                    //mydatabase.execSQL("UPDATE employeeitems_backup SET eb_approvedby=" + qttext(approveby) + ",eb_reqstatus=" + qttext(status) + " WHERE eb_emp_id=" + qttext(emp_id));
                    chkcount++;
                }
            }
        }
        if(chkcount==0)
        {
            msgtoaster("Nothing is selected.");
        }
    }

    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }

    public void msgtoaster(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
        toast.show();
    };
}
