package com.baigu.dms.domain.db;

import android.content.Context;
import android.util.Log;

import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.dao.DaoMaster;
import com.baigu.dms.domain.db.dao.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;


public class DBCore {
    private static final String DEFAULT_DB_NAME = "_dms.db";
    private static final String ENCRYPT_PASSWORD = "baigu_dms_ml_db";// 加密密码

    private static DaoSession sDaoSession;

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;

        init();
    }

    public static DaoSession getDaoSession() {

        return sDaoSession;
    }

    public static void openDatabase() {
        closeDatabase();

        String dbName = UserCache.getInstance().getUser().getCellphone() + DEFAULT_DB_NAME;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(sContext, dbName);
        Database db = helper.getEncryptedWritableDb(ENCRYPT_PASSWORD);
        sDaoSession = new DaoMaster(db).newSession();
    }

    public static void closeDatabase() {
        try {
            if (sDaoSession != null) {
                sDaoSession.getDatabase().close();
            }
            if (sDaoSession != null) {
                sDaoSession = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        QueryBuilder.LOG_SQL = Constants.DEBUG;
        QueryBuilder.LOG_VALUES = Constants.DEBUG;
    }

}