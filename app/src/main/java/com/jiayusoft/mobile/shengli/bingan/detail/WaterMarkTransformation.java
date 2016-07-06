package com.jiayusoft.mobile.shengli.bingan.detail;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.squareup.picasso.Transformation;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by ASUS on 2014/12/29.
 */
public class WaterMarkTransformation implements Transformation {

    String waterMark;

    public WaterMarkTransformation(String waterMark) {
        this.waterMark = waterMark;
    }

    @Override
    public Bitmap transform(Bitmap src) {
        if (src == null) {
            return null;
        }
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = createBitmap(src.getWidth(), src.getHeight(), src.getConfig());// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        //加入文字
        if(waterMark!=null)
        {
            String familyName ="宋体";
            Typeface font = Typeface.create(familyName,Typeface.BOLD);
            TextPaint textPaint=new TextPaint();
            textPaint.setColor(0x59FF0000);
            textPaint.setTypeface(font);
            textPaint.setTextSize(64);
            //这里是自动换行的
            StaticLayout layout = new StaticLayout(waterMark,textPaint,src.getWidth(), Layout.Alignment.ALIGN_CENTER,1.0F,0.0F,true);
            cv.translate(0,src.getHeight()/4);
            layout.draw(cv);
            cv.translate(0,src.getHeight()/2);
            layout.draw(cv);
            cv.translate(0,src.getHeight()/4*3);
            layout.draw(cv);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        src.recycle();
        return newb;
    }

    @Override
    public String key() {
        return "WaterMarkTransformation";
    }
}
