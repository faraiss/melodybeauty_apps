package com.melodybeauty.melody_beauty_apps;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailProfile extends AppCompatActivity {

    Uri uri;
    ImageView pickUser;
    Bitmap bitmap;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        pickUser = findViewById(R.id.pictUser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pickUser.setBackgroundResource(R.drawable.bg_user);
            pickUser.setClipToOutline(true);
        }
        SharedPreferences preferences = DetailProfile.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        AuthServices.getUserData(getApplicationContext(), token, new AuthServices.UserDataResponseListener() {
            @Override
            public void onSuccess(User user) {
                String image = user.getImage();
                if (image == null){
                    pickUser.setImageResource(R.drawable.placeholder_image);
                } else {
                    Glide.with(getApplicationContext()).load(AuthServices.getImageUser()+image).centerCrop().into(pickUser);
                }
            }

            @Override
            public void onError(String message) {
                Log.e("error get data user" , message);
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Glide.with(DetailProfile.this)
                                .load(bitmap)
                                .centerCrop()
                                .into(pickUser);
                        File file = new File(getCacheDir(), "profile_image.jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Membuat permintaan upload menggunakan OkHttp
                        String url = AuthServices.getApi() + "ufoto"; // Sesuaikan dengan URL backend Anda

                        OkHttpClient client = new OkHttpClient();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                                .build();


                        Request.Builder requestBuilder = new Request.Builder()
                                .url(url)
                                .post(requestBody);
                        SharedPreferences preferences = DetailProfile.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                        String token = preferences.getString("token", "");

                        requestBuilder.addHeader("Authorization", "Bearer " + token);

                        Request request = requestBuilder.build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                // Gagal mengirim permintaan HTTP
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailProfile.this, "Gagal mengirim permintaan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    // Berhasil mengupload foto
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(DetailProfile.this, HomepageActivity.class);
                                            startActivity(i);
                                            finish();
                                            Toast.makeText(DetailProfile.this, "Foto profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Gagal mengupload foto
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailProfile.this, "Foto profil gagal diupdate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        pickUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
    }
}