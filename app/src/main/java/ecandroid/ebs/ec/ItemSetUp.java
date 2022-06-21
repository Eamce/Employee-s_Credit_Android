package ecandroid.ebs.ec;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemSetUp extends AppCompatActivity {

    Button btn_save;
    EditText et_code, et_desc, et_qty, et_unit_cost, et_expiry_date, et_end_date, et_remarks;
    Spinner spin_uom;
    SQLiteDatabase mydatabase;
    String itemcode, description, qty, uom, unitcost, expirydate, startdate, enddate, status, remarks, str_sql, str_startdate;
    Date location;
    Boolean bol;
    Globalvars globalvars;

    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.itemsetup);

        globalvars = new Globalvars(this,this);

        et_code = (EditText)findViewById(R.id.et_itemcode);
        et_desc = (EditText)findViewById(R.id.et_description);
        et_qty = (EditText)findViewById(R.id.et_qty);
        spin_uom = (Spinner)findViewById(R.id.spin_uom);
        et_unit_cost = (EditText)findViewById(R.id.et_unitcost);
        et_expiry_date = (EditText)findViewById(R.id.et_expirydate);
        et_end_date = (EditText)findViewById(R.id.et_enddate);
        et_remarks = (EditText)findViewById(R.id.et_remarks);
        //et_reference = (EditText)findViewById(R.id.et_reference);
        btn_save = (Button) findViewById(R.id.btn_save);

        //Display pending status to Status EditText...
        //et_status.setText("Pending");
        //et_status.setEnabled(false);

        //Display current date to Start Date EditText...
        //final Calendar myCalendar = Calendar.getInstance();
        //SimpleDateFormat date_today = new SimpleDateFormat("yyyy/MM/dd");
        //et_start_date.setText(date_today.format(myCalendar.getTime()));
        //et_start_date.setEnabled(false);

        //Display current date to Start Date EditText...
        final Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat date_today = new SimpleDateFormat("yyyy-MM-dd");
        str_startdate = date_today.format(myCalendar.getTime());

        //Datepicker for End Date EditText...
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            Calendar myCalendar = Calendar.getInstance();
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(bol == true) {
                    updateLabel1();
                }else{
                    updateLabel2();
                }
            }
            private void updateLabel1() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat date_format = new SimpleDateFormat(myFormat, Locale.US);

                et_end_date.setText(date_format.format(myCalendar.getTime()));
            }
            private void updateLabel2() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat date_format = new SimpleDateFormat(myFormat, Locale.US);

                et_expiry_date.setText(date_format.format(myCalendar.getTime()));
            }
        };
        et_end_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bol = true;
                new DatePickerDialog(ItemSetUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //Datepicker for Expiry Date EditText...
        et_expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bol = false;
                new DatePickerDialog(ItemSetUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //SQLite Database Connection...
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);
        //Execute after clicking Save Button...
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            itemcode = et_code.getText().toString().trim();
            description = et_desc.getText().toString().trim();
            qty = et_qty.getText().toString().trim();
            uom = spin_uom.getSelectedItem().toString();
            unitcost = et_unit_cost.getText().toString().trim();
            expirydate = et_expiry_date.getText().toString().trim();
            startdate = str_startdate;
            enddate = et_end_date.getText().toString().trim();
            status = "Pending";
            remarks = et_remarks.getText().toString().trim();
                if(itemcode.isEmpty() || description.isEmpty() || qty.isEmpty() || uom.isEmpty() || unitcost.isEmpty() || expirydate.isEmpty() || startdate.isEmpty() || enddate.isEmpty() || status.isEmpty() || remarks.isEmpty())
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please fill-up all fields!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
                else
                {
                    //Calling AddItem Function...
                    AddItem(mydatabase, itemcode, description, qty, uom, unitcost, expirydate, startdate, enddate, status, remarks);
                    et_code.setText("");
                    et_desc.setText("");
                    et_qty.setText("");
                    et_unit_cost.setText("");
                    et_expiry_date.setText("");
                    et_end_date.setText("");
                    et_remarks.setText("");
                }
            }
            //Adding Item to MYSQLite Database(ECFORM)...
            public void AddItem(SQLiteDatabase mydatabase1, String code1, String desc1, String qty1, String unit1, String cost1, String expirydate1, String startdate1, String enddate1, String status1, String remarks1)
            {
                try
                {
                    //String str_businessunit = "", str_deptname = "";
                    Cursor resultSet = mydatabase.rawQuery("Select businessunit,deptname from employeemasterfile where emp_id = '"+globalvars.get("inchargeempid")+"'",null);
                         if(resultSet.moveToNext()) {
                             String str_businessunit = resultSet.getString(resultSet.getColumnIndex("businessunit"));
                             String str_deptname = resultSet.getString(resultSet.getColumnIndex("deptname"));

                             str_sql = "INSERT INTO ecform (" +
                                     "code," +
                                     "description," +
                                     "qty," +
                                     "unit," +
                                     "cost," +
                                     "store," +
                                     "expirydate," +
                                     "startdate," +
                                     "enddate," +
                                     "status," +
                                     "remarks," +
                                     "reference" +
                                     ")" +
                                     " VALUES(" +
                                     "'" + code1 + "','" + desc1 + "','" + qty1 + "','" + unit1 + "','" + cost1 + "','" + str_businessunit + "-" + str_deptname + "','" + expirydate1 + "','" + startdate1 + "','" + enddate1 + "','" + status1 + "','" + remarks1 +"','Special'" +
                                     ");";
                             mydatabase1.execSQL(str_sql);
                         }
                         else
                         {
                             str_sql = "INSERT INTO ecform (" +
                                     "code," +
                                     "description," +
                                     "qty," +
                                     "unit," +
                                     "cost," +
                                     "store," +
                                     "expirydate," +
                                     "startdate," +
                                     "enddate," +
                                     "status," +
                                     "remarks," +
                                     "reference" +
                                     ")" +
                                     " VALUES(" +
                                     "'" + code1 + "','" + desc1 + "','" + qty1 + "','" + unit1 + "','" + cost1 + "','','" + expirydate1 + "','" + startdate1 + "','" + enddate1 + "','" + status1 + "','" + remarks1 + "','Special'" +
                                     ");";
                             mydatabase1.execSQL(str_sql);
                         }
                         resultSet.close();
                }
                catch (Exception ex)
                {
                    Log.e("AddingItemError:",ex.toString());
                }
                finally
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Item has been successfully saved!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
            }
        });
    }
}
