package xserver.api.module.appointment;

import xserver.api.module.CommonParam;

public class AppointmentConstant {

    public static final String APPOINTMENT_TP_SUCCESS = "10000";

    public static final String getEventCenterAPPID(CommonParam c) {
        return "com.letv.android.sports";
    }

    public static final String getEventCenterEventType(String sourceType) {
        return sourceType;
    }

    public static final String getEventCenterEventID(String eventId) {
        return eventId;
    }

    public static final String getAppAPPID(String cappId) {
        return cappId;
    }

    public static final String getAppEventType(String sourceType) {
        return sourceType;
    }

    public static final String getAppEventID(String eventId) {
        return eventId;
    }
}
