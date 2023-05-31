package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Keluhan;
import com.melodybeauty.melody_beauty_apps.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddKonsultasiActivity extends AppCompatActivity {

    private TextView tv_tgl, tv_name;
    private DatePickerDialog.OnDateSetListener mDate;
    private FrameLayout bt_antrian;
    private EditText et_keluhan;
    String formattedDate = "";
    Spinner spinner;
    String selectedId = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_konsultasi);
        SharedPreferences preferences = AddKonsultasiActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        tv_tgl = findViewById(R.id.tv_tanggal);
        tv_name = findViewById(R.id.nameuser);
        bt_antrian = findViewById(R.id.btn_antrian);
        spinner = findViewById(R.id.spinner);
        et_keluhan = findViewById(R.id.et_detail);
        List<Keluhan> keluhans = new ArrayList<>();
        AuthServices.keluhan(getApplicationContext(), new AuthServices.KeluhanResponseListener() {
            @Override
            public void onSuccess(List<Keluhan> keluhanList) {

                List<String> keluhanNames = new ArrayList<>();
                for (Keluhan keluhan : keluhanList) {
                    keluhanNames.add(keluhan.getName());
                }
                keluhans.clear();
                keluhans.addAll(keluhanList);
                KeluhanSpinnerAdapter keluhanSpinnerAdapter = new KeluhanSpinnerAdapter(getApplicationContext(), keluhans);
                spinner.setAdapter(keluhanSpinnerAdapter);
                keluhanSpinnerAdapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Keluhan selectedKeluhan = (Keluhan) parent.getItemAtPosition(position);
                        String selectedName = selectedKeluhan.getName();
                        // Cari objek Skin yang sesuai dengan nama yang dipilih
                        for (Keluhan keluhan : keluhanList) {
                            if (keluhan.getName().equals(selectedName)) {
                                selectedId = keluhan.getId();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onError(String message) {

            }
        });
        AuthServices.getUserData(getApplicationContext(), token, new AuthServices.UserDataResponseListener() {
            @Override
            public void onSuccess(User user) {
                String name = user.getName();
                tv_name.setText(name);
            }

            @Override
            public void onError(String message) {

            }
        });
        tv_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddKonsultasiActivity.this,
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

                tv_tgl.setText(formattedDate);
            }
        };
        bt_antrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detail = et_keluhan.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(formattedDate);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String tanggal = outputFormat.format(date);
                    SharedPreferences preferences = AddKonsultasiActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                    String token = preferences.getString("token", "");
                    if (formattedDate.equals("")) {
                        Toast.makeText(getApplicationContext(), "Silahkan masukkan tanggal", Toast.LENGTH_SHORT).show();
                    } else if (detail.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Silahkan masukkan detail keluhan anda", Toast.LENGTH_SHORT).show();
                    } else {
                        AuthServices.antrian(getApplicationContext(), token, tanggal, selectedId, detail, new AuthServices.AntrianResponseListener() {
                            @Override
                            public void onSuccess(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public class KeluhanSpinnerAdapter extends ArrayAdapter<Keluhan> {

        private LayoutInflater inflater;
        private List<Keluhan> keluhanList;

        public KeluhanSpinnerAdapter(Context context, List<Keluhan> keluhanList) {
            super(context, 0, keluhanList);
            this.keluhanList = keluhanList;
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
            textView.setText(keluhanList.get(position).getName());

            return convertView;
        }
    }
}