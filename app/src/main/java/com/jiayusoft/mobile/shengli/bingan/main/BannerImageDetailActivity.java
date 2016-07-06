package com.jiayusoft.mobile.shengli.bingan.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import uk.co.senab.photoview.PhotoView;

public class BannerImageDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoView imageView = (PhotoView) findViewById(R.id.banner_img);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String temp = bundle.getString("ImageUrl");
            ImageLoader.getInstance().displayImage(temp, imageView,
                    new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.drawable.logo_sph)
                            .showImageForEmptyUri(R.drawable.logo_sph)
                            .showImageOnFail(R.drawable.logo_sph)
                            .cacheInMemory(true)
                            .cacheOnDisk(false)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build());
        }
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_banner_image_detail);
    }
}
