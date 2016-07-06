package com.jiayusoft.mobile.utils.database;


import android.content.Context;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created by ASUS on 14-4-4.
 */
public class DBHelper {

    private static final String DB_NAME = "green.db";

    private DaoSession daoSession;

    public DBHelper(Context context) {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME, null);
        daoSession = new DaoMaster(helper.getWritableDatabase()).newSession();

    }

    public boolean addFavourite(Bingan bingan){
        boolean result = false;
        if (bingan!=null){
            bingan.setShoucang(true);
            daoSession.getBinganDao().insertOrReplace(bingan);
            result = true;
        }
        return result;
    }
    public boolean isFavourite(Bingan bingan){
        boolean result = false;
        if (bingan!=null){
            QueryBuilder qb = daoSession.getBinganDao().queryBuilder();
            qb.where(qb.and(
                    BinganDao.Properties.Shoucang.eq(true),
                    BinganDao.Properties.Biaoshima.eq(bingan.getBiaoshima()),
                    BinganDao.Properties.Zuzhidaima.eq(bingan.getZuzhidaima())
            ));
            result = qb.count()>0;
        }
        return result;
    }
    public boolean removeFavourite(Bingan bingan){
        boolean result = false;
        if (bingan!=null){
            bingan.setShoucang(false);
            daoSession.getBinganDao().deleteByKey(bingan.getBiaoshima());
            result = true;
        }
        return result;
    }
    public List<Bingan> getFavourites(){
        QueryBuilder qb = daoSession.getBinganDao().queryBuilder();
        qb.where(BinganDao.Properties.Shoucang.eq(true)).orderAsc(BinganDao.Properties.Chuyuanshijian);
        return qb.list();
    }

    public List<Office> getOffices(String orgCode){
        QueryBuilder qb = daoSession.getOfficeDao().queryBuilder();
        qb.where(OfficeDao.Properties.Orgcode.eq(orgCode)).orderAsc(OfficeDao.Properties.Code);
        return qb.list();
    }
    public List<Office> getOffices(String orgCode,String[] inIDs){
        QueryBuilder qb = daoSession.getOfficeDao().queryBuilder();
        qb.where(qb.and(OfficeDao.Properties.Orgcode.eq(orgCode),
                OfficeDao.Properties.Code.in(inIDs))).orderAsc(OfficeDao.Properties.Code);
        return qb.list();
    }
    public String getOfficeName(String orgCode,String officeCode){
        QueryBuilder qb = daoSession.getOfficeDao().queryBuilder();
        qb.where(qb.and(OfficeDao.Properties.Orgcode.eq(orgCode),
                OfficeDao.Properties.Code.eq(officeCode)));
        List<Office> temp = qb.list();
        if (temp== null || temp.size()<=0){
            return null;
        }else {
            return temp.get(0).getName();
        }
    }
}