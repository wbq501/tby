package com.baigu.dms.domain.db;


import org.greenrobot.greendao.database.Database;

public class UpgradeHelper {

    public static void onUpgrade(Database db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
//                db.execSQL("ALTER TABLE " + GroupDao.TABLENAME + " ADD COLUMN MEMBER_LIMIT INTEGER NOT NULL default (0)");
            default:
                break;
        }
    }
}
