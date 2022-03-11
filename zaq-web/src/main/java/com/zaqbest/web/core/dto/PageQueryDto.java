package com.zaqbest.web.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description 分页功能
 *
 * @author admin
 * @date 2022/1/12 15:51
 * @version 1.0.0
 **/
@Setter
@Getter
@ApiModel(value = "PageRequest ",description = "分页请求基类")
public class PageQueryDto implements Serializable {

    @ApiModelProperty(name = "index", value = "当前页码,第一页从1开始", example = "1", required = true)
    @NotNull(message = "当前页码不能为空")
    private Integer index;

    @ApiModelProperty(name = "pageSize", value = "每页条数", example = "10", required = true)
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize;

    public PageQueryDto() {
    }

    public PageQueryDto(Integer index, Integer pageSize) {
        this.index = index;
        this.pageSize = pageSize;
    }

    public PageQueryDto(Integer index, Integer pageSize, String orderBy) {
        this.index = index;
        this.pageSize = pageSize;
    }




}

