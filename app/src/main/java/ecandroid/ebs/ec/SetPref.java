package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SetPref extends AppCompatActivity
{
    Typeface font;
    SQLiteDatabase mydatabase;
    Globalvars globalvars;
    String com1 = "agc1";
    String com2 = "agc2";
    String com23 = "agc3";
    String com24 = "agc4";
    String com3 = "mfi1";
    String com4 = "mfi2";
    String com5 = "others1";
    String com6 = "others2";
    String com61 = "others3";
    String com62 = "others4";
    String com7 = "mfi3";
    String com8 = "mfi4";
    String com81 = "mfi5";
    String com82 = "mfi6";
    String com9 = "asc1";
    String com10 = "asc2";
    String com11 = "asc3";
    String com12 = "asc4";
    Msgbox msgbox;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpref);
        mydatabase = openOrCreateDatabase("db.db",MODE_PRIVATE,null);

        globalvars = new Globalvars((Context)this,(Activity)this);
        font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        Button btnagc1 = (Button) findViewById(R.id.btnagc1);
        Button btnagc2 = (Button) findViewById(R.id.btnagc2);
        Button btnagc3 = (Button) findViewById(R.id.btnagc3);
        Button btnagc4 = (Button) findViewById(R.id.btnagc4);
        Button btnasc1 = (Button) findViewById(R.id.btnasc1);
        Button btnasc2 = (Button) findViewById(R.id.btnasc2);
        Button btnasc3 = (Button) findViewById(R.id.btnasc3);
        Button btnasc4 = (Button) findViewById(R.id.btnasc4);
        Button btnmfi = (Button) findViewById(R.id.btnmfi);
        Button btnmfi2 = (Button) findViewById(R.id.btnmfi2);
        Button btnmfi3 = (Button) findViewById(R.id.btnmfi3);
        Button btnmfi4 = (Button) findViewById(R.id.btnmfi4);
        Button btnmfi5 = (Button) findViewById(R.id.btnmfi5);
        Button btnmfi6 = (Button) findViewById(R.id.btnmfi6);
        Button btnothers1 = (Button) findViewById(R.id.btnothers1);
        Button btnothers2 = (Button) findViewById(R.id.btnothers2);
        Button btnothers3 = (Button) findViewById(R.id.btnothers3);
        Button btnothers4 = (Button) findViewById(R.id.btnothers4);

        setfontawesome(R.id.txtagc1,"\uf0ca");
        setfontawesome(R.id.txtagc2,"\uf0ca");
        setfontawesome(R.id.txtagc3,"\uf0ca");
        setfontawesome(R.id.txtagc4,"\uf0ca");
        setfontawesome(R.id.txtasc1,"\uf0ca");
        setfontawesome(R.id.txtasc2,"\uf0ca");
        setfontawesome(R.id.txtasc3,"\uf0ca");
        setfontawesome(R.id.txtasc4,"\uf0ca");
        setfontawesome(R.id.txtmfi,"\uf0ca");
        setfontawesome(R.id.txtmfi2,"\uf0ca");
        setfontawesome(R.id.txtmfi3,"\uf0ca");
        setfontawesome(R.id.txtmfi4,"\uf0ca");
        setfontawesome(R.id.txtmfi5,"\uf0ca");
        setfontawesome(R.id.txtmfi6,"\uf0ca");
        setfontawesome(R.id.txtldi,"\uf0ca");
        setfontawesome(R.id.txtothers2,"\uf0ca");
        setfontawesome(R.id.txtldi3,"\uf0ca");
        setfontawesome(R.id.txtothers4,"\uf0ca");

        msgbox = new Msgbox(context);

        btnagc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com1);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "AGC1 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnagc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com2);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "AGC2 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnagc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com23);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "AGC3 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnagc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com24);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "AGC4 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });


        btnasc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com9);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "ASC1 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnasc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com10);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "ASC2 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnasc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com11);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "ASC3 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnasc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com12);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "ASC4 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnmfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com3);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 1 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });
        btnmfi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com4);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 2 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnmfi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com7);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 3 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnmfi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com8);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 4 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnmfi5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com81);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 5 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnmfi6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com82);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "MFI 6 Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnothers1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com5);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "LDI, Franchise etc. Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });
        btnothers2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com6);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "LDI, Franchise etc. Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnothers3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com61);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "LDI, Franchise etc. Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });

        btnothers4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("company",com62);
