package com.example.lcom151_two.veroapp;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageDisplay extends AppCompatActivity {

    ImageView iview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        iview=(ImageView)findViewById(R.id.imageDisplay);

        String url=getIntent().getStringExtra("imgurl");
        Picasso.get()
                .load(url)
                .into(iview);

        iview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    ActivityCompat.finishAfterTransition(ImageDisplay.this);
                }
                return false;
            }
        });
    }
}
