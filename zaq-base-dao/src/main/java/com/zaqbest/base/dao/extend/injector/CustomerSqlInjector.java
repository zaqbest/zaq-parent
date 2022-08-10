package com.zaqbest.base.dao.extend.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.zaqbest.base.dao.extend.injector.methods.SelectListForUpdate;
import com.zaqbest.base.dao.extend.injector.methods.SelectOneForUpdate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new SelectOneForUpdate());
        methodList.add(new SelectListForUpdate());
        return methodList;
    }
}