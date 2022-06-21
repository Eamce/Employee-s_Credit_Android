package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ScanEmployee3Details extends AppCompatActivity {
    EditText txtenteremployeeid;
    SQLiteDatabase mydatabase;
    Globalvars globalvars;
    Boolean newid = true;
    String temptext = "";
    String employeeid = "";
    Msgbox msgbox;
    Context context = this;
    ImageView thesignature;
    Typeface font;
    double totalamount = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_employee3_details);
        globalvars = new Globalvars((Context) this, (Activity) this);
        txtenteremployeeid = (EditText) findViewById(R.id.txtenteremployeeid);
        mydatabase = openOrCreateDatabase("db.db", MODE_PRIVATE, null);
        msgbox = new Msgbox(context);
        Button btndone = (Button) findViewById(R.id.btnscanemployee3detailsdone);
        Button btncancel = (Button) findViewById(R.id.btnscanemployee3detailscancel);
        font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        btndone.setFocusable(false);
        btncancel.setFocusable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalamount = Double.parseDouble(extras.getString("totalamount"));
            //The key argument here must match that used in the other activity
        }
        txtenteremployeeid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (newid) {
                    temptext = txtenteremployeeid.getText().toString();
                }
            }
        });
        txtenteremployeeid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66 && event.getAction() == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TextView txtviewdepartment = (TextView) findViewById(R.id.txtviewdepartment);
                            TextView txtviewemployeename = (TextView) findViewById(R.id.txtviewemployeename);
                            TextView txtviewpayrollnumber = (TextView) findViewById(R.id.txtviewpayrollnumber);
                            TextView txtvieweocdate = (TextView) findViewById(R.id.txtvieweocdate);
                            temptext = txtenteremployeeid.getText().toString().trim();
                            employeeid = temptext;
                            newid = false;
                            if (count_no_of_char(employeeid) == 13) {
                                employeeid = delete_zero(employeeid);
                                temptext = employeeid;
                            }
                            if (globalvars.get("ifduplicate").equals("0")) {
                                if (validatepinno()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Duplicate Emp Pins. Please scan Emp No.", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                                    toast.show();
                                    globalvars.set("ifduplicate", "1");
                                    txtenteremployeeid.setText("");
                                    emp_id = null;
                                    txtviewemployeename.setText("");
                                    txtviewdepartment.setText("");
                                    txtviewpayrollnumber.setText("");
                                    txtvieweocdate.setText("");
                                } else {
                                    displayemployeeinfo();
                                    txtenteremployeeid.setText("");
                                    newid = true;
                                    txtenteremployeeid.requestFocus();
                                }
                            } else {
                                if (validateempno()) {

                                    displayemployeeinfo_2();
                                    txtenteremployeeid.setText("");
                                    newid = true;
                                    txtenteremployeeid.requestFocus();
                                    globalvars.set("ifduplicate", "0");

                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Emp No. not found!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                                    toast.show();
                                    globalvars.set("ifduplicate", "0");
                                    txtenteremployeeid.setText("");
                                    emp_id = null;
                                    txtviewemployeename.setText("");
                                    txtviewdepartment.setText("");
                                    txtviewpayrollnumber.setText("");
                                    txtvieweocdate.setText("");
                                }
                            }
                        }
                    }, 500);
                }
                return false;
            }
        });
        txtenteremployeeid.setInputType(InputType.TYPE_NULL);

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stat = "Pending";
                String tagspecial = "";
                if (emp_id != null) {
                    Cursor resultSet = mydatabase.rawQuery("Select max(requestgroup)+1 from employeeitems", null);
                    int requestgroup = 0;
                    if (resultSet != null) {
                        resultSet.moveToNext();
                        requestgroup = resultSet.getInt(0);
                    }
                    if (globalvars.get("usertype").equalsIgnoreCase("6"))     //If Special, automatic Approved status...
                    {
                        //Not Auto-Approve if PK even if special Credit...
                        if (globalvars.get("company_code").equalsIgnoreCase("09") && globalvars.get("bunit_code").equalsIgnoreCase("01")) {
                            stat = "Pending";
                        }
                        if (globalvars.get("company_code").equalsIgnoreCase("03") && globalvars.get("bunit_code").equalsIgnoreCase("32")) {
                            stat = "Pending";
                        } else {
                            stat = "Approved";
                            tagspecial = ",approvedby='SPECIAL'";
                        }
                    } else {
                        for (int a = 0; a <= 8; a++) {
                            if (emp_id.equals(globalvars.manager_emp_id[a])) {
                                stat = "Approved";
                                tagspecial = ",approvedby='Auto Approved'";
                            }
                        }
                    }
                    if(globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("03") ||
                       globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("04") ||
                       globalvars.get("company_code").equals("12") && globalvars.get("bunit_code").equals("05")){
                        Cursor employee = mydatabase.rawQuery("SELECT companyname, businessunit " +
                                "FROM employeemasterfile " +
                                "WHERE emp_id = " + qttext(emp_id), null);
                        employee.moveToFirst();
                        if (employee.getCount() == 1)
                        {
                            globalvars.set("emp_company_name", employee.getString(0));
                            globalvars.set("emp_bunit_name", employee.getString(1));
                        }
                        String a1q = globalvars.get("emp_company_name");
                        String a22 = globalvars.get("emp_bunit_name");

                        Cursor discount_result = mydatabase.rawQuery("SELECT discount " +
                                "FROM employeediscount " +
                                "WHERE company_name = " + qttext(globalvars.get("emp_company_name")) + " AND bunit_name = " + qttext(globalvars.get("emp_bunit_name")), null);
                        discount_result.moveToFirst();
                        String a1 = globalvars.get("emp_company_name");
                        String a2 = globalvars.get("emp_bunit_name");

                        if (discount_result.getCount() == 1)
                        {
//                            String dbl_discount = discount_result.getString(0);
//                            String dbl_discount3 = discount_result.getString(0);
//                            int dbl_discount2 = discount_result.getInt(0);
                            globalvars.set("discount_rate", discount_result.getString(0));
                        }
                        else
                        {
                            globalvars.set("discount_rate", ".20");
                        }

                        Cursor result = mydatabase.rawQuery("SELECT ei.id," +
                                                                    "ei.emp_id," +
                                                                    "ei.itemid," +
                                                                    "ei.item_desc," +
                                                                    "ei.quantity," +
                                                                    "ei.amount," +
                                                                    "ei.amount_discount," +
                                                                    "ei.discount_percentage," +
                                                                    "ei.discount_amt," +
                                                                    "ei.company," +
                                                                    "ei.requestgroup," +
                                                                    "ei.approvedby," +
                                                                    "ei.reqstatus," +
                                                                    "ei.requestdate, " +
                                                                    "ef.discountstatus " +
                                "FROM employeeitems ei\n" +
                                "INNER JOIN ecform ef ON ei.itemid = ef.id\n" +
                                "WHERE emp_id = 'walapa'", null);

                            while (result.moveToNext()) {
                                String emp_items_id = result.getString(0);
                                double qty = Double.parseDouble(result.getString(4));
                                String amount = result.getString(5);
                                String discount_status = result.getString(14);

                                double dbl_discount = Double.parseDouble(globalvars.get("discount_rate"));
                                double dbl_discounted_amt = Double.parseDouble(amount) * dbl_discount;

                                if(discount_status.equalsIgnoreCase("No Discount"))
                                {
                                    mydatabase.execSQL("UPDATE employeeitems set discount_amt=" + qttext(dbl_discounted_amt * qty) + ", discount_percentage=" + qttext(dbl_discount) + ", amount_discount=" + qttext("0.0") + ", emp_id=" + qttext(emp_id) + ",reqstatus=" + qttext(stat) + tagspecial + ",requestgroup=" + qttext(requestgroup) + ",requestdate=strftime('%Y-%m-%d %H:%M:%S','now') where id = " + emp_items_id);
                                }
                                else
                                {
                                    mydatabase.execSQL("UPDATE employeeitems set discount_amt=" + qttext(dbl_discounted_amt * qty) + ", discount_percentage=" + qttext(dbl_discount) + ", amount_discount=" + qttext(dbl_discounted_amt) + ", emp_id=" + qttext(emp_id) + ",reqstatus=" + qttext(stat) + tagspecial + ",requestgroup=" + qttext(requestgroup) + ",requestdate=strftime('%Y-%m-%d %H:%M:%S','now') where id = " + emp_items_id);
                                }
                            }
                    }else{
                        mydatabase.execSQL("UPDATE employeeitems set emp_id=" + qttext(emp_id) + ",reqstatus=" + qttext(stat) + tagspecial + ",requestgroup=" + qttext(requestgroup) + ",requestdate=strftime('%Y-%m-%d %H:%M:%S','now') where emp_id=" + qttext("walapa"));
                    }

                    //mydatabase.execSQL("UPDATE employeeitems_backup set eb_emp_id="+qttext(emp_id)+",eb_reqstatus="+qttext(stat)+ tagspecial +",eb_requestgroup="+qttext(requestgroup)+",eb_requestdate=strftime('%Y-%m-%d %H:%M:%S','now') where eb_emp_id="+qttext("walapa")) ;
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("ecsignature", Context.MODE_PRIVATE);
                    File mypath = new File(directory, emp_id + "_" + requestgroup + ".png");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ScanEmployee3Details.this, ScanEmployee3.class));
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please scan Employee ID.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {     //Trigger after clicking Cancel Button...
            @Override
            public void onClick(View v) {
                TextView txtviewdepartment = (TextView) findViewById(R.id.txtviewdepartment);
                TextView txtviewemployeename = (TextView) findViewById(R.id.txtviewemployeename);
                TextView txtviewpayrollnumber = (TextView) findViewById(R.id.txtviewpayrollnumber);
                TextView txtvieweocdate = (TextView) findViewById(R.id.txtvieweocdate);
                emp_id = null;
                txtviewdepartment.setText("");
                txtviewemployeename.setText("");
                txtviewpayrollnumber.setText("");
                txtvieweocdate.setText("");
            }
        });


        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);    //Style to set in the paint...
        mPaint.setStrokeJoin(Paint.Join.ROUND);   //Set the paint's Join...
        mPaint.setStrokeCap(Paint.Cap.ROUND);   //Set the paint's Cap...
        mPath = new Path();
        thesignature = (ImageView) findViewById(R.id.imgviewscanemployee3detailssignature);
        thesignature.post(new Runnable() {
            @Override
            public void run() {
                mBitmap = Bitmap.createBitmap(thesignature.getWidth(), thesignature.getHeight(), Bitmap.Config.ARGB_8888);    //Returns a mutable bitmap with the specified width and height...
                canvas = new Canvas(mBitmap);


                drawsignature();
            }
        });

        thesignature.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPath.moveTo(event.getX(), event.getY());
                        mPath.lineTo(event.getX() + 1, event.getY() + 1);
                        canvas.drawPath(mPath, mPaint);
                        // thesignature.setImageBitmap(mBitmap);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPath.lineTo(event.getX(), event.getY());
                        canvas.drawPath(mPath, mPaint);
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return true;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                thesignature.setImageBitmap(mBitmap);
                handler.postDelayed(this, 70);
            }
        };
        handler.postDelayed(runnable, 70);

        setfontawesome(R.id.txtviewscanemployee3detailsclearsignature, "\uf014");
        TextView btnclearsignature = (TextView) findViewById(R.id.txtviewscanemployee3detailsclearsignature);


        btnclearsignature.setOnClickListener(new View.OnClickListener() {   //Trigger after clicking  Cancel Button...
            @Override
            public void onClick(View v) {
//                canvas.drawColor(Color.WHITE);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                mBitmap.eraseColor(Color.WHITE);
                mBitmap.eraseColor(Color.TRANSPARENT);
                mPath = new Path();
                drawsignature();
            }
        });
    }


    public void drawsignature() {
        mPaint.setStrokeWidth(1);    //Set the width for stroking...
        mPaint.setColor(Color.GRAY);    //Set the paint's color...
        mPaint.setTextAlign(Paint.Align.CENTER);    //Set the paint's text alignment...
        mPaint.setTextSize(30f);     //Set the paint's text size...
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);    //Set the paint's style...

