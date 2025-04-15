package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.annotation.Execute;
import com.hosecloud.hab.plugin.exception.PluginException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * 插件执行器
 * 用于查找并执行带有@Execute注解的方法
 */
public final class PluginExecutor {

    private PluginExecutor() {
    }

    /**
     * 执行插件
     * 查找并调用带有@Execute注解的方法
     *
     * @param plugin 插件实例
     * @return 执行结果
     * @throws IllegalStateException 如果没有找到带有@Execute注解的方法，或者执行过程中出现异常
     */
    public static Object executePlugin(TaskPluginInterface plugin) {
        Class<?> pluginClass = plugin.getClass();

        // 查找带有@Execute注解的方法
        Optional<Method> executeMethod = Arrays.stream(pluginClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Execute.class))
                .findFirst();

        if (executeMethod.isPresent()) {
            try {
                Method method = executeMethod.get();
                return method.invoke(plugin);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PluginException("执行插件方法时出错: " + e.getMessage(), e);
            }
        } else {
            // 如果没有找到带有@Execute注解的方法，则尝试调用execute方法（向后兼容）
            try {
                Method legacyExecuteMethod = pluginClass.getMethod("execute");
                return legacyExecuteMethod.invoke(plugin);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new PluginException("插件没有定义带有@Execute注解的方法，也没有实现execute方法", e);
            }
        }
    }
}