package com.zaqbest.base.support;

import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.zaqbest.base.support.mbpg.Generator;
import com.zaqbest.base.support.mbpg.dto.GenerateReqDto;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ZaqBaseSupportMbpgTest
{
    @Test
    public void generateTest(){

        GenerateReqDto reqDto = new GenerateReqDto();
        reqDto.setUrl("jdbc:mysql://192.168.223.20:3306/fusion_risk_center_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        reqDto.setUsername("user_risk_center_dev");
        reqDto.setPassword("gfvE84_RX_npZG1z");

        reqDto.setParent("com.fusionfintrade.mt");
        reqDto.setModuleName("data");
        reqDto.setOutputHome("/Users/lipan/MyData/tmpDir/generator");

        reqDto.setAuthor("mbp-generator");
        reqDto.setTableInclude(new String[]{"template_market"});
        reqDto.setTablePrefix(new String[]{});

        reqDto.setDisableTemplateTypes(new TemplateType[]{TemplateType.CONTROLLER, TemplateType.SERVICE, TemplateType.SERVICE_IMPL});

        Generator.generate(reqDto);
    }
}