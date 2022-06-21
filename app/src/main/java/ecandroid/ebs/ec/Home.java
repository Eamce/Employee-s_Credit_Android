package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity{
    Typeface font;
    SQLiteDatabase mydatabase;
    Globalvars globalvars;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        globalvars = new Globalvars((Context)this,(Activity)this);
        setContentView(R.layout.activity_home);


        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);

        font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        RelativeLayout rlblue = (RelativeLayout) findViewById(R.id.bluebanner);
        LinearLayout llspecial = (LinearLayout) findViewById(R.id.forspecial);
        Button btndownloaddata = (Button) findViewById(R.id.btndownloaddata);
        Button btnempleveling = (Button) findViewById(R.id.btnemployeeleveling);
        TextView tv_empleveling = (TextView) findViewById(R.id.txtviewemployeeleveling);
        Button btnscanemployee = (Button) findViewById(R.id.btnscanemployee);
        Button btnappendtotextfiels = (Button) findViewById(R.id.btnappendtotextfile);
        TextView tv_setpref = (TextView) findViewById(R.id.txtviewsetpref);
        Button btnviewdwnloadeditems = (Button) findViewById(R.id.btndownloadeditems);
        Button btnsetpref = (Button) findViewById(R.id.btnsetpref);
        setfontawesome(R.id.txtviewdownloaddata,"\uf0ed");
        setfontawesome(R.id.txtviewsetupitems,"\uf0cb");
        setfontawesome(R.id.txtviewstartscan,"\uf02a");
        setfontawesome(R.id.txtviewappendtotextfile,"\uf0ee");
        setfontawesome(R.id.txtviewsetpref,"\uf013");
        setfontawesome(R.id.txtviewemployeeleveling,"\uf022");
//        setfontawesome(R.id.txtviewgeneratereportpercustomer,"\uf022");
        if(globalvars.get("usertype").equals("6")) {
            //uncomment this para makita tong button sa item setup if ever gamiton na
            //rlblue.setVisibility(View.GONE);
            //llspecial.setVisibility(View.VISIBLE);
        }
        //Micheal
        if((globalvars.get("company_code").equals("01")) && (globalvars.get("bunit_code").equals("02"))) {
        }
        //DP
        else if((globalvars.get("company_code").equals("03")) && (globalvars.get("bunit_code").equals("34"))) {
        }
        //RED RIBBON- ALTURAS
        else if((globalvars.get("company_code").equals("12")) && (globalvars.get("bunit_code").equals("03"))) {
        }
        //RED RIBBON- ICM
        else if((globalvars.get("company_code").equals("12")) && (globalvars.get("bunit_code").equals("04"))) {
        }
        //RED RIBBON- TALIBON
        else if((globalvars.get("company_code").equals("12")) && (globalvars.get("bunit_code").equals("05"))) {
        }
        //THE PRAWN FARM-ICM && ALTA-CITTA
        else if((globalvars.get("company_code").equals("03")) && (globalvars.get("bunit_code").equals("32"))) {
        }
        //PEANUT KISSES
        else if((globalvars.get("company_code").equals("09")) && (globalvars.get("bunit_code").equals("01"))) {
        }
        //CDC
        else if((globalvars.get("company_code").equals("01")) && (globalvars.get("bunit_code").equals("07"))) {
        }
        //BSCOM
        else if((globalvars.get("company_code").equals("03")) && (globalvars.get("bunit_code").equals("21"))) {
        }
        //FSCOM
        else if((globalvars.get("company_code").equals("03")) && (globalvars.get("bunit_code").equals("20"))) {
        }
        //Noodles
        else if((globalvars.get("company_code").equals("03")) && (globalvars.get("bunit_code").equals("22"))) {
        }
        //Clinic
        else if((globalvars.get("company_code").equals("01")) && (globalvars.get("bunit_code").equals("01"))|| (globalvars.get("company_code").equals("01")) && (globalvars.get("bunit_code").equals("04"))) {
        }
        else{
                btnsetpref.setVisibility(View.GONE);
                tv_setpref.setVisibility(View.GONE);
                btnempleveling.setVisibility(View.GONE);
                tv_empleveling.setVisibility(View.GONE);
        }
        btndownloaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Downloaddata.class));
            }
        });
        btnviewdwnloadeditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Viewdownloadeditems.class));
            }
        });
        btnscanemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(Home.this,ScanEmployee3.class));
            }
        });
        btnappendtotextfiels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(Home.this,Appendtotextfiles.class));

            }
        });
        btnsetpref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SetPref.class));
            }
        });
        btnempleveling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,EmpLeveling.class));
            }
        });
    }

    public void setfontawesome(int id,String icon){
        TextView txtview = (TextView) findViewById(id);
        txtview.setTypeface(font);
        txtview.setText(icon);
    }
    public String qttext(Object txt){
        return '"' + txt.toString() + '"';
    }
}
