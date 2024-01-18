/*
 * 版权信息: © fusionfintrade
 */
package com.zaqbest.core.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Objects;

/**
 * mongodb操作类
 *
 * @author allan
 * @date 2022/2/17 18:09
 */
public abstract class BaseMongodbDao<T> {

    /**
     * spring mongodb　集成操作类
     */
    @Autowired(required = false)
    protected MongoTemplate mongoTemplate;

    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 通过条件查询实体(集合)
     *
     * @param query
     */
    public List<T> find(Query query, Sort sort) {
        try {
            if (Objects.nonNull(mongoTemplate)) {
                return mongoTemplate.find(query.with(sort), this.getEntityClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 通过一定的条件查询一个实体
     *
     * @param query
     * @return
     */
    public T findOne(Query query) {
        if (Objects.nonNull(mongoTemplate)) {
            return mongoTemplate.findOne(query, this.getEntityClass());
        }
        return null;
    }

    /**
     * 通过ID获取记录
     *
     * @param id
     * @return
     */
    public T get(String id) {
        if (Objects.nonNull(mongoTemplate)) {
            return mongoTemplate.findById(id, this.getEntityClass());
        }
        return null;
    }

    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     *
     * @param id
     * @param collectionName 集合名
     * @return
     */
    public T findById(String id, String collectionName) {
        if (Objects.nonNull(mongoTemplate)) {
            return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
        }
        return null;
    }

    /**
     * 通过条件查询更新数据
     *
     * @param query
     * @param update
     * @return
     */
    public void update(Query query, Update update) {
        if (Objects.nonNull(mongoTemplate)) {
            mongoTemplate.findAndModify(query, update, this.getEntityClass());
        }
    }

    /**
     * 保存一个对象到mongodb
     *
     * @param entity
     * @return
     */
    public T save(T entity) {
        if (Objects.nonNull(mongoTemplate)) {
            mongoTemplate.insert(entity);
        }
        return entity;
    }

    /**
     * 按条件查询,并且删除记录
     *
     * @param query
     * @return
     */
    public T findAndRemove(Query query) {
        if (Objects.nonNull(mongoTemplate)) {
            return this.mongoTemplate.findAndRemove(query, this.getEntityClass());
        }
        return null;
    }
}
