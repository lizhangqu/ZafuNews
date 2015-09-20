package cn.edu.zafu.news.ui.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.ui.main.NewsFragment;
import cn.edu.zafu.news.model.Category;

/**
 * Created by lizhangqu on 2015/5/15.
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES;
    private final String[] URLS;
    private static SparseArray<Fragment> fragments;
    public NewsPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        TITLES=context.getResources().getStringArray(R.array.category_title);
        URLS=context.getResources().getStringArray(R.array.category_url);
        fragments=new SparseArray<Fragment>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=fragments.get(position);
        if(fragment==null){
            Category category=new Category(TITLES[position],URLS[position]);
            fragment= NewsFragment.newInstance(category);
            fragments.put(position,fragment);
        }
        return fragment;
    }
}
