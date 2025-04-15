package com.hosecloud.hab.plugin.exception;

public class PluginException extends RuntimeException {
    public PluginException(String s, Throwable e) {
        super(s, e);
    }
}