//                if(globalvars.get("company_code").equals("01")) {
//                    globalvars.set("company_status", "0");
//                }
                Toast toast = Toast.makeText(getApplicationContext(), "LDI, Franchise etc. Company has been successfully selected.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
        });
    }

    public void setfontawesome(int id,String icon){
        TextView txtview = (TextView) findViewById(id);
        txtview.setTypeface(font);
        txtview.setText(icon);
    }
    public String qttext(Object txt)
    {
        return '"' + txt.toString() + '"';
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case android.R.id.home:
                //If this activity started from other activity
                finish();

            /*If you wish to open new activity and close this one
            startNewActivity();
            */
                return true;
            case R.id.action_filter:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDialog() {
        final String[] company = {""};
            msgbox.showyesno("Confirmation", "Do you want to view company breakdown?");
            msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
                @Override
                public void onyes() {

                    company[0] = "\nAGC1\n\n" +
                            "HO Accounting Dept \n" +
                            "HO Internal Audit Department\n" +
                            "HO Legal Department\n" +
                            "HO Finance\n" +
                            "HO Benefits\n" +
                            "HO Engineering Department\n" +
                            "HO Payroll Department\n" +
                            "HO Corporate Treasury\n" +
                            "HO Human Resource Department\n" +
                            "GROUP 1 Grocery Group Management\n" +
                            "GROUP 3 Food Group Management\n" +
                            "\n" +
                            "\nAGC2\n\n" +
                            "HO Fixed Asset Department\n" +
                            "HO Marketing\n" +
                            "HO Information Technology\n" +
                            "HO Auxiliary Services\n" +
                            "HO Communications\n" +
                            "HO RESEARCH & DEVELOPMENT\n" +
                            "HO INTERIOR DESIGNS & VISUAL ARTS\n" +
                            "HO CREDIT & COLLECTION DEPARTMENT\n" +
                            "HO General Services Department\n" +
                            "HO SECURITY & SAFETY DEPARTMENT\n" +
                            "HO CEBU EXTENSION OFFICE\n" +
                            "HO Manila Extension Office\n" +
                            "HO Messhall\n" +
                            "HO PHARMACY GROUP\n" +
                            "HO GM OFFICE\n" +
                            "HO Food Safety and Quality-Research and Dev\n" +
                            "HO Dao Substation\n" +
                            "HO PSQM\n" +
                            "HO CFS\n" +
                            "HO Corporate Leasing Department\n" +
                            "GROUP 4 - FARMS\n" +
                            "MOTORPOOL\n" +
                            "\n" +
                            "\nAGC3\n\n" +
                            "HO PLANNING, DESIGNS & CONSTRUCTION\n" +
                            "\n" +
                            "\nAGC4\n\n" +
                            "WAREHOUSES\n" +
                            "LOGISTICS\n" +
                            "\n" +

                            "\nASC1\n\n" +
                            "ASC MAIN\n" +
                            "\n" +

                            "\nASC2\n\n" +
                            "ISLAND CITY MALL\n" +
                            "\n" +

                            "\nASC3\n\n" +
                            "ALTURAS TALIBON\n" +
                            "KAMISETA- ICM\n" +
                            "MOSSIMO- ICM\n" +
                            "GUESS- ICM\n" +
                            "MARCELA HOMES\n" +
                            "HABITAT\n" +
                            "ANAHAW\n" +
                            "ESPUELAS\n" +
                            "\n" +

                            "\nASC4\n\n" +
                            "GRAHAM\n" +
                            "DAMPAS DORM\n" +
                            "DAMPAS COTTAGE\n" +
                            "VAN DORM\n" +
                            "MARIA CLARA\n" +
                            "WHITE HOUSE\n" +
                            "MA. CLARA CONSTRUCTION\n" +
                            "PLANNING & ARCHITECTURE\n" +
                            "GLASS SERVICE- TALIBON\n" +
                            "GLASS SERVICE- TAGBILARAN\n" +
                            "ZAMORA DORMITORY\n" +
                            "ALTA CITTA\n" +
                            "\n" +

                            "\nMFI1\n\n" +
                            "PLAZA MARCELA\n" +
                            "FEEDMILL\n" +
                            "DEMO FARMS / CONTRACT FARMING\n" +
                            "POST HARVEST\n" +
                            "\n" +
                            "\nMFI2\n\n" +
                            "MFI POULTRY BROILER- GROWOUT\n" +
                            "MFI POULTRY BROILER-DRESSING PLANT\n" +
                            "BACONG FARMS\n" +
                            "BANANA PLANTATION\n" +
                            "AGRI FARMS (UBAY)\n" +
                            "AGRI-FARMS (CARMEN)\n" +
                            "AGRI- FARMS (TABALONG)\n" +
                            "COLD STORAGE COMMISSARY\n" +
                            "NOODLES FACTORY\n" +
                            "\n" +
                            "\nMFI3\n\n" +
                            "MFI POULTRY BROILER- GROWOUT\n" +
                            "MFI POULTRY BROILER-DRESSING PLANT\n" +
                            "BACONG FARMS\n" +
                            "BANANA PLANTATION\n" +
                            "AGRI FARMS (UBAY)\n" +
                            "AGRI-FARMS (CARMEN)\n" +
                            "AGRI- FARMS (TABALONG)\n" +
                            "COLD STORAGE COMMISSARY\n" +
                            "NOODLES FACTORY\n" +
                            "\n" +
                            "\nMFI4\n\n" +
                            "COPRA BUYING STATION (UBAY)\n" +
                            "THE PRAWN FARM\n" +
                            "COMMISSARY COMPOUND\n" +
                            "MFI-DRESSING PLANT\n" +
                            "\n" +
                            "\nMFI5\n\n" +
                            "MFI- PIGGERY (CORTES)\n" +
                            "MFI-Slaughterhouse & Meat Cutting Plant I\n" +
                            "MFI - POULTRY LAYER\n" +
                            "MFI POULTRY BROILER-BILAR BREEDER\n" +
                            "MFI POULTRY BROILER-RIZAL BREEDER\n" +
                            "MFI POULTRY BROILER- HATCHERY\n" +
                            "\n" +
                            "\nMFI6\n\n" +
                            "HEAVY EQUIPMENT\n" +
                            "MFI POULTRY BROILER BILAR HATCHERY\n" +
                            "MFI-Slaughterhouse & Meat Cutting Plant II\n" +
                            "MFI POULTRY BROILER - CANHAYUPON BREEDER\n" +
                            "MFI POULTRY BROILER - LAPSAON BREEDER\n" +
                            "\n" +
                            "\nOTHERS1\n\n" +
                            "MFRI- LILA\n" +
                            "MFRI- TANGNAN\n" +
                            "TUBIGON PLANT\n" +
                            "PEANUT KISSES\n" +
                            "CORALANDIA\n" +
                            "\n" +
                            "\nOTHERS2\n\n" +
                            "SOUTH PALMS RESORT PANGLAO\n" +
                            "BOHOL SEA RESORT\n" +
                            "North Zen Villas\n" +
                            "SOUTH FARM\n" +
                            "GREENWICH- PLAZA MARCELA\n" +
                            "JOLLIBEE- PLAZA MARCELA\n" +
                            "JOLLIBEE- ISLAND CITY MALL\n" +
                            "CHOWKING- ISLAND CITY MALL\n" +
                            "CHOWKING- ALTURAS\n" +
                            "ACCOUNTING/FRANCHISE\n" +
                            "WAREHOUSE\n" +
                            "ADMIN-CHOWKING\n" +
                            "WAREHOUSE-CHOWKING\n" +
                            "GREENWICH- ALTURAS MALL\n" +
                            "CHOWKING- ALTA CITTA\n" +
                            "CHOWKING - ALTURAS TALIBON\n" +
                            "\n" +
                            "\nOTHERS3\n\n" +
                            "DISTRIBUTION SALES GROUP\n" +
                            "WHOLESALE DISTRIBUTION GROUP\n" +
                            "\n" +
                            "\nOTHERS4\n\n" +
                            "JOLLIBEE- ALTURAS\n" +
                            "GREENWICH- ISLAND CITY MALL\n" +
                            "RED RIBBON- ALTURAS\n" +
                            "RED RIBBON- ISLAND CITY MALL\n" +
                            "RED RIBBON-TALIBON\n" +
                            "ASC TECH- TAGBILARAN\n" +
                            "ASC TECH- TALIBON\n" +
                            "ABENSON TAGBILARAN - ICM\n" +
                            "ABENSON TAGBILARAN - ASC Main\n" +
                            "Netman\n" +
                            "Nautica Shipping\n" +
                            "Bohol Tech Voc\n" +
                            "MANG INASAL - ISLAND CITY MALL\n" +
                            "JOLLIBEE DRIVE THRU\n" +
                            "JOLLIBEE-TALIBON\n" +
                            "JOLLIBEE ALTA CITTA\n" +
                            "JOLLIBEE-PANGLAO\n" +
                            "NORTH ZEN VILLAS\n" +
                            "\n";

                    msgbox.showyes("List of Companies/Departments", company[0]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
