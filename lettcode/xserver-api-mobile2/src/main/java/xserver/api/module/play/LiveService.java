package xserver.api.module.play;

import java.util.List;

import org.springframework.stereotype.Service;

import xserver.api.module.BaseService;
import xserver.api.module.play.dto.Live;
import xserver.lib.dto.live.LiveDto;
import xserver.lib.tp.live.response.LiveChannelStream;

@Service(value = "LiveService")
public class LiveService extends BaseService {

    /**
     * @param liveId
     * @param stream
     * @param type
     * @return
     */
    public Live getLiveInfo(String liveId, String liveType, String stream, Integer type) {
        Live live = new Live();
        switch (type) {
        case 1:
            LiveDto response = this.facadeService.getLiveService().liveDetail(liveType, liveId, "1007", "zh_cn");
            List<LiveChannelStream> liveStreamList = this.facadeTpDao.getLiveTpDao().getStreamResponseTp("", liveId);

            live.setLiveName(response.getLiveName());
            for (LiveChannelStream liveChannelStream : liveStreamList) {
                if (liveChannelStream.getRateType().contains(stream)) {

                }

            }
            // live.setPlayUrl(playUrl);
            break;

        default:
            break;
        }

        return null;
    }
}
