package com.zaqbest.base.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    Long createUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;

    Long updateUser;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;

    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    Integer isDelete;
}
