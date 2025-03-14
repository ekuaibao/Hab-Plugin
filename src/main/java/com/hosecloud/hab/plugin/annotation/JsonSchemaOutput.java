package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记方法的输出JSON Schema
 * 用于定义插件执行方法的输出结构
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonSchemaOutput {
    /**
     * 输出结果的类型
     *
     * @return 输出结果的类
     */
    Class<?> value();

    /**
     * 输出结果的描述
     *
     * @return 描述信息
     */
    String description() default "";

    /**
     * 输出结果的示例
     *
     * @return JSON格式的示例
     */
    String example() default "";
}