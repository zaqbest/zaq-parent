/*
 * 版权信息: © fusionfintrade
 */ 
package com.zaqbest.core.mongodb;

import java.io.Serializable;

/**
 * mongo id自增对象
 *
 * @author allan
 * @date 2021/04/13
 */
@SuppressWarnings("serial")
public class MongodbAutoIncrementKey implements Serializable {

    /**
     * mongodb数据表名
     */
    private String mongoTableName;

    /**
     * mongodb字段名
     */
    private String keyName;

    /**
     * 自增长值
     */
    private Long idnex;

    public String getMongoTableName() {
        return mongoTableName;
    }

    public void setMongoTableName(String mongoTableName) {
        this.mongoTableName = mongoTableName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Long getIdnex() {
        return idnex;
    }

    public void setIdnex(Long idnex) {
        this.idnex = idnex;
    }

}
