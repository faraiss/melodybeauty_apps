package com.melodybeauty.melody_beauty_apps.AuthServices;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melodybeauty.melody_beauty_apps.Model.Kategori;
import com.melodybeauty.melody_beauty_apps.Model.Keluhan;
import com.melodybeauty.melody_beauty_apps.Model.Konsultasi;
import com.melodybeauty.melody_beauty_apps.Model.Post;
import com.melodybeauty.melody_beauty_apps.Model.Product;
import com.melodybeauty.melody_beauty_apps.Model.Skin;
import com.melodybeauty.melody_beauty_apps.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AuthServices {
    //connect host url
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";
    private static String ImageProduct = HOST + "/foto/product/";
    private static String ImagePost = HOST + "/foto/post/";
    private static String ImageProfile = HOST + "/foto/user/";

    public static String getImageProduct() {
        return ImageProduct;
    }

    public static String getApi(){return API;}

    public static String getImagePost() {
        return ImagePost;
    }
    public static String getImageUser() {
        return ImageProfile;
    }

    //interface listener login
    public interface LoginResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    public interface SkinResponseListener {
        void onSuccess(List<Skin> response);
        void onError(String message);
    }
    public interface RegisterResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    public interface UserDataResponseListener {
        void onSuccess(User user);
        void onError(String message);
    }
    public interface LogoutResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }
    public interface KategoriResponseListener {
        void onSuccess(List<Kategori> kategoris);
        void onError(String message);
    }
    public interface PostResponseListener {
        void onSuccess(List<Post> posts);
        void onError(String message);
    }
    public interface ProductResponseListener {
        void onSuccess(List<Product> productList);
        void onError(String message);
    }
    public interface AntrianResponseListener {
        void onSuccess(String response);
        void onError(String message);
    }
    public interface KeluhanResponseListener {
        void onSuccess(List<Keluhan> response);
        void onError(String message);
    }
    public interface KonsultasiSelesaiResponseListener {
        void onSuccess(List<Konsultasi> response);
        void onError(String message);
    }
    public interface KonsultasiBelumSelesaiResponseListener {
        void onSuccess(List<Konsultasi> response);
        void onError(String message);
    }

    public static void register(Context context, String name, String email, String password, String jk, String jkulit, String tgl, String no_hp, String alamat, RegisterResponseListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "signup", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    Log.e("Response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("Berhasil Register")) {
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        listener.onSuccess(userObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                if (message.equals("Email sudah terdaftar")) {
                                    listener.onError("Email sudah terdaftar , Silahkan gunakan email yang lain");
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal register: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal register: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("jenis_kelamin", jk);
                params.put("id_kulit", jkulit);
                params.put("tanggal_lahir", tgl);
                params.put("no_hp", no_hp);
                params.put("alamat", alamat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void login(Context context, String email, String pass, LoginResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "signin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("login successfull")){
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putString("token", token);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        listener.onSuccess(userObj);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                if (message.equals("incorrect email")) {
                                    listener.onError("Email Anda Belum Terdaftar");
                                } else if (message.equals("incorrect password")) {
                                    listener.onError("Password Anda Salah");
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal Login: " + e.getMessage());
                            }
                        }
                        else{
                            listener.onError("Gagal Login: network response is null");
                            Log.e("AuthServices", "Error: " + error.getMessage());
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void getUserData(Context context, String token, final UserDataResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "userlogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONObject userObj = jsonObject.getJSONObject("user");
                                String id = userObj.getString("id");
                                String name = userObj.getString("name");
                                String image = userObj.getString("image");
                                String password = userObj.getString("password");
                                String email = userObj.getString("email");
                                String jenisKelamin = userObj.getString("jenis_kelamin");
                                String JenisKulit = userObj.getString("jenis_kulit");
                                String dateStr = userObj.getString("tanggal_lahir");
                                String nohp = userObj.getString("no_hp");
                                String Alamat = userObj.getString("alamat");
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                Date date;
                                String tgl = "";
                                try {
                                    date = inputFormat.parse(dateStr);
                                    tgl = outputFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                User user = new User(id, name,image,email,jenisKelamin, JenisKulit, tgl,nohp,Alamat);
                                listener.onSuccess(user);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan data user: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data user: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void logOut(Context context, String token, LogoutResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, API + "logout", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public static void skins(Context context,final SkinResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "kulit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("skins");
                                List<Skin> skinList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject skinObj = jsonArray.getJSONObject(i);
                                    String id = skinObj.getString("id");
                                    String name = skinObj.getString("name");

                                    Skin skin = new Skin(id, name);
                                    skinList.add(skin);
                                }
                                listener.onSuccess(skinList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan posts: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan posts: network response is null");
                        }
                    }
                }
        ) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void product(Context context,final ProductResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "productall",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Product> productList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject productObj = jsonArray.getJSONObject(i);
                                    String id = productObj.getString("id");
                                    String name = productObj.getString("name");
                                    String image = productObj.getString("image");
                                    String desciption = productObj.getString("description");
                                    String price = productObj.getString("price");
                                    String idKategori = productObj.getString("id_kategori");
                                    String jumlahTerjual = productObj.getString("jumlah_terjual");
                                    String createAtStr = productObj.getString("created_at");
                                    String updateAtStr = productObj.getString("updated_at");

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                                    Date createAt;
                                    Date updateAt;
                                    try {
                                        createAt = sdf.parse(createAtStr);
                                        updateAt = sdf.parse(updateAtStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        continue;
                                    }

                                    Product product = new Product(id, name, image, desciption, price, idKategori, jumlahTerjual, createAt, updateAt);
                                    productList.add(product);

                                }
                                listener.onSuccess(productList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan product: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan product: network response is null");
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //kategori6
    public static void kategori(Context context, final KategoriResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "kategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Kategori> kategoriList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject kategoriObj = jsonArray.getJSONObject(i);
                                    String id = kategoriObj.getString("id");
                                    String name = kategoriObj.getString("name");

                                    Kategori kategori = new Kategori(id, name);
                                    kategoriList.add(kategori);
                                }
                                listener.onSuccess(kategoriList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan kategori: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan kategori: network response is null");
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //product sesuai kategori
    public static void kategoriproduct(Context context,String id,final ProductResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "product/"+ id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("status");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Product> productList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject productObj = jsonArray.getJSONObject(i);
                                    String id = productObj.getString("id");
                                    String name = productObj.getString("name");
                                    String image = productObj.getString("image");
                                    String desciption = productObj.getString("description");
                                    String price = productObj.getString("price");
                                    String idKategori = productObj.getString("id_kategori");
                                    String jumlahTerjual = productObj.getString("jumlah_terjual");
                                    String createAtStr = productObj.getString("created_at");
                                    String updateAtStr = productObj.getString("updated_at");

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                                    Date createAt;
                                    Date updateAt;
                                    try {
                                        createAt = sdf.parse(createAtStr);
                                        updateAt = sdf.parse(updateAtStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        continue;
                                    }

                                    Product product = new Product(id, name, image, desciption, price, idKategori, jumlahTerjual, createAt, updateAt);
                                    productList.add(product);
                                }
                                listener.onSuccess(productList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan product: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan product: network response is null");
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //post user
    public static void post(Context context,final PostResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "posts/user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("Data retrieved successfully")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("posts");
                                List<Post> postList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject postObj = jsonArray.getJSONObject(i);
                                    String id = postObj.getString("id");
                                    String title = postObj.getString("title");
                                    String slug = postObj.getString("slug");
                                    String image = postObj.getString("image");
                                    String content = postObj.getString("content");
                                    String dateStr = postObj.getString("date");
                                    String createAtStr = postObj.getString("created_at");
                                    String updateAtStr = postObj.getString("updated_at");

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date createAt;
                                    Date updateAt;
                                    Date date;
                                    try {
                                        createAt = sdf.parse(createAtStr);
                                        updateAt = sdf.parse(updateAtStr);
                                        date = sdf1.parse(dateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        continue;
                                    }

                                    Post post = new Post(id, title, slug, image, content, date, createAt, updateAt);
                                    postList.add(post);
                                }
                                listener.onSuccess(postList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan posts: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan posts: network response is null");
                        }
                    }
                }
        ) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void antrian(Context context, String token, String tanggal,String id, String detail, final AntrianResponseListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "antrian",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                listener.onSuccess("Berhasil membuat nomor antrian");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Invalid JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
//                                JSONObject jsonObject = new JSONObject(responseBody);
//                                String message = jsonObject.getString("message");
                                listener.onError("Tidak dapat menambah antrian. Masih ada antrian yang belum selesai.");
                            } catch ( UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Failed to update photo: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Failed to update: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tanggal", tanggal);
                params.put("id_keluhan", id);
                params.put("detail_keluhan", detail);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
    public static void keluhan(Context context,final KeluhanResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "keluhan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("keluhan");
                                List<Keluhan> keluhans = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject skinObj = jsonArray.getJSONObject(i);
                                    String id = skinObj.getString("id");
                                    String name = skinObj.getString("name");

                                    Keluhan keluhan = new Keluhan(id, name);
                                    keluhans.add(keluhan);
                                }
                                listener.onSuccess(keluhans);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan posts: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan posts: network response is null");
                        }
                    }
                }
        ) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void konsultasiselesai(Context context, String token, final KonsultasiSelesaiResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "konsultasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Konsultasi> konsultasiList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject dataObj = jsonArray.getJSONObject(i);
                                    JSONObject antrianObj = dataObj.getJSONObject("antrian");
                                    JSONObject keluhanObj = antrianObj.getJSONObject("keluhan");
                                    String id = dataObj.getString("id");
                                    String idAntrian = dataObj.getString("id_antrian");
                                    String hasil = dataObj.getString("hasil_konsultasi");
                                    String idUser = antrianObj.getString("id_user");
                                    String noAntrian = antrianObj.getString("no_antrian");
                                    String tanggal = antrianObj.getString("tanggal");
                                    String status = antrianObj.getString("status");
                                    String detailKeluhan = antrianObj.getString("detail_keluhan");
                                    String name = keluhanObj.getString("name");
                                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                    Date date;
                                    String tgl = "";
                                    try {
                                        date = inputFormat.parse(tanggal);
                                        tgl = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Konsultasi konsultasi = new Konsultasi(id, idAntrian,hasil,idUser, noAntrian, tgl, status,detailKeluhan,name);
                                    konsultasiList.add(konsultasi);
                                }
                                listener.onSuccess(konsultasiList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan data user: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data user: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void konsultasibelumselesai(Context context, String token, final KonsultasiBelumSelesaiResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "konsultasibs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Konsultasi> konsultasiList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject dataObj = jsonArray.getJSONObject(i);
                                    JSONObject antrianObj = dataObj.getJSONObject("antrian");
                                    JSONObject keluhanObj = antrianObj.getJSONObject("keluhan");
                                    String id = dataObj.getString("id");
                                    String idAntrian = dataObj.getString("id_antrian");
                                    String hasil = dataObj.getString("hasil_konsultasi");
                                    String idUser = antrianObj.getString("id_user");
                                    String noAntrian = antrianObj.getString("no_antrian");
                                    String tanggal = antrianObj.getString("tanggal");
                                    String status = antrianObj.getString("status");
                                    String detailKeluhan = antrianObj.getString("detail_keluhan");
                                    String name = keluhanObj.getString("name");
                                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                    Date date;
                                    String tgl = "";
                                    try {
                                        date = inputFormat.parse(tanggal);
                                        tgl = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Konsultasi konsultasi = new Konsultasi(id, idAntrian,hasil,idUser, noAntrian, tgl, status,detailKeluhan,name);
                                    konsultasiList.add(konsultasi);
                                }
                                listener.onSuccess(konsultasiList);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan data user: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data user: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
