package com.jiayusoft.mobile.shengli.bingan.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.main.MainActivity;
import com.jiayusoft.mobile.shengli.bingan.setting.SettingActivity;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.app.dialog.ChooseOrgDialogFragment;
import com.jiayusoft.mobile.utils.app.dialog.ChooseOrgEvent;
import com.jiayusoft.mobile.utils.app.listener.HideKeyboardListener;
import com.jiayusoft.mobile.utils.database.Office;
import com.jiayusoft.mobile.utils.database.OfficeDao;
import com.jiayusoft.mobile.utils.http.BaseResponse;
import com.jiayusoft.mobile.utils.http.HttpDownloadTask;
import com.jiayusoft.mobile.utils.http.HttpEvent;
import com.jiayusoft.mobile.utils.http.HttpTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class LoginActivity extends BaseActivity {


    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_login);
    }

    @InjectView(R.id.login_et_username)
    MaterialEditText mLoginEtUsername;
    @InjectView(R.id.login_et_password)
    MaterialEditText mLoginEtPassword;
    @InjectView(R.id.login_btn_sign)
    Button mLoginBtnSignOnline;
    @InjectView(R.id.login_layout)
    LinearLayout mLoginLayout;
    @InjectView(R.id.login_cb_save_password)
    CheckBox mLoginCbSavePassword;
    @InjectView(R.id.login_cb_auto_login)
    CheckBox mLoginCbAutoLogin;
    @InjectView(R.id.suoshujigou)
    MaterialEditText mSuoshujigou;

    int mDBVersion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginLayout.setOnClickListener(new HideKeyboardListener(getBaseActivity()));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseActivity());
        mLoginEtUsername.setText(sharedPreferences.getString(LOGIN_USER_NAME, null));
        mLoginEtPassword.setText(sharedPreferences.getString(LOGIN_PASSWORD, null));
        mLoginCbSavePassword.setChecked(sharedPreferences.getBoolean(LOGIN_SAVE_PASSWORD, false));
        mLoginCbAutoLogin.setChecked(sharedPreferences.getBoolean(loginAutoLogin, false));
        mSuoshujigou.setText(sharedPreferences.getString(loginSuoshuJigouName, null));
        mSuoshujigou.setTag(sharedPreferences.getString(loginSuoshuJigouID, null));

//        if (!sharedPreferences.getBoolean(officeInited, false)) {
//            initOffice();
//        }
//        if (sharedPreferences.getInt(officeVersion, 0)<officeCurrentVersion) {
//            initOffice();
//        }

        if (sharedPreferences.getString(serverUrl,null)==null){
            SharedPreferences.Editor spEd = sharedPreferences.edit();
            spEd.putString(serverUrl, defaultServerUrl);
            spEd.apply();
        }
        if (sharedPreferences.getBoolean(loginAutoLogin, false)){
            attemptLogin();
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
    public void onChooseOrgEvent(ChooseOrgEvent event) {
        mSuoshujigou.setText(event.getTitle());
        mSuoshujigou.setTag(event.getId());
    }

    @OnClick(R.id.login_btn_sign)
    public void attemptLogin() {
        if (TextUtils.isEmpty(mLoginEtUsername.getText())) {
            mLoginEtUsername.setError(getString(R.string.error_field_required));
            mLoginEtUsername.requestFocus();
        } else if (TextUtils.isEmpty(mSuoshujigou.getText())) {
            mSuoshujigou.requestFocus();
        } else {
            // Reset errors.
            mLoginEtUsername.setError(null);
            mLoginEtPassword.setError(null);

            checkUpdate();
        }
    }

    @OnCheckedChanged(R.id.login_cb_auto_login)
    public void onAutoLogin(boolean checked){
        if (checked && !mLoginCbSavePassword.isChecked()){
            mLoginCbSavePassword.toggle();
        }
    }

    @Subscribe
    public void onHttpEvent(HttpEvent event) {
        if (event == null || StringUtils.isEmpty(event.getResponse())) {
            showMessage("网络连接错误，请稍后重试。");
        } else {
            int tag = event.getTag();
            switch (tag) {
                case tagLogin:
                    DebugLog.e(event.getResponse());
                    BaseResponse<UserDoctor> response = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<UserDoctor>>() {
                    }.getType());
                    switch (response.getErrorCode()) {
                        case 0:
                            BaseApplication.setCurrentUser(response.getData());
                            BaseApplication.getCurrentUser().setOrgName(mSuoshujigou.getText().toString());
                            startMainActivity();
                            break;
                        default:
                            String msg = response.getErrorMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "网络连接错误，请稍后重试。";
                            }
                            showMessage(msg);
                            break;
                    }
                    break;
                case tagCheckUpdate:
                    DebugLog.e(event.getResponse());
                    final BaseResponse<UpdateInfo> responseCheckUpdate = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<UpdateInfo>>() {
                    }.getType());
                    switch (responseCheckUpdate.getErrorCode()) {
                        case 0:
                            new AlertDialog.Builder(getBaseActivity())
                                    .setTitle("升级信息提示")
                                    .setMessage("发现新版本："+responseCheckUpdate.getData().getVersionName()
                                        +"\n更新内容："+responseCheckUpdate.getData().getUpdateLog())
                                    .setNegativeButton("现在升级", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new HttpDownloadTask(
                                                    getBaseActivity(),"下载中...",tagDownloadNewFile,
                                                    responseCheckUpdate.getData().getSoftUrl(),
                                                    updateFolder+"EMR.apk").execute();
                                        }
                                    })
                                    .setPositiveButton("以后再说",null)
                                    .setCancelable(false)
                                    .show();
                            break;
                        default:
                            checkDBUpdate();
                            break;
                    }
                    break;
                case tagDownloadNewFile:
                    if (StringUtils.isNotEmpty(event.getResponse())) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(event.getResponse())), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                    break;
                case tagCheckDBUpdate:
                    DebugLog.e(event.getResponse());
                    final BaseResponse<UpdateInfo> responseCheckDBUpdate = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<UpdateInfo>>() {
                    }.getType());
                    switch (responseCheckDBUpdate.getErrorCode()) {
                        case 0:
                            mDBVersion = responseCheckDBUpdate.getData().getVersionCode();
                            new HttpDownloadTask(
                                    getBaseActivity(),"科室信息更新中...",tagDownloadDBNewFile,
                                    responseCheckDBUpdate.getData().getSoftUrl(),
                                    updateFolder+"db.txt").execute();
                            break;
                        default:
                            login();
                            break;
                    }
                    break;
                case tagDownloadDBNewFile:
                    initOffice();
                    login();
                    break;
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

    void startMainActivity() {
        DebugLog.e("startMainActivity");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseActivity());
        SharedPreferences.Editor spEd = sp.edit();
        spEd.putString(LOGIN_USER_NAME, mLoginEtUsername.getText().toString());
        if (mLoginCbSavePassword.isChecked()) {
            spEd.putBoolean(LOGIN_SAVE_PASSWORD, true);
            spEd.putString(LOGIN_PASSWORD, mLoginEtPassword.getText().toString());
            spEd.putString(loginSuoshuJigouName, mSuoshujigou.getText().toString());
            spEd.putString(loginSuoshuJigouID, (String) mSuoshujigou.getTag());
        } else {
            spEd.remove(LOGIN_SAVE_PASSWORD);
            spEd.remove(LOGIN_PASSWORD);
            spEd.remove(loginSuoshuJigouID);
            spEd.remove(loginSuoshuJigouName);
        }
        spEd.putBoolean(loginAutoLogin, mLoginCbAutoLogin.isChecked());
        spEd.apply();
