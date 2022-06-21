package ecandroid.ebs.ec;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Appendtotextfiles extends AppCompatActivity {
    Typeface font;
    Ajax mo;
    ProgressBar loadingbar;
    TextView txtviewstatus;
    Msgbox msgbox;
    Context context = this;
    SQLiteDatabase mydatabase;
    Globalvars globalvars;
    EditText txtinchargeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appendtotextfiles);

        globalvars = new Globalvars(this,this);

        msgbox = new Msgbox(context);

        font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        loadingbar = (ProgressBar) findViewById(R.id.loadingbar);
        txtviewstatus = (TextView) findViewById(R.id.txtviewstatus);
        final Button btncheckconnection = (Button) findViewById(R.id.btncheckconnection);
        final Button btnstartappend = (Button) findViewById(R.id.btnstartppend);

        btncheckconnection.setOnClickListener(new View.OnClickListener() {     //Trigger if check connection
            @Override
            public void onClick(View v) {
                loadingbar.setVisibility(View.VISIBLE);
                txtviewstatus.setText("Checking server connection. Please wait...");
                checkconnection();    //Calling checkconnection() function...
            }
        });

        btnstartappend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdummy();
            }
        });
        setfontawesome(R.id.txtviewdownloaddata,"\uf0ee");
        checkconnection();
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
    }
    public void checkconnection(){
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener(){
            @Override
            public void onerror(){
                new android.os.Handler().postDelayed(
                        new Runnable(){
                            public void run(){
                                loadingbar.setVisibility(View.INVISIBLE);
                                txtviewstatus.setText("Unable to connect to ebm server. Please make sure you are connected to a wifi.");
                            }
                        },1000
                );
            }
            @Override
            public void onsuccess(final String data) {
                new android.os.Handler().postDelayed(
                        new Runnable(){
                            @Override
                            public void run(){
                                loadingbar.setVisibility(View.INVISIBLE);
                                txtviewstatus.setText("Connection established. Ready to append.");
                            }
                        },1000
                );
            }
        });
        mo.execute(this.getString(R.string.serveraddress) + "android_checkconnection.php");
    }
    public void getdummy()
    {
        final String inchargedummy;

        String str_inchargeempid = globalvars.get("inchargeempid");
        Cursor dummyid = mydatabase.rawQuery("SELECT idfront,idback,inchargeemp FROM inchargeid WHERE inchargeemp="+qttext(globalvars.get("inchargeempid")),null);
        if(dummyid.getCount() == 1)
        {
            dummyid.moveToFirst();
            inchargedummy = dummyid.getString(0);
        }
        else
        {
            inchargedummy = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please scan your Incharge ID");
        txtinchargeid = new EditText(this);
        txtinchargeid.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(txtinchargeid);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //msgbox.show("asdf",input.getText().toString().substring(0,2));
                String a = inchargedummy;
                if(txtinchargeid.getText().toString().equals(inchargedummy))
                {
                    if((globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03")) || (globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04")) || (globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))){
                        initiateappend_rr();
                    }else{
                        initiateappend();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid Incharge ID",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void initiateappend_rr()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...Please Wait");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        Ajax ajax = new Ajax();
        ajax.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror(){
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
                mydatabase.execSQL("DELETE FROM ecform WHERE id!=0");
                mydatabase.execSQL("DELETE FROM employeeitems WHERE id!=0");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mketoday = new SimpleDateFormat("MM/dd/yyyy");
                logUpload(mketoday.format(calendar.getTime()));
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"Data successfully uploaded",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
        });

        Cursor rowadj = mydatabase.rawQuery("SELECT id, adjqty FROM ecform",null);
        JSONArray adjarr = new JSONArray();

        while(rowadj.moveToNext())
        {
            String id, adjqty;
            JSONObject adjthejs = new JSONObject();
            id = rowadj.getString(0);
            adjqty = rowadj.getString(1);
            try
            {
                adjthejs.put("id",id);
                adjthejs.put("adjqty",adjqty);
                adjarr.put(adjthejs);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        Cursor row = mydatabase.rawQuery("SELECT a.emp_id,a.itemid,SUM(a.quantity),a.approvedby,a.reqstatus,a.requestdate, a.amount,SUM((a.quantity * a.amount) - (a.amount_discount * a.quantity)) as totalamount, b.code, c.name, b.unit, a.company, a.item_desc, a.requestgroup, a.discount_percentage, a.discount_amt, b.discountstatus\n" +
                "FROM employeeitems a \n" +
                "INNER JOIN ecform b ON a.itemid = b.id\n" +
                "INNER JOIN employeemasterfile c ON c.emp_id = a.emp_id\n" +
                "WHERE  a.emp_id != 'walapa' GROUP BY a.emp_id,a.itemid ORDER BY a.emp_id ASC",null);
        JSONArray arr_credits = new JSONArray();

        while(row.moveToNext())
        {
            String discount_amt,discount_percentage,employeeid,itemid,quantity,inchargeempid,approvedby,reqstatus,requestdate,amount,totaltocredit,itemcode,name,unit,requestgroup;
            JSONObject thejs = new JSONObject();
            employeeid = row.getString(0);
            itemid = row.getString(1);
            quantity = row.getString(2);
            inchargeempid = globalvars.get("inchargeempid");
            approvedby=row.getString(3);
            reqstatus=row.getString(4);
            requestdate=row.getString(5);
            amount=row.getString(6);
            totaltocredit=row.getString(7);
            itemcode=row.getString(8);
            name=row.getString(9);
            unit=row.getString(10);
            requestgroup=row.getString(13);
            if(row.getString(16).equalsIgnoreCase("No Discount"))
            {
                discount_percentage="0";
                discount_amt="0";
            }
            else
            {
                discount_percentage=row.getString(14);
                discount_amt=row.getString(15);
            }

            try
            {
                if(approvedby == null ){
                    approvedby = "none";
                }

                mydatabase.execSQL("INSERT INTO employeeitems_bak(eb_emp_id,eb_itemid,eb_item_desc,eb_quantity,eb_amount,eb_company,eb_requestgroup,eb_approvedby,eb_reqstatus,eb_requestdate) VALUES(" + qttext(row.getString(0)) + "," + qttext(row.getString(1)) + "," + qttext(row.getString(12)) + "," + qttext(row.getString(2)) + "," + qttext(row.getString(6)) + "," + qttext(row.getString(11)) + "," + qttext(requestgroup) + "," + qttext(approvedby) + "," + qttext(reqstatus) +",strftime('%Y-%m-%d %H:%M:%S','now'))");

                thejs.put("emp_id",employeeid);
                thejs.put("itemid",itemid);
                thejs.put("quantity",quantity);
                thejs.put("inchargeempid",inchargeempid);
                thejs.put("approvedby",approvedby);
                thejs.put("reqstatus",reqstatus);
                thejs.put("requestdate",requestdate);
                thejs.put("amount",amount);
                thejs.put("inchargeid",txtinchargeid.getText().toString());
                thejs.put("totaltocredit",totaltocredit);
                thejs.put("itemcode",itemcode);
                thejs.put("name",name);
                thejs.put("unit",unit);
                thejs.put("discount_percentage",discount_percentage);
                thejs.put("discount_amt",discount_amt);
                arr_credits.put(thejs);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        ajax.adddata("inchargeempid",globalvars.get("inchargeempid"));
        ajax.adddata("adjustment",adjarr.toString());
        ajax.adddata("credits",arr_credits.toString());
            if(arr_credits.length() == 0){
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"No Data to be uploaded...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }else{
                ajax.execute(this.getString(R.string.serveraddress) + "android_uploadcredit_rr.php");
            }
    }

    public void initiateappend()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...Please Wait");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        Ajax ajax = new Ajax();
        ajax.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror(){
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
//                String a = globalvars.get("downloaded_length");
//                Cursor row = mydatabase.rawQuery("SELECT * FROM ecform",null);
//                String b = String.valueOf(row.getCount());
//                if(a == b)
//                {}
                mydatabase.execSQL("DELETE FROM ecform WHERE id!=0");
                mydatabase.execSQL("DELETE FROM employeeitems WHERE id!=0");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mketoday = new SimpleDateFormat("MM/dd/yyyy");
                logUpload(mketoday.format(calendar.getTime()));
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"Data successfully uploaded",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
        });

//        Cursor rowadj = mydatabase.rawQuery("SELECT id, adjqty FROM ecform",null);
        //        JSONArray adjarr = new JSONArray();
//
//        while(rowadj.moveToNext())
//        {
//            String id, adjqty;
//            JSONObject adjthejs = new JSONObject();
//            id = rowadj.getString(0);
//            adjqty = rowadj.getString(1);
//            try
//            {
//                adjthejs.put("id",id);
//                adjthejs.put("adjqty",adjqty);
//                adjarr.put(adjthejs);
//            }
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }
        Cursor row = mydatabase.rawQuery("SELECT a.emp_id,a.itemid,SUM(a.quantity),a.approvedby,a.reqstatus,a.requestdate, a.amount,SUM(a.amount * a.quantity) amount, b.code, c.name, b.unit, a.company, a.item_desc, a.requestgroup\n" +
                "FROM employeeitems a \n" +
                "INNER JOIN ecform b ON a.itemid = b.id\n" +
                "INNER JOIN employeemasterfile c ON c.emp_id = a.emp_id\n" +
                "WHERE  a.emp_id != 'walapa' GROUP BY a.emp_id,a.itemid ORDER BY a.emp_id ASC",null);
        JSONArray arr_credits = new JSONArray();

        while(row.moveToNext())
        {
            String employeeid,itemid,quantity,inchargeempid,approvedby,reqstatus,requestdate,amount,totaltocredit,itemcode,name,unit,requestgroup, company;
            JSONObject thejs = new JSONObject();
            employeeid = row.getString(0);
            itemid = row.getString(1);
            quantity = row.getString(2);
            inchargeempid = globalvars.get("inchargeempid");
            approvedby=row.getString(3);
            reqstatus=row.getString(4);
            requestdate=row.getString(5);
            amount=row.getString(6);
            totaltocredit=row.getString(7);
            itemcode=row.getString(8);
            name=row.getString(9);
            unit=row.getString(10);
            company=row.getString(11);
            requestgroup=row.getString(13);
            try
            {
                if(approvedby == null ){
                    approvedby = "none";
                }

                mydatabase.execSQL("INSERT INTO employeeitems_bak(eb_emp_id,eb_itemid,eb_item_desc,eb_quantity,eb_amount,eb_company,eb_requestgroup,eb_approvedby,eb_reqstatus,eb_requestdate) VALUES(" + qttext(row.getString(0)) + "," + qttext(row.getString(1)) + "," + qttext(row.getString(12)) + "," + qttext(row.getString(2)) + "," + qttext(row.getString(6)) + "," + qttext(row.getString(11)) + "," + qttext(requestgroup) + "," + qttext(approvedby) + "," + qttext(reqstatus) +",strftime('%Y-%m-%d %H:%M:%S','now'))");

                thejs.put("emp_id",employeeid);
                thejs.put("itemid",itemid);
                thejs.put("quantity",quantity);
                thejs.put("inchargeempid",inchargeempid);
                thejs.put("approvedby",approvedby);
                if(company.equalsIgnoreCase("020303") && reqstatus.equalsIgnoreCase("Approved"))
                {
                    thejs.put("reqstatus","Initial Approved");
                }
                else
                {
                    thejs.put("reqstatus",reqstatus);
                }
                thejs.put("requestdate",requestdate);
                thejs.put("amount",amount);
                thejs.put("inchargeid",txtinchargeid.getText().toString());
                thejs.put("totaltocredit",totaltocredit);
                thejs.put("itemcode",itemcode);
                thejs.put("name",name);
                thejs.put("unit",unit);
                arr_credits.put(thejs);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
    }

        ajax.adddata("inchargeempid",globalvars.get("inchargeempid"));
        //ajax.adddata("adjustment",adjarr.toString());
        ajax.adddata("credits",arr_credits.toString());

        //MFI-DRESSING PLANT...
        if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("34"))
        {
            if(arr_credits.length() == 0){
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"No Data to be uploaded...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }else{
            ajax.execute(this.getString(R.string.serveraddress) + "android_uploadcredit_dp.php");
            }
        }
        //PEANUT KISSES...
        else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
        {
            if(arr_credits.length() == 0){
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"No Data to be uploaded...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }else{
                ajax.execute(this.getString(R.string.serveraddress) + "android_uploadcredit_pk.php");
            }
        }
        else
        {
            ajax.execute(this.getString(R.string.serveraddress) + "android_uploadcredit.php");
        }
    }

    private void logUpload(String logdate)
    {
        Cursor resultSet = mydatabase.rawQuery("Select * from appsetting where setting_name='last_data_upload'",null);
        if(resultSet.getCount() == 1 ) {
            resultSet.moveToFirst();
            String settingid = resultSet.getString(0);
            mydatabase.execSQL("UPDATE appsetting SET setting_value=" + qttext(logdate) + " WHERE id=" + qttext(settingid));
        } else {
            mydatabase.execSQL("INSERT INTO appsetting(setting_name,setting_value) VALUES ('last_data_upload'," + qttext(logdate) + ")");
        }
    }

    public String qttext(Object txt){
        return '"' + txt.toString().trim() + '"';
    }

    public void setfontawesome(int id,String icon){
        TextView txtview = (TextView) findViewById(id);
        txtview.setTypeface(font);
        txtview.setText(icon);
    }
}
