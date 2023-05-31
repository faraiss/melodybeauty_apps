package com.melodybeauty.melody_beauty_apps;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Skin;
import com.melodybeauty.melody_beauty_apps.Model.User;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    Button button, simpan;
    EditText name,email,nohp,alamat,tanggal;
    Spinner spinner;
    private DatePickerDialog.OnDateSetListener mDate;
    String formattedDate = "";
    private String selectedSkinId;
    String finalSelectedSkinId = selectedSkinId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        pickUser = findViewById(R.id.pictUser);
        name = findViewById(R.id.etl_name);
        email = findViewById(R.id.etl_email);
        nohp = findViewById(R.id.etl_no_hp);
        alamat = findViewById(R.id.etl_alamat);
        tanggal = findViewById(R.id.etl_tanggallahir);
        simpan = findViewById(R.id.bt_simpan);
        spinner = findViewById(R.id.spinner);

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
                String uname = user.getName();
                String uemail = user.getEmail();
                String unohp = user.getNoHp();
                String utanggal = user.getTanggalLahir();
                String ualamat = user.getAlamat();
                selectedSkinId = user.getIdKulit();
                Log.e("selectedSkinId", selectedSkinId);

                name.setText(uname);
                email.setText(uemail);
                nohp.setText(unohp);
                tanggal.setText(utanggal);
                alamat.setText(ualamat);
                finalSelectedSkinId = selectedSkinId;

                List<Skin> skins = new ArrayList<>();
                AuthServices.skins(getApplicationContext(), new AuthServices.SkinResponseListener() {
                    @Override
                    public void onSuccess(List<Skin> skinList) {
                        List<String> skinNames = new ArrayList<>();

                        for (int i = 0; i < skinList.size(); i++) {
                            Skin skin = skinList.get(i);
                            skinNames.add(skin.getName());
                            if (skin.getId().equals(user.getIdKulit())) {
                                selectedSkinId = skin.getId();
                            }
                        }

                        skins.clear();
                        skins.addAll(skinList);
                        RegisterActivity.SkinSpinnerAdapter spinnerAdapter = new RegisterActivity.SkinSpinnerAdapter(DetailProfile.this, skins);
                        spinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();

                        int selectedPosition = -1;
                        for (int i = 0; i < skinList.size(); i++) {
                            Skin skin = skinList.get(i);
                            if (skin.getId().equals(selectedSkinId)) {
                                selectedPosition = i;
                                break;
                            }
                        }
                        spinner.setSelection(selectedPosition);
                        finalSelectedSkinId = selectedSkinId; // Inisialisasi finalSelectedSkinId dengan selectedSkinId yang benar
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Skin selectedSkin = (Skin) parent.getItemAtPosition(position);
                                finalSelectedSkinId = selectedSkin.getId();
                                Log.e("Skin id", finalSelectedSkinId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Tangani jika tidak ada yang dipilih
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Tangani kesalahan
                    }
                });

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
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DetailProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDate, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());

                tanggal.setText(formattedDate);
            }
        };
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
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String nameuser = name.getText().toString().trim();
                String emailuser = email.getText().toString().trim();
                String nohpuser = nohp.getText().toString().trim();
                String tanggallahir = tanggal.getText().toString().trim();
                String alamatuser = alamat.getText().toString().trim();
                String idkulit = finalSelectedSkinId;
                Date date = null;
                try {
                    date = dateFormat.parse(tanggallahir);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String tanggal = outputFormat.format(date);
                AuthServices.updateProfile(getApplicationContext(), token, nameuser, emailuser, idkulit, tanggal, nohpuser, alamatuser, new AuthServices.UpdateProfileResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Berhasil mengupdate profile", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DetailProfile.this, HomepageActivity.class);
                        startActivity(i);
                        finish();
                        AuthServices.getUserData(getApplicationContext(), token, new AuthServices.UserDataResponseListener() {
                            @Override
                            public void onSuccess(User user) {

                            }

                            @Override
                            public void onError(String message) {

                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), "Gagal mengupdate profile", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}