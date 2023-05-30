package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Post;


public class DetailPostActivity extends AppCompatActivity {
    ImageView imgp;
    TextView tglp,contentp,slugp,titlep;
    Post post;
    String title;
    String slug;
    String content;
    String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        imgp = findViewById(R.id.imgpost);
        contentp = findViewById(R.id.contentpost);
        slugp = findViewById(R.id.slugpost);
        titlep = findViewById(R.id.titlepost);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            post = (Post) intent.getSerializableExtra("posts");
            image = post.getImage();
            title = post.getTitle();
            slug = post.getSlug();
            content = post.getContent();
        }
        titlep.setText(title);
        slugp.setText(slug);
        Glide.with(getApplicationContext()).load(AuthServices.getImagePost()+image).into(imgp);
        contentp.setText(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgp.setBackgroundResource(R.drawable.custom_button);
            imgp.setClipToOutline(true);
        }
    }
}