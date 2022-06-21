package ecandroid.ebs.ec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Addtocartitems extends AppCompatActivity {

    private static final String TAG = "Viewdownloadeditems";
    SQLiteDatabase mydatabase;
    Context mContext = this;
    Downloadeditems itemtocart;
    DownloadedStore downloadedstorelist;
    Toolbar mToolbar;
    DownloadedListAdapter2 adapter;
    DownloadedStoreAdapter storeadapter;
    ListView mListView;
    TextView mEmptyView;
    private DownloadedListAdapter2[] langList;
    ArrayList<Downloadeditems> itemlist;
    ArrayList<DownloadedStore> storelist;
    ArrayList<Downloadeditems> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdownloadeditems2);
        mydatabase = openOrCreateDatabase("db.db", MODE_PRIVATE, null);
        Log.d(TAG, "onCreate: Started.");
        mListView = (ListView) findViewById(R.id.list);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
        mEmptyView = (TextView) findViewById(R.id.lstviewitems);

        itemlist = new ArrayList<>();
        Cursor resultSet = mydatabase.rawQuery("SELECT a.code, a.id, a.description, a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining,a.unit,a.cost,a.store,a.expirydate,a.remarks " +
                "FROM ecform a " +
                "LEFT JOIN employeeitems b ON a.id = b.itemid AND b.reqstatus != 'Disapproved' WHERE date(a.enddate) >= date('now') GROUP BY a.id ORDER BY a.description ASC", null);
        // add restriction sa pending items nga dli ma utang

        if (resultSet != null) {
            while (resultSet.moveToNext()) {
                if (!resultSet.getString(3).equalsIgnoreCase("0")) {
                    String itemcode = resultSet.getString(0);
                    String itemid = resultSet.getString(1);
                    String itemdescription = resultSet.getString(2);
                    String itemqty = resultSet.getString(3);
                    String itemunit = resultSet.getString(4);
                    String itemcost = df.format(Double.parseDouble(resultSet.getString(5)));
                    String itemstore = resultSet.getString(6);
                    String[] expdate = resultSet.getString(7).split(" ");
                    String remarks = resultSet.getString(8);
                    File ecpath = mContext.getApplicationContext().getDir("ecimages", Context.MODE_PRIVATE);
                    File theimg = new File(ecpath, itemid + ".png");

                    Downloadeditems itemrow = new Downloadeditems(itemid, itemdescription, getString(R.string.item_details, itemcode, itemqty, itemunit, expdate[0], itemstore, itemcost, remarks), itemqty,
                            "file://" + theimg.toString());
                    itemlist.add(itemrow);
                }
            }
            resultSet.close();

            adapter = new DownloadedListAdapter2(this, R.layout.view_downloaded_row2, itemlist);
            adapter.viewimgclicklistener(new DownloadedListAdapter2.listener() {
                @Override
                public void onimgclick(String id) {
                    Intent newintent = new Intent(getBaseContext(), ImagePreview.class);
                    newintent.putExtra("id", id);
                    startActivity(newintent);
                }
            });
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int a = position;
                    long b = id;

                    if (filteredList == null) {
                        itemtocart = itemlist.get(position);
                    } else {
                        itemtocart = filteredList.get(position);
                    }
//                    String sample1 = itemtocart.getItemid();
//                    String sample2 = itemtocart.getItemname();


                    addtocart(itemtocart.getItemid(), itemtocart.getItemname());
                    //finish();
                }
            });
        }
        mListView.setEmptyView(mEmptyView);
    }

    public void addtocart(String itemid, String itemname) {
        Cursor row = mydatabase.rawQuery("select emp_id,itemid,quantity from employeeitems where itemid=" + qttext(itemid) + " and emp_id=" + qttext("walapa"), null);
        if (row.getCount() == 0) {
            Cursor resultSet = mydatabase.rawQuery("SELECT a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining, a.id , a.cost, a.description, a.bcode " +
                    "FROM ecform a " +
                    "LEFT JOIN employeeitems b ON a.id = b.itemid WHERE a.id=" + qttext(itemid) + " GROUP BY a.id", null);
            resultSet.moveToNext();
            if (Double.parseDouble(resultSet.getString(0)) > 0) {
                mydatabase.execSQL("INSERT INTO employeeitems(emp_id,itemid,item_desc,quantity,amount,company,requestdate) VALUES(" + qttext("walapa") + "," + qttext(itemid) + "," + qttext(resultSet.getString(3)) + "," + qttext(0) + "," + resultSet.getString(2) + "," + qttext(resultSet.getString(4)) + ",strftime('%Y-%m-%d %H:%M:%S','now'))");
                //mydatabase.execSQL("INSERT INTO employeeitems_backup(eb_emp_id,eb_itemid,eb_item_desc,eb_quantity,eb_amount,eb_company,eb_requestdate) VALUES(" + qttext("walapa") + "," + qttext(itemid) + "," + qttext(resultSet.getString(3)) + "," + qttext(0) + "," + resultSet.getString(2) + "," + qttext(resultSet.getString(4)) + ",strftime('%Y-%m-%d %H:%M:%S','now'))");
                msgtoaster(itemname + " has been successfully added!");
            } else {
                msgtoaster("No more stocks available.");
            }
            row.close();
            resultSet.close();
        } else {
            msgtoaster("Item already added.");
        }
    }

    public void msgtoaster(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public String qttext(Object txt) {
        return '"' + txt.toString() + '"';
    }

    public boolean exist_item(String itemid, String emp_id) {
        boolean status = false;
        Cursor row = mydatabase.rawQuery("select * from employeeitems where itemid=" + qttext(itemid) + " and emp_id=" + qttext(emp_id), null);
        if (row.getCount() > 0) {
            status = true;
        }
        return status;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                filter(newText);
                return true;
            }

            private void filter(String text) {
                filteredList = new ArrayList<Downloadeditems>();
                if (text.length() > 0) {
                    for (Downloadeditems la : itemlist) {
                        if (la.getItemname().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                            filteredList.add(la);
                        }
                    }
                } else {
                    filteredList.addAll(itemlist);
                }
                adapter.setData(filteredList);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void openDialog() {
        final BottomSheetDialog dialog;
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_bu, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        ListView camera_sel = (ListView) view.findViewById(R.id.lv_bottomsheet_bu);

        storelist = new ArrayList<>();
        Cursor resultSet = mydatabase.rawQuery("SELECT e.store FROM ecform e GROUP BY e.store", null);

        if (resultSet != null) {
            DownloadedStore static_store = new DownloadedStore("ALL BUSINESS UNIT");
            storelist.add(static_store);
            while (resultSet.moveToNext()) {
                String store = resultSet.getString(0);
                DownloadedStore stores = new DownloadedStore(store);
                storelist.add(stores);
            }
            resultSet.close();

            storeadapter = new DownloadedStoreAdapter(this, R.layout.bottomsheet_bu_content, storelist);

            camera_sel.setAdapter(storeadapter);
            camera_sel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int a = position;
                    long b = id;

                    downloadedstorelist = storelist.get(position);
                    String filteredStore = downloadedstorelist.getStore();
                    getFilteredStores(filteredStore);
                    dialog.dismiss();
                }
            });
        }
        camera_sel.setEmptyView(mEmptyView);

        dialog.show();
    }

    private void getFilteredStores(String filteredStore)
    {
        Cursor resultSet;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        mEmptyView = (TextView) findViewById(R.id.lstviewitems);

        itemlist = new ArrayList<>();
        if(filteredStore.equalsIgnoreCase("ALL BUSINESS UNIT"))
        {
            resultSet = mydatabase.rawQuery("SELECT a.code, a.id, a.description, a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining,a.unit,a.cost,a.store,a.expirydate,a.remarks " +
                    "FROM ecform a " +
                    "LEFT JOIN employeeitems b ON a.id = b.itemid AND b.reqstatus != 'Disapproved' WHERE date(a.enddate) >= date('now') GROUP BY a.id ORDER BY a.description ASC", null);

        }
        else
        {
            resultSet = mydatabase.rawQuery("SELECT a.code, a.id, a.description, a.qty-COALESCE(a.adjqty,0)-COALESCE(SUM(b.quantity),0) remaining,a.unit,a.cost,a.store,a.expirydate,a.remarks " +
                    "FROM ecform a " +
                    "LEFT JOIN employeeitems b ON a.id = b.itemid AND b.reqstatus != 'Disapproved' WHERE date(a.enddate) >= date('now') AND a.store = '" + filteredStore + "' GROUP BY a.id ORDER BY a.description ASC", null);

        }

        if (resultSet != null) {
            while (resultSet.moveToNext()) {
                if (!resultSet.getString(3).equalsIgnoreCase("0")) {
                    String itemcode = resultSet.getString(0);
                    String itemid = resultSet.getString(1);
                    String itemdescription = resultSet.getString(2);
                    String itemqty = resultSet.getString(3);
                    String itemunit = resultSet.getString(4);
                    String itemcost = df.format(Double.parseDouble(resultSet.getString(5)));
                    String itemstore = resultSet.getString(6);
                    String[] expdate = resultSet.getString(7).split(" ");
                    String remarks = resultSet.getString(8);
                    File ecpath = mContext.getApplicationContext().getDir("ecimages", Context.MODE_PRIVATE);
                    File theimg = new File(ecpath, itemid + ".png");

                    Downloadeditems itemrow = new Downloadeditems(itemid, itemdescription, getString(R.string.item_details, itemcode, itemqty, itemunit, expdate[0], itemstore, itemcost, remarks), itemqty,
                            "file://" + theimg.toString());
                    itemlist.add(itemrow);
                }
            }
            resultSet.close();

            adapter = new DownloadedListAdapter2(this, R.layout.view_downloaded_row2, itemlist);
            adapter.viewimgclicklistener(new DownloadedListAdapter2.listener() {
                @Override
                public void onimgclick(String id) {
                    Intent newintent = new Intent(getBaseContext(), ImagePreview.class);
                    newintent.putExtra("id", id);
                    startActivity(newintent);
                }
            });
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int a = position;
                    long b = id;

                    if (filteredList == null) {
                        itemtocart = itemlist.get(position);
                    } else {
                        itemtocart = filteredList.get(position);
                    }
//                    String sample1 = itemtocart.getItemid();
//                    String sample2 = itemtocart.getItemname();


                    addtocart(itemtocart.getItemid(), itemtocart.getItemname());
                    //finish();
                }
            });
        }
        mListView.setEmptyView(mEmptyView);
    }
}

