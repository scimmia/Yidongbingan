package com.jiayusoft.mobile.shengli.bingan.favourite;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.shengli.bingan.card.CardAdapter;
import com.jiayusoft.mobile.shengli.bingan.card.CardEvent;
import com.jiayusoft.mobile.shengli.bingan.detail.DetailActivity;
import com.jiayusoft.mobile.utils.app.BaseActivity;
import com.jiayusoft.mobile.utils.database.Bingan;
import com.jiayusoft.mobile.utils.database.DBHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class FavouriteActivity extends BaseActivity {

    @InjectView(R.id.list_result)
    ListView mListResult;
    DBHelper mDBHelper;
    ArrayList<Bingan> cardItems;
    CardAdapter mCardAdapter;
    @InjectView(android.R.id.empty)
    TextView mEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBHelper = new DBHelper(getBaseActivity());
        cardItems = new ArrayList<Bingan>();

        mCardAdapter = new CardAdapter(getBaseContext(), cardItems, cardImageStar);
        mListResult.setEmptyView(mEmpty);
        mListResult.setAdapter(mCardAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        cardItems.clear();
        cardItems.addAll(mDBHelper.getFavourites());
        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_favourite);
    }


    @Subscribe
    public void onCardEvent(CardEvent event) {
        Bingan binganCard;
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
                mCardAdapter.notifyDataSetChanged();
                break;
        }
    }
}
