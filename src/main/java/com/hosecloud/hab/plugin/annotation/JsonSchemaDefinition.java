package com.hosecloud.hab.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要生成JSON Schema的类
 * 该注解可以用于TaskPluginInterface的实现类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSchemaDefinition {
    /**
     * Schema标题
     *
     * @return Schema标题
     */
    String title() default "";

    /**
     * Schema描述
     *
     * @return Schema描述
     */
    String description() default "";

    /**
     * Schema版本
     *
     * @return Schema版本
     */
    String version() default "http://json-schema.org/draft-04/schema#";
}