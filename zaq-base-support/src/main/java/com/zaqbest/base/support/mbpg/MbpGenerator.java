package com.zaqbest.base.support.mbpg;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.zaqbest.base.dao.entity.BaseEntity;
import com.zaqbest.base.dao.mapper.CommonMapper;
import com.zaqbest.base.support.mbpg.dto.GenerateReqDto;

import java.util.HashMap;
import java.util.Map;

public class MbpGenerator {

    public static void generate(GenerateReqDto reqDto){
        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.xml, reqDto.getOutputHome()+"/resources/mappers");
        TemplateType[] disableTemplateTypes = reqDto.getDisableTemplateTypes();

        FastAutoGenerator fastAutoGenerator =
                FastAutoGenerator.create(reqDto.getUrl(), reqDto.getUsername(), reqDto.getPassword());

        fastAutoGenerator.globalConfig(builder -> {
            builder.author(reqDto.getAuthor()) // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                    .dateType(DateType.ONLY_DATE)
                    .outputDir(reqDto.getOutputHome()); // 指定输出目录
        });

        fastAutoGenerator.packageConfig(
                builder -> {
                    builder.parent(reqDto.getParent()) // 设置父包名
                            .moduleName(reqDto.getModuleName()) // 设置父包模块名
                            .pathInfo(pathInfo); // 设置mapperXml生成路径
                }
        );

        fastAutoGenerator.strategyConfig(
                builder -> {
                    builder.addInclude(reqDto.getTableInclude());
                    builder.addTablePrefix(reqDto.getTablePrefix());

                    // entity
                    Entity.Builder tableBuilder = builder.entityBuilder();
                    tableBuilder.formatFileName("%sEntity")
                            .enableFileOverride()
                            //.enableColumnConstant()
                            .enableChainModel()
                            .enableTableFieldAnnotation()
                            .enableLombok();
                    if (reqDto.getEnableBaseEntity() == null || reqDto.getEnableBaseEntity()){
                        tableBuilder.superClass(BaseEntity.class);
                    } else {
                        tableBuilder.logicDeletePropertyName("isDelete")
                                .addTableFills(new Column("create_time", FieldFill.INSERT))
                                .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                                .addTableFills(new Column("is_delete", FieldFill.INSERT));
                    }

                    // mapper
                    Mapper.Builder mapperBuilder = builder.mapperBuilder();
                    mapperBuilder.enableFileOverride()
                            .superClass(CommonMapper.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .enableMapperAnnotation();

                    // service
                    Service.Builder serviceBuilder = builder.serviceBuilder();
                    serviceBuilder.enableFileOverride();

                    // controller
                    Controller.Builder controllerBuilder = builder.controllerBuilder();
                    controllerBuilder.enableFileOverride();
                }
        );

         fastAutoGenerator.templateConfig(builder -> builder.disable(disableTemplateTypes));

         fastAutoGenerator.templateEngine(new VelocityTemplateEngine());

         fastAutoGenerator.execute();
    }
}
