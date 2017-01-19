package com.ibeaconmqp.atwaterkentnav;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private static final int REQUEST_OPEN_RESULT_CODE=0;

    private PinchZoomImageView PZIV;
    private Uri imageUri;
    private Animator currentAnimator;
    private int longAnimationDuration;

    private Button but1;
    private Button but2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        //PZIV = (PinchZoomImageView)findViewById(R.id.pinchZoomImageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Toast.makeText(getApplicationContext(), "ImageView long pressed!", Toast.LENGTH_SHORT).show();
                //zoomImageFromThumb();
                //pinchZoomPan();
                return true;
            }
        });

        //init();

        longAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_OPEN_RESULT_CODE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        if(hasFocus){
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if(requestCode == REQUEST_OPEN_RESULT_CODE && resultCode == RESULT_OK){
            if(resultData!=null){
                imageUri = resultData.getData();
                *//*
                try{
                    Bitmap bitmap = getBitmapFromUri(uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e){
                    e.printStackTrace();
                }
                *//*

                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);

            }
        }
    }*/

    private Bitmap getBitmapFromUri(Uri uri)throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;

    }

    private void zoomImageFromThumb(){
        if(currentAnimator != null){
            currentAnimator.cancel();
        }

        Glide.with(this)
                .load(imageUri)
                .into(PZIV);

        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        imageView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds,globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if((float) finalBounds.width()/finalBounds.height()>
                (float) startBounds.width()/startBounds.height()){
            startScale = (float)startBounds.height()/finalBounds.height();
            float startWidth = (float)startScale*finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width())/2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else{
            startScale = (float) startBounds.width()/ finalBounds.width();
            float startHeight = (float)startScale*finalBounds.height();
            float deltaHeight = (startHeight -startBounds.height())/2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        imageView.setAlpha(0f);
        PZIV.setVisibility(View.VISIBLE);

        PZIV.setPivotX(0f);
        PZIV.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(PZIV, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(PZIV, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(PZIV, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(PZIV, View.SCALE_Y, startScale, 1f));
        set.setDuration(longAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

                currentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator=set;

    }

    private void pinchZoomPan(){
        PZIV.setImageUri(imageUri);
        imageView.setAlpha(0.f);
        PZIV.setVisibility(View.VISIBLE);
    }

    private void init(){
        but1 = (Button)findViewById(R.id.but1);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(MainActivity.this, ProfessorTableActivity.class);
                startActivity(nextScreen);
            }
        });

        but2 = (Button)findViewById(R.id.but2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(MainActivity.this, ProfessorTableActivity.class);
                startActivity(nextScreen);
            }
        });
    }
}
