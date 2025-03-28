package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要生成JSON Schema的字段
 * 该注解可以用于TaskPluginInterface的实现类中的字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSchemaProperty {
    /**
     * 字段标题
     *
     * @return 字段标题
     */
    String title() default "";

    /**
     * 字段描述
     *
     * @return 字段描述
     */
    String description() default "";

    /**
     * 是否必填
     *
     * @return 是否必填
     */
    boolean required() default false;

    /**
     * 字段示例值
     *
     * @return 字段示例值
     */
    String example() default "";

    /**
     * 字段格式
     *
     * @return 字段格式
     */
    String format() default "";

    /**
     * 最小值（适用于数字类型）
     *
     * @return 最小值
     */
    double minimum() default Double.MIN_VALUE;

    /**
     * 最大值（适用于数字类型）
     *
     * @return 最大值
     */
    double maximum() default Double.MAX_VALUE;

    /**
     * 最小长度（适用于字符串类型）
     *
     * @return 最小长度
     */
    int minLength() default 0;

    /**
     * 最大长度（适用于字符串类型）
     *
     * @return 最大长度
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * 正则表达式模式（适用于字符串类型）
     *
     * @return 正则表达式模式
     */
    String pattern() default "";
    
    /**
     * 默认值（JSON字符串格式）
     * 在Formily中，此值会被映射到initialValue属性
     *
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * 枚举值列表
     * 用于定义字段的可选值列表
     * 例如：@EnumValue(label = "启用", value = "enabled")
     *
     * @return 枚举值列表
     */
    EnumValue[] enumValues() default {};

    /**
     * 自定义Formily样式（JSON字符串格式）
     * 用于定义字段在Formily中的自定义样式
     * 如果设置了此属性，将优先使用此样式配置
     * 例如：{"width": "100%", "marginBottom": "10px"}
     *
     * @return 自定义Formily样式
     */
    String formilyStyle() default "";
}