package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定execute方法的返回值类型
 * 该注解可以用于TaskPluginInterface的实现类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSchemaResult {
    /**
     * 返回值类型的类
     *
     * @return 返回值类型的类
     */
    Class<?> value();

    /**
     * 返回值描述
     *
     * @return 返回值描述
     */
    String description() default "";

    /**
     * 返回值示例
     *
     * @return 返回值示例
     */
    String example() default "";
}