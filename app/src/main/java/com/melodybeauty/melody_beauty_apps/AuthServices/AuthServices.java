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
import com.melodybeauty.melody_beauty_apps.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AuthServices {
    //connect host url
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";
    private static String ImageProduct = HOST + "/foto/product/";
    private static String ImagePost = HOST + "/foto/post/";

    public static String getImageProduct() {
        return ImageProduct;
    }

    public static String getApi(){return API;}

    public static String getImagePost() {
        return ImagePost;
    }

    //interface listener login
    public interface LoginResponseListener {
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
}
