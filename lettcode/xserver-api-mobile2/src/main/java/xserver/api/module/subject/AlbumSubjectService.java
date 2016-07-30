package xserver.api.module.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.subject.constants.SubjectConstants;
import xserver.api.module.subject.dto.BaseSubjectDataDto;
import xserver.api.module.subject.dto.SubjectAlbumInfoDto;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.lib.constant.CommonConstants;
import xserver.lib.constant.VideoConstants;
import xserver.lib.tp.cms.response.ContentPackage;
import xserver.lib.tp.cms.response.ContentPackage.ContentItem;

/**
 * 专题专辑名数据获取与封装
 */
@Service
class AlbumSubjectService extends BaseService implements SubjectServiceInterface {
    /**
     * 数据封装
     */
    @Override
    public SubjectInfoDto getSubjectInfo(ContentPackage contentPackage, CommonParam commonParam) {
        List<BaseSubjectDataDto> dataList = new ArrayList<BaseSubjectDataDto>();
        SubjectInfoDto subjectInfoDto = new SubjectInfoDto();
        subjectInfoDto.setDataList(dataList);
        // 获取数据列表
        for (ContentItem contentItem : contentPackage.getDataList()) {
            if (contentItem.getPlayPlatform() != null) {
                SubjectAlbumInfoDto subjectAlbumInfoDto = new SubjectAlbumInfoDto();
                // 专辑id
                subjectAlbumInfoDto.setId(contentItem.getId());
                // 所属专题类型，供前端展示
                subjectAlbumInfoDto.setDataType(SubjectConstants.SUBJECT_PACKAGE_TYPE_ALBUM);
                // 专辑名称
                subjectAlbumInfoDto.setNameCn(contentItem.getNameCn());
                subjectAlbumInfoDto.setDownload(canDownLoad(contentItem.getDownloadPlatform()));
                // 设置图片
                setPic(subjectAlbumInfoDto, contentItem);
                // 设置副标题
                subjectAlbumInfoDto.setSubTitle(contentItem.getSubTitle());
                dataList.add(subjectAlbumInfoDto);
            }
        }
        // 专题下，专辑包名称
        subjectInfoDto.setName(contentPackage.getName());
        // 专题类型，方便供前端展示
        subjectInfoDto.setType(SubjectConstants.SUBJECT_PACKAGE_TYPE_ALBUM);
        return subjectInfoDto;
    }

    private int canDownLoad(Map<String, String> map) {
        if (map == null || !map.keySet().contains(VideoConstants.Downloadright.PHONE)) {
            return 0;// 不可下载
        }
        return 1;// 可下载
    }

    /**
     * 前端图片兼容
     * @param baseSubjectDataDto
     * @param contentItem
     */
    private void setPic(BaseSubjectDataDto baseSubjectDataDto, ContentItem contentItem) {
        Map<String, String> picAllMap = contentItem.getPicCollections();
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
