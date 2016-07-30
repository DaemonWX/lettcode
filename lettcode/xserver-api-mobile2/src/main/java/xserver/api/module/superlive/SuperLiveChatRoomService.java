package xserver.api.module.superlive;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import xserver.api.module.BaseService;
import xserver.common.request.CommonParam;
import xserver.common.response.Response;
import xserver.lib.tp.chatroom.request.GetHistoryChatHistoryRequest;
import xserver.lib.tp.chatroom.request.SendChatMsgRequest;
import xserver.lib.tp.chatroom.response.ChatRoomChatHistoryDto;
import xserver.lib.tp.chatroom.response.GetHistoryChatHistoryResponse;
import xserver.lib.tp.chatroom.response.GetHistoryChatHistoryResponse.ChatHistoryResult;
import xserver.lib.tp.chatroom.response.SendChatMsgResposne;

/**
 * 超级live直播弹幕业务
 */
@Service(value = "superLiveChatRoomService")
public class SuperLiveChatRoomService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(SuperLiveChatRoomService.class);

    /**
     * 获取弹幕服务器地址
     * 获取弹幕最近聊天记录
     * @param roomId
     *            聊天室id
     * @param server
     *            是否创建服务。此参数由第三方决定传什么值，目前从前端默认
     * @param param
     *            通用参数
     * @return
     */
    public Response<ChatRoomChatHistoryDto> getHistoryMsg(String roomId, Boolean server, String version,
            CommonParam param) {
        Response<ChatRoomChatHistoryDto> response = new Response<ChatRoomChatHistoryDto>();
        GetHistoryChatHistoryRequest getHistoryChatHistoryRequest = new GetHistoryChatHistoryRequest();
        getHistoryChatHistoryRequest.roomId(roomId).server(server).version(version);
        log.info("getHistoryMsg roomId=" + roomId + ",server=" + server);
        GetHistoryChatHistoryResponse r = facadeTpDao.getChatRoomTpDao().getChatHistory(getHistoryChatHistoryRequest);
        if (r != null && r.getData() != null && r.getData().getResult() != null) {
            ChatHistoryResult result = r.getData().getResult();
            ChatRoomChatHistoryDto dto = new ChatRoomChatHistoryDto();
            dto.setList(result.getList());
            dto.setRoomInfo(result.getRoomInfo());

            Object serverObj = result.getServer(); // 历史遗留问题，此字段可能是一个对象，也可能是一个字符串
            if (serverObj != null) {
                if (serverObj instanceof String) {
                    dto.setServer((String) serverObj);
                } else if (serverObj instanceof Map) {
                    try {
                        Map map = (Map) serverObj;
                        dto.setServer((String) map.get("server"));
                    } catch (Exception e) {
                        log.error("chat history parse server error", e);
                    }
                }
                response.setData(dto);
            }

        } else {
            log.info("getHistoryMsg roomId=" + roomId + ",server=" + server + ",response is null.");
        }
        return response;
    }

    /**
     * 提交信息
     * @param roomId
     *            聊天室id
     * @param message
     *            消息内容
     * @param sso_tk
     *            用户token
     * @param param
     *            通用参数
     * @return
     */
    public Response<HashMap<String, Object>> sendMsg(String roomId, String message, String sso_tk, CommonParam param) {
        Response<HashMap<String, Object>> response = new Response<HashMap<String, Object>>();
        SendChatMsgRequest sendRequest = new SendChatMsgRequest();
        sendRequest.clientIp(param.getIp()).message(message).roomId(roomId).sso_tk(sso_tk);
        log.info("sendMsg roomId=" + roomId + ",message=" + message + ",sso_tk=" + sso_tk);
        SendChatMsgResposne r = facadeTpDao.getChatRoomTpDao().sendChatMsg(sendRequest);
        if (r != null && r.getCode() != null) {
            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("code", r.getCode());
            if(r.getCode()!=200&&r.getData()!=null){
                map.put("msg", r.getData().get("errorMessage"));
            }
            response.setData(map);
        } else {
            log.info("sendMsg roomId=" + roomId + ",message=" + message + ",sso_tk=" + sso_tk + " failed!");
        }
        return response;
    }

}
