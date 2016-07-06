package com.jiayusoft.mobile.utils.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.GlobalData;
import com.jiayusoft.mobile.utils.eventbus.BusProvider;
import com.squareup.okhttp.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ASUS on 2014/12/4.
 */
public class HttpTask extends AsyncTask<Void,Void,String> implements GlobalData {

    ProgressDialog mpDialog;
    Context mContent;
    String msgToShow;

    int httpTpye;
    int mTag;
    String mUrl;
    HashMap<String,String> mBody;

    public HttpTask(Context mContent, String msgToShow, int httpTpye, int mTag,String mUrl, HashMap<String,String> mBody) {
        this.mContent = mContent;
        this.msgToShow = msgToShow;
        this.httpTpye = httpTpye;
        this.mTag = mTag;
        this.mBody = mBody;
        this.mUrl = "http://"+
                PreferenceManager.getDefaultSharedPreferences(mContent).getString(serverUrl, defaultServerUrl)
                + mUrl;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mpDialog = new ProgressDialog(mContent, ProgressDialog.THEME_HOLO_LIGHT);
//        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
        mpDialog.setMessage(msgToShow);
        mpDialog.setCancelable(true);

        mpDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                cancel(true);
            }
        });
        mpDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        Request request = null;
        switch (httpTpye){
            case httpGet:
                request = new Request.Builder().tag(mTag)
                        .url(mUrl)
                        .build();
                break;
            case  httpPost:
                StringBuilder stringBuilder = new StringBuilder("");
                if (mBody!=null){
                    Set<String> keys = mBody.keySet();
                    for (String key : keys){
                        String value = mBody.get(key);
                        if (StringUtils.isNotEmpty(value)){
                            stringBuilder.append(key).append('=').append(value).append('&');
                        }
                    }
                }
                String content = StringUtils.removeEnd(stringBuilder.toString(),"&");
                DebugLog.e(content);
                RequestBody formBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),content);

                request = new Request.Builder().tag(mTag)
                        .url(mUrl)
                        .post(formBody)
                        .build();
                break;
        }
        DebugLog.e(mUrl);
        if (request != null){
            OkHttpClient okHttpClient = BaseApplication.getOkHttpClient();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String temp = response.body().string();
                    DebugLog.e(temp);
                    return temp;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mpDialog.dismiss();
        BusProvider.getInstance().post(new HttpEvent(mTag,result));
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        DebugLog.e("onCancelled");
        BaseApplication.getOkHttpClient().cancel(mTag);

        //todo delete
//        String result = "result";
//        switch (mTag){
//            case tagLogin:
//                BusProvider.getInstance().post(new HttpEvent(mTag,"{\"errorCode\":0,\"errorMsg\":null,\"data\":{\"id\":null,\"name\":null,\"idcard\":null,\"password\":null,\"glksbm\":\"101011,201011\",\"cxlb\":\"1\",\"yyidentiry\":null,\"officecode\":\"101010\",\"bnjyyxq\":7,\"jyyxq\":null}}"));
//                break;
//            case tagSearchLoadMore:
////                BinganDao binganDao = BaseApplication.getDaoSession(mContent).getBinganDao();
////                QueryBuilder qb = binganDao.queryBuilder();
////                List<Bingan> list = qb.list();
////                BaseResponse<List<Bingan>> temp = new BaseResponse<List<Bingan>>(0,null,list);
////                result = new Gson().toJson(temp);
//
////                BusProvider.getInstance().post(new HttpEvent(mTag, "{\"errorCode\":0,\"errorMsg\":null,\"data\":[{\"biaoshima\":\"919006\",\"binganhao\":\"919006\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"朱惠\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-10-30\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921192\",\"binganhao\":\"921192\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"宗玉敏\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-28\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921200\",\"binganhao\":\"921200\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"尹雪\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-28\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921205\",\"binganhao\":\"921205\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"王凯香\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-28\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921191\",\"binganhao\":\"921191\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"商翠竹\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921201\",\"binganhao\":\"921201\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"徐瑶\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921207\",\"binganhao\":\"921207\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"魏承霞\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921206\",\"binganhao\":\"921206\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"牛鲁颜\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921203\",\"binganhao\":\"921203\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"宫玉霞\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921195\",\"binganhao\":\"921195\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"孙重静\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921197\",\"binganhao\":\"921197\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"付佳\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921194\",\"binganhao\":\"921194\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"吴启明\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-27\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921175\",\"binganhao\":\"921175\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"张秀丽\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-26\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921176\",\"binganhao\":\"921176\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"徐桂凤\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-26\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921196\",\"binganhao\":\"921196\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"贺文梅\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-26\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921182\",\"binganhao\":\"921182\",\"zuzhidaima\":\"49554101-9\",\"zuzhiname\":\"山东省妇幼保健院\",\"fenzhijigoubiaosi\":\"\",\"xingming\":\"苏林青\",\"chuyuankeshi\":\"\",\"chuyuanshijian\":\"2013-07-26\",\"zhusu\":\"\",\"shoucang\":false,\"jieyue\":false},{\"biaoshima\":\"921188\",\"binganhao\":\"921188\",\"zuzhidaima\":\"49554101-9\",\"zuzhina\n"));
//
//                break;
//        }
    }
}
