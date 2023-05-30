package com.melodybeauty.melody_beauty_apps.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.DetailProfile;
import com.melodybeauty.melody_beauty_apps.Model.User;
import com.melodybeauty.melody_beauty_apps.R;
import com.melodybeauty.melody_beauty_apps.WelcomeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    TextView tv_name, tv_email, tv_jk, tv_jkulit,tv_tgl,tv_nohp,tv_alamat, tv_umur;
    Button btn_logout;
    LinearLayout ll_detail;
    ImageView profile;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        tv_name = view.findViewById(R.id.tvu_name);
        tv_email = view.findViewById(R.id.tvu_email);
        tv_jk = view.findViewById(R.id.tvu_jk);
        tv_jkulit = view.findViewById(R.id.tvu_jkulit);
        tv_tgl = view.findViewById(R.id.tvu_tl);
        tv_nohp = view.findViewById(R.id.tvu_nohp);
        tv_alamat = view.findViewById(R.id.tvu_alamat);
        tv_umur = view.findViewById(R.id.tvu_umur);
        btn_logout = view.findViewById(R.id.btu_logout);
        ll_detail = view.findViewById(R.id.ll_detailprofil);
        profile = view.findViewById(R.id.profileuser);

        //get token user
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        //get data user
        AuthServices.getUserData(getContext(), token, new AuthServices.UserDataResponseListener() {
            @Override
            public void onSuccess(User user) {
                String name = user.getName();
                String email = user.getEmail();
                String jenisKelamin = user.getJenisKelamin();
                String jenisKulit = user.getJenisKulit();
                String tgl = user.getTanggalLahir();
                String nohp = user.getNoHp();
                String alamat = user.getAlamat();
                String image = user.getImage();
                //umur
                Calendar isToday = Calendar.getInstance();
                Calendar isBirth = Calendar.getInstance();
                isBirth.setTimeInMillis(System.currentTimeMillis());

                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    isBirth.setTime(format.parse(tgl));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int age = isToday.get(Calendar.YEAR) - isBirth.get(Calendar.YEAR);
                if (isToday.get(Calendar.DAY_OF_YEAR) < isBirth.get(Calendar.DAY_OF_YEAR)) {
                    age--; // Pengurangan umur jika belum melewati hari lahir di tahun ini
                }
                String umur = String.valueOf(age);
                //set text textview
                tv_name.setText(name);
                tv_email.setText(email);
                tv_jk.setText(jenisKelamin);
                tv_jkulit.setText(jenisKulit);
                tv_tgl.setText(tgl);
                tv_nohp.setText(nohp);
                tv_alamat.setText(alamat);
                tv_umur.setText(umur);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    profile.setBackgroundResource(R.drawable.bg_user);
                    profile.setClipToOutline(true);
                }
                if (image == null){
                    profile.setImageResource(R.drawable.placeholder_image);
                } else {
                    Glide.with(getContext()).load(AuthServices.getImageUser()+image).centerCrop().into(profile);
                }
            }

            @Override
            public void onError(String message) {
                Log.e("get user data error", message);
            }
        });

        //button logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert =new AlertDialog.Builder(getActivity());
                View alertView =LayoutInflater.from(getContext()).inflate(R.layout.popup_logout,
                        (LinearLayout) v.findViewById(R.id.popup_box));
                alert.setView(alertView);
                final AlertDialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button bt_batal, ok;
                bt_batal = alertView.findViewById(R.id.btl_batal);
                ok = alertView.findViewById(R.id.btl_ok);
                bt_batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthServices.logOut(getContext(), token, new AuthServices.LogoutResponseListener() {
                            @Override
                            public void onSuccess(String message) {
                                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove("isLogin");
                                editor.remove("token");
                                editor.apply();
                                dialog.cancel();
                                Toast.makeText(getContext(), "Berhasil Logout", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        ll_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DetailProfile.class);
                startActivity(i);
            }
        });
        return view;
    }
}