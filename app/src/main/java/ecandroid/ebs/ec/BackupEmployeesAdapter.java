package ecandroid.ebs.ec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BackupEmployeesAdapter extends ArrayAdapter<BackupEmployees> implements Filterable {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<BackupEmployees> languageModelList;
    static class ViewHolder {
        TextView tv_uploadeddate;
        TextView tv_totalamount;
    }
    public BackupEmployeesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BackupEmployees> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.languageModelList = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        String str_uploadeddate = getItem(position).getUploadeddate();
        String str_totalamount = getItem(position).getTotalamount();
        String strtotalemp = getItem(position).getTotalemp();
        final View result;
        BackupEmployeesAdapter.ViewHolder holder;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new BackupEmployeesAdapter.ViewHolder();
            holder.tv_uploadeddate = (TextView) convertView.findViewById(R.id.tv_uploadeddate);
            holder.tv_totalamount = (TextView) convertView.findViewById(R.id.tv_subname);
            convertView.setTag(holder);
        }
        else
        {
            holder = (BackupEmployeesAdapter.ViewHolder) convertView.getTag();
        }
//        result = convertView;
//
//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.loading_down_anim: R.anim.loading_up_anim);
//        result.startAnimation(animation);
//        lastPosition = position;

        holder.tv_uploadeddate.setText(str_uploadeddate);
        holder.tv_totalamount.setText("Total Amount: PHP " + df.format(Double.parseDouble(str_totalamount)) + " / Total Emp: " + strtotalemp);
        return convertView;
    }


}
