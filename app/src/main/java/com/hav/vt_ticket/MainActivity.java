package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hav.vt_ticket.Adapter.ViewPagerAdapter;
import com.hav.vt_ticket.Fragment.HomeFragment;
import com.hav.vt_ticket.Fragment.NotiFragment;
import com.hav.vt_ticket.Fragment.ProfileFragment;
import com.hav.vt_ticket.Fragment.TicketFragment;
import com.hav.vt_ticket.Service.TicketCheckService;

public class MainActivity extends AppCompatActivity {

    private Toast backToast;
    private long pressedBackTime;
    private BottomNavigationView bnv;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        setUpViewPager(viewPager);

        bnv = findViewById(R.id.bottom_nav);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    viewPager.setCurrentItem(0);
                } else if (item.getItemId() == R.id.nav_ticket) {
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.nav_noti) {
                    viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.nav_profile) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
        Intent serviceIntent = new Intent(this, TicketCheckService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bnv.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bnv.getMenu().findItem(R.id.nav_ticket).setChecked(true);
                        break;
                    case 2:
                        bnv.getMenu().findItem(R.id.nav_noti).setChecked(true);
                        break;
                    case 3:
                        bnv.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(pressedBackTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT);
            backToast.show();
        }
        pressedBackTime = System.currentTimeMillis();
    }
}
