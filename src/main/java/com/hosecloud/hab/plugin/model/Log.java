package com.hosecloud.hab.plugin.model;

/**
 * 表示带有级别的日志
 */
public class Log {
    /**
     * -- GETTER --
     *  获取日志消息
     *
     * @return 日志消息
     */
    private final String message;
    /**
     * -- GETTER --
     *  获取日志级别
     *
     * @return 日志级别
     */
    private final LogLevel level;

    /**
     * 创建一个新的日志
     *
     * @param message 日志消息
     * @param level 日志级别
     */
    public Log(String message, LogLevel level) {
        this.message = message;
        this.level = level;
    }

    /**
     * 创建一个成功级别的日志
     *
     * @param message 日志消息
     * @return 成功级别的日志
     */
    public static Log success(String message) {
        return new Log(message, LogLevel.SUCCESS);
    }

    /**
     * 创建一个失败级别的日志
     *
     * @param message 日志消息
     * @return 失败级别的日志
     */
    public static Log failure(String message) {
        return new Log(message, LogLevel.FAILURE);
    }

    /**
     * 创建一个信息级别的日志
     *
     * @param message 日志消息
     * @return 信息级别的日志
     */
    public static Log info(String message) {
        return new Log(message, LogLevel.INFO);
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Log{" +
                "message='" + message + '\'' +
                ", level=" + level +
                '}';
    }
}