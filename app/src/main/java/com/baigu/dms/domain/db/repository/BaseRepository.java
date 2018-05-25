package com.baigu.dms.domain.db.repository;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:01
 */
public interface BaseRepository<T, K> {

    /**
     * 保存对象
     * @param item
     */
    long save(T item);

    /**
     * 保存可变参数对象数组
     * @param items
     */
    @SuppressWarnings("unchecked")
    void save(T... items);

    /**
     * 保存对象List
     * @param list
     */
    void save(List<T> list);

    /**
     * 保存或更新对象
     * @param item
     */
    void saveOrUpdate(T item);

    /**
     * 保存或更新对象数组
     * @param items
     */
    @SuppressWarnings("unchecked")
    void saveOrUpdate(T... items);

    /**
     * 保存或更新对象list
     * @param list
     */
    void saveOrUpdate(List<T> list);

    /**
     * 根据key删除
     * @param key
     */
    void deleteByKey(K key);

    /**
     * 根据对象item
     * @param item
     */
    void delete(T item);

    /**
     * 删除可变数组中的对象
     * @param items
     */
    void delete(T... items);

    /**
     * 删除list中的对象
     * @param list
     */
    void delete(List<T> list);

    /**
     * 删除表中的所有数据
     */
    void deleteAll();

    /**
     * 更新对象item
     * @param item
     */
    void update(T item);

    /**
     * 更新可变数组中的对象
     * @param items
     */
    void update(T... items);

    /**
     * 更新list中的对象
     * @param list
     */
    void update(List<T> list);

    /**
     * 根据主键查询对象
     * @param key
     * @return
     */
    T query(K key);

    /**
     * 查询表中的所有对象
     * @return
     */
     List<T> queryAll();

    /**
     * 根据条件查询
     * @param where 条件
     * @param params 条件whree所需要的参数
     * @return
     */
    List<T> query(String where, String... params);

    /**
     * 返回表中所有数据的记录数
     * @return
     */
    long count();

    QueryBuilder<T> queryBuilder();
}