package cn.edu.zafu.news.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.adapter.NewsPagerAdapter;
import cn.edu.zafu.news.view.PagerSlidingTabStrip;


public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToolbar(null);
        initDrawLayout();
        initViewPager();
    }

    private void initViewPager() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new NewsPagerAdapter(this,getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        initTabsValue();
    }
    /**
     * mPagerSlidingTabStrip默认值配置
     *
     */
    private void initTabsValue() {
        Resources resources=getResources();
        DisplayMetrics metrics=resources.getDisplayMetrics();
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(resources.getColor(R.color.tab_selected_strip));
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(resources.getColor(R.color.tab_background));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                0, metrics));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, metrics));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(resources.getColor(R.color.tab_text_selected));
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(resources.getColor(R.color.tab_text_unselected));
        //设置字体大小
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                13, metrics));
    }

    private void initDrawLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected boolean showNavigationIcon() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
