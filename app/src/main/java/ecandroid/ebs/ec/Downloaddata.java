package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Downloaddata extends AppCompatActivity {
    Typeface font;
    Ajax mocheck, mo,mo1,mo2,mo3, moimg;
    Globalvars globalvars;
    ProgressBar loadingbar;
    ProgressBar loadingbar_h;
    TextView txtviewstatus;
    Msgbox msgbox;
    Context context = this;
    SQLiteDatabase mydatabase;

    JSONArray thedata;
    private static final String TAG = Downloaddata.class.getSimpleName();
    String idnumberfront,idnumberback,incharge_empid;
    String emp_no,emp_pins,name,credit_limit, bu_code, companyname,businessunit,deptname,payrollno,emp_id,eoc_date,credit_balance;
    String  itemcode,description,quantity,unit,cost,bcode,store,expirydate,startdate,enddate,itemid,theimg, status, remarks, reference, discount_status;
    String loc_bcode, loc_business_unit, loc_company_code, loc_bunit_code, loc_status, loc_acroname, ratee, subordinates_rater;
    String com_code, bunit_code, discount, company_name, bunit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        msgbox = new Msgbox(context);
        globalvars = new Globalvars((Context)this,(Activity)this);
        setContentView(R.layout.activity_downloaddata);
        font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        loadingbar = (ProgressBar) findViewById(R.id.loadingbar); //Horizontal Loading Bar....
        loadingbar_h = (ProgressBar) findViewById(R.id.loadingbar_h); //Circle Loading Bar....
        txtviewstatus = (TextView) findViewById(R.id.txtviewstatus); //Status TextView....
        loadingbar.setVisibility(View.INVISIBLE); //Hide Horizontal Loading Bar....

        final Button btncheckconnection = (Button) findViewById(R.id.btncheckconnection);
        final Button btnstartdownload = (Button) findViewById(R.id.btnstartdownload);

        btncheckconnection.setOnClickListener(new View.OnClickListener() {   //Clicking btncheckconnection Button...
            @Override
            public void onClick(View v) {
                loadingbar_h.setVisibility(View.VISIBLE);
                txtviewstatus.setText("Checking server connection. Please wait...");
                checkconnection();   //Calling checkconnection() function...
            }
        });

        btnstartdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
////                SimpleDateFormat fdate = new SimpleDateFormat("MM/dd/yyyy");
////
////                Cursor resultLog = mydatabase.rawQuery("SELECT setting_value FROM appsetting WHERE setting_name ='last_data_upload'",null);
////                if(resultLog.getCount() == 1) {
////                    resultLog.moveToNext();
////                    String last_update = resultLog.getString(0);
////                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
////                    Date date_lastupload = new Date(), today = new Date();
////                    try {
////                        date_lastupload = sdf.parse(last_update);
////                        today = sdf.parse(fdate.format(calendar.getTime()));
////                    } catch (ParseException e) {
////                        e.printStackTrace();
////                    }
////                    if(date_lastupload.before(today)){
////                        downloadincharge();
////                    } else {
////                        msgtoaster("Data today has been uploaded. You may try again tomorrow.");
////                    }
////                } else {
////                }
                //Micheal
                if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("02"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //Dressing Plant
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("34"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //RED RIBBON- ALTURAS
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //RED RIBBON- ICM
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //RED RIBBON- TALIBON
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //THE PRAWN FARM-ICM && ALTA-CITTA
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("32"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //PEANUT KISSES
                else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //CDC
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("07"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //BSCOM
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("21"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                //Clinic
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("01"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("04"))
                {
                    if(globalvars.get("company").equals("none"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please select company before downloading.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        downloadincharge();
                    }
                }
                else
                {
                    downloadincharge();
                }

            }
        });
        setfontawesome(R.id.txtviewdownloaddata,"\uf0ed");
        checkconnection();
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
    }

    public void downloadincharge()
    {
        txtviewstatus.setText("Downloading Incharge Information from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE); //Unhide Horizontal Loading Bar....
        loadingbar.setProgress(1);
        mo = new Ajax();

        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror() {
                new android.os.Handler().postDelayed(
                        new Runnable(){
                            public void run(){
                                loadingbar.setVisibility(View.INVISIBLE);  //Unhide Horizontal Loading Bar if not connected to the server....
                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                            }
                        },1000
                );
            }
            @Override
            public void onsuccess(final String data)
            {
                mydatabase.execSQL("DELETE FROM inchargeid");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadingbar.setProgress(10);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            Log.e("Incharge Length:"," " + thedata.length());
                            for(int a=0;a < thedata.length();a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                idnumberfront = row.getString(0);
                                idnumberback = row.getString(1);
                                incharge_empid = row.getString(2);

                                cv.put("idfront",idnumberfront);
                                cv.put("idback",idnumberback);
                                cv.put("inchargeemp",incharge_empid);
                                mydatabase.insert("inchargeid",null,cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    loadingbar.setProgress(20);
                                    //Micheal
                                    if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("02"))
                                    {
                                        downloademployee2();
                                    }
                                    else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("34"))
                                    {
                                        downloademployee2();
                                    }
                                    //PK
                                    else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
                                    {
                                        downloademployee2();
                                    }
                                    //THE PRAWN FARM-ICM & ALTA-CITTA
                                    else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("32"))
                                    {
                                        downloademployee2();
                                    }
                                    //RED RIBBON- ALTURAS
                                    else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
                                    {
                                        downloaddiscount();
                                    }
                                    //RED RIBBON- ISLAND CITY MALL
                                    else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
                                    {
                                        downloaddiscount();
                                    }
                                    //RED RIBBON- TALIBON...
                                    else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))
                                    {
                                        downloaddiscount();
                                    }
                                    //CDC...
                                    else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("07"))
                                    {
                                        downloademployee2();
                                    }
                                    //BSCOM...
                                    else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("21"))
                                    {
                                        downloademployee2();
                                    }
                                    //FSCOM...
                                    else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("20"))
                                    {
                                        downloademployee2();
                                    }
                                    //Noodles...
                                    else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("22"))
                                    {
                                        downloademployee2();
                                    }
                                    //Clinic...
                                    else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("01"))
                                    {
                                        downloademployee2();
                                    }
                                    //gamo
                                    else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("04"))
                                    {
                                        downloademployee2();
                                    }
                                    else
                                    {
                                        downloademployee();
                                    }
                                }
                            },1000);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });
        mo.execute(this.getString(R.string.serveraddress) + "android_download_inchargedummy.php");
    }

    public void downloaddiscount()
    {
        txtviewstatus.setText("Downloading Discount per BU from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);
        mo1 = new Ajax();

        mo1.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                new android.os.Handler().postDelayed(
                        new Runnable()
                        {
                            public void run()
                            {
                                loadingbar.setVisibility(View.INVISIBLE);
                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                            }
                        },1000);
            }
            @Override
            public void onsuccess(final String data)
            {
                    mydatabase.execSQL("DELETE FROM employeediscount");

                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadingbar.setProgress(25);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            for(int a = 0 ; a < thedata.length() ; a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                com_code = row.getString(0).trim();
                                bunit_code = row.getString(1).trim();
                                discount = row.getString(2).trim();
                                company_name = row.getString(3).trim();
                                bunit_name = row.getString(4).trim();
                                cv.put("company_code",com_code);
                                cv.put("bunit_code",bunit_code);
                                cv.put("discount",discount);
                                cv.put("company_name",company_name);
                                cv.put("bunit_name",bunit_name);

                                mydatabase.insert("employeediscount",null, cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                        downloademployee2();
                                }
                            },1000);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });

        mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_discount.php");
    }

    public void downloademployee()
    {
        txtviewstatus.setText("Downloading Employee Master file from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);
        mo1 = new Ajax();

        mo1.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                new android.os.Handler().postDelayed(
                        new Runnable()
                        {
                            public void run()
                            {
                                loadingbar.setVisibility(View.INVISIBLE);
                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                            }
                        },1000);
            }
            @Override
            public void onsuccess(final String data)
            {
                mydatabase.execSQL("DELETE FROM employeemasterfile");
                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadingbar.setProgress(30);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            for(int a=0;a < thedata.length();a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                emp_no = row.getString(0).trim();
                                emp_pins = row.getString(1).trim();
                                name = row.getString(2);
                                credit_limit = row.getString(3);
                                companyname = row.getString(4);
                                businessunit = row.getString(5);
                                deptname = row.getString(6);
                                payrollno = row.getString(7);
                                emp_id = row.getString(8);
                                eoc_date = row.getString(9);
                                credit_balance = row.getString(10);
                                cv.put("emp_no",emp_no);
                                cv.put("emp_pins",emp_pins);
                                cv.put("name",name);
                                cv.put("credit_limit",credit_limit);
                                cv.put("credit_balance",credit_balance);
                                cv.put("companyname",companyname);
                                cv.put("businessunit",businessunit);
                                cv.put("deptname",deptname);
                                cv.put("payrollno",payrollno);
                                cv.put("emp_id",emp_id);
                                cv.put("eoc_date",eoc_date);
                                mydatabase.insert("employeemasterfile",null,cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    loadingbar.setProgress(40);
                                    if(globalvars.get("company_code").equals("07"))
                                    {
                                        downloaditem();
                                    }
                                    else
                                    {
                                        downloadsubordinate();
                                    }
                                }
                            },1000);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });
        mo1.adddata("company_code",globalvars.get("company_code"));
        mo1.adddata("bunit_code",globalvars.get("bunit_code"));
        mo1.adddata("company",globalvars.get("company"));
        //Micheal
        if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("02"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee2.php");
        }
        //UDC
        else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("07"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee3.php");
        }
        //PK && Noodles...
        else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee2.php");
        }
        //THE PRAWN FARM-ICM & ALTA-CITTA
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("32"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee3.php");
        }
        //RED RIBBON- ALTURAS
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee3.php");
        }
        //RED RIBBON- ISLAND CITY MALL
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee3.php");
        }
        //RED RIBBON- TALIBON
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee3.php");
        }
        else
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee.php");
        }
    }

    public void downloademployee2()
    {
        txtviewstatus.setText("Downloading Employee Master file from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);
        mo1 = new Ajax();

        mo1.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                new android.os.Handler().postDelayed(
                        new Runnable()
                        {
                            public void run()
                            {
                                loadingbar.setVisibility(View.INVISIBLE);
                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                            }
                        },1000);
            }
            @Override
            public void onsuccess(final String data)
            {
                if(globalvars.get("company").equals("agc1"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'AGC1'");
                }
                else if(globalvars.get("company").equals("agc2"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'AGC2'");
                }
                else if(globalvars.get("company").equals("agc3"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'AGC3'");
                }
                else if(globalvars.get("company").equals("agc4"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'AGC4'");
                }
                else if(globalvars.get("company").equals("asc1"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'ASC1'");
                }
                else if(globalvars.get("company").equals("asc2"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'ASC2'");
                }
                else if(globalvars.get("company").equals("asc3"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'ASC3'");
                }
                else if(globalvars.get("company").equals("asc4"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'ASC4'");
                }
                else if(globalvars.get("company").equals("mfi1"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI1'");
                }
                else if(globalvars.get("company").equals("mfi2"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI2'");
                }
                else if(globalvars.get("company").equals("mfi3"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI3'");
                }
                else if(globalvars.get("company").equals("mfi4"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI4'");
                }
                else if(globalvars.get("company").equals("mfi5"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI5'");
                }
                else if(globalvars.get("company").equals("mfi6"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname = 'MFI6'");
                }
                else if(globalvars.get("company").equals("others1"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname IN ('MFRI','AGROMARINE','BUCAREZ')");
                }
                else if(globalvars.get("company").equals("others2"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname IN ('PANGLAO BAY','ROSE EN HONEY')");
                }
                else if(globalvars.get("company").equals("others3"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname IN ('LDI')");
                }
                else if(globalvars.get("company").equals("others4"))
                {
                    mydatabase.execSQL("DELETE FROM employeemasterfile where companyname IN ('CRUSTPEPPER','ASC TECH','ABENSON','NETMAN','NAUTICA','BTV','CCFI','RTFI','KOMPAS RESORTS')");
                }
                else
                {

                }

                    new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadingbar.setProgress(30);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            Log.e("Employee2 Length:"," " + thedata.length());
                            for(int a=0;a < thedata.length();a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                emp_no = row.getString(0).trim();
                                emp_pins = row.getString(1).trim();
                                name = row.getString(2);
                                credit_limit = row.getString(3);
                                companyname = row.getString(4);
                                businessunit = row.getString(5);
                                deptname = row.getString(6);
                                payrollno = row.getString(7);
                                emp_id = row.getString(8);
                                eoc_date = row.getString(9);
                                credit_balance = row.getString(10);
                                cv.put("emp_no",emp_no);
                                cv.put("emp_pins",emp_pins);
                                cv.put("name",name);
                                cv.put("credit_limit",credit_limit);
                                cv.put("credit_balance",credit_balance);
                                if(globalvars.get("company").equals("agc1")) {
                                    cv.put("companyname", "AGC1");
                                }else if(globalvars.get("company").equals("agc2")) {
                                    cv.put("companyname", "AGC2");
                                }else if(globalvars.get("company").equals("agc3")) {
                                    cv.put("companyname", "AGC3");
                                }else if(globalvars.get("company").equals("agc4")) {
                                    cv.put("companyname", "AGC4");
                                }else if(globalvars.get("company").equals("asc1")) {
                                    cv.put("companyname", "ASC1");
                                }else if(globalvars.get("company").equals("asc2")) {
                                    cv.put("companyname", "ASC2");
                                }else if(globalvars.get("company").equals("asc3")) {
                                    cv.put("companyname", "ASC3");
                                }else if(globalvars.get("company").equals("asc4")) {
                                    cv.put("companyname", "ASC4");
                                } else if(globalvars.get("company").equals("mfi1")) {
                                    cv.put("companyname", "MFI1");
                                }else if(globalvars.get("company").equals("mfi2")) {
                                    cv.put("companyname", "MFI2");
                                }else if(globalvars.get("company").equals("mfi3")) {
                                    cv.put("companyname", "MFI3");
                                }else if(globalvars.get("company").equals("mfi4")) {
                                    cv.put("companyname", "MFI4");
                                }else if(globalvars.get("company").equals("mfi5")) {
                                    cv.put("companyname", "MFI5");
                                }else if(globalvars.get("company").equals("mfi6")) {
                                    cv.put("companyname", "MFI6");
                                }else if(globalvars.get("company").equals("others1")) {
                                    cv.put("companyname", "others1");
                                }else if(globalvars.get("company").equals("others2")) {
                                    cv.put("companyname", "others2");
                                }else if(globalvars.get("company").equals("others3")) {
                                    cv.put("companyname", "others3");
                                }else if(globalvars.get("company").equals("others4")) {
                                    cv.put("companyname", "others4");
                                }
                                else{
                                    cv.put("companyname", companyname);
                                }
                                cv.put("businessunit",businessunit);
                                cv.put("deptname",deptname);
                                cv.put("payrollno",payrollno);
                                cv.put("emp_id",emp_id);
                                cv.put("eoc_date",eoc_date);
                                mydatabase.insert("employeemasterfile",null,cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    loadingbar.setProgress(40);
                                    if(globalvars.get("company_code").equals("07"))
                                    {
                                        downloaditem();
                                    }
                                    else
                                    {
                                        downloadsubordinate();
                                    }
                                }
                            },1000);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });
        String company = globalvars.get("company");
        String company2 = globalvars.get("company");
        mo1.adddata("company_code",globalvars.get("company_code"));
        mo1.adddata("bunit_code",globalvars.get("bunit_code"));
        mo1.adddata("company",globalvars.get("company"));
        //Micheal
        if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("02"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //Ubay-DP
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("34"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //PK
        else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //THE PRAWN FARM-ICM & ALTA-CITTA
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("32"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //RED RIBBON- ALTURAS
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //RED RIBBON- ISLAND CITY MALL
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //RED RIBBON- TALIBON
        else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //CDC
        else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("07"))
        {
                mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //BSCOM
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("21"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //FSCOM
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("20"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //Noodles
        else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("22"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //Clinic
        else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("01"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        //gamo
        else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("04"))
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee_conso.php");
        }
        else
        {
            mo1.execute(this.getString(R.string.serveraddress) + "android_downloaddata_employee.php");
        }
    }
    //Not used in cebu. only the HR can approve...
    public void downloadsubordinate()
    {
        txtviewstatus.setText("Downloading Employee Leveling Subordinates from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);

        mo3 = new Ajax();
        mo3.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                new android.os.Handler().postDelayed
                        (
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        loadingbar.setVisibility(View.INVISIBLE);
                                        txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                                    }
                                },1000
                        );
            }
            @Override
            public void onsuccess(final String data)
            {
                mydatabase.execSQL("DELETE FROM levelingsubordinates");
                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadingbar.setProgress(50);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            Log.e("Leveling Length:", " " + thedata.length());
                            for(int a=0 ; a < thedata.length() ; a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                ratee = row.getString(0);
                                subordinates_rater = row.getString(1);
                                cv.put("ratee",ratee);
                                cv.put("subordinates_rater",subordinates_rater);
                                mydatabase.insert("levelingsubordinates",null,cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    loadingbar.setProgress(60);
                                    if(globalvars.get("company_code").equals("01"))
                                    {
                                        downloadlocation();
                                    }
                                    else
                                    {
                                        downloaditem();
                                    }
                                }
                            },1000);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });
        mo3.adddata("inchargeempid",globalvars.get("inchargeempid"));
        mo3.execute(this.getString(R.string.serveraddress)+"android_downloaddata_leveling_subordinates.php");
    }

    //Only for Micheal...
    public void downloadlocation()
    {
        txtviewstatus.setText("Downloading Locations/Business Units from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);

        mo3 = new Ajax();
        mo3.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                new android.os.Handler().postDelayed
                        (
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        loadingbar.setVisibility(View.INVISIBLE);
                                        txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                                    }
                                },1000
                        );
            }
            @Override
            public void onsuccess(final String data)
            {
                mydatabase.execSQL("DELETE FROM locatebusinessunit");
                new Handler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadingbar.setProgress(61);
                    }
                });
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            thedata = new JSONArray(data);
                            mydatabase.beginTransaction();
                            Log.e("Location Length:", " " + thedata.length());
                            for(int a=0 ; a < thedata.length() ; a++)
                            {
                                ContentValues cv = new ContentValues();
                                JSONArray row = thedata.getJSONArray(a);
                                loc_bcode = row.getString(0);
                                loc_business_unit = row.getString(1);
                                loc_company_code = row.getString(2);
                                loc_bunit_code = row.getString(3);
                                loc_status = row.getString(4);
                                loc_acroname = row.getString(5);
                                cv.put("loc_bcode",loc_bcode);
                                cv.put("loc_business_unit",loc_business_unit);
                                cv.put("loc_company_code",loc_company_code);
                                cv.put("loc_bunit_code",loc_bunit_code);
                                cv.put("loc_status",loc_status);
                                cv.put("loc_acroname",loc_acroname);
                                mydatabase.insert("locatebusinessunit",null,cv);
                            }
                            mydatabase.setTransactionSuccessful();
                            mydatabase.endTransaction();

                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    loadingbar.setProgress(69);

                                    downloaditem();
                                }
                            },1000);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },1000);
            }
        });
        mo3.execute(this.getString(R.string.serveraddress)+"android_downloaddata_location.php");
    }

    public void downloaditem()
    {
        txtviewstatus.setText("Downloading Item Master file from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);
        if(globalvars.get("company_code").equalsIgnoreCase("01") && globalvars.get("bunit_code").equalsIgnoreCase("04") &&  globalvars.get("dept_code").equalsIgnoreCase("02") ){
            globalvars.set("company_code", "03");
            globalvars.set("bunit_code", "21");
            globalvars.set("dept_code", "02");
            System.out.println("ROMEO Gamo");
        }else{
            mo2 = new Ajax();
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    loadingbar.setProgress(70);
                }
            });
            mo2.setCustomObjectListener(new Ajax.MyCustomObjectListener()
            {
                @Override
                public void onerror() {
                    new android.os.Handler().postDelayed
                            (
                                    new Runnable()
                                    {
                                        public void run()
                                        {
                                            loadingbar.setVisibility(View.INVISIBLE);
                                            txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                                        }
                                    },1000
                            );
                }

                @Override
                public void onsuccess(final String data)
                {
                    mydatabase.execSQL("DELETE FROM ecform");
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                thedata = new JSONArray(data);
                                mydatabase.beginTransaction();
                                Log.e("Items Length:", " " + thedata.length());
                                //int a1 = thedata.length();
//                            globalvars.set("downloaded_length", String.valueOf(thedata.length()));
                                for(int a=0 ; a < thedata.length() ; a++){
                                    ContentValues cv = new ContentValues();
                                    JSONArray row = thedata.getJSONArray(a);
                                    itemcode = row.getString(0);
                                    description = row.getString(1).trim();
                                    quantity = row.getString(2);
                                    unit = row.getString(3);
                                    cost = row.getString(4);
                                    bcode = row.getString(5);
                                    store = row.getString(6);
                                    expirydate = row.getString(7);
                                    startdate = row.getString(8);
                                    enddate = row.getString(9);
                                    itemid = row.getString(10);
                                    status = row.getString(11);
                                    remarks = row.getString(12);
                                    reference = row.getString(13);
                                    if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
                                    {
                                        discount_status = row.getString(14);
                                    }
                                    else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
                                    {
                                        discount_status = row.getString(14);
                                    }
                                    else
                                    {
                                        discount_status = "";
                                    }
                                    cv.put("id",itemid);
                                    cv.put("code",itemcode);
                                    cv.put("description",description);
                                    cv.put("qty",quantity);
                                    cv.put("unit",unit);
                                    cv.put("bcode",bcode);
                                    cv.put("cost",cost);
                                    cv.put("store",store);
                                    cv.put("expirydate",expirydate);
                                    cv.put("startdate",startdate);
                                    cv.put("enddate",enddate);
                                    cv.put("status",status);
                                    cv.put("remarks",remarks);
                                    cv.put("reference",reference);
                                    cv.put("discountstatus",discount_status);
                                    mydatabase.insert("ecform",null,cv);
                                }
                                mydatabase.setTransactionSuccessful();
                                mydatabase.endTransaction();

                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        loadingbar.setProgress(80);
                                        downloaditemimages();
                                    }
                                },1000);

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    },1000);
                }
            });
            mo2.adddata("inchargeempid",globalvars.get("inchargeempid"));
            mo2.adddata("company_code",globalvars.get("company_code"));
            mo2.adddata("bunit_code",globalvars.get("bunit_code"));
            mo2.adddata("dept_code",globalvars.get("dept_code"));
            //Condition if user is Tig Suroy(Special) && BU == Plaza Marcela, ALTURAS MALL, & ICM && Department != Supermarket...
            if((globalvars.get("usertype").equals("6") && globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("01") && !globalvars.get("dept_code").equals("01")) || (globalvars.get("usertype").equals("6") && globalvars.get("company_code").equals("02") && globalvars.get("bunit_code").equals("01") && !globalvars.get("dept_code").equals("01")) || (globalvars.get("usertype").equals("6") && globalvars.get("company_code").equals("02") && globalvars.get("bunit_code").equals("03") && !globalvars.get("dept_code").equals("01")))
            {
                mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item2.php");
            }
            else
            {
                mo1.adddata("company",globalvars.get("company"));
                //Micheal...
                if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("02"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item3.php");
                }
                else if(globalvars.get("company_code").equals("09") && globalvars.get("bunit_code").equals("01"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_pk.php");
                }
                //RED RIBBON- ISLAND CITY MALL
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                }
                //RED RIBBON- ALTURAS
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                }
                //RED RIBBON-TALIBON
                else if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                }
                //CDC
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("07"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item3.php");
                }
                //BSCOM
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("21"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                }
                //gamo
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("04"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                    System.out.println("company_code: "+globalvars.get("company_code"));
                    System.out.println("bunit_code: "+globalvars.get("bunit_code"));
                    System.out.println("dept_code: "+globalvars.get("dept_code"));
                }
                //FSCOM
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("20"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_rb.php");
                }
                //Noodles...
                else if(globalvars.get("company_code").equals("03") && globalvars.get("bunit_code").equals("22"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item_nf.php");
                }
                //Clinic...
                else if(globalvars.get("company_code").equals("01") && globalvars.get("bunit_code").equals("01"))
                {
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item4.php");
                }
                else
                {
                    //Only for Regular except micheal...
                    mo2.execute(this.getString(R.string.serveraddress) + "android_downloaddata_item.php");
                }
            }
        }

    }
