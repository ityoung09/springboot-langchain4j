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

    @Tool("我要取消预定，预定号和姓名")
    public String cancel(@P("预定号") String yd, @P("姓名") String name) {
        System.out.println("开始取消航班" + yd + "，姓名为" + name);
        return "预定号为" + yd + "，姓名为" + name;
    }

    @Tool("我要查询航班信息预定，预定号和姓名")
    public String queryFlyInfo(@P("预定号") String yd, @P("姓名") String name) {
        System.out.println("开始查询航班信息" + yd + "，姓名为" + name);
        return "你的预定详细信息为预定号为" + yd + "，姓名为" + name;
    }
}
