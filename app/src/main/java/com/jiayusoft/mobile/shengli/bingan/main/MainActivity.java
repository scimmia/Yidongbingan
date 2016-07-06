package com.jiayusoft.mobile.shengli.bingan.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import butterknife.InjectView;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.borrow.BorrowListActivity;
import com.jiayusoft.mobile.shengli.bingan.favourite.FavouriteActivity;
import com.jiayusoft.mobile.shengli.bingan.search.SearchActivity;
import com.jiayusoft.mobile.shengli.bingan.search.SearchResultActivity;
import com.jiayusoft.mobile.shengli.bingan.setting.SettingActivity;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.app.clientinfo.ClientinfoAdapter;
import com.jiayusoft.mobile.utils.app.clientinfo.ClientinfoItem;
import com.jiayusoft.mobile.utils.app.viewPager.LoopViewPager;
import com.jiayusoft.mobile.utils.app.viewPager.transforms.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_main);
    }

    ArrayList<ClientinfoItem> iconItems;
    ClientinfoAdapter adapter;

    @InjectView(R.id.grid_main)
    GridView mGridMain;
    @InjectView(R.id.logo_img)
    ImageView mLogoImg;
    @InjectView(R.id.logo_imgs)
    LoopViewPager mLogoImgs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLogoImg.setImageResource(R.drawable.logo_sph);
        String logoName = BaseApplication.getCurrentUser().getLogoName();
        if (StringUtils.isNotEmpty(logoName)){
            String[] logoNames = StringUtils.split(logoName,";");
            if (logoNames!=null){
                if (logoNames.length == 1){
                    String imageUrl = "http://"
                            + PreferenceManager.getDefaultSharedPreferences(getBaseActivity()).getString(serverUrl, defaultServerUrl)
                            + String.format(logoImgUrl, logoNames[0]);
                    ImageLoader.getInstance().displayImage(imageUrl, mLogoImg);
//                    mLogoImg.setTag(imageUrl);
//                    mLogoImg.setOnClickListener(imageClickListener);
                }else{
                    mLogoImgs.setAdapter(new SamplePagerAdapter(logoNames));
                    mLogoImgs.setVisibility(View.VISIBLE);
                    mLogoImg.setVisibility(View.GONE);
                    handler.postDelayed(runnable, TIME); //每隔1s执行
                }
            }
        }
        iconItems = new ArrayList<ClientinfoItem>();
        iconItems.add(new ClientinfoItem(SearchActivity.class, R.drawable.icon_main_chayuebingan, R.string.main_chayuebingan));
        iconItems.add(new ClientinfoItem(FavouriteActivity.class, R.drawable.icon_main_chayuejilu, R.string.main_chayuejilu));
        iconItems.add(new ClientinfoItem(SearchActivity.class, R.drawable.icon_main_jieyuebingan, R.string.main_jieyuebingan));
        iconItems.add(new ClientinfoItem(BorrowListActivity.class, R.drawable.icon_main_jieyuejilu, R.string.main_jieyuejilu));
        iconItems.add(new ClientinfoItem(SearchResultActivity.class, R.drawable.icon_main_person, R.string.main_binganzhikong));
        iconItems.add(new ClientinfoItem(SettingActivity.class, R.drawable.icon_main_setting, R.string.main_shezhi));
        adapter = new ClientinfoAdapter(getBaseActivity(), iconItems);
        mGridMain.setAdapter(adapter);
        mGridMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DebugLog.e("position:" + position + iconItems.get(position).toString());
                Bundle bundle = new Bundle();
                bundle.putInt(itemType, iconItems.get(position).getmTitleId());
                beginActivity(iconItems.get(position).getLaunche(), bundle);
            }
        });
    }

    static int i = 0;
    private int TIME = 3000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME);
                i = (i+1)%TRANSFORM_CLASSES.size();
                DebugLog.e("position---"+i);
                mLogoImgs.setPageTransformer(true, TRANSFORM_CLASSES.get(i).clazz.newInstance());

                int count = mLogoImgs.getAdapter().getCount();
                int index = mLogoImgs.getCurrentItem();
                index = (index+1) % (count); //这里修改过
                mLogoImgs.setCurrentItem(index, true);
                } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
    private static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            showMessage("再按一次退出移动病案!");
        back_pressed = System.currentTimeMillis();
    }

    class SamplePagerAdapter extends PagerAdapter {
        String[] mItemsUrlList;
        public SamplePagerAdapter(String[] urls) {
            mItemsUrlList = urls;
        }

        @Override
        public int getCount() {
            return mItemsUrlList.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView photoView = new ImageView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);

//            PhotoView photoView = new PhotoView(container.getContext());
            String url = "http://"
                    + PreferenceManager.getDefaultSharedPreferences(getBaseActivity()).getString(serverUrl, defaultServerUrl)
                    + String.format(logoImgUrl, mItemsUrlList[position]);
            ImageLoader.getInstance().displayImage(
                    url,
                    photoView,new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.drawable.logo_sph)
                            .showImageForEmptyUri(R.drawable.logo_sph)
                            .showImageOnFail(R.drawable.logo_sph)
                            .cacheInMemory(true)
                            .cacheOnDisk(false)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build());
//            photoView.setTag(url);
//            photoView.setOnClickListener(imageClickListener);
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
//    View.OnClickListener imageClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            DebugLog.e((String)v.getTag());
//            Bundle bundle = new Bundle();
//            bundle.putString("ImageUrl",(String)v.getTag());
//            beginActivity(BannerImageDetailActivity.class, bundle);
//        }
//    };

    private static final class TransformerItem {

        final String title;
        final Class<? extends ViewPager.PageTransformer> clazz;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
            this.clazz = clazz;
            title = clazz.getSimpleName();
        }

        @Override
        public String toString() {
            return title;
        }

    }
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    static {
        TRANSFORM_CLASSES = new ArrayList<>();
        TRANSFORM_CLASSES.add(new TransformerItem(DefaultTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(AccordionTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(BackgroundToForegroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(DepthPageTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipHorizontalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipVerticalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateDownTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateUpTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ScaleInOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(StackTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(TabletTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutSlideTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutTranformer.class));
    }
}
