package com.example.messengerquery.util;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggingUtil {

    public void error(Logger log, String type, String message, String methodName) {
        log.error("type : {}, message : {}, method : {}", type, message, methodName);
    }

    public void debug(Logger log, String type, String message, String methodName, Throwable ex) {
        log.debug("type : {}, message : {}, method : {}, details : ", type, message, methodName, ex);
    }

    public void info(Logger log, String type, String methodName, Object data) {
        log.info("type : {}, method : {}, data : {}", type, methodName, data);
    }
}
