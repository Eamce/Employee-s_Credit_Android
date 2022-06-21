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

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class DownloadedListAdapter2 extends ArrayAdapter<Downloadeditems> implements Filterable {
    private static final String TAG = "DownloadedListAdapter";
    public String langName;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<Downloadeditems> languageModelList;

    public interface listener{
        public void onimgclick(String id);
    }
    public listener listener;
    public void viewimgclicklistener(listener lst){
        this.listener = lst;
    }

    static class ViewHolder {
        TextView holditemname;
        TextView holditemsubname;
        ImageView img;
    }

    public DownloadedListAdapter2(@NonNull Context context, int resource, @NonNull ArrayList<Downloadeditems> objects) {
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
    public Downloadeditems getItem(int position) {
        return languageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        setupImageLoader();

        final String itemid = getItem(position).getItemid();
        String itemname = getItem(position).getItemname();
        String itemsubname = getItem(position).getItemsubname();
        String imgURL = getItem(position).getImgURL();
        final View result;
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.holditemname = (TextView) convertView.findViewById(R.id.txtditemname);
            holder.holditemsubname = (TextView) convertView.findViewById(R.id.txtditemsubname);
            holder.img = (ImageView) convertView.findViewById(R.id.imgdview);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        result = convertView;

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim: R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        ImageLoader imageLoader = ImageLoader.getInstance();
        int defaultImg = mContext.getResources().getIdentifier("@drawable/no_image", null, mContext.getPackageName());
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImg)
                .showImageOnFail(defaultImg)
                .showImageOnLoading(defaultImg).build();

        imageLoader.displayImage(imgURL, holder.img, options);

        holder.holditemname.setText(itemname);
        holder.holditemsubname.setText(itemsubname);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onimgclick(itemid);
            }
        });

        return convertView;
    }

    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    public void  setData(ArrayList<Downloadeditems> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
