package xserver.api.module.subject;

import org.springframework.stereotype.Service;

import xserver.api.module.BaseService;
import xserver.api.module.CommonParam;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.lib.tp.cms.response.ContentPackage;

/**
 * 直播专题数据获取与封装业务
 */
@Service
class LiveSubjectService extends BaseService implements SubjectServiceInterface {
    /**
     * 直播专题数据封装
     * 目前无需求
     */
    @Override
    public SubjectInfoDto getSubjectInfo(ContentPackage contentPackage, CommonParam commonParam) {
        // TODO need implements
        return null;
    }
}
