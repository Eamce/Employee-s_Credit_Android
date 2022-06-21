package ecandroid.ebs.ec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;

import java.util.ArrayList;
import java.util.HashMap;

public class Lstbusinessunits extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater = null;
    private ImageSwitcher sw;

    public Lstbusinessunits(Activity a, ArrayList<HashMap<String, String>> d){
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;

        if(convertView==null) {
            vi = inflater.inflate(R.layout.lstbusinessunits, null, true);
        }
//
        HashMap<String, String> category = new HashMap<String, String>();
        category = data.get(position);

        CheckBox cb_businessunit = (CheckBox) vi.findViewById(R.id.cb_businessunit);
        cb_businessunit.setText(category.get("loc_business_unit"));

        Integer stat = 0;

            stat = R.color.white;

        vi.setBackgroundColor(vi.getResources().getColor(stat));
        return vi;
    }

    public ArrayList<HashMap<String,String>> getAllData()
    {
        return data;
    }
}
