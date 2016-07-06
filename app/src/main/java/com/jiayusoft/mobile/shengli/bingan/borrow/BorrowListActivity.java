package com.jiayusoft.mobile.shengli.bingan.borrow;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.card.CardAdapter;
import com.jiayusoft.mobile.shengli.bingan.card.CardEvent;
import com.jiayusoft.mobile.shengli.bingan.detail.DetailActivity;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.database.Bingan;
import com.jiayusoft.mobile.utils.http.BaseResponse;
import com.jiayusoft.mobile.utils.http.HttpEvent;
import com.jiayusoft.mobile.utils.http.HttpTask;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BorrowListActivity extends BaseActivity {

    @InjectView(R.id.list_result)
    ListView mListResult;
    @InjectView(android.R.id.empty)
    TextView mEmpty;
    ArrayList<Bingan> cardItems;
    CardAdapter mCardAdapter;

    Button mLoadmore;
    int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentState = cardImageAccepted;

        cardItems = new ArrayList<Bingan>();
        mCardAdapter = new CardAdapter(getBaseContext(), cardItems,currentState);
        mLoadmore = new Button(getBaseActivity());
        mLoadmore.setText(R.string.loadmore);
        mLoadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });
        mListResult.addFooterView(mLoadmore);
        mListResult.setEmptyView(mEmpty);

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(mCardAdapter);
        animationAdapter.setAbsListView(mListResult);
        mListResult.setAdapter(animationAdapter);

        changeType(R.id.action_accepted);
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_borrow_list);
    }

    //    @OnClick(R.id.loadmore)
    void loadMore() {
        HashMap<String,String> formBody = new HashMap<String, String>();

        formBody.put("userid", BaseApplication.getCurrentUser().getIdcard());
        formBody.put("orgcode", BaseApplication.getCurrentUser().getYyidentiry());
        switch (currentState){
            case cardImageNotYet:
                formBody.put("state", "1");
                break;
            case cardImageAccepted:
                formBody.put("state", "2");
                break;
            case cardImageRefused:
                formBody.put("state", "3");
                break;
        }
        formBody.put("startindex", String.valueOf(cardItems.size()));
        new HttpTask(getBaseActivity(), "查询中...", httpPost, tagSearchStatesLoadMore, searchBorrowStatesLoadMoreUrl, formBody)
                .execute();
    }

    @Subscribe
    public void onHttpEvent(HttpEvent event) {
        if (event == null || StringUtils.isEmpty(event.getResponse())) {
            showMessage("网络连接错误，请稍后重试。");
        } else {
            int tag = event.getTag();
            DebugLog.e(event.getResponse());
            switch (tag) {
                case tagSearchStatesLoadMore:
                    BaseResponse<List<Bingan>> response = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<List<Bingan>>>() {
                    }.getType());
                    switch (response.getErrorCode()) {
                        case 0:
                            cardItems.addAll(response.getData());
                            mCardAdapter.notifyDataSetChanged();
                            if (response.getData().size() < 30) {
//                                mLoadmore.setVisibility(View.GONE);
                                mListResult.removeFooterView(mLoadmore);
                            }else {
                                if (mListResult.getFooterViewsCount()<=0){
                                    mListResult.addFooterView(mLoadmore);
                                }
                            }
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
            }
        }
    }

    @Subscribe
    public void onCardEvent(CardEvent event){
        Bingan binganCard;
        switch (event.getEventType()){
            case cardClickEvent:
                switch (currentState){
                    case cardImageNotYet:
                        showMessage("未批复病案不可查看。");
                        break;
                    case cardImageAccepted:
                        binganCard = cardItems.get(event.getPosition());
                        Bundle bundle = new Bundle();
                        bundle.putString("bingan", new Gson().toJson(binganCard));
                        beginActivity(DetailActivity.class, bundle);
                        break;
                    case cardImageRefused:
                        break;
                }
                break;
//            case cardImageEvent:
//                binganCard = cardItems.get(event.getPosition());
//                binganCard.setShoucang(!binganCard.getShoucang());
//                BinganDao binganDao = BaseApplication.getDaoSession(getBaseActivity()).getBinganDao();
//                binganDao.update(binganCard);
//                mCardAdapter.notifyDataSetChanged();
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_borrow_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        changeType(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    void changeType(int queryType){
        cardItems.clear();
        switch (queryType){
            case R.id.action_notyet:
                currentState = cardImageNotYet;
                getSupportActionBar().setSubtitle(R.string.action_notyet);
                break;
            case R.id.action_accepted:
                currentState = cardImageAccepted;
                getSupportActionBar().setSubtitle(R.string.action_accepted);
                break;
            case R.id.action_refused:
                currentState = cardImageRefused;
                getSupportActionBar().setSubtitle(R.string.action_refused);
                break;
        }
        mCardAdapter.setCardImageType(currentState);
        loadMore();
    }
}
