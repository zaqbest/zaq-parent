package com.zaqbest.base.cache.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.redisson.api.NameMapper;

@Data
public class ZaqNameMapper implements NameMapper {
    private String prefix;
    private String sep = ":";

    public ZaqNameMapper() {
    }

    public ZaqNameMapper(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String map(String name) {
        return StrUtil.format("{}", prefix + sep + name);
    }

    @Override
    public String unmap(String name) {
        return StrUtil.removePrefix(name, prefix + sep);
    }
}