//    public void downloaditemimages()
//    {
//        txtviewstatus.setText("Downloading Item Images Master file from server. Please wait...");
//        loadingbar.setVisibility(View.VISIBLE);
//
//        new Handler().post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                loadingbar.setProgress(90);
//            }
//        });
//        txtviewstatus.setText("Data successfully updated!");
//        loadingbar.setVisibility(View.INVISIBLE);
//        msgtoaster("Data successfully updated!");
//        loadingbar.setProgress(0);
//
//    }
    public void downloaditemimages()
    {
        txtviewstatus.setText("Downloading Item Images Master file from server. Please wait...");
        loadingbar.setVisibility(View.VISIBLE);

        clearEcimages();
        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                loadingbar.setProgress(90);
            }
        });
        Cursor resultSet = mydatabase.rawQuery("SELECT id FROM ecform", null);
        if(resultSet.getCount() > 0)
        {
            while (resultSet.moveToNext())
            {
                String imgid =resultSet.getString(0);
                moimg = new Ajax();
                moimg.setCustomObjectListener(new Ajax.MyCustomObjectListener()
                {
                    @Override
                    public void onerror() {
                        new android.os.Handler().postDelayed
                                (
                                        new Runnable()
                                        {
                                            public void run()
                                            {
                                                loadingbar.setVisibility(View.INVISIBLE);
                                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
                                            }
                                        },1000
                                );
                    }
                    @Override
                    public void onsuccess(final String data)
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    thedata = new JSONArray(data);
                                    JSONArray row = thedata.getJSONArray(0);
                                    itemid = row.getString(0);
                                    theimg = row.getString(1);
                                    Log.e("Itemid:", " " + itemid);
                                    byte[] decodedString = Base64.decode(theimg, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                    File directory = cw.getDir("ecimages", Context.MODE_PRIVATE);
                                    File mypath = new File(directory, itemid + ".png");
                                    FileOutputStream fos = null;
                                    try
                                    {
                                        fos = new FileOutputStream(mypath);
                                        // Use the compress method on the BitMap object to write image to the OutputStream
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 75, fos);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    finally
                                    {
                                        try
                                        {
                                            fos.close();
                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                                if (moimg.getStatus() == AsyncTask.Status.FINISHED)
                                {
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
//                                            if(globalvars.get("company_code").equals("01"))
//                                            {
//                                                globalvars.set("company_status", "1");
//                                            }
                                            txtviewstatus.setText("Data successfully updated!");
                                            loadingbar.setVisibility(View.INVISIBLE);
                                            loadingbar.setProgress(0);
                                        }
                                    }, 1000);
                                }
                            }
                        }, 1000);
                    }
                });
                moimg.adddata("imgid", imgid);
                moimg.execute(this.getString(R.string.serveraddress) + "android_downloadimage_item.php");
            }
        }
        else
        {
            txtviewstatus.setText("No Available Items/s for Credit.");
            loadingbar.setVisibility(View.INVISIBLE);
            msgtoaster("No Available Item/s!");
            loadingbar.setProgress(0);
        }
    }
    public void checkconnection(){    //Checking Connection from the server...
        mocheck = new Ajax();
        mocheck.setCustomObjectListener(new Ajax.MyCustomObjectListener(){
            @Override
            public void onerror(){
                new android.os.Handler().postDelayed(
                        new Runnable(){
                            public void run(){
                                loadingbar_h.setVisibility(View.INVISIBLE);  //Hidden Circle Loading Bar if not connected to the server....
                                txtviewstatus.setText("Unable to connect to server. Please make sure you are connected to a wifi.");
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
                                loadingbar_h.setVisibility(View.INVISIBLE);  //Hidden Circle Loading Bar if connected to the server....
                                txtviewstatus.setText("Connection established. Ready for download.");
                            }
                        },1000);
            }
        });
        mocheck.execute(this.getString(R.string.serveraddress) + "android_checkconnection.php");
    }
    public void clearEcimages(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("ecimages", Context.MODE_PRIVATE);
        File dir = new File(directory.getAbsolutePath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }
    public void setfontawesome(int id,String icon){
        TextView txtview = (TextView) findViewById(id);
        txtview.setTypeface(font);
        txtview.setText(icon);
    }
    public void msgtoaster(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,80);
        toast.show();
    }
}
























