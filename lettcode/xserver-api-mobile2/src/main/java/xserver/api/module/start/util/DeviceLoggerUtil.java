package xserver.api.module.start.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeviceLoggerUtil {

    private static final DeviceLoggerUtil L = new DeviceLoggerUtil();
    public Log LOGGER = LogFactory.getLog("device");

    public void log(String msg) {
        LOGGER.info(msg);
    }

    public static DeviceLoggerUtil getLogger() {
        return L;
    }
}
