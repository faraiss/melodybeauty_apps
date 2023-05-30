package com.melodybeauty.melody_beauty_apps.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melodybeauty.melody_beauty_apps.AddKonsultasiActivity;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Konsultasi;
import com.melodybeauty.melody_beauty_apps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KonsultasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KonsultasiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KonsultasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KonsultasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KonsultasiFragment newInstance(String param1, String param2) {
        KonsultasiFragment fragment = new KonsultasiFragment();
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

    private LinearLayout ll_konsultasi;
    RecyclerView recyclerViewbs;
    List<Konsultasi> konsultasiList = new ArrayList<>();
    CustomAdapterKonsultasi customAdapter;
    LinearLayoutManager linearLayoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_konsultasi, container, false);

        ll_konsultasi = view.findViewById(R.id.ll_konsultasi);
        recyclerViewbs = view.findViewById(R.id.recyclerbs);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewbs.setLayoutManager(linearLayoutManager);
        recyclerViewbs.setHasFixedSize(true);
        linearLayoutManager = (LinearLayoutManager) recyclerViewbs.getLayoutManager();
        customAdapter = new CustomAdapterKonsultasi(new ArrayList<>(), getContext());

        //get token user
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        AuthServices.konsultasibelumselesai(getContext(), token, new AuthServices.KonsultasiBelumSelesaiResponseListener() {
            @Override
            public void onSuccess(List<Konsultasi> response) {
                customAdapter = new CustomAdapterKonsultasi(response, getContext());
                recyclerViewbs.setAdapter(customAdapter);
                konsultasiList = response;
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("Error", message);
            }
        });

        ll_konsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddKonsultasiActivity.class);
                startActivity(i);
            }
        });

        return  view;
    }
    public static class CustomAdapterKonsultasi extends RecyclerView.Adapter<CustomAdapterKonsultasi.ViewHolder> {
        private List<Konsultasi> konsultasiList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterKonsultasi(List<Konsultasi> konsultasiList, Context context) {
            this.konsultasiList = konsultasiList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public CustomAdapterKonsultasi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_konsultasi, parent, false);
            return new CustomAdapterKonsultasi.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapterKonsultasi.ViewHolder holder, int position) {
            holder.namek.setText(konsultasiList.get(position).getNameKeluhan());
            holder.status.setText(konsultasiList.get(position).getStatus());
            holder.tglk.setText(konsultasiList.get(position).getTanggal());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder alert =new AlertDialog.Builder(context);
                        View alertView =LayoutInflater.from(context).inflate(R.layout.popup_noantrian,
                                (LinearLayout) v.findViewById(R.id.popup_box));
                        alert.setView(alertView);
                        final AlertDialog dialog = alert.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        TextView tvjkonsultasi, tvtgl, tvno;
                        tvjkonsultasi = alertView.findViewById(R.id.kname);
                        tvtgl = alertView.findViewById(R.id.ktanggal);
                        tvno = alertView.findViewById(R.id.knomer);

                        tvjkonsultasi.setText(konsultasiList.get(pos).getNameKeluhan());
                        tvtgl.setText(konsultasiList.get(pos).getTanggal());
                        tvno.setText(konsultasiList.get(pos).getNoAntrian());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return konsultasiList.size();
        }

        public void setProductList(List<Konsultasi> konsultasiList) {
            this.konsultasiList = konsultasiList;
        }

        public List<Konsultasi> getProductList() {
            return konsultasiList;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView namek,tglk,status;

            public ViewHolder(View itemView) {
                super(itemView);
                namek = itemView.findViewById(R.id.kname);
                tglk = itemView.findViewById(R.id.ktgl);
                status = itemView.findViewById(R.id.status);
            }
        }
    }
}