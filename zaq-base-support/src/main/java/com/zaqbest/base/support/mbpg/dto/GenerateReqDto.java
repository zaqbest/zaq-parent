package com.zaqbest.base.support.mbpg.dto;

import com.baomidou.mybatisplus.generator.config.TemplateType;
import lombok.Data;

@Data
public class GenerateReqDto {

    String url;

    String username;

    String password;

    String outputHome;

    String parent;

    String moduleName;

    String author;

    String[] tablePrefix;

    String[] tableInclude;

    TemplateType[] disableTemplateTypes;

    Boolean enableBaseEntity;
}