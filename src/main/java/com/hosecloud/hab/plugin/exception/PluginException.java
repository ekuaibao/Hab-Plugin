package com.hosecloud.hab.plugin.exception;

/**
 * 插件异常
 * @author zhangping
 */
public class PluginException extends RuntimeException {
    public PluginException(String s, Throwable e) {
        super(s, e);
    }
}
