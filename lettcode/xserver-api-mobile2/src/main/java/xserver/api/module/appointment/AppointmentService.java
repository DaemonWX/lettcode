package xserver.api.module.appointment;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.appointment.dto.AppointmentEventDto;
import xserver.api.module.superlive.dto.LiveChannelCurrentStateDataDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.appointment.request.AddAppointmentRequest;
import xserver.lib.tp.appointment.request.CheckAppointmentRequest;
import xserver.lib.tp.appointment.request.DelAppointmentRequest;
import xserver.lib.tp.appointment.request.UserAppointmentListRequest;
import xserver.lib.tp.appointment.response.AppointmentAddOrDelTpResponse;
import xserver.lib.tp.appointment.response.CheckAppointmentTpResponse;
import xserver.lib.tp.appointment.response.UserAppointmentListTpResponse;
import xserver.lib.tp.appointment.response.UserAppointmentListTpResponse.UserAppointmentData;
import xserver.lib.tpcache.CacheConstants;

@Service(value = "appointmentService")
public class AppointmentService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public Response<ValueDto<Boolean>> add(String token, String sourceType, String eventId, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String[] eventIds = eventId.split(",");
        if (eventIds == null || eventIds.length > 20) {
            setErrorResponse(response, ErrorCodeConstants.Appointment_ADD_FAIL_2,
                    ErrorCodeConstants.Appointment_ADD_FAIL_2, param.getLangcode());
            return response;
        }
        for (String e : eventIds) {
            AddAppointmentRequest add = new AddAppointmentRequest();
            add.appId(AppointmentConstant.getEventCenterAPPID(param));
            add.eventId(AppointmentConstant.getEventCenterEventID(e));
            add.eventType(AppointmentConstant.getEventCenterEventType(sourceType));
            add.token(token);
            AppointmentAddOrDelTpResponse addOrDelTpResponse = facadeTpDao.getAppointmentTpDao().addAppointment(add);
            if (addOrDelTpResponse != null
                    && AppointmentConstant.APPOINTMENT_TP_SUCCESS.equals(addOrDelTpResponse.getErrno())) {
                if (addOrDelTpResponse.getData() != null && addOrDelTpResponse.getData().getResult() != null
                        && addOrDelTpResponse.getData().getResult() == Boolean.TRUE) {
                    response.setData(new ValueDto<Boolean>(Boolean.TRUE));
                } else {
                    setErrorResponse(response, ErrorCodeConstants.Appointment_ADD_FAIL_1,
                            ErrorCodeConstants.Appointment_ADD_FAIL_1, param.getLangcode());
                }
            } else {
                setErrorResponse(response, ErrorCodeConstants.Appointment_ADD_FAIL_2,
                        ErrorCodeConstants.Appointment_ADD_FAIL_2, param.getLangcode());
            }
        }
        return response;
    }

    public Response<ValueDto<Boolean>> delete(String token, String sourceType, String eventId, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String[] eventIds = eventId.split(",");
        if (eventIds == null || eventIds.length > 20) {
            setErrorResponse(response, ErrorCodeConstants.Appointment_DEL_FAIL_2,
                    ErrorCodeConstants.Appointment_DEL_FAIL_2, param.getLangcode());
            return response;
        }
        for (String e : eventIds) {
            DelAppointmentRequest del = new DelAppointmentRequest();
            del.appId(AppointmentConstant.getEventCenterAPPID(param));
            del.eventId(AppointmentConstant.getEventCenterEventID(e));
            del.eventType(AppointmentConstant.getEventCenterEventType(sourceType));
            del.token(token);
            AppointmentAddOrDelTpResponse addOrDelTpResponse = facadeTpDao.getAppointmentTpDao().delAppointment(del);
            if (addOrDelTpResponse != null
                    && AppointmentConstant.APPOINTMENT_TP_SUCCESS.equals(addOrDelTpResponse.getErrno())) {
                if (addOrDelTpResponse.getData() != null && addOrDelTpResponse.getData().getResult() != null
                        && addOrDelTpResponse.getData().getResult() == Boolean.TRUE) {
                    response.setData(new ValueDto<Boolean>(Boolean.TRUE));
                } else {
                    setErrorResponse(response, ErrorCodeConstants.Appointment_DEL_FAIL_1,
                            ErrorCodeConstants.Appointment_DEL_FAIL_1, param.getLangcode());
                }
            } else {
                setErrorResponse(response, ErrorCodeConstants.Appointment_DEL_FAIL_2,
                        ErrorCodeConstants.Appointment_DEL_FAIL_2, param.getLangcode());
            }
        }
        return response;
    }

    public Response<ValueDto<Boolean>> check(String token, String sourceType, String eventId, CommonParam param) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        CheckAppointmentRequest check = new CheckAppointmentRequest();
        check.eventId(AppointmentConstant.getEventCenterEventID(eventId));
        check.eventType(AppointmentConstant.getEventCenterEventType(sourceType));
        check.token(token);
        CheckAppointmentTpResponse checkAppointmentTpResponse = facadeTpDao.getAppointmentTpDao().checkAppointment(
                check);
        if (checkAppointmentTpResponse != null
                && AppointmentConstant.APPOINTMENT_TP_SUCCESS.equals(checkAppointmentTpResponse.getErrno())) {
            if (checkAppointmentTpResponse.getData() != null
                    && checkAppointmentTpResponse.getData().getResult() != null
                    && checkAppointmentTpResponse.getData().getResult() == Boolean.TRUE) {
                response.setData(new ValueDto<Boolean>(Boolean.TRUE));
            } else {
                response.setData(new ValueDto<Boolean>(Boolean.FALSE));
            }
        } else {
            setErrorResponse(response, ErrorCodeConstants.Appointment_CHECK_FAIL_2,
                    ErrorCodeConstants.Appointment_CHECK_FAIL_2, param.getLangcode());
        }
        return response;
    }

    public PageResponse<AppointmentEventDto> list(String token, Integer offSet, Integer size, CommonParam param) {
        PageResponse<AppointmentEventDto> response = new PageResponse<AppointmentEventDto>();
        UserAppointmentListRequest list = new UserAppointmentListRequest();
        list.length(size);
        list.offset(offSet);
        list.token(token);
        // 从事件中心获取用户预约事件列表
        UserAppointmentListTpResponse userAppointmentListTpResponse = facadeTpDao.getAppointmentTpDao()
                .listUserAppointment(list);
        if (userAppointmentListTpResponse != null && userAppointmentListTpResponse.getData() != null) {
            // 请求成功
            List<UserAppointmentData> list2 = userAppointmentListTpResponse.getData();
            List<AppointmentEventDto> l = new ArrayList<AppointmentEventDto>();
            for (UserAppointmentData userAppointmentData : list2) {
                AppointmentEventDto dto = new AppointmentEventDto();
                dto.setEventType(AppointmentConstant.getAppEventType(userAppointmentData.getSourceType()));
                dto.setEventId(AppointmentConstant.getAppEventType(userAppointmentData.getSourceID()));
                dto.setAppId(AppointmentConstant.getAppAPPID(userAppointmentData.getcAppID()));
                dto.setUserId(userAppointmentData.getUserID());
                if (EventTypeConstant.LIVE_MUSIC.getEventType() == Integer
                        .parseInt(userAppointmentData.getSourceType())
                        || EventTypeConstant.LIVE_MUSIC.getEventType() == Integer.parseInt(userAppointmentData
                                .getSourceType())
                        || EventTypeConstant.LIVE_ENT.getEventType() == Integer.parseInt(userAppointmentData
                                .getSourceType())) {
                    LiveChannelCurrentStateDataDto liveChannelCurrentStateDataDto = tpCacheTemplate.get(
                            CacheConstants.SUPERLIVE_LIVEROOM_LIVEID + userAppointmentData.getSourceID(),
                            LiveChannelCurrentStateDataDto.class);
                    if (liveChannelCurrentStateDataDto == null) {
                        continue;
                    } else {
                        dto.setTitle(liveChannelCurrentStateDataDto.getCur().getTitle());
                    }
                }
                l.add(dto);
            }
            response.setData(l);
        } else {
            setErrorResponse(response, ErrorCodeConstants.Appointment_USER_LIST_FAIL_1,
                    ErrorCodeConstants.Appointment_USER_LIST_FAIL_1, param.getLangcode());
        }
        return response;
    }

}
