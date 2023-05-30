package com.melodybeauty.melody_beauty_apps.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Product;
import com.melodybeauty.melody_beauty_apps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentKategori3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentKategori3 extends Fragment {
    private String kategoriId;

    public FragmentKategori3(String kategoriId) {
        this.kategoriId = kategoriId;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentKategori3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentKategori3.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentKategori3 newInstance(String param1, String param2) {
        FragmentKategori3 fragment = new FragmentKategori3();
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

    RecyclerView recyclerView;
    List<Product> productList1 = new ArrayList<>();
    SemuaFragment.CustomAdapterProduct customAdapterProduct;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kategori3, container, false);
        recyclerView = view.findViewById(R.id.rv_kategori3);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        customAdapterProduct = new SemuaFragment.CustomAdapterProduct(new ArrayList<>(), getContext());
        AuthServices.kategoriproduct(getContext(), kategoriId, new AuthServices.ProductResponseListener() {
            @Override
            public void onSuccess(List<Product> productList) {
                customAdapterProduct = new SemuaFragment.CustomAdapterProduct(productList, getContext());
                recyclerView.setAdapter(customAdapterProduct);
                productList1 = productList;
                customAdapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("Kategori 1", message);
            }
        });
        return view;
    }
}