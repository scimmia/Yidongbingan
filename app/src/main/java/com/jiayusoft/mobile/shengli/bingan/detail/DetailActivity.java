package com.jiayusoft.mobile.shengli.bingan.detail;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.GlobalData;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.app.HackyViewPager;
import com.jiayusoft.mobile.utils.app.LockableDrawerLayout;
import com.jiayusoft.mobile.utils.database.Bingan;
import com.jiayusoft.mobile.utils.database.DBHelper;
import com.jiayusoft.mobile.utils.http.BaseResponse;
import com.jiayusoft.mobile.utils.http.HttpEvent;
import com.jiayusoft.mobile.utils.http.HttpTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import uk.co.senab.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends BaseActivity implements GlobalData {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_detail);
    }

    ActionBarDrawerToggle mDrawerToggle;

    @InjectView(R.id.drawer_layout)
    LockableDrawerLayout mDrawerLayout;
    @InjectView(R.id.view_pager)
    HackyViewPager mViewPager;
    @InjectView(R.id.left_drawer)
    ListView mLeftDrawer;
    @InjectView(R.id.marka)
    TextView mMarka;
    @InjectView(R.id.markb)
    TextView mMarkb;
    @InjectView(R.id.markc)
    TextView mMarkc;
    @InjectView(R.id.markd)
    TextView mMarkd;
    @InjectView(R.id.marke)
    TextView mMarke;

    DBHelper mDBHelper;
    ArrayList<String> mItemsList;
    ArrayAdapter<String> mItemsAdapter;
    ArrayList<String> mItemsUrlList;
    int currentPosition;

    String mURLHeader;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DebugLog.e("onCreateOptionsMenu");
        if (!mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            getMenuInflater().inflate(R.menu.menu_detail, menu);
            MenuItem menuItem = menu.findItem(R.id.action_star);
            if (menuItem != null && mDBHelper.isFavourite(mBingan)) {
                menuItem.setChecked(true);
                menuItem.setIcon(R.drawable.ic_star_on);
            }
            MenuItem menuItemLock = menu.findItem(R.id.action_lock);
            if (menuItemLock != null && mViewPager.isLocked()) {
                menuItemLock.setTitle(R.string.menu_unlock);
                menuItemLock.setIcon(R.drawable.ic_lock_on);
            }
            getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.action_star:
                if (item.isCheckable()) {
                    if (item.isChecked()) {
                        item.setChecked(false);
                        item.setIcon(R.drawable.ic_star_off);
                        mDBHelper.removeFavourite(mBingan);
                    } else {
                        item.setChecked(true);
                        item.setIcon(R.drawable.ic_star_on);
                        mDBHelper.addFavourite(mBingan);
                    }
                }
                break;
            case R.id.action_lock:
                DebugLog.e("action_lock");
                mViewPager.toggleLock();
                boolean isLocked = mViewPager.isLocked();
                String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
                item.setTitle(title);
                item.setIcon((isLocked) ? R.drawable.ic_lock_on: R.drawable.ic_lock_off);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    String mBiaoshima;
    Bingan mBingan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.e("onCreate");

        mURLHeader = "http://" +
                PreferenceManager.getDefaultSharedPreferences(getBaseActivity()).getString(serverUrl, defaultServerUrl);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_detail_loading)
                .showImageForEmptyUri(R.drawable.ic_detail_empty)
                .showImageOnFail(R.drawable.ic_detail_empty)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.title_activity_list_detail, // nav drawer open - description for accessibility
                R.string.title_activity_detail // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setLocked(true);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        mImageContent.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);

        mDBHelper = new DBHelper(getBaseActivity());
        mItemsList = new ArrayList<String>();
        mItemsUrlList = new ArrayList<String>();
        mItemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, mItemsList);
        mLeftDrawer.setAdapter(mItemsAdapter);
        mLeftDrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        displayView(0);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                mLeftDrawer.setItemChecked(currentPosition, true);
                mLeftDrawer.setSelection(currentPosition);
                getSupportActionBar().setSubtitle(mItemsList.get(currentPosition));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        String waterMark = BaseApplication.getCurrentUser().getName();
        mMarka.setText(waterMark);
        mMarkb.setText(waterMark);
        mMarkc.setText(waterMark);
        mMarkd.setText(waterMark);
        mMarke.setText(waterMark);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String temp = bundle.getString("bingan");
            if (temp != null) {
                mBingan = new Gson().fromJson(temp, Bingan.class);
                String urlTemp = String.format(binganDetailUrl, mBingan.getZuzhidaima(), mBingan.getBiaoshima());
                if (StringUtils.isNotEmpty(mBingan.getBaomijibie())){
                    urlTemp = urlTemp+"?baomijibie="+mBingan.getBaomijibie();
                }
                new HttpTask(getBaseActivity(), "查询中...", httpGet,
                        tagBinganDetail,
                        urlTemp,
                        null)
                        .execute();
            }
        }
    }

    DisplayImageOptions options;

    SamplePagerAdapter adapter;
    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mItemsUrlList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageLoader.getInstance().displayImage(
                    mURLHeader + String.format(binganDetailImageUrl,mItemsUrlList.get(position)),
                    photoView,options);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Subscribe
    public void onHttpEvent(HttpEvent event) {
        if (event == null || StringUtils.isEmpty(event.getResponse())) {
            showMessage("网络连接错误，请稍后重试。");
        } else {
            int tag = event.getTag();
            switch (tag) {
                case tagBinganDetail:
                    DebugLog.e(event.getResponse());
                    BaseResponse<List<MutablePair<String, String>>> response = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<List<MutablePair<String, String>>>>() {
                    }.getType());
                    switch (response.getErrorCode()) {
                        case 0:
                            if(response.getData()==null || response.getData().size()<=0){
                                showMessage("病案尚未归档！", AppMsg.STYLE_ALERT);
                            }
                            for (MutablePair<String, String> temp : response.getData()) {
                                mItemsList.add(temp.getRight());
                                mItemsUrlList.add(temp.getLeft());
                            }
                            mItemsAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                            displayView(0);
                            break;
                        default:
                            String msg = response.getErrorMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "网络连接错误，请稍后重试。";
                            }
                            showMessage(msg);
                            break;
                    }
//                    List<Bingan> bingans = new Gson().fromJson(event.getResponse(),
//                            new TypeToken<List<Bingan>>() {}.getType());
////                            new TypeToken<List<Bingan>>() {}.getType());
//                    cardItems.addAll(bingans);
//                    mCardAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @OnItemClick(R.id.left_drawer)
    void displayView(int position) {
        DebugLog.e("position" + position);
        if (mItemsList != null && mItemsList.size() > position && position >= 0) {
            currentPosition = position;
            mLeftDrawer.setItemChecked(currentPosition, true);
            mLeftDrawer.setSelection(currentPosition);
            mViewPager.setCurrentItem(currentPosition);
        }
        mDrawerLayout.closeDrawer(mLeftDrawer);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeftDrawer)){
            mDrawerLayout.closeDrawer(mLeftDrawer);
        }else {
            super.onBackPressed();
        }
    }
}

