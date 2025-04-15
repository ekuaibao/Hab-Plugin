package com.hosecloud.hab.plugin.exception;

/**
 * 插件执行异常
 * @author zhangping
 */
public class PluginExecutionException extends PluginException {

    public PluginExecutionException(String s, Throwable e) {
        super(s, e);
    }
}
