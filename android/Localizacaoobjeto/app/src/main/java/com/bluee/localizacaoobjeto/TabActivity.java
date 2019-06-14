package com.bluee.localizacaoobjeto;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bluee.localizacaoobjeto.fragments.HomeFragment;
import com.bluee.localizacaoobjeto.fragments.ScanFragment;
import com.bluee.localizacaoobjeto.fragments.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabActivity extends AppCompatActivity implements SettingFragment.SendMessage {
    private static final String TAG = TabActivity.class.getName();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void sendData(String message) {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 0;
        HomeFragment f = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.receive(message);
    }

    /*====================================Adapter=========================================*/


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int tabsCount;
        private Map<String, Item> tabsMap;
        private String[] tabsTags;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            tabsMap = new HashMap<>();

            // create a map with all tabs
            tabsMap.put(getString(R.string.tag_tab1),
                    new Item(getString(R.string.tab_tab1_title), 1));
            tabsMap.put(getString(R.string.tag_tab2),
                    new Item(getString(R.string.tab_tab2_title), 1));
            tabsMap.put(getString(R.string.tag_tab3),
                    new Item(getString(R.string.tab_tab3_title), 1));

            // retrieve tab tags
            tabsTags = getResources().getStringArray(R.array.tab_tags);
            tabsCount = tabsTags.length; // count tab tags
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragment = null;
            String tabTag = getTagByPosition(position);
            if (position == 0) {
                Log.v("fatal","cria tab");
                fragment = new HomeFragment();
            } else if (position == 1) {
                fragment = new  ScanFragment();
            } else if (position == 2) {
                fragment = new SettingFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabsCount;
        }

        private String getTagByPosition(int position) {
            return tabsTags[position];
        }

        private String getTitleByTag(String tag) {
            return tabsMap.get(tag).getTitle();
        }

        private int getIconByTag(String tag) {
            return tabsMap.get(tag).getIcon();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabTag = getTagByPosition(position);
            return getTitleByTag(tabTag);
        }

        private class Item {
            private String title;
            private int icon;

            private Item(String title, int icon) {
                setTitle(title);
                setIcon(icon);
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getIcon() {
                return icon;
            }

            public void setIcon(int icon) {
                this.icon = icon;
            }

            @Override
            public String toString() {
                return "Item{" +
                        "title='" + title + '\'' +
                        ", icon='" + icon + '\'' +
                        '}';
            }
        }
    }
}