//        canvas.drawText("S i g n a t u r e",thesignature.getWidth()/2,(thesignature.getHeight()/2)+70,mPaint);

        mPaint.setStyle(Paint.Style.STROKE);     //Set the paint's style...
        mPaint.setColor(Color.RED);      //Set the paint's color...
        mPaint.setStrokeWidth(5);     //Set the width for stroking...
        thesignature.setImageBitmap(mBitmap);   //Sets a Bitmap as the content of this ImageView...
    }

    private Handler handler = new Handler();
    private Paint mPaint;
    Canvas canvas;
    private Path mPath;
    private Bitmap mBitmap;
    private String emp_id;

    //validate if duplicate Pin No...
    public boolean validatepinno() {
        boolean a = false;
        if (!employeeid.equals("")) {
            Cursor resultSet = mydatabase.rawQuery("SELECT * FROM employeemasterfile a WHERE emp_pins = " + qttext(employeeid), null);
            if (resultSet.getCount() > 1)     //If Employee ID found...
            {
                a = true;
            }
        }
        return a;
    }

    //validate if duplicate Emp No...
    public boolean validateempno() {
        boolean a = false;
        if (!employeeid.equals("")) {
            Cursor resultSet = mydatabase.rawQuery("SELECT * FROM employeemasterfile a WHERE emp_no = " + qttext(employeeid), null);
            if (resultSet.getCount() == 1) {
                a = true;
            }
        }
        return a;
    }

    public int count_no_of_char(String emp_pins) {
        int count = 0;

        emp_pins = emp_pins.trim();
        //Counts each character except space.
        for (int i = 0; i < emp_pins.length(); i++) {
            if (emp_pins.charAt(i) != ' ')
                count++;
        }
        return count;
    }

    public String delete_zero(String emp_pins) {
        String final_emp_pins = "";

        //Delete the first character of the String.
        final_emp_pins = emp_pins.substring(1, 13);
        return final_emp_pins;
    }

    public void displayemployeeinfo() {
        TextView txtviewdepartment = (TextView) findViewById(R.id.txtviewdepartment);
        TextView txtviewemployeename = (TextView) findViewById(R.id.txtviewemployeename);
        TextView txtviewpayrollnumber = (TextView) findViewById(R.id.txtviewpayrollnumber);
        TextView txtvieweocdate = (TextView) findViewById(R.id.txtvieweocdate);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String stringToday = df.format(c);

        if (!employeeid.equals("")) {
            Cursor resultSet = mydatabase.rawQuery("SELECT name,companyname,businessunit,deptname,payrollno,a.emp_id,credit_limit-SUM(COALESCE(cost*quantity,0.00)),eoc_date FROM employeemasterfile a LEFT JOIN employeeitems b ON b.emp_id = a.emp_id LEFT JOIN ecform c ON c.id = b.itemid WHERE emp_pins = " + qttext(employeeid), null);
            if (resultSet.getCount() == 1)     //If Employee ID found...
            {
                resultSet.moveToFirst();
                if (resultSet.getString(0) != null) {
                    String employeename = resultSet.getString(0);
                    String department = resultSet.getString(1) + " / " + resultSet.getString(2) + " / " + resultSet.getString(3);
                    String payrollnumber = resultSet.getString(4);
                    String eocdate = resultSet.getString(7);
                    Boolean allow = eocdate.equalsIgnoreCase("REGULAR");    //True if "Regular"...
                    Boolean allow2 = payrollnumber.equalsIgnoreCase("NICO");    //True if "NICO"...
                    Boolean validate_employee = true;
                    String company_code;
                    String bunit_code;
                    String dept_code;
                    String val_com_code;
                    String bu_code;

                    company_code = globalvars.get("company_code");
                    bunit_code = globalvars.get("bunit_code");
                    dept_code = globalvars.get("dept_code");

                    if (company_code.equals("01") && bunit_code.equals("02")) {
                        Cursor query = mydatabase.rawQuery("SELECT company FROM employeeitems WHERE emp_id = 'walapa'", null);
                        while (query.moveToNext()) {
                            val_com_code = query.getString(0);
                            val_com_code = val_com_code.substring(0, 4);
                            Cursor dummyid = mydatabase.rawQuery("SELECT l.loc_bcode FROM employeemasterfile e INNER JOIN locatebusinessunit l ON e.businessunit = l.loc_business_unit WHERE emp_pins = " + qttext(temptext), null);
                            dummyid.moveToFirst();
                            bu_code = dummyid.getString(0).substring(0, 4);
                            if (bu_code.equals(val_com_code)) {
                                validate_employee = false;
                            }
                        }
                    }

                    if (validate_employee) {
                        if (!resultSet.getString(6).equals("null"))         //If Credit Limit != "null"...
                        {
                            if (!allow)     //If not Regular Employee...
                            {
                                if (!allow2)  //If not NICO Employee...
                                {
                                    allow = (getCountOfDays(stringToday, eocdate) > 15);
                                } else {
                                    allow = true;
                                }
                            }
                            if (allow) {
                                if (Double.parseDouble(resultSet.getString(6)) >= totalamount) {
                                    //If Remaining Credit Limit > Credit totalamount...
                                    emp_id = resultSet.getString(5);
                                    txtviewemployeename.setText(employeename);
                                    txtviewdepartment.setText(department);
                                    txtviewpayrollnumber.setText(payrollnumber);
                                    txtvieweocdate.setText(eocdate);
                                } else {
                                    msgtoaster("Your credit exceed your credit limit " + resultSet.getString(6) + ".");
                                }
                            } else {
                                msgtoaster("Credit is not allowed as of this moment. Please check end of contract details.");
                            }
                        } else {
                            msgtoaster("Credit Limit not set.");
                        }
                    } else {
                        msgtoaster("Item`s exclusive only for other BU.");
                    }
                } else {
                    msgtoaster("Employee Pin NOT FOUND.");
                    emp_id = null;
                    txtviewemployeename.setText("");
                    txtviewdepartment.setText("");
                    txtviewpayrollnumber.setText("");
                    txtvieweocdate.setText("");
                }
            } else {
                msgtoaster("Employee Pin NOT FOUND.");
                emp_id = null;
                txtviewemployeename.setText("");
                txtviewdepartment.setText("");
                txtviewpayrollnumber.setText("");
                txtvieweocdate.setText("");
            }
        }
    }

    public void displayemployeeinfo_2() {
        TextView txtviewdepartment = (TextView) findViewById(R.id.txtviewdepartment);
        TextView txtviewemployeename = (TextView) findViewById(R.id.txtviewemployeename);
        TextView txtviewpayrollnumber = (TextView) findViewById(R.id.txtviewpayrollnumber);
        TextView txtvieweocdate = (TextView) findViewById(R.id.txtvieweocdate);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String stringToday = df.format(c);

        if (!employeeid.equals("")) {
            Cursor resultSet = mydatabase.rawQuery("SELECT name,companyname,businessunit,deptname,payrollno,a.emp_id,credit_limit-SUM(COALESCE(cost*quantity,0.00)),eoc_date FROM employeemasterfile a LEFT JOIN employeeitems b ON b.emp_id = a.emp_id LEFT JOIN ecform c ON c.id = b.itemid WHERE emp_no = " + qttext(employeeid), null);
            if (resultSet.getCount() == 1)     //If Employee ID found...
            {
                resultSet.moveToFirst();
                if (resultSet.getString(0) != null) {
                    String employeename = resultSet.getString(0);
                    String department = resultSet.getString(1) + " / " + resultSet.getString(2) + " / " + resultSet.getString(3);
                    String payrollnumber = resultSet.getString(4);
                    String eocdate = resultSet.getString(7);
                    Boolean allow = eocdate.equalsIgnoreCase("REGULAR");    //True if "Regular"...
                    Boolean allow2 = payrollnumber.equalsIgnoreCase("NICO");
                    Boolean validate_employee = true;
                    String company_code;
                    String val_com_code;
                    company_code = globalvars.get("company_code");
                    if (company_code.equals("01")) {
                        Cursor query = mydatabase.rawQuery("SELECT company FROM employeeitems WHERE emp_id = 'walapa'", null);
                        while (query.moveToNext()) {
                            val_com_code = query.getString(0);
                            val_com_code = val_com_code.substring(0, 1);
                            if (company_code.equals(val_com_code)) {
                                validate_employee = false;
                            }
                        }
                    }

                    if (validate_employee) {
                        if (!resultSet.getString(6).equals("null"))         //If Credit Limit != "null"...
                        {
                            if (!allow || !allow2)     //If not Regular Employee or not NICO Employee...
                            {
                                allow = (getCountOfDays(stringToday, eocdate) > 15);
                            }
                            if (allow || allow2) {
                                if (Double.parseDouble(resultSet.getString(6)) >= totalamount) {      //If Remaining Credit Limit > Credit totalamount...
                                    emp_id = resultSet.getString(5);
                                    txtviewemployeename.setText(employeename);
                                    txtviewdepartment.setText(department);
                                    txtviewpayrollnumber.setText(payrollnumber);
                                    txtvieweocdate.setText(eocdate);
                                } else {
                                    msgtoaster("Your credit exceed your credit limit " + resultSet.getString(6) + ".");
                                }
                            } else {
                                msgtoaster("Credit is not allowed as of this moment. Please check end of contract details.");
                            }
                        } else {
                            msgtoaster("Credit Limit not set.");
                        }
                    }
                } else {
                    msgtoaster("Employee No NOT FOUND.");
                    emp_id = null;
                    txtviewemployeename.setText("");
                    txtviewdepartment.setText("");
                    txtviewpayrollnumber.setText("");
                    txtvieweocdate.setText("");
                }
            } else {
                msgtoaster("Employee No NOT FOUND.");
                emp_id = null;
                txtviewemployeename.setText("");
                txtviewdepartment.setText("");
                txtviewpayrollnumber.setText("");
                txtvieweocdate.setText("");
            }
        }
    }

    public Integer getCountOfDays(String firstDateString, String secondDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date firstConvertedDate = null, secondCovertedDate = null, todayWithZeroTime = null;
        try {
            firstConvertedDate = dateFormat.parse(firstDateString);
            secondCovertedDate = dateFormat.parse(secondDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (firstConvertedDate.after(todayWithZeroTime))     //Check if this date is after the specified date...
        {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(firstConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();          //If this date is before the specified date...
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(secondCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount;
    }

    public String qttext(Object txt) {
        return '"' + txt.toString().trim() + '"';
    }

    public void msgtoaster(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
        toast.show();
    }

    public void setfontawesome(int id, String icon) {
        TextView txtview = (TextView) findViewById(id);
        txtview.setTypeface(font);
        txtview.setText(icon);
    }
}
