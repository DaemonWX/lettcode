package xserver.api.module.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import xserver.api.constant.DataConstants;
import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.subject.constants.SubjectConstants;
import xserver.api.module.subject.dto.BaseSubjectDataDto;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.api.module.subject.dto.SubjectVideoInfoDto;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tp.cms.response.ContentPackage;
import xserver.lib.tp.cms.response.ContentPackage.ContentItem;

/**
 * 视频专题获取封装与获取业务
 */
@Service
class VideoSubjectService extends BaseService implements SubjectServiceInterface {
    /**
     * 视频专题数据获取
     */
    @Override
    public SubjectInfoDto getSubjectInfo(ContentPackage contentPackage, CommonParam commonParam) {
        List<BaseSubjectDataDto> dataList = new ArrayList<BaseSubjectDataDto>();
        SubjectInfoDto subjectInfoDto = new SubjectInfoDto();
        subjectInfoDto.setDataList(dataList);
        //遍历视频专题数据
        for (ContentItem contentItem : contentPackage.getDataList()) {
            SubjectVideoInfoDto subjectVideoInfoDto = new SubjectVideoInfoDto();
            //视频专题的每一个视频需要有一个投票的id,为此视频投票。目前主要在热点频道上有
            subjectVideoInfoDto.setVoteId(SubjectConstants.VOTE_PREFIX + contentItem.getId());
            //视频id
            subjectVideoInfoDto.setId(contentItem.getId());
            //设置一个所属专题属性，方便客户端判断显示逻辑
            subjectVideoInfoDto.setDataType(SubjectConstants.SUBJECT_PACKAGE_TYPE_VIDEO);
            //数据时长
            subjectVideoInfoDto.setDuration(contentItem.getDuration());
            //判断此片是否可下载
            if (contentItem.getDownloadPlatform() != null
                    && contentItem.getDownloadPlatform().containsKey(DataConstants.MOBILE_DOWNLOAD_PLATFORM_CODE)) {
                //可下载
                subjectVideoInfoDto.setDownload(1);
            } else {
                //不可下载
                subjectVideoInfoDto.setDownload(0);
            }
            //片名
            subjectVideoInfoDto.setNameCn(contentItem.getNameCn());
            //判断是否为单视频
            if (contentItem.getPid() != null && contentItem.getPid() > 0) {
                //含有专辑id，非单片
                subjectVideoInfoDto.setType(1);
                //并设置专辑id
                subjectVideoInfoDto.setPid(contentItem.getPid());
            } else {
                //单片
                subjectVideoInfoDto.setType(2);
            }
            subjectVideoInfoDto.setDownload(canDownLoad(contentItem.getDownloadPlatform()));
            //设置显示图片
            setPic(subjectVideoInfoDto, contentItem);
            // 判断是否可播放
            if (contentItem.getPlayPlatform() != null) {
                // 可以播放
                subjectVideoInfoDto.setPlay(1);
            } else {
                //不可以播放
                subjectVideoInfoDto.setPlay(0);
            }
            //取一个视频类型返回给前端，例如正片，预告片，片花等
            Map<String, String> videoTypeMap = contentItem.getVideoType();
            String vt = "";
            if (videoTypeMap != null) {
                for (String k : videoTypeMap.keySet()) {
                    vt = k;
                    break;
                }
            }
            subjectVideoInfoDto.setVideoType(vt);
            dataList.add(subjectVideoInfoDto);
        }
        //专题下数据包名
        subjectInfoDto.setName(contentPackage.getName());
        //专题数据包类型，也是方便前端业务处理
        subjectInfoDto.setType(SubjectConstants.SUBJECT_PACKAGE_TYPE_VIDEO);
        return subjectInfoDto;
    }
    private int canDownLoad(Map<String, String> map){
        if(map==null||!map.keySet().contains(VideoConstants.Downloadright.PHONE)){
            return 0;//不可下载
        }
        return 1;//可下载
    }
    /**
     * 前端显示图片兼容
     * @param baseSubjectDataDto
     * @param contentItem
     */
    private void setPic(BaseSubjectDataDto baseSubjectDataDto, ContentItem contentItem) {
        Map<String, String> picAllMap = contentItem.getPicAll();
        if (picAllMap == null || picAllMap.isEmpty()) {
            return;
        }
        if (picAllMap.get("400*300") != null) {
            baseSubjectDataDto.setPicUrl(picAllMap.get("400*300"));
            return;
        }
        if (picAllMap.get("160*120") != null) {
            baseSubjectDataDto.setPicUrl(picAllMap.get("160*120"));
            return;
        }
    }
}
