package xserver.api.module.superlive;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.module.BaseController;
import xserver.common.request.CommonParam;
import xserver.common.response.Response;
import xserver.lib.tp.chatroom.response.ChatRoomChatHistoryDto;

/**
 * 超级Live弹幕
 */
@Controller
public class SuperLiveChatRoomController extends BaseController {

    /**
     * 提交聊天记录
     * @param param
     */
    @RequestMapping(value = "/superlive/chatroom/commit")
    public Response<HashMap<String, Object>> sendMsg(@ModelAttribute CommonParam param,
            @RequestParam("roomId") String roomId, @RequestParam("message") String message,
            @RequestParam("sso_tk") String sso_tk) {
        Response<HashMap<String, Object>> response = facadeService.getSuperLiveChatRoomService().sendMsg(roomId, message,
                sso_tk, param);
        return response;
    }

    /**
     * 获取历史聊天记录
     * @param param
     */
    @RequestMapping(value = "/superlive/chatroom/history/get")
    public Response<ChatRoomChatHistoryDto> getHistoryMsg(@RequestParam("roomId") String roomId,
            @RequestParam(value = "server", defaultValue = "true", required = false) Boolean server,
            @RequestParam(value = "version", required = false) String version, @ModelAttribute CommonParam param) {
        Response<ChatRoomChatHistoryDto> response = facadeService.getSuperLiveChatRoomService().getHistoryMsg(roomId,
                server, version, param);
        return response;
    }

}
