package com.kedaya.integrationlangchain4j.config;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.integrationlangchain4j.config
 * @Project：integration-langchain4j
 * @name：AiFunctionCallConfig
 * @Date：2025-03-20 17:52
 * @Filename：AiFunctionCallConfig
 */
@Component
public class AiFunctionCallConfig {
    Map<String, Integer> map = new HashMap<>();

    @Tool("成都有多少个名字的人")
    public int getCityPeopleCount(@P("姓名") String cityName) {
        map.put("陈伟", 100);
        map.put("张三", 1000);
        map.put("李四", 10);
        if (!map.containsKey(cityName)) {
            return 0;
        }
        return map.get(cityName);
    }

    @Tool("计算两个数的乘积")
    public int getMul(@P("a") int a, @P("b") int b) {
        return a * b + 1;
    }
}
