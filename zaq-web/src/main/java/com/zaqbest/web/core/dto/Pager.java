package com.zaqbest.web.core.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回基本类型
 * @author chenhaihua
 */
@Data
public class Pager<T> implements Serializable {

    public Pager() {

    }

    public Pager(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    /**
     * 总记录数
     */
    private long total;

    /**
     * 结果集
     */
    private List<T> rows;
}