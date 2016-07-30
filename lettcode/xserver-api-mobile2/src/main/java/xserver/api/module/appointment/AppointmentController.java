package xserver.api.module.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.appointment.dto.AppointmentEventDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;

/**
 * 预约业务控制器
 */

@Controller
public class AppointmentController extends BaseController {
    @RequestMapping(value = "/appointment/event/add")
    public Response<ValueDto<Boolean>> add(@ModelAttribute CommonParam param,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "eventType") String eventType, @RequestParam(value = "eventId") String eventId) {
        Response<ValueDto<Boolean>> response = facadeService.getAppointmentService().add(token, eventType, eventId,
                param);
        return response;
    }

    @RequestMapping(value = "/appointment/event/delete")
    public Response<ValueDto<Boolean>> delete(@ModelAttribute CommonParam param,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "eventType") String eventType, @RequestParam(value = "eventId") String eventId) {
        Response<ValueDto<Boolean>> response = facadeService.getAppointmentService().delete(token, eventType, eventId,
                param);
        return response;
    }

    @RequestMapping(value = "/appointment/event/check")
    public Response<ValueDto<Boolean>> check(@ModelAttribute CommonParam param,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "eventType") String eventType, @RequestParam(value = "eventId") String eventId) {
        Response<ValueDto<Boolean>> response = facadeService.getAppointmentService().check(token, eventType, eventId,
                param);
        return response;
    }

    @RequestMapping(value = "/appointment/event/list")
    public PageResponse<AppointmentEventDto> list(@ModelAttribute CommonParam param,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "size", required = false) Integer size) {
        PageResponse<AppointmentEventDto> response = facadeService.getAppointmentService().list(token, start, size,
                param);
        return response;
    }
}
