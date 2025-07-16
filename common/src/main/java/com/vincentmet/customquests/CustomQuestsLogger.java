package com.vincentmet.customquests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomQuestsLogger {
    public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

    public static void info(String message) {
        LOG.info(message);
    }
    public static void info(String message, Object... args) {
        LOG.info(message, args);
    }
    public static void warn(String message) {
        LOG.warn(message);
    }
    public static void warn(String message, Throwable throwable) {
        LOG.warn(message, throwable);
    }
    public static void error(String message) {
        LOG.error(message);
    }
    public static void error(String message, Throwable throwable) {
        LOG.error(message, throwable);
    }
    public static void debug(String message) {
        LOG.debug(message);
    }
    public static void debug(String message, Object... args) {
        LOG.debug(message, args);
    }
    public static void fatal(String message) {
        // There's no fatal log level in SLF4J, but we can use error for critical issues
        error(message);
    }
    public static void fatal(String message, Throwable throwable) {
        // There's no fatal log level in SLF4J, but we can use error for critical issues
        error(message, throwable);
    }
}
