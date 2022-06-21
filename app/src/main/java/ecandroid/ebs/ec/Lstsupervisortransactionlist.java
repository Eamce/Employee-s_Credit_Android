package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bluebook1 on 12/8/2017.
 */

public class Lstsupervisortransactionlist extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater=null;

    public boolean ischecking=false;
    public boolean checkingcheckvalue=false;

    public Lstsupervisortransactionlist(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        data=d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.lstsupervisortransactionlists, null);

        HashMap<String, String> lst;
        lst = data.get(position);

        TextView txtemployeename = (TextView) vi.findViewById(R.id.supervisortxtemployeename);
        TextView txtdepartment = (TextView) vi.findViewById(R.id.supervisortxtdepartmentname);
        TextView txteocdate = (TextView) vi.findViewById(R.id.supervisortxteocdate);
        TextView txtamount = (TextView) vi.findViewById(R.id.supervisortxttotalamount);
        TextView txtstatus = (TextView) vi.findViewById(R.id.supervisortxtstatus);
        CheckBox chkselect = (CheckBox) vi.findViewById(R.id.supervisorchkselect);
        txtemployeename.setText(lst.get("employeename"));
        txtdepartment.setText(lst.get("department"));
        txteocdate.setText("EOC Date: " + lst.get("eoc_date"));
        txtamount.setText(lst.get("totalamount"));
        chkselect.setTag(lst.get("emp_id"));
        txtstatus.setText(lst.get("status"));

       if(ischecking){
           if(checkingcheckvalue && chkselect.isEnabled()) {
               chkselect.setChecked(true);
           }
        else {
               chkselect.setChecked(false);
           }
        }

        if(lst.get("status").equalsIgnoreCase("Approved")){
            txtstatus.setTextColor(ContextCompat.getColor(vi.getContext(),R.color.thegreen));
            chkselect.setEnabled(false);
        } else if(lst.get("status").equalsIgnoreCase("Disapproved")){
            txtstatus.setTextColor(Color.BLUE);
            chkselect.setEnabled(false);
        }
        else{
            txtstatus.setTextColor(Color.RED);
            chkselect.setEnabled(true);
        }
        return vi;
    }
}
