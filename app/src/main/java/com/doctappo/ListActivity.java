package com.doctappo;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import fragments.ListFragment;
import fragments.MapFragment;
import models.ActiveModels;
import util.GPSTracker;

public class ListActivity extends CommonActivity {

    private GPSTracker gpsTracker;
    private Double cur_latitude, cur_longitude;
    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        allowBack();

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        if (common.getSessionBool("nearby_enable")) {
            gpsTracker = new GPSTracker(this);
            if (gpsTracker.canGetLocation()) {
                if (gpsTracker.getLatitude() != 0.0)
                    cur_latitude = gpsTracker.getLatitude();
                if (gpsTracker.getLongitude() != 0.0)
                    cur_longitude = gpsTracker.getLongitude();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
        if (getIntent().getExtras().containsKey("search")) {
            setHeaderTitle(getIntent().getExtras().getString("search"));
        } else if (ActiveModels.CATEGORY_MODEL != null) {
            setHeaderTitle(ActiveModels.CATEGORY_MODEL.getTitle());
        }

        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                    }
                });

        final ActionBar actionBar = getSupportActionBar();
        // Specify that tabs should be displayed in the action bar.
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

    }

    // set icons on tab
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_map);

    }

    // bind fragment in viewpager
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private String tabTitles[] = new String[]{getString(R.string.tab_list), getString(R.string.tab_map)};

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new ListFragment();
            } else if (i == 1) {
                fragment = new MapFragment();
            }
            if (fragment != null) {
                Bundle args = new Bundle();
                if (getIntent().getExtras().containsKey("cat_id")) {
                    args.putString("cat_id", getIntent().getExtras().getString("cat_id"));
                }
                if (getIntent().getExtras().containsKey("search")) {
                    args.putString("search", getIntent().getExtras().getString("search"));
                }
                if (getIntent().getExtras().containsKey("lat")) {
                    cur_latitude = Double.parseDouble(getIntent().getExtras().getString("lat"));
                }
                if (getIntent().getExtras().containsKey("lon")) {
                    cur_longitude = Double.parseDouble(getIntent().getExtras().getString("lon"));


                }
                if (cur_latitude != null) {
                    args.putString("lat", String.valueOf(cur_latitude));
                }

                if (cur_longitude != null) {
                    args.putString("lon", String.valueOf(cur_longitude));
                }
                if (getIntent().getExtras().containsKey("locality")) {
                    args.putString("locality", getIntent().getExtras().getString("locality"));
                }
                if (getIntent().getExtras().containsKey("locality_id")) {
                    args.putString("locality_id", getIntent().getExtras().getString("locality_id"));
                }
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
