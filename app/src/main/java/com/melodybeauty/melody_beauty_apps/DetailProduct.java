package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Product;

public class DetailProduct extends AppCompatActivity {

    TextView tv_name, tv_price,tv_jumlah, tv_description, tv_nameappbar;
    ImageView iv_image;
    Product product;
    String image = "";
    String name = "";
    String price = "";
    String jumlah = "";
    String description = "";
    String nameApp = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        iv_image = findViewById(R.id.ivd_image);

        tv_name = findViewById(R.id.tvd_name);
        tv_price = findViewById(R.id.tvd_price);
        tv_jumlah = findViewById(R.id.tvd_jterjual);
        tv_description = findViewById(R.id.tvd_description);
        tv_nameappbar = findViewById(R.id.nameappbar);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            product = (Product) intent.getSerializableExtra("data");
            image = product.getImage();
            name = product.getName();
            price = product.getPrice();
            jumlah = product.getJumlahTerjual();
            description = product.getDescription();
        }

        tv_name.setText(name);
        Glide.with(getApplicationContext()).load(AuthServices.getImageProduct()+image).into(iv_image);
        tv_price.setText(price);
        tv_jumlah.setText(jumlah);
        tv_description.setText(description);
        tv_nameappbar.setText(name);
    }
}