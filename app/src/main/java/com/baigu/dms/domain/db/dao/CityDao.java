package com.baigu.dms.domain.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baigu.dms.domain.model.City;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CITY".
*/
public class CityDao extends AbstractDao<City, String> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property ParentId = new Property(1, String.class, "parentId", false, "PARENTID");
        public final static Property ParentIds = new Property(2, String.class, "parentIds", false, "PARENTIDS");
        public final static Property ParentName = new Property(3, String.class, "parentName", false, "PARENTNAME");
        public final static Property ParentNames = new Property(4, String.class, "parentNames", false, "PARENTNAMES");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
        public final static Property Code = new Property(6, String.class, "code", false, "CODE");
        public final static Property Name = new Property(7, String.class, "name", false, "NAME");
        public final static Property Sort = new Property(8, int.class, "sort", false, "SORT");
    }


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITY\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"PARENTID\" TEXT," + // 1: parentId
                "\"PARENTIDS\" TEXT," + // 2: parentIds
                "\"PARENTNAME\" TEXT," + // 3: parentName
                "\"PARENTNAMES\" TEXT," + // 4: parentNames
                "\"TYPE\" INTEGER NOT NULL ," + // 5: type
                "\"CODE\" TEXT," + // 6: code
                "\"NAME\" TEXT," + // 7: name
                "\"SORT\" INTEGER NOT NULL );"); // 8: sort
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, City entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindString(2, parentId);
        }
 
        String parentIds = entity.getParentIds();
        if (parentIds != null) {
            stmt.bindString(3, parentIds);
        }
 
        String parentName = entity.getParentName();
        if (parentName != null) {
            stmt.bindString(4, parentName);
        }
 
        String parentNames = entity.getParentNames();
        if (parentNames != null) {
            stmt.bindString(5, parentNames);
        }
        stmt.bindLong(6, entity.getType());
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(7, code);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
        stmt.bindLong(9, entity.getSort());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindString(2, parentId);
        }
 
        String parentIds = entity.getParentIds();
        if (parentIds != null) {
            stmt.bindString(3, parentIds);
        }
 
        String parentName = entity.getParentName();
        if (parentName != null) {
            stmt.bindString(4, parentName);
        }
 
        String parentNames = entity.getParentNames();
        if (parentNames != null) {
            stmt.bindString(5, parentNames);
        }
        stmt.bindLong(6, entity.getType());
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(7, code);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
        stmt.bindLong(9, entity.getSort());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // parentId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // parentIds
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // parentName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // parentNames
            cursor.getInt(offset + 5), // type
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // code
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // name
            cursor.getInt(offset + 8) // sort
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setParentId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setParentIds(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setParentName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setParentNames(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType(cursor.getInt(offset + 5));
        entity.setCode(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSort(cursor.getInt(offset + 8));
     }
    
    @Override
    protected final String updateKeyAfterInsert(City entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(City entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(City entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
