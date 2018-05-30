package com.baigu.dms.domain.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baigu.dms.domain.model.ExclusiveGroups;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EXCLUSIVE_GROUPS".
*/
public class ExclusiveGroupsDao extends AbstractDao<ExclusiveGroups, String> {

    public static final String TABLENAME = "EXCLUSIVE_GROUPS";

    /**
     * Properties of entity ExclusiveGroups.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Value = new Property(1, String.class, "value", false, "VALUE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Type = new Property(3, String.class, "type", false, "TYPE");
        public final static Property Sort = new Property(4, int.class, "sort", false, "SORT");
    }


    public ExclusiveGroupsDao(DaoConfig config) {
        super(config);
    }
    
    public ExclusiveGroupsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EXCLUSIVE_GROUPS\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"VALUE\" TEXT," + // 1: value
                "\"NAME\" TEXT," + // 2: name
                "\"TYPE\" TEXT," + // 3: type
                "\"SORT\" INTEGER NOT NULL );"); // 4: sort
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EXCLUSIVE_GROUPS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ExclusiveGroups entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(2, value);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
        stmt.bindLong(5, entity.getSort());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ExclusiveGroups entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(2, value);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
        stmt.bindLong(5, entity.getSort());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ExclusiveGroups readEntity(Cursor cursor, int offset) {
        ExclusiveGroups entity = new ExclusiveGroups( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // value
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // type
            cursor.getInt(offset + 4) // sort
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ExclusiveGroups entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setValue(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSort(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ExclusiveGroups entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(ExclusiveGroups entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ExclusiveGroups entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
