/*
 * 版权信息: © fusionfintrade
 */ 
package com.zaqbest.core.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * mongo id自增服务
 *
 * @author allan
 * @date 2022/03/18
 */
@Component
public class MongodbAutoIncrementKeyManager {

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    /**
     * 获取自增长的值
     *
     * @param collectionName
     * @param fieldName
     * @return
     */
    public Long getIncrementValue(String collectionName, String fieldName) {
        if (Objects.nonNull(mongoTemplate)) {
            MongodbAutoIncrementKey mongodbAutoIncrementKey = mongoTemplate.findAndModify(
                    new Query(Criteria.where("mongoTableName").is(collectionName).and("keyName").is(fieldName)),
                    new Update().inc("idnex", 1),
                    // 更新后的文档，没有则新增
                    new FindAndModifyOptions().upsert(true).returnNew(true), MongodbAutoIncrementKey.class);
            return mongodbAutoIncrementKey.getIdnex();
        }
        return -1L;
    }

    /**
     * 获取id的自增长
     *
     * @param collectionName
     * @return
     */
    public Long getIncrementId(String collectionName) {
        return getIncrementValue(collectionName, "pk");
    }
}