//            BaseApplication.setCurrentCustomer(new Customer("王五","123","sdf","","",""));
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            startActivity(intent);
        beginActivity(MainActivity.class);
        finish();
    }

    void initOffice() {
//        InputStream is = getBaseActivity().getResources().openRawResource(R.raw.shengli);
        BufferedReader reader = null;
        LinkedList<Office> o = new LinkedList<Office>();
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(updateFolder+"db.txt"), "gb2312");
            DebugLog.e("initOffice");
//                        System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(is);
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                String[] t = StringUtils.split(tempString, '|');
                if (t.length == 3) {
                    o.add(new Office(t[0], t[1], t[2]));
                }
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        OfficeDao officeDao = BaseApplication.getDaoSession(getBaseActivity()).getOfficeDao();
        officeDao.deleteAll();
        officeDao.insertInTx(o);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseActivity());
        SharedPreferences.Editor spEd = sp.edit();
//        spEd.putBoolean(officeInited, true);
//        spEd.putInt(officeVersion, officeCurrentVersion);
        spEd.putInt(defaultDBName, mDBVersion);
        spEd.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            beginActivity(SettingActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void checkUpdate(){
        HashMap<String, String> formBody = new HashMap<String, String>();
        String versionTemp = "0";
        try {
            PackageManager pm = getBaseActivity().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getBaseActivity().getPackageName(), 0);
            versionTemp = pi.versionCode-1+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        formBody.put(versionCode, versionTemp);
        formBody.put(softName, defaultSoftName);
        new HttpTask(getBaseActivity(), "检查更新...", httpPost, tagCheckUpdate, checkUpdateUrl, formBody).execute();
    }

    void checkDBUpdate(){
        HashMap<String, String> formBody = new HashMap<String, String>();
        int versionTemp = PreferenceManager.getDefaultSharedPreferences(getBaseActivity()).getInt(defaultDBName,0);
        formBody.put(versionCode, versionTemp+"");
        formBody.put(softName, defaultDBName);
        new HttpTask(getBaseActivity(), "检查更新...", httpPost, tagCheckDBUpdate, checkUpdateUrl, formBody).execute();
    }

    void login(){
        String userName = mLoginEtUsername.getText().toString();
        String password = mLoginEtPassword.getText().toString();
        String orgcode = (String) mSuoshujigou.getTag();

        HashMap<String, String> formBody = new HashMap<String, String>();
        formBody.put(loginUserID, userName);
        formBody.put(loginPassword, password);
        formBody.put(loginOrgcode, orgcode);
        new HttpTask(getBaseActivity(), "登陆中...", httpPost, tagLogin, loginUrl, formBody).execute();
    }
}
