package com.example.sproutproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sproutproject.fragment.MetalFragment;
import com.example.sproutproject.fragment.PlanFragment;
import com.example.sproutproject.fragment.ProfileFragment;
import com.example.sproutproject.fragment.SearchFragment;
import com.example.sproutproject.utils.ToolbarUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_title;
    private ViewPager vp_mainPage;
    private LinearLayout ll_botton;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ToolbarUtils toolbarUtils;
    private String[] title;
    private long firstTime = 0;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = findViewById(R.id.textView2);
        vp_mainPage = findViewById(R.id.vp_main);
        ll_botton = findViewById(R.id.bar_main);



        mFragments.add(new SearchFragment());
        mFragments.add(new PlanFragment());
        mFragments.add(new MetalFragment());
        mFragments.add(new ProfileFragment());
        vp_mainPage.setAdapter(new MyPageAdapter(getSupportFragmentManager()));



        toolbarUtils = new ToolbarUtils();
        title = new String[]{"Search", "Plan", "Medal", "Profile"};
        int[] icon_array = {R.drawable.icon_search, R.drawable.icon_plan, R.drawable.icon_metal, R.drawable.icon_profile};
        toolbarUtils.createToolBar(ll_botton,title, icon_array);
        toolbarUtils.changeColor(0);
        initListener();

        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            vp_mainPage.setCurrentItem(3);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {
                showToast("Click once again to exit");
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }

        return super.onKeyUp(keyCode, event);
    }


    private void initListener() {
        vp_mainPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbarUtils.changeColor(position);
                tv_title.setText(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbarUtils.setOnToolBarClickListener(new ToolbarUtils.OnToolBarClickListener() {
            @Override
            public void OnToolBarClick(int position) {
                vp_mainPage.setCurrentItem(position);
            }
        });
    }


    private void showToast(String msg){
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
