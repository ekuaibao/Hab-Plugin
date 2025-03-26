package com.hosecloud.hab.plugin;

import com.hosecloud.hab.plugin.api.HoseHttpService;
import com.hosecloud.hab.plugin.cache.CacheService;
import com.hosecloud.hab.plugin.model.Log;
import org.apache.http.client.HttpClient;
import org.pf4j.ExtensionPoint;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 任务插件接口，所有插件都需要实现此接口
 */
public interface TaskPluginInterface extends ExtensionPoint {

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getName();

    /**
     * 设置公司ID
     *
     * @param corporationId 公司ID
     */
    void setCorporationId(String corporationId);

    /**
     * 执行插件任务
     * 此方法现在是默认实现，会查找并调用带有@Execute注解的方法
     * 为了向后兼容，如果没有找到带有@Execute注解的方法，则会尝试调用子类重写的execute方法
     *
     * @return 执行结果
     */
    default Object execute() {
        return PluginExecutor.executePlugin(this);
    }

    /**
     * 设置缓存服务
     *
     * @param cacheService 缓存服务
     */
    void setCacheService(CacheService cacheService);

    /**
     * 设置HTTP服务
     *
     * @param httpClient HTTP服务
     */
    void setHttpClient(HttpClient httpClient);

    /**
     * 设置Hose HTTP服务
     * @param httpService Hose HTTP服务
     */
    void setHoseHttpService(HoseHttpService httpService);

    /**
     * 获取插件执行日志
     *
     * @return 执行日志列表
     */
    List<Log> getExecuteLogs();

    /**
     * 设置插件参数
     *
     * @param parameters 参数Map
     */
    default void setParameters(Map<String, Object> parameters) {
        // 默认空实现，插件可以选择性重写
        // 通过反射实现这个能力，查找实现类中参数中是否有这个参数，如果有则设置
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            try {
                Field field = this.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, entry.getValue());
            } catch (NoSuchFieldException e) {
                // 忽略不存在的字段
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}