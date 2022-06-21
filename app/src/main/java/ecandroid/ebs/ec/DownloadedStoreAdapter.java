package ecandroid.ebs.ec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class DownloadedStoreAdapter extends ArrayAdapter<DownloadedStore> implements Filterable {
    private static final String TAG = "DownloadedStore";
    private Context mContext;
    private int mResource;
    public ArrayList<DownloadedStore> languageModelList;

    public interface listener{
        public void onimgclick(String id);
    }
    public DownloadedStoreAdapter.listener listener;
    public void viewimgclicklistener(DownloadedStoreAdapter.listener lst){
        this.listener = lst;
    }

    static class ViewHolder {
        TextView holdstore;
    }

    public DownloadedStoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedStore> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.languageModelList = objects;
    }

    @Override
    public int getCount() {
        return languageModelList.size();
    }

    @Override
    public DownloadedStore getItem(int position) {
        return languageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String store = getItem(position).getStore();

        final View result;
        DownloadedStoreAdapter.ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new DownloadedStoreAdapter.ViewHolder();
            holder.holdstore = (TextView) convertView.findViewById(R.id.tv_store);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DownloadedStoreAdapter.ViewHolder) convertView.getTag();
        }
        result = convertView;


        holder.holdstore.setText(store);

        return convertView;
    }



    public void  setData(ArrayList<DownloadedStore> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
