package github.tiger.xfile.control;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import github.tiger.greendao.DaoMaster;
import github.tiger.greendao.DaoSession;
import github.tiger.xfile.constants.Constant;

/**
 * Author: Tiger zhang
 * Date:   2016/5/7
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MyApp extends Application {
    public static DaoMaster                daoMaster;
    public static DaoSession daoSession;
    public static SQLiteDatabase db;
    public static DaoMaster.DevOpenHelper  helper;

    public static String psw = "";

    @Override
    public void onCreate() {
        super.onCreate();
        /*
      * 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的
        SQLiteOpenHelper 对象
      */
        helper = new DaoMaster.DevOpenHelper(this, Constant.DATABASE_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();


    }
    public static DaoSession getDaoSession()    {
        return daoSession;
    }
    public static SQLiteDatabase getSqLiteDatabase()    {
        return db;
    }
}
