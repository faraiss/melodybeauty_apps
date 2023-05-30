package com.melodybeauty.melody_beauty_apps.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;
import com.melodybeauty.melody_beauty_apps.Model.Kategori;
import com.melodybeauty.melody_beauty_apps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    TabLayout tabLayout;
    ViewPager viewPager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tab_layouts);
        viewPager = view.findViewById(R.id.view_pager);

        AuthServices.kategori(getContext(), new AuthServices.KategoriResponseListener() {
            @Override
            public void onSuccess(List<Kategori> kategoris) {
                setupTabLayout(kategoris);
            }

            @Override
            public void onError(String message) {
                Log.e("Error get kategori", message);
            }
        });
        return view;
    }
    private void setupTabLayout(List<Kategori> kategoris) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.AddFragment(new UntukAndaFragment(), "Untuk Anda");
        adapter.AddFragment(new SemuaFragment(), "Semua");

        int kategoriCount = kategoris.size();
        if (kategoriCount == 3) {
//            adapter.AddFragment(new FragmentKategori1(kategoris.get(0).getId()), kategoris.get(0).getName());
//            adapter.AddFragment(new FragmentKategori2(kategoris.get(1).getId()), kategoris.get(1).getName());
//            adapter.AddFragment(new FragmentKategori3(kategoris.get(2).getId()), kategoris.get(2).getName());
        } else if (kategoriCount == 2) {
//            adapter.AddFragment(new FragmentKategori2(kategoris.get(0).getId()), kategoris.get(0).getName());
//            adapter.AddFragment(new FragmentKategori3(kategoris.get(1).getId()), kategoris.get(1).getName());
        } else if (kategoriCount == 1) {
//            adapter.AddFragment(new FragmentKategori3(kategoris.get(0).getId()), kategoris.get(0).getName());
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        int init = 1;
        TabLayout.Tab initialTab = tabLayout.getTabAt(init);
        if (initialTab != null) {
            initialTab.select();
        }
    }

    private class TabAdapter extends FragmentPagerAdapter {
        //inialisasi array list
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        //buat constructor
        public void AddFragment(Fragment f, String s){
            //tambah fragment
            fragmentArrayList.add(f);
            //tambah string(title)
            stringArrayList.add(s);
        }


        public TabAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //return posisi fragment
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            //return fragment list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //return tab title
            return stringArrayList.get(position);
        }
    }
}