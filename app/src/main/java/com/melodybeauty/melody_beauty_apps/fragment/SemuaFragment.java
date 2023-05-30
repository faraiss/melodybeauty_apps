package com.melodybeauty.melody_beauty_apps.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.DetailProduct;
import com.melodybeauty.melody_beauty_apps.Model.Product;
import com.melodybeauty.melody_beauty_apps.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SemuaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SemuaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SemuaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SemuaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SemuaFragment newInstance(String param1, String param2) {
        SemuaFragment fragment = new SemuaFragment();
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
    CustomAdapterProduct customAdapterProduct;
    LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_semua, container, false);

        recyclerView = view.findViewById(R.id.rv_product);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        customAdapterProduct = new CustomAdapterProduct(new ArrayList<>(), getContext());
        AuthServices.product(getContext(), new AuthServices.ProductResponseListener() {
            @Override
            public void onSuccess(List<Product> productList) {
                customAdapterProduct = new CustomAdapterProduct(productList, getContext());
                recyclerView.setAdapter(customAdapterProduct);
                productList1 = productList;
                customAdapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("Product Error",message);
            }
        });
        return view;
    }


    public static class CustomAdapterProduct extends RecyclerView.Adapter<CustomAdapterProduct.ViewHolder> {
        private List<Product> productList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterProduct(List<Product> productList, Context context) {
            this.productList = productList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_product, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.price.setText(productList.get(position).getPrice());
            holder.jumlah.setText(productList.get(position).getJumlahTerjual());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = dateFormat.format(productList.get(position).getCreatedAt());
            holder.date.setText(formattedDate);
            holder.name.setText(productList.get(position).getName());
            holder.desc.setText(productList.get(position).getDescription());
            Glide.with(context).load(AuthServices.getImageProduct() + productList.get(position).getImage()).placeholder(R.drawable.placeholder_image).error(R.drawable.error_img).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailProduct.class);
                        intent.putExtra("data", productList.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

        public List<Product> getProductList() {
            return productList;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView logo,img;
            TextView price,jumlah,date,name,desc;

            public ViewHolder(View itemView) {
                super(itemView);
                logo = itemView.findViewById(R.id.logo);
                img = itemView.findViewById(R.id.pimage);
                price = itemView.findViewById(R.id.pprice);
                jumlah = itemView.findViewById(R.id.pjumlah);
                date = itemView.findViewById(R.id.ptanggal);
                name = itemView.findViewById(R.id.pname);
                desc = itemView.findViewById(R.id.pdesc);

                desc.setMaxLines(1);
                desc.setEllipsize(TextUtils.TruncateAt.END);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    logo.setBackgroundResource(R.drawable.bg_logo);
                    logo.setClipToOutline(true);
                }
            }
        }
    }
}