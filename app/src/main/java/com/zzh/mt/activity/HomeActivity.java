package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.fragments.CalendarFragment;
import com.zzh.mt.fragments.ClassMateFragment;
import com.zzh.mt.fragments.HomeFragment;
import com.zzh.mt.fragments.MineFragment;
import com.zzh.mt.fragments.NewsActivity;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.MyViewPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/10/26.
 */

public class HomeActivity extends BaseActivity {
    @BindView(R.id.main_page)
    MyViewPage mPage;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> view = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesUtil.getInstance(mContext).getString("userid") == null || TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("userid"))){
            startActivity(new Intent(mContext,LoginActivity.class));
            finish();
        }
        MyApplication.getInstance().add(this);
        hasToolBar(false);
        initBottom();
        getFragment();
        addView();
    }

    private void addView(){
        Fragment home = new HomeFragment();
        Fragment mate = new ClassMateFragment();
        Fragment news = new CalendarFragment();//修 + 日历界面
        Fragment mine = new MineFragment();
        view.add(home);
        view.add(mate);
        view.add(news);
        view.add(mine);

        pagerAdapter  = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return view.get(position);
            }

            @Override
            public int getCount() {
                return view.size();
            }
        };

        mPage.setAdapter(pagerAdapter);
        mPage.setCurrentItem(0);

        mPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int num = mPage.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initBottom(){
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setInActiveColor(R.color.line_color);
        mBottomNavigationBar.setActiveColor(R.color.bottom_color);
        mBottomNavigationBar.setBarBackgroundColor(android.R.color.white);
        List<BottomNavigationItem> items = new ArrayList<>();
        items.add(getItem(R.drawable.index_home,R.string.main_page ));
        items.add(getItem(R.drawable.index_address, R.string.mail_list));
        //修改为日历
        items.add(getItem(R.mipmap.index_calendar, R.string.news));
        //
        items.add(getItem(R.drawable.index_my, R.string.mine));

        for (BottomNavigationItem item : items) {
            mBottomNavigationBar.addItem(item);
        }
        mBottomNavigationBar.setFirstSelectedPosition(0);
        mBottomNavigationBar.initialise();
    }
    private BottomNavigationItem getItem(int icon, int s) {
        return new BottomNavigationItem(icon, s);
    }
    public void getFragment() {
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        mPage.setCurrentItem(0);
                        break;
                    case 1:
                        mPage.setCurrentItem(1);
                        break;
                    case 2:
                        mPage.setCurrentItem(2);
                        break;
                    case 3:
                        mPage.setCurrentItem(3);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) { }

            @Override
            public void onTabReselected(int position) { }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.home_layout;
    }
}
