package ecandroid.ebs.ec;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class Login extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    Globalvars globalvars;
    Ajax mo;
    String temptext="";
    CharSequence txtusername,txtpassword;
    Msgbox msgbox;
    EditText txtviewpassword,txtviewusername;
    SharedPreferences settingsShrepref;
    SharedPreferences.Editor editor;
    CheckBox cb_showpassword;
    private static final String PREF_NAME = "MyPrefFileShred";
    public static final String KEY_TODAY_DATE_ANDTIME = "TimeToday";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button btnlogin = (Button) findViewById(R.id.btnlogin);
        cb_showpassword = (CheckBox) findViewById(R.id.cb_showpassword);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        globalvars = new Globalvars((Context)this,(Activity)this);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
//        mydatabase.execSQL("UPDATE employeeitems SET approvedby='', reqstatus='Pending' WHERE id !=0");
//        mydatabase.execSQL("UPDATE ecform SET reference='SPECIAL' WHERE id=532 or id=534");
//        mydatabase.execSQL("UPDATE users SET dept_code='01' WHERE emp_id='53967-2013'");
//        mydatabase.execSQL("DROP TABLE ecform");
        createdatabases();
        msgbox = new Msgbox(this);
        TextView txtview = (TextView) findViewById(R.id.txtusername);
        txtusername = txtview.getText();
        txtview = (TextView) findViewById(R.id.textviewuesrname);
        txtview.setTypeface(font);
        txtview.setText("\uf007");
        txtview = (TextView) findViewById(R.id.txtpassword);
        txtpassword = txtview.getText();
        txtview = (TextView) findViewById(R.id.textviewpassword);
        txtview.setTypeface(font);
        txtview.setText("\uf023");
        txtviewpassword = (EditText) findViewById(R.id.txtpassword);
        txtviewusername = (EditText) findViewById(R.id.txtusername);

        txtviewpassword.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == 66 && event.getAction()==0){
                    temptext = txtviewpassword.getText().toString();
                    String emp_no = txtviewusername.getText().toString().trim();
                    String emp_pins = txtviewpassword.getText().toString().trim();
                    if(emp_no.equalsIgnoreCase("")){
                        emp_no = "blank";
                    }
                    String query = "SELECT emp_name,usertype,emp_id, company_code, bunit_code, dept_code FROM `users` " +
                            "where emp_no LIKE '%" + emp_no + "'" +
                            "and emp_pins = '" + emp_pins + "' LIMIT 1";
                    Cursor resultSet = mydatabase.rawQuery(query,null);
                    if(resultSet.getCount() == 1) {
                        checklogin(resultSet);
                    } else {
                        btnlogin.callOnClick();
                    }
                }
                return false;
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fdate = new SimpleDateFormat("MM/dd/yyyy");
    Cursor resultLog = mydatabase.rawQuery("SELECT setting_value FROM appsetting WHERE setting_name ='last_user_update'",null);
    if(resultLog.getCount() == 1)
    {
        resultLog.moveToNext();
        String last_update = resultLog.getString(0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date_lastup = new Date(), today = new Date();
        try
        {
            date_lastup = sdf.parse(last_update);
            today = sdf.parse(fdate.format(calendar.getTime()));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        //Return true if and only if the instant of time represented by this Date object is strictly
        //earlier than the instant represented by when false otherwise.
        if(date_lastup.before(today))
        {
            userupdate(fdate.format(calendar.getTime()));
        }
    }
    else
    {
        userupdate(fdate.format(calendar.getTime()));
    }

        btnlogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             Cursor resultSet = mydatabase.rawQuery("select emp_name,usertype,emp_id, company_code, bunit_code,dept_code from users "+
                                                    "where username="+qttext(txtusername.toString().toLowerCase())+
                                                            " and password="+qttext(MD5(txtpassword.toString())),null);
            checklogin(resultSet);
            }
        });
    }

    public void checklogin(final Cursor resultSet){
        if(resultSet.getCount() == 1 ) {
            resultSet.moveToNext();
            final String emp_name = resultSet.getString(0);
            final String usertype = resultSet.getString(1);
            final String emp_id = resultSet.getString(2).trim();
            final String company_code = resultSet.getString(3);
            final String bunit_code = resultSet.getString(4);
            final String dept_code = resultSet.getString(5);
            msgbox.showyesno("Login as ", emp_name);
            msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
                @Override
                public void onyes() {
                    globalvars.set("inchargeempid", emp_id);
                    //Orig, Jessica in PF(Switched from Supervisor to EC-Incharge)...
                    //DAG-UM, JOSEPHINE in PK(Switched from Tig Set-up to EC-Incharge)...
                    //Bon, Cheryl in CDC(Switched from Tig Set-up to EC-Incharge)...
                    if(emp_id.equalsIgnoreCase("03619-2016") || emp_id.equalsIgnoreCase("12468-2013")){
                        globalvars.set("usertype", "6");
                    }
//                    else if(emp_id.equalsIgnoreCase("12111-2013")){
//                        globalvars.set("usertype", "3");
//                    }
                    else{
                        globalvars.set("usertype", usertype);
                    }

                    //Condition if user is Tig Suroy(Special) && BU == Plaza Marcela && Department == PM-Foodmix...
                    if(globalvars.get("usertype").equalsIgnoreCase("6") && company_code.equalsIgnoreCase("03") && bunit_code.equalsIgnoreCase("01") && dept_code.equalsIgnoreCase("12"))
                    {

                        globalvars.set("dept_code","07");
                    }
//                    else if(company_code.equalsIgnoreCase("01") && bunit_code.equalsIgnoreCase("04") &&  dept_code.equalsIgnoreCase("02") ){
//                        System.out.println("ROMEO Gamo");
//                    }
                    else
                    {
                        globalvars.set("dept_code", dept_code);
                    }
                    globalvars.set("company_code", company_code);
                    globalvars.set("bunit_code", bunit_code);
                    globalvars.set("company","none");
                    String sample_dcode = globalvars.get("dept_code");
                    System.out.println("company_code: "+globalvars.get("company_code"));
                    System.out.println("bunit_code: "+globalvars.get("bunit_code"));
                    System.out.println("dept_code: "+globalvars.get("dept_code"));
                    if (usertype.equalsIgnoreCase("3") || usertype.equalsIgnoreCase("6") || emp_id.equalsIgnoreCase("03619-2016"))
                    {
                        Cursor dummyid = mydatabase.rawQuery("SELECT idfront,idback,inchargeemp FROM inchargeid WHERE inchargeemp="+qttext(emp_id),null);
                        if(dummyid.getCount() == 1)
                        {
                            dummyid.moveToFirst();
                            globalvars.set("inchargedummy", dummyid.getString(0));
                            String tst = dummyid.getString(0);
                        }
                        globalvars.set("ifduplicate","0");
//                        if(globalvars.get("company_code").equals("01"))
//                        {
//                            globalvars.set("company_status", "0");
//                        }
                        startActivity(new Intent(Login.this, Home.class));
                    }
                    else if(usertype.equalsIgnoreCase("1"))
                    {
                        startActivity(new Intent(Login.this, AdminHome.class));
                    }
                    else
                    {
                        startActivity(new Intent(Login.this, Supervisorhome.class));
                    }
                }

                @Override
                public void onno() {
                    finish();
                    startActivity(getIntent());
                }
            });
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
            toast.show();
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cb_showpassword:
                if (checked)
                {
                    txtviewpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    txtviewpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public void userupdate(String datenow)
    {
        final String forlog = datenow;
        final ProgressDialog pd = new ProgressDialog(Login.this);
        pd.setMessage("Updating users. Please wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Users update failed. Please check your connection.",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
                pd.dismiss();
            }
            @Override
            public void onsuccess(String data)
            {
                JSONArray thedata;
                try
                {
                    thedata = new JSONArray(data);
                    String username,password,emp_id,emp_no,emp_pins,emp_name,company_code, bunit_code, usertype, dept_code;
                    mydatabase.execSQL("DELETE FROM users");
                    for(int a=0;a<thedata.length();a++)
                    {
                        ContentValues cv = new ContentValues();
                        JSONArray row = thedata.getJSONArray(a);
                        username = row.getString(0);
                        password = row.getString(1);
                        emp_id = row.getString(2);
                        emp_no = row.getString(3);
                        emp_pins = row.getString(4);
                        emp_name = row.getString(5);
                        company_code = row.getString(6);
                        bunit_code = row.getString(7);
                        usertype = row.getString(8);
                        dept_code = row.getString(9);
                        if(emp_no.length() == 10)
                        {
                            emp_no = 0 + emp_no;
                        }
                        cv.put("emp_id",emp_id);
                        cv.put("username",username);
                        cv.put("password",password);
                        cv.put("emp_no",emp_no);
                        cv.put("emp_pins",emp_pins);
                        cv.put("emp_name",emp_name);
                        cv.put("company_code",company_code);
                        cv.put("bunit_code",bunit_code);
                        cv.put("usertype",usertype);
                        cv.put("dept_code",dept_code);
                        mydatabase.insert("users",null,cv);
                    }
                    logUpdate(forlog);
                    pd.dismiss();

                    Toast toast = Toast.makeText(getApplicationContext(),"Users updated!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        mo.execute(this.getString(R.string.serveraddress) + "android_downloadusers.php");
    }

    private void logUpdate(String logdate)
    {
        Cursor resultSet = mydatabase.rawQuery("Select * from appsetting where setting_name='last_user_update'",null);
        if(resultSet.getCount() == 1 )
        {
            resultSet.moveToFirst();
            String settingid = resultSet.getString(0);
            mydatabase.execSQL("UPDATE appsetting SET setting_value=" + qttext(logdate) + " WHERE id=" + qttext(settingid));
        }
        else
        {
            mydatabase.execSQL("INSERT INTO appsetting(setting_name,setting_value) VALUES ('last_user_update'," + qttext(logdate) + ")");
        }
    }
    public void createdatabases(){
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY," +
                "emp_id TEXT," +
                "username TEXT," +
                "password TEXT," +
                "emp_no TEXT," +
                "emp_pins TEXT," +
                "emp_name TEXT," +
                "company_code TEXT," +
                "bunit_code TEXT," +
                "dept_code TEXT," +
                "usertype TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS employeemasterfile(id INTEGER PRIMARY KEY, " +
                "emp_no TEXT, " +
                "emp_pins TEXT, " +
                "name TEXT, " +
                "credit_limit TEXT, " +
                "credit_balance TEXT, " +
                "companyname TEXT, " +
                "businessunit TEXT, " +
                "deptname TEXT, " +
                "payrollno TEXT, " +
                "emp_id TEXT, " +
                "eoc_date TEXT" +
                ")");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS ecform(id INTEGER PRIMARY KEY," +
                "code TEXT," +
                "description TEXT," +
                "qty TEXT," +
                "adjqty TEXT," +
                "unit TEXT," +
                "cost TEXT," +
                "bcode TEXT," +
                "store TEXT," +
                "expirydate TEXT," +
                "startdate TEXT," +
                "enddate TEXT," +
                "status TEXT," +
                "remarks TEXT," +
                "reference TEXT," +
                "discountstatus TEXT" +
                ")");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS employeeitems(id INTEGER PRIMARY KEY," +
                "emp_id TEXT," +
                "itemid TEXT," +
                "item_desc TEXT," +
                "quantity TEXT," +
                "amount TEXT," +
                "amount_discount TEXT," +  //Additional for Red Ribbon.
                "discount_percentage TEXT," +  //Additional for Red Ribbon.
                "discount_amt TEXT," +  //Additional for Red Ribbon.
                "company TEXT," +
                "requestgroup TEXT," +
                "approvedby TEXT," +
                "reqstatus TEXT," +
                "requestdate TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS employeediscount(id INTEGER PRIMARY KEY," +
                "company_code TEXT," +
                "bunit_code TEXT," +
                "discount TEXT," +
                "company_name TEXT," +
                "bunit_name TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS employeeitems_bak(bak_id INTEGER PRIMARY KEY," +
                "eb_emp_id TEXT," +
                "eb_itemid TEXT," +
                "eb_item_desc TEXT," +
                "eb_quantity TEXT," +
                "eb_amount TEXT," +
                "eb_company TEXT," +
                "eb_requestgroup TEXT," +
                "eb_approvedby TEXT," +
                "eb_reqstatus TEXT," +
                "eb_requestdate TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS transactionnumber(id INTEGER PRIMARY KEY," +
                "thenumber TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS levelingsubordinates(id INTEGER PRIMARY KEY," +
                "ratee TEXT," +
                "subordinates_rater TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS inchargeid(id INTEGER PRIMARY KEY," +
                "idfront TEXT," +
                "idback TEXT," +
                "inchargeemp TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS locatebusinessunit(id INTEGER PRIMARY KEY," +
                "loc_bcode TEXT," +
                "loc_business_unit TEXT," +
                "loc_company_code TEXT," +
                "loc_bunit_code TEXT," +
                "loc_status TEXT," +
                "loc_acroname TEXT)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS appsetting(id INTEGER PRIMARY KEY," +
                "setting_name TEXT," +
                "setting_value TEXT)");
        mydatabase.execSQL("CREATE INDEX IF NOT EXISTS empnames ON employeemasterfile(emp_pins,name,companyname,businessunit,deptname)");
        mydatabase.execSQL("CREATE INDEX IF NOT EXISTS theforms ON ecform(code,description,qty,unit,cost,expirydate,startdate,enddate,id)");
    }
}
