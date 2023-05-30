package com.melodybeauty.melody_beauty_apps.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.melodybeauty.melody_beauty_apps.DetailPostActivity;
import com.melodybeauty.melody_beauty_apps.Model.Post;
import com.melodybeauty.melody_beauty_apps.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UntukAndaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UntukAndaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UntukAndaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UntukAndaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UntukAndaFragment newInstance(String param1, String param2) {
        UntukAndaFragment fragment = new UntukAndaFragment();
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
    List<Post> postList = new ArrayList<>();
    CustomAdapterPost customAdapterPost;
    LinearLayoutManager linearLayoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_untuk_anda, container, false);
        recyclerView = view.findViewById(R.id.rv_foryou);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        customAdapterPost = new CustomAdapterPost(new ArrayList<>(), getContext());

        //get token user
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        AuthServices.post(getContext(), new AuthServices.PostResponseListener() {
            @Override
            public void onSuccess(List<Post> posts) {
                customAdapterPost = new CustomAdapterPost(posts, getContext());
                recyclerView.setAdapter(customAdapterPost);
                postList = posts;
                customAdapterPost.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("Error get data post", message);
            }
        });
        return view;
    }
    public static class CustomAdapterPost extends RecyclerView.Adapter<CustomAdapterPost.ViewHolder> {
        private List<Post> postList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterPost(List<Post> postList, Context context) {
            this.postList = postList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public CustomAdapterPost.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_post, parent, false);
            return new CustomAdapterPost.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapterPost.ViewHolder holder, int position) {
            holder.title.setText(postList.get(position).getTitle());
            holder.slug.setText(postList.get(position).getSlug());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = dateFormat.format(postList.get(position).getCreatedAt());
            holder.date.setText(formattedDate);
            Glide.with(context).load(AuthServices.getImagePost() + postList.get(position).getImage()).placeholder(R.drawable.placeholder_image).error(R.drawable.error_img).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailPostActivity.class);
                        intent.putExtra("posts", postList.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        public void setProductList(List<Post> postList) {
            this.postList = postList;
        }

        public List<Post> getProductList() {
            return postList;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView title,date, slug;

            public ViewHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.pimage);
                date = itemView.findViewById(R.id.pdate);
                title = itemView.findViewById(R.id.ptitle);
                slug = itemView.findViewById(R.id.pslug);

                slug.setMaxLines(2);
                slug.setEllipsize(TextUtils.TruncateAt.END);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    img.setBackgroundResource(R.drawable.bg_box);
                    img.setClipToOutline(true);
                }
            }
        }
    }
}