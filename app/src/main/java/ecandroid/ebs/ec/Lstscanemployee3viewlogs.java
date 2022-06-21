package ecandroid.ebs.ec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bluebook1 on 10/18/2017.
 */

public class Lstscanemployee3viewlogs extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater=null;
    private ImageSwitcher sw;

    public Lstscanemployee3viewlogs(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data=d;
    }
    public int getCount()
    {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView==null) {
            vi = inflater.inflate(R.layout.lstviewscanemployee3logs, null, true);
        }
//
        HashMap<String, String> category = new HashMap<String, String>();
        category = data.get(position);

        TextView txtviewemployeename = (TextView) vi.findViewById(R.id.txtviewscanemployee3logsemployeename);
        TextView txtviewdepartment = (TextView) vi.findViewById(R.id.txtviewscanemployee3logsdepartment);
        TextView txttotalamount = (TextView) vi.findViewById(R.id.txtscanemployee3logstotalamount);
        TextView txtcrlimit = (TextView) vi.findViewById(R.id.txtcrlimit);
        txtviewemployeename.setText(category.get("employeename"));
        txtviewdepartment.setText(category.get("department"));
        txttotalamount.setText(category.get("totalamount"));
        txtcrlimit.setText(category.get("limit"));
        txtviewemployeename.setTag(category.get("approvedby")+"|"+category.get("emp_id"));

        Integer stat = 0;

        if (category.get("status").equals("Pending"))
        {
            stat = R.color.statpending;
        }
        else if (category.get("status").equals("Disapproved"))
        {
            stat = R.color.statdisapprove;
        }
        else
        {
            stat = R.color.statapprove;
        }

        vi.setBackgroundColor(vi.getResources().getColor(stat));

        return vi;
    }
}
