package com.example.coachlea.data_access;

import android.content.Context;

import org.greenrobot.greendao.database.Database;


public class PatientDataService
{

    private Context invocationcontext;
    private String dbname;
    public PatientDataService(Context context){
        this.invocationcontext = context;
        this.dbname = "citdb";
    }

    public void savePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().save(patient);
        db.close();
    }

    public PatientDA getPatient(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        PatientDADao dao = session.getPatientDADao();
        PatientDA patient = dao.queryBuilder().list().get(0);

        db.close();
        return patient;
    }

}
