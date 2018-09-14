package com.example.teng.myapplication;


import android.annotation.SuppressLint;
import android.icu.math.BigDecimal;
import android.icu.util.Currency;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private BottomNavigationView mMainNav;
    private DrawerLayout drawer;
    private FrameLayout mMainFrame;
    private ActionBarDrawerToggle toggle;
    private HomeFragment homeFragment;
    private LoginFragment loginFragment;
    private MyProfileFragment myProfileFragment;
    private SettingFragment settingFragment;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        FindView();
        NewObject();
        //製作BottonNavigation 切換Fragment , 並取消動畫
        makeBottonNavigation();
        //FackBook 記錄應用程式的啟動作業
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void FindView() {
        toolbar = findViewById(R.id.toolbar);
        mMainNav = findViewById(R.id.main_nav);
        mMainFrame = findViewById(R.id.main_farme);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void NewObject() {
        homeFragment = new HomeFragment();
        loginFragment = new LoginFragment();
        myProfileFragment = new MyProfileFragment();
        settingFragment = new SettingFragment();
        //Fragment 預設 HomeFragment
        setFragment(homeFragment);
        //標題選單樣式預設toolbar
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //切換中間的Fragment
    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_farme, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //左側選單內容
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_checkbox) {
            // Handle the camera action
            Toast.makeText(this, "限時折扣活動", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_document) {
            Toast.makeText(this, "訊息通知與公告", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gift) {
            Toast.makeText(this, "瀏覽紀錄", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_item4) {
            Toast.makeText(this, "全站商品", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_item5) {
            Intent intent = new Intent(MainActivity.this, Register.class);
            //取消動畫
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.menu_item6) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            //取消動畫
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeBottonNavigation() {
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_login:
                        setFragment(loginFragment);
                        return true;
                    case R.id.nav_myprofile:
                        setFragment(myProfileFragment);
                        return true;
                    case R.id.nav_setting:
                        setFragment(settingFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        //取消BottonNagivation動畫
        removeBottonNagivationShiftMode(mMainNav);
    }

    //最底層BottomNavigation取消動畫
    @SuppressLint("RestrictedApi")
    static void removeBottonNagivationShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}
