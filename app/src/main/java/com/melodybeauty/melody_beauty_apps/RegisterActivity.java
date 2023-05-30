package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Skin;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    TextView tv_signin;
    EditText etr_name,etr_email,etr_pass,etr_con_pass, etr_tgl, etr_nohp, etr_alamat;
    Button btr_signup;
    RadioGroup rg_jk;
    RadioButton rb_jk;
    String formattedDate = "";
    Spinner spinner;
    private DatePickerDialog.OnDateSetListener mDate;
    String selectedId = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());

                etr_tgl.setText(formattedDate);
            }
        };

        iv_back = findViewById(R.id.iv_back);
        tv_signin = findViewById(R.id.tv_sign_in);
        btr_signup = findViewById(R.id.btr_signup);
        rg_jk = findViewById(R.id.rg);
        etr_name = findViewById(R.id.etr_name);
        etr_email = findViewById(R.id.etr_email);
        etr_pass = findViewById(R.id.etr_password);
        etr_con_pass = findViewById(R.id.etr_conn_password);
        etr_tgl = findViewById(R.id.etr_tanggallahir);
        etr_nohp = findViewById(R.id.etr_no_hp);
        etr_alamat = findViewById(R.id.etr_alamat);
        spinner = findViewById(R.id.spinner);

        etr_tgl.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_signin.setOnClickListener(this);
        btr_signup.setOnClickListener(this);

        List<Skin> skins = new ArrayList<>();
        AuthServices.skins(getApplicationContext(), new AuthServices.SkinResponseListener() {
            @Override
            public void onSuccess(List<Skin> skinList) {
                // Konversi daftar objek Skin menjadi daftar nama Skin
                List<String> skinNames = new ArrayList<>();
                for (Skin skin : skinList) {
                    skinNames.add(skin.getName());
                }
                // Update data dalam Spinner dan adapter
                skins.clear();
                skins.addAll(skinList);
                SkinSpinnerAdapter spinnerAdapter = new SkinSpinnerAdapter(RegisterActivity.this, skins);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Skin selectedSkin = (Skin) parent.getItemAtPosition(position);
                        String selectedName = selectedSkin.getName();
                        // Cari objek Skin yang sesuai dengan nama yang dipilih
                        for (Skin skin : skinList) {
                            if (skin.getName().equals(selectedName)) {
                                selectedId = skin.getId();
                                break;
                            }
                        }
                        Log.e("Skin id" , selectedId);
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


    }

    @Override
    public void onClick(View v) {
        if (v == iv_back) {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else if (v == tv_signin){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v == btr_signup){
            String name = etr_name.getText().toString().trim();
            String email = etr_email.getText().toString().trim();
            String password = etr_pass.getText().toString().trim();
            String con_password = etr_con_pass.getText().toString().trim();
            String nohp = etr_nohp.getText().toString().trim();
            String alamat = etr_alamat.getText().toString().trim();

            if (name.isEmpty()) {
                etr_name.setError("Enter your name");
            } else if (email.isEmpty()) {
                etr_email.setError("Enter your email");
            } else if (!isValidEmail(email)) {
                etr_email.setError("Enter a valid email address");
            } else if (password.isEmpty()) {
                etr_pass.setError("Enter your password");
            } else if (con_password.isEmpty()) {
                etr_con_pass.setError("Enter your confirm password");
            } else if (password.length() < 8) {
                etr_pass.setError("Password cannot be less than 8 characters");
            } else if (!password.equals(con_password)) {
                etr_pass.setError("Passwords cannot be different");
                etr_con_pass.setError("Passwords cannot be different");
            } else {
                int radioId = rg_jk.getCheckedRadioButtonId();
                rb_jk = findViewById(radioId);
                String jk = rb_jk.getText().toString().trim();

                AuthServices.register(RegisterActivity.this, name, email, password,jk,selectedId,formattedDate,nohp,alamat, new AuthServices.RegisterResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (v == etr_tgl){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog,
                    mDate, year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }
    public static boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void checkButton(View v){
        int radioInt = rg_jk.getCheckedRadioButtonId();
        rb_jk = findViewById(radioInt);
    }
    public class SkinSpinnerAdapter extends ArrayAdapter<Skin> {

        private LayoutInflater inflater;
        private List<Skin> skinList;

        public SkinSpinnerAdapter(Context context, List<Skin> skinList) {
            super(context, 0, skinList);
            this.skinList = skinList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.names);
            textView.setText(skinList.get(position).getName());
            return convertView;
        }
    }

}