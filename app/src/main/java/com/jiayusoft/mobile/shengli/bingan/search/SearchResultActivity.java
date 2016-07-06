//package com.jiayusoft.mobile.shengli.bingan.search;
//
//import android.os.Bundle;
//import android.widget.*;
//import butterknife.InjectView;
//import com.jiayusoft.mobile.shengli.bingan.R;
//import com.jiayusoft.mobile.shengli.bingan.card.BinganCard;
//import com.jiayusoft.mobile.shengli.bingan.card.CardAdapter;
//import com.jiayusoft.mobile.shengli.bingan.card.CardEvent;
//import com.jiayusoft.mobile.shengli.bingan.detail.DetailActivity;
//import com.jiayusoft.mobile.utils.app.BaseActivity;
//import com.squareup.otto.Subscribe;
//import org.apache.commons.lang3.time.DateFormatUtils;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Random;
//
//public class SearchResultActivity extends BaseActivity{
//
//    @InjectView(R.id.list_result)
//    ListView mListResult;
//    ArrayList<BinganCard> cardItems;
//    CardAdapter mCardAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        cardItems = new ArrayList<BinganCard>();
//        //todo delete
//        String[] patientNames = new String[]{"杨朝来","蒋平","唐灿华","马达","赵小雪","薛文泉","丁建伟","凡小芬","文明","文彭凤","王丽","王建华","王梓人","王震","王保真","王景亮","王丹","邓志勇","邓婕","尹会南","叶汝红","付伟娜","付双红","毕泗迁","孙平","毛华强","孙益奇","孙媛媛","伍婷","阳娣","阳倩莹","刘小梅","刘俊鸣","刘海兵","刘伟华","刘启龙","刘勇辉","吕红","朱智新"};
//        String[] doctorNames = new String[]{
//                "放射治疗科:周伟",
//                "风湿免疫科:王丽英",
//                "妇产科:吉淑芳",
//                "呼吸内科:李一鸣",
//                "口腔科:路东升",
//                "内分泌代谢病中心:卜石",
//                "皮肤病与性病科:张晓艳",
//                "神经内科:邵自强"};
//        for (int i = 0;i<10;i++){
//            BinganCard binganCard= new BinganCard();
//            binganCard.setBinganhao("45-" + i + "3" + i);
//            binganCard.setXingming(patientNames[i%patientNames.length]);
//            binganCard.setChuyuankeshi(doctorNames[i%doctorNames.length]);
//            binganCard.setShoucang(new Random().nextBoolean());
//            binganCard.setZhuyuantianshu(new Random().nextInt(30));
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_YEAR,-1*i*(new Random().nextInt(10)));
//            binganCard.setChuyuanriqi(DateFormatUtils.ISO_DATE_FORMAT.format(calendar));
//            cardItems.add(binganCard);
//        }
//        //todo delete
//        mCardAdapter = new CardAdapter(getBaseContext(), cardItems,cardImageStar);
//        mListResult.setAdapter(mCardAdapter);
//    }
//
//    @Override
//    protected void initContentView() {
//        setContentView(R.layout.activity_search_result);
//    }
//
//    void initData(){
//
//    }
//
//
//    @Subscribe
//    public void onCardEvent(CardEvent event){
//        BinganCard binganCard;
//        switch (event.getEventType()){
//            case cardClickEvent:
//                binganCard = cardItems.get(event.getPosition());
//                beginActivity(DetailActivity.class);
//                break;
//            case cardImageEvent:
//                binganCard = cardItems.get(event.getPosition());
//                binganCard.setShoucang(!binganCard.isShoucang());
//                mCardAdapter.notifyDataSetChanged();
//                break;
//        }
//    }
//
////    @OnItemClick(R.id.list_result)
////    void showPosition(int position){
////        DebugLog.e("position:"+position);
////        beginActivity(DetailActivity.class);
////    }
//
////    class CardAdapter extends BaseAdapter {
////
////        public Context context;
////        public ArrayList<BinganCard> cardItems;
////
////        public CardAdapter(Context context, ArrayList<BinganCard> navDrawerItems) {
////            this.context = context;
////            this.cardItems = navDrawerItems;
////        }
////
////        @Override
////        public int getCount() {
////            return cardItems.size();
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return cardItems.get(position);
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            ViewHolder viewHolder;
////            if (convertView==null){
////                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_card, null);
////                viewHolder = new ViewHolder(convertView);
////                convertView.setTag(viewHolder);
////            }else {
////                viewHolder = (ViewHolder)convertView.getTag();
////            }
////            viewHolder.mCardContent.setText(cardItems.get(position).getText());
////            viewHolder.setPosition(position);
////            if (cardItems.get(position).isShoucang()){
////                viewHolder.mCardShoucang.setImageResource(R.drawable.ic_star_black);
////            }else{
////                viewHolder.mCardShoucang.setImageResource(R.drawable.ic_star_outline);
////            }
////            return convertView;
////        }
////
////        /**
////         * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_card.xml'
////         * for easy to all layout elements.
////         *
////         * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
////         */
////        class ViewHolder {
////            @InjectView(R.id.card_content)
////            TextView mCardContent;
////            @InjectView(R.id.card_shoucang)
////            ImageButton mCardShoucang;
////
////            @OnClick(R.id.card_shoucang)
////            void shoucang(){
////                int position = (Integer)mCardShoucang.getTag();
////                boolean shoucang = cardItems.get(position).isShoucang();
////                cardItems.get(position).setShoucang(!shoucang);
////                if (shoucang){
////                    mCardShoucang.setImageResource(R.drawable.ic_star_outline);
////                }else{
////                    mCardShoucang.setImageResource(R.drawable.ic_star_black);
////                }
////            }
////            @OnClick(R.id.card_view)
////            void showPosition(){
////                int position = (Integer)mCardShoucang.getTag();
////                DebugLog.e("showPosition:"+position);
////                beginActivity(DetailActivity.class);
////            }
////
////            ViewHolder(View view) {
////                ButterKnife.inject(this, view);
////            }
////
////            void setPosition(int position){
//////                mCardView.setTag(position);
////                mCardShoucang.setTag(position);
////            }
////        }
////
////    }
//}

package com.jiayusoft.mobile.shengli.bingan.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiayusoft.mobile.shengli.bingan.BaseApplication;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.card.BinganQuality;
import com.jiayusoft.mobile.shengli.bingan.card.CardAdapter;
import com.jiayusoft.mobile.shengli.bingan.card.CardEvent;
import com.jiayusoft.mobile.shengli.bingan.detail.DetailActivity;
import com.jiayusoft.mobile.utils.DebugLog;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.database.Bingan;
import com.jiayusoft.mobile.utils.database.DBHelper;
import com.jiayusoft.mobile.utils.http.BaseResponse;
import com.jiayusoft.mobile.utils.http.HttpEvent;
import com.jiayusoft.mobile.utils.http.HttpTask;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends BaseActivity {
    HashMap<String,String> mFormBody;
    @InjectView(R.id.list_result)
    ListView mListResult;
    @InjectView(android.R.id.empty)
    TextView mEmpty;

    ArrayList<Bingan> cardItems;
    ArrayList<BinganQuality> mQualities;
    CardAdapter mCardAdapter;
    //    @InjectView(R.id.loadmore)
    Button mLoadmore;

    DBHelper mDBHelper;
    int cardImageType;
    String searchUrl;
    int mUrlTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int searchType = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchType = bundle.getInt(itemType);
            String temp = bundle.getString("searchItem");
            if (temp != null) {
                mFormBody = new Gson().fromJson(temp, new TypeToken<HashMap<String,String>>() {
                }.getType());
            }
        }

        mDBHelper = new DBHelper(getBaseActivity());
        cardItems = new ArrayList<Bingan>();

        switch (searchType) {
            case R.string.main_chayuebingan:
                cardImageType = cardImageStar;
                searchUrl = searchLoadMoreUrl;
                mUrlTag = tagSearchLoadMore;
                break;
            case R.string.main_jieyuebingan:
                cardImageType = cardImageBorrow;
                searchUrl = searchBorrowLoadMoreUrl;
                mUrlTag = tagSearchLoadMore;
                break;
            case R.string.main_binganzhikong:
                setTitle(R.string.main_binganzhikong);
                mEmpty.setText("没有质控信息");
                mFormBody = new HashMap<String,String>();
                mFormBody.put("userid", BaseApplication.getCurrentUser().getIdcard());
                mFormBody.put("orgcode", BaseApplication.getCurrentUser().getYyidentiry());
                mQualities = new ArrayList<BinganQuality>();
                cardImageType = cardImageQuality;
                searchUrl = searchQualityLoadMoreUrl;
//                mUrlTag = tagSearchQualityLoadMore;
                mUrlTag = tagSearchLoadMore;
                break;
            default:
                cardImageType = cardImageNull;
                mUrlTag = tagSearchLoadMore;
                break;
        }
        mCardAdapter = new CardAdapter(getBaseContext(), cardItems, cardImageType);
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


        loadMore();
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_search_result);
    }

    void loadMore() {
        if (mFormBody != null) {
            mFormBody.put("startindex", String.valueOf(cardItems.size()));
            new HttpTask(getBaseActivity(), "查询中...", httpPost, mUrlTag, searchUrl, mFormBody)
                    .execute();
        }
    }


    @Subscribe
    public void onHttpEvent(HttpEvent event) {
        if (event == null || StringUtils.isEmpty(event.getResponse())) {
            showMessage("网络连接错误，请稍后重试。");
        } else {
            int tag = event.getTag();
            DebugLog.e(event.getResponse());
            switch (tag) {
                case tagSearchLoadMore:
                    BaseResponse<List<Bingan>> response = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<List<Bingan>>>() {
                    }.getType());
                    switch (response.getErrorCode()) {
                        case 0:
                            cardItems.addAll(response.getData());
                            mCardAdapter.notifyDataSetChanged();
                            if (response.getData().size() < 30) {
//                                mLoadmore.setVisibility(View.GONE);
                                mListResult.removeFooterView(mLoadmore);
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
                case tagSearchQualityLoadMore:
                    BaseResponse<List<BinganQuality>> qualitys = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<List<BinganQuality>>>() {
                    }.getType());
                    switch (qualitys.getErrorCode()) {
                        case 0:
                            for (BinganQuality quality : qualitys.getData()){
                                mQualities.add(quality);
                                cardItems.add(quality.getBingan());
                            }
                            mCardAdapter.notifyDataSetChanged();
                            if (qualitys.getData().size() < 30) {
//                                mLoadmore.setVisibility(View.GONE);
                                mListResult.removeFooterView(mLoadmore);
                            }
                            break;
                        default:
                            String msg = qualitys.getErrorMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "网络连接错误，请稍后重试。";
                            }
                            showMessage(msg);
                            break;
                    }
                    break;
                case tagBorrowBingan:
                    BaseResponse<String> borrowResponse = new Gson().fromJson(event.getResponse(), new TypeToken<BaseResponse<String>>() {
                    }.getType());
                    switch (borrowResponse.getErrorCode()) {
                        case 0:
                            int size = cardItems.size();
                            for (int i = 0; i < size; i++) {
                                if (StringUtils.equals(cardItems.get(i).getBiaoshima(), borrowResponse.getData())) {
                                    cardItems.get(i).setJieyue(true);
                                    break;
                                }
                            }
                            mCardAdapter.notifyDataSetChanged();
                            showMessage("借阅成功");
                            break;
                        default:
                            String msg = borrowResponse.getErrorMsg();
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
    public void onCardEvent(CardEvent event) {
        Bingan binganCard;
        switch (cardImageType) {
            case cardImageStar: {
                switch (event.getEventType()) {
                    case cardClickEvent:
                        binganCard = cardItems.get(event.getPosition());
                        Bundle bundle = new Bundle();
                        bundle.putString("bingan", new Gson().toJson(binganCard));
                        beginActivity(DetailActivity.class, bundle);
                        break;
                    case cardImageEvent:
                        binganCard = cardItems.get(event.getPosition());
                        if (binganCard.getShoucang()) {
                            mDBHelper.removeFavourite(binganCard);
                        } else {
                            mDBHelper.addFavourite(binganCard);
                        }
//                binganCard.setShoucang(!binganCard.getShoucang());
//                new DBHelper(getBaseActivity()).addFavourite(binganCard,!binganCard.getShoucang());
                        mCardAdapter.notifyDataSetChanged();
                        break;
                }
                break;
            }
            case cardImageBorrow: {
                switch (event.getEventType()) {
                    case cardImageEvent:
                        binganCard = cardItems.get(event.getPosition());
                        if (!binganCard.getJieyue()) {
                            HashMap<String,String> formBody = new HashMap<String,String>();
                            formBody.put("zuzhidaima", binganCard.getZuzhidaima());
                            formBody.put("zuzhiname", binganCard.getZuzhiname());
                            formBody.put("binganid", binganCard.getBiaoshima());
                            formBody.put("shenqingren", BaseApplication.getCurrentUser().getIdcard());
                            formBody.put("shenqingkeshicode", BaseApplication.getCurrentUser().getOfficecode());
                            formBody.put("shenqingkeshiname", mDBHelper.getOfficeName(BaseApplication.getCurrentUser().getYyidentiry(), BaseApplication.getCurrentUser().getOfficecode()));
                            formBody.put("shenqingdanweicode", BaseApplication.getCurrentUser().getYyidentiry());
                            formBody.put("shenqingdanweiname", BaseApplication.getCurrentUser().getOrgName());
//            String content = String.format("userid=%s&password=%s&orgcode=%s",userName, password, orgcode);
//            RequestBody formBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),content);
//                new HttpTask(getBaseActivity(),"登陆中...",httpPost,tagLogin,"http://192.168.1.108:8080/user/login",formBody).execute();

//            showProgress("登陆中...");
                            new HttpTask(getBaseActivity(), "借阅中...", httpPost, tagBorrowBingan, borrowBinganUrl, formBody).execute();
                        }
                        break;
                }
                break;
            }
//            case cardImageQuality: {
//                switch (event.getEventType()) {
//                    case cardClickEvent:
//                        binganCard = cardItems.get(event.getPosition());
//                        Bundle bundle = new Bundle();
//                        bundle.putString("bingan", new Gson().toJson(binganCard));
//                        beginActivity(DetailActivity.class, bundle);
//                        break;
//                    case cardImageEvent:
//                        BinganQuality binganQuality = mQualities.get(event.getPosition());
//                        DebugLog.e(binganQuality.getText());
//                        new AlertDialog.Builder(getBaseActivity())
//                                .setTitle("质控详情")
//                                .setMessage(Html.fromHtml(binganQuality.getText())).show();
//                        break;
//                }
//                break;
//            }
        }
    }
}
