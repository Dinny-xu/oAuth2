package com.study.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class CommonUtils {

    /**
     * 实体拷贝
     *
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        T t = ReflectUtil.newInstance(destinationClass);
        BeanUtil.copyProperties(source, t);
        return t;
    }

    /**
     * 实体拷贝
     *
     * @param source
     * @param destinationClass
     * @param field            需要忽略的字段
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass, String... field) {
        T t = ReflectUtil.newInstance(destinationClass);
        BeanUtil.copyProperties(source, t, field);
        return t;
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        return (List<T>) sourceList.stream()
                .map(ob -> map(ob, destinationClass))
                .collect(Collectors.toList());
    }

}
