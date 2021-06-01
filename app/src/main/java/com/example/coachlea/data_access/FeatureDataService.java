package com.example.coachlea.data_access;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeatureDataService {
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    //Features to save
    public String vrate_name="vrate";
    public String intonation_name="intonation";
    public String hearing_name ="hearing";
    public String pitch_mean_name ="pitch mean";
    public String real_intonation_name ="real_intonation";
    public String real_speech_rate_name ="real_speech_rate";
    public String real_pitch_mean_name = "real pitch mean";


    private Context invocationcontext;
    private String dbname;
    public FeatureDataService(Context context){
        this.invocationcontext = context;
        this.dbname = "citdb";
    }

    public void save_feature(String feature_name, Date feature_date, Float feature_value){
        Date dateflook=new Date();
        try {
            dateflook= formatter.parse(formatter.format(feature_date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        FeatureDA feature=new FeatureDA(feature_name, dateflook, feature_value);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getFeatureDADao().save(feature);
        db.close();
    }

    private List<FeatureDA> get_feature_by_date_and_name(String name, Date feature_date){

        String dateflook= formatter.format(feature_date);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        FeatureDADao dao = session.getFeatureDADao();
        QueryBuilder<FeatureDA> qb = dao.queryBuilder();
        qb.where(FeatureDADao.Properties.Feature_name.eq(name),
                qb.and(FeatureDADao.Properties.Feature_name.eq(name),FeatureDADao.Properties.Feature_date_str.eq(dateflook)));
        qb.orderAsc(FeatureDADao.Properties.Feature_date);
        List<FeatureDA> features = qb.list();
        db.close();

        return features;

    }

    private List<FeatureDA> get_feature_by_month_and_name(String name, Date feature_date){

        Calendar c = Calendar.getInstance();
        Date d = new Date();
        c.setTime(feature_date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String dateflook1 = formatter.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, 1);
        String dateflook= formatter.format(c.getTime());
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        FeatureDADao dao = session.getFeatureDADao();
        QueryBuilder<FeatureDA> qb = dao.queryBuilder();
        qb.where(FeatureDADao.Properties.Feature_name.eq(name),
        qb.and(FeatureDADao.Properties.Feature_name.eq(name),FeatureDADao.Properties.Feature_date_str.between(dateflook, dateflook1)));
        qb.orderAsc(FeatureDADao.Properties.Feature_date);
        List<FeatureDA> features = qb.list();
        List<FeatureDA> finalFeatures = new ArrayList<>();
        for(FeatureDA f : features) {
            Calendar compare = Calendar.getInstance();
            compare.setTime(f.getFeature_date());
            if(c.get(Calendar.MONTH) == compare.get(Calendar.MONTH)) {
                finalFeatures.add(f);
            }
        }
        db.close();

        return finalFeatures;

    }

    public FeatureDA get_avg_feature_for_month(String feature_name, Date date){

        List<FeatureDA> features_date=get_feature_by_month_and_name(feature_name, date);
        float avgfeat=get_avg_feat(features_date);
        return new FeatureDA(feature_name, date, avgfeat);

    }

    public FeatureDA get_last_feat_value(String feature_name){
        Date curr0= Calendar.getInstance().getTime();
        List<FeatureDA> features=get_feature_by_date_and_name(feature_name, curr0);
        if (features.size()==0){
            return new FeatureDA(feature_name);
        }
        else{
            return features.get(features.size()-1);
        }
    }

    public float get_avg_feat(List<FeatureDA> features){
        int cont_great_zero=0;
        float sum_feat=0;
        FeatureDA featurec;
        for (int i=0;i<features.size();i++){
            featurec=features.get(i);
            sum_feat+=featurec.getFeature_value();
            cont_great_zero+=1;

        }

        if (cont_great_zero==0){

            return 0;
        }
        return sum_feat/cont_great_zero;
    }

}
