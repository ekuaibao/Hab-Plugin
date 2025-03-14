package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记插件的执行方法
 * 被此注解标记的方法将作为插件的执行入口
 * 方法必须是公共的，且返回值类型不限
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Execute {
    /**
     * 执行方法的描述信息
     *
     * @return 描述信息
     */
    String description() default "";

    /**
     * 操作ID，用于API文档
     *
     * @return 操作ID
     */
    String operationId() default "";

    /**
     * 标签，用于API文档分组
     *
     * @return 标签数组
     */
    String[] tags() default {};

    /**
     * 输出结果的类型
     * 如果设置了此属性，将覆盖方法上的@JsonSchemaOutput注解
     *
     * @return 输出结果的类
     */
    Class<?> outputClass() default void.class;

    /**
     * 输出结果的描述
     * 如果设置了此属性，将覆盖方法上的@JsonSchemaOutput注解
     *
     * @return 描述信息
     */
    String outputDescription() default "";

    /**
     * 输出结果的示例
     * 如果设置了此属性，将覆盖方法上的@JsonSchemaOutput注解
     *
     * @return JSON格式的示例
     */
    String outputExample() default "";
}