package com.example.teng.myapplication;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    View view ;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();
    }

    private void initComponent(){
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = view.findViewById(R.id.tablayout_fragment_home);
        viewPager = view.findViewById(R.id.viewpager_fragment_home);
        adapter.AddFragment(new LatestNews(),"最新動態");
        adapter.AddFragment(new PopularProduct(),"人氣商品");
        adapter.AddFragment(new ThemeActivity(),"主題活動");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
