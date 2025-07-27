package com.cucumberconstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

    /**
     * Returns a Log4j2 Logger instance for the given class.
     * @param clazz The class for which to get the logger.
     * @return A Logger instance.
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
