package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于定义枚举值的注解
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
    /**
     * 显示文本
     *
     * @return 显示文本
     */
    String label();

    /**
     * 实际值
     *
     * @return 实际值
     */
    String value();
}
