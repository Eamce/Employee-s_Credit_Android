package ecandroid.ebs.ec;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LstsupervisorAdapter  extends ArrayAdapter<Lstsupervisormod> {
    private final List<Lstsupervisormod> list;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    boolean checkAll_flag = false;
    boolean ischecking = false;
    boolean checkItem_flag = false;
    private View result;

    static class ViewHolder {
        TextView name;
        TextView deptname;
        TextView eocdate;
        TextView totalamount;
        TextView cr_balance;
        TextView status;
        CheckBox chkselect;
    }

    public LstsupervisorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Lstsupervisormod> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String emp_id = getItem(position).getEmp_id();
        String txtemployeename = getItem(position).getTxtemployeename();
        String txtdepartment = getItem(position).getTxtdepartment();
        String txtEOC = getItem(position).getTxtEOC();
        String txtamount = getItem(position).getTxtamount();
        String cr_balance = getItem(position).getTxtbalance();
        String txtstatus = getItem(position).getTxtstatus();

        LstsupervisorAdapter.ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new LstsupervisorAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.supervisortxtemployeename);
            holder.deptname = (TextView) convertView.findViewById(R.id.supervisortxtdepartmentname);
            holder.eocdate = (TextView) convertView.findViewById(R.id.supervisortxteocdate);
            holder.totalamount = (TextView) convertView.findViewById(R.id.supervisortxttotalamount);
            holder.cr_balance = (TextView) convertView.findViewById(R.id.supervisortxtbalance);
            holder.status = (TextView) convertView.findViewById(R.id.supervisortxtstatus);
            holder.chkselect = (CheckBox) convertView.findViewById(R.id.supervisorchkselect);

            holder.chkselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                }
            });
            convertView.setTag(holder);
            convertView.setTag(R.id.supervisorchkselect, holder.chkselect);
        }
        else
        {
            holder = (LstsupervisorAdapter.ViewHolder) convertView.getTag();
        }


        holder.name.setText(txtemployeename);
        holder.deptname.setText(txtdepartment);
        holder.eocdate.setText(txtEOC);
        holder.totalamount.setText("Php " + String.format ("%.2f", Double.parseDouble(txtamount)) + "");
        holder.cr_balance.setText(cr_balance);
        holder.status.setText(txtstatus);
        if(holder.status.getText().equals("Approved"))
        {
            holder.status.setTextColor(ContextCompat.getColor(convertView.getContext(),R.color.thegreen));
            holder.chkselect.setEnabled(false);
        }
        else if(holder.status.equals("Disapproved"))
        {
            holder.status.setTextColor(Color.BLUE);
            holder.chkselect.setEnabled(false);
        }
        else
        {
            holder.status.setTextColor(Color.RED);
            holder.chkselect.setEnabled(true);
        }
        holder.chkselect.setTag(position); // This line is important.
        holder.name.setTag(emp_id); // This line is important.
//
//        viewHolder.text.setText(list.get(position).getName());
        //holder.chkselect.setChecked(list.get(position).isSelected());
        if(ischecking)
        {
            if (checkAll_flag && holder.chkselect.isEnabled())
            {
                holder.chkselect.setChecked(true);
                list.get(position).setSelected(true);
            }
            else
            {
                holder.chkselect.setChecked(false);
                list.get(position).setSelected(false);
            }
        }
        else
        {
            holder.chkselect.setChecked(list.get(position).isSelected());
        }

        holder.chkselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischecking = false;
                checkAll_flag = false;
            }
        });
        result = convertView;

        return result;
    }



}
