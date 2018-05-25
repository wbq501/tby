package com.baigu.dms.domain.db.repository.impl;

import com.baigu.dms.domain.db.repository.BaseRepository;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:02
 */
public class BaseRepositoryImpl<T, K> implements BaseRepository<T, K> {
    protected AbstractDao<T, K> mDao;

    public BaseRepositoryImpl(AbstractDao dao) {
        mDao = dao;
    }

    @Override
    public long save(T item) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return mDao.insert(item);
    }

    @Override
    public void save(T... items) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.insertInTx(items);
    }

    @Override
    public void save(List<T> list) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.insertInTx(list);
    }

    @Override
    public void saveOrUpdate(T item) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.insertOrReplace(item);
    }

    @Override
    public void saveOrUpdate(T... items) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.insertOrReplaceInTx(items);
    }

    @Override
    public void saveOrUpdate(List<T> list) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.insertOrReplaceInTx(list);
    }

    @Override
    public void deleteByKey(K key) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.deleteByKey(key);
    }

    @Override
    public void delete(T item) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.delete(item);
    }

    @Override
    public void delete(T... items) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.deleteInTx(items);
    }

    @Override
    public void delete(List<T> list) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.deleteInTx(list);
    }

    @Override
    public void deleteAll() {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.deleteAll();
    }

    @Override
    public void update(T item) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.update(item);
    }

    @Override
    public void update(T... items) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.updateInTx(items);
    }

    @Override
    public void update(List<T> list) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        mDao.updateInTx(list);
    }

    @Override
    public T query(K key) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return mDao.load(key);
    }

    @Override
    public List<T> queryAll() {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return mDao.loadAll();
    }

    @Override
    public List<T> query(String where, String... params) {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return mDao.queryRaw(where, params);
    }

    @Override
    public long count() {
        return 0;
    }

    /**
     * 获取QueryBuilder
     * @return
     */
    public QueryBuilder<T> queryBuilder() {
        if (mDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return mDao.queryBuilder();
    }
}