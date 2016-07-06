package com.jiayusoft.mobile.shengli.bingan.setting;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import butterknife.InjectView;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.utils.app.BaseActivity;

public class PersonInfoActivity extends BaseActivity {

    @InjectView(R.id.personinfo)
    TextView mPersoninfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPersoninfo.setText(Html.fromHtml(BaseApplication.getCurrentUser().getText(getBaseActivity())));
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_person_info);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_person_info, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
