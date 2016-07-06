package com.jiayusoft.mobile.shengli.bingan.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.login.UserDoctor;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.GlobalData;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.app.dialog.*;
import com.jiayusoft.mobile.utils.database.DBHelper;
import com.jiayusoft.mobile.utils.database.Office;
import com.jiayusoft.mobile.utils.http.BaseResponse;
import com.jiayusoft.mobile.utils.http.HttpEvent;
import com.jiayusoft.mobile.utils.http.HttpTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseActivity implements GlobalData {

    @InjectView(R.id.shenfenzheng)
    MaterialEditText mShenfenzheng;
    @InjectView(R.id.xingming)
    MaterialEditText mXingming;
    @InjectView(R.id.chuyuanriqi)
    MaterialEditText mChuyuanriqi;
    @InjectView(R.id.chuyuankeshi)
    MaterialEditText mChuyuankeshi;
    @InjectView(R.id.binganhao)
    MaterialEditText mBinganhao;
    @InjectView(R.id.suoshujigou)
    MaterialEditText mSuoshujigou;

    DBHelper mDBHelper;
    int searchType;
    long beginTime;
    long endTime;
    List<Office> guanliOffices;
    ArrayList<Integer> guanliResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchType = bundle.getInt(itemType, -1);
            if (searchType != -1) {
                setTitle(searchType);
            }
        }

        mDBHelper = new DBHelper(getBaseActivity());
        UserDoctor userDoctor = BaseApplication.getCurrentUser();
        String[] chuyuankeshiIDs = StringUtils.split(userDoctor.getGlksbm(), ",");
        //chuyuanriqi
        beginTime = -1;
        endTime = -1;

        switch (searchType){
            case R.string.main_chayuebingan:
                mSuoshujigou.setVisibility(View.GONE);
                if (ArrayUtils.isEmpty(chuyuankeshiIDs)) {
                    if(StringUtils.equals(userDoctor.getCxlb(),"2")){
                        mChuyuankeshi.setVisibility(View.VISIBLE);
                        guanliOffices = mDBHelper.getOffices(userDoctor.getYyidentiry());
                        int size = guanliOffices.size();
                        guanliResults = new ArrayList<Integer>(size);
                        showoffices();
                    }else {
                        mChuyuankeshi.setVisibility(View.GONE);
                    }
                } else {
                    mChuyuankeshi.setVisibility(View.VISIBLE);
                    guanliOffices = mDBHelper.getOffices(userDoctor.getYyidentiry(), chuyuankeshiIDs);
                    int size = guanliOffices.size();
                    guanliResults = new ArrayList<Integer>(size);
                    for (int i = 0; i < size; i++) {
                        guanliResults.add(i);
                    }
                    showoffices();
                }
                break;
            case R.string.main_jieyuebingan:
                mChuyuankeshi.setVisibility(View.GONE);
                mSuoshujigou.setVisibility(View.VISIBLE);
                mSuoshujigou.setTag(userDoctor.getYyidentiry());
                mSuoshujigou.setText(userDoctor.getOrgName());
//                refreshOffices();
                break;
        }
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_search);
    }

    @OnFocusChange(R.id.chuyuanriqi)
    void onchuyuanriqiFocus(boolean focused) {
        if (focused) {
            onchuyuanriqi();
        }
    }

    @OnClick(R.id.chuyuanriqi)
    void onchuyuanriqi() {
        DebugLog.e("chuyuanriqi");
        int maxBeforeDays = getResources().getInteger(R.integer.maxBeforeDays);
        ChooseFromToDatesDialogFragment.newInstance(beginTime, endTime,maxBeforeDays).show(getFragmentManager(), "ChooseFromToDates");
    }

    @Subscribe
    public void onChooseFromToDatesEvent(ChooseFromToDatesEvent event) {
        DebugLog.e(event.getBeginTime() + "---" + event.getEndTime());
        if(event.isCleanFlag()){
            mChuyuanriqi.setText(null);
            beginTime = -1;
            endTime = -1;
        }else{
            beginTime = event.getBeginTime();
            endTime = event.getEndTime();
            mChuyuanriqi.setText(DateFormatUtils.ISO_DATE_FORMAT.format(beginTime)
                    + "~~"
                    + DateFormatUtils.ISO_DATE_FORMAT.format(endTime));
        }
    }

    List<MutablePair<String, String>> orgInfos;
    @OnFocusChange(R.id.suoshujigou)
    void onsuoshujigouFocus(boolean focused) {
        if (focused) {
            onsuoshujigou();
        }
    }

    @OnClick(R.id.suoshujigou)
    void onsuoshujigou() {
        DebugLog.e("onsuoshujigou");
        if (orgInfos!=null && orgInfos.size()>0){
            ChooseOrgDialogFragment.newInstance(
                    orgInfos,
                    mSuoshujigou.getText().toString())
                    .show(getFragmentManager(), "ChooseOrg");
        }else {
            new HttpTask(getBaseActivity(), "查询中...", httpGet, tagGetOrgInfo, getOrgInfoUrl, null)
                    .execute();
        }
    }
    @Subscribe
    public void onChooseOrgEvent(ChooseOrgEvent event){
        if (!StringUtils.equalsIgnoreCase((String) mSuoshujigou.getTag(),event.getId())){
            mSuoshujigou.setText(event.getTitle());
            mSuoshujigou.setTag(event.getId());
//            refreshOffices();
        }
    }
    void refreshOffices(){
        guanliOffices = mDBHelper.getOffices((String) mSuoshujigou.getTag());
        int size = guanliOffices.size();
        guanliResults = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            guanliResults.add(i);
        }
        showoffices();
    }

    @OnFocusChange(R.id.chuyuankeshi)
    void onchuyuankeshiFocus(boolean focused) {
        if (focused) {
            onchuyuankeshi();
        }
    }

    @OnClick(R.id.chuyuankeshi)
    void onchuyuankeshi() {
        DebugLog.e("chuyuankeshi");
        ChooseOfficesDialogFragment.newInstance(guanliOffices, guanliResults).show(getFragmentManager(), "ChooseGuanliKeshis");
    }

    @Subscribe
    public void onChooseOfficesEvent(ChooseOfficesEvent event) {
        DebugLog.e(event.getResults().toString());
        guanliResults = event.getResults();
        showoffices();
    }

    private void showoffices() {
        if(guanliResults!=null){
            StringBuilder stringBuilder = new StringBuilder();
            int maxCount = 5;
            int count = Math.min(maxCount,guanliResults.size());
            for (int i=0;i<count;i++){
                stringBuilder.append(guanliOffices.get(guanliResults.get(i)).getName()).append('\n');
            }
            if (maxCount < guanliResults.size()){
                stringBuilder.append("等").append(guanliResults.size()).append("项，点击查看详细");
            }
            mChuyuankeshi.setText(StringUtils.removeEnd(stringBuilder.toString(), "\n"));
        }
    }

    void onliyuanfangshiFocus(boolean focused) {
        if (focused) {
            onliyuanfangshi();
        }
    }

    private String[] liyuanfangshiItems = {"医嘱离院", "医嘱转院", "医嘱转社区卫生服务机构/乡镇卫", "非医嘱离院", "死亡", "其他"};
    private boolean[] liyuanfangshiResults = {false, false, false, false, false, false};
    boolean[] tempResult;

    void onliyuanfangshi() {
        DebugLog.e("liyuanfangshi");
        tempResult = ArrayUtils.clone(liyuanfangshiResults);

        new AlertDialog.Builder(getBaseActivity()).setTitle("选择离院方式")
//                .setView(dialogView)
                .setMultiChoiceItems(liyuanfangshiItems, tempResult,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                tempResult[which] = isChecked;
                            }
                        })
                .setNegativeButton("全选", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < liyuanfangshiResults.length; i++) {
                            liyuanfangshiResults[i] = true;
                        }
                        showLiyuanfangshi();
                    }
                })
                .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < liyuanfangshiResults.length; i++) {
                            liyuanfangshiResults[i] = false;
                        }
                        showLiyuanfangshi();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < liyuanfangshiResults.length; i++) {
                            liyuanfangshiResults[i] = tempResult[i];
                        }
                        showLiyuanfangshi();
                    }
                })
                .show();
    }

    void showLiyuanfangshi() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < liyuanfangshiResults.length; i++) {
            if (liyuanfangshiResults[i]) {
                stringBuilder.append(liyuanfangshiItems[i]).append('\n');
            }
        }
        mBinganhao.setText(StringUtils.removeEnd(stringBuilder.toString(), "\n"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_menu) {
            Bundle bundle = new Bundle();
            StringBuilder s = new StringBuilder();
            if (guanliOffices != null && guanliResults != null) {
                for (Integer i : guanliResults) {
                    s.append(',').append(guanliOffices.get(i).getCode());
                }
            }
            HashMap<String,String> formBody = new HashMap<String,String>();
            formBody.put("userid", BaseApplication.getCurrentUser().getIdcard());
            formBody.put("orgcode", BaseApplication.getCurrentUser().getYyidentiry());
            formBody.put("binganhao", mBinganhao.getText().toString());
            formBody.put("shenfenzhengid", mShenfenzheng.getText().toString());
            formBody.put("xingming", mXingming.getText().toString());
            if (beginTime !=-1) {
                formBody.put("chuyuanriqibegin", DateFormatUtils.ISO_DATE_FORMAT.format(beginTime));
            }
            if (endTime !=-1) {
                formBody.put("chuyuanriqiend", DateFormatUtils.ISO_DATE_FORMAT.format(endTime));
            }else{
                int maxBeforeDays = getResources().getInteger(R.integer.maxBeforeDays);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR,maxBeforeDays);
                formBody.put("chuyuanriqiend", DateFormatUtils.ISO_DATE_FORMAT.format(c));
            }
            switch (searchType) {
                case R.string.main_chayuebingan:
                    formBody.put("chaxunleibie", BaseApplication.getCurrentUser().getCxlb());
                    formBody.put("keshibianma", StringUtils.replaceOnce(s.toString(), ",", ""));
                    break;
                case R.string.main_jieyuebingan:
                    formBody.put("mubiaoorgcode", (String) mSuoshujigou.getTag());
                    break;
            }
            bundle.putString("searchItem", new Gson().toJson(formBody));
            bundle.putInt(itemType,searchType);
            beginActivity(SearchResultActivity.class, bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onHttpEvent(HttpEvent event) {
        if (event == null || StringUtils.isEmpty(event.getResponse())) {
            showMessage(defaultNetErrorMsg);
        } else {
            int tag = event.getTag();
            switch (tag) {
                case tagGetOrgInfo:
                    DebugLog.e(event.getResponse());
                    BaseResponse<List<MutablePair<String, String>>> responseOrgInfo = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<List<MutablePair<String, String>>>>() {
                    }.getType());
                    switch (responseOrgInfo.getErrorCode()) {
                        case 0:
                            if(responseOrgInfo.getData()==null || responseOrgInfo.getData().size()<=0){
                                showMessage("机构信息为空，请联系管理员！", AppMsg.STYLE_ALERT);
                            }else{
                                orgInfos = responseOrgInfo.getData();
                                ChooseOrgDialogFragment.newInstance(
                                        orgInfos,
                                        mSuoshujigou.getText().toString())
                                        .show(getFragmentManager(), "ChooseOrg");
                            }
                            break;
                        default:
                            String msg = responseOrgInfo.getErrorMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = defaultNetErrorMsg;
                            }
                            showMessage(msg);
                            break;
                    }
                    break;
            }
        }
    }
}
