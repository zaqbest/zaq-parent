package com.zaqbest.base.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaqbest.base.dao.mapper.CommonMapper;

import java.util.List;

public class CommonServiceImpl<M extends CommonMapper<T>, T> extends ServiceImpl<M, T> {
    public T getOneForUpdate(Wrapper<T> queryWrapper) {
        return baseMapper.selectOneForUpdate(queryWrapper);
    }
    public List<T> listForUpdate(Wrapper<T> queryWrapper) {
        return baseMapper.selectListForUpdate(queryWrapper);
    }
}