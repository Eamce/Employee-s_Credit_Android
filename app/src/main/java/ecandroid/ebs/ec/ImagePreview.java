package ecandroid.ebs.ec;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ImagePreview extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ImageView img = (ImageView) findViewById(R.id.imgimagepreviewpreviewimage);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("ecimages", Context.MODE_PRIVATE);
        String id = getIntent().getStringExtra("id");
        File theimg = new File(directory.getAbsolutePath()+"/"+id+".png");
        img.setImageDrawable(Drawable.createFromPath(theimg.toString()));
    }
}