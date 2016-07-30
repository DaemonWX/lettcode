package xserver.api.module.subject;

import xserver.api.module.CommonParam;
import xserver.api.module.subject.dto.SubjectInfoDto;
import xserver.lib.tp.cms.response.ContentPackage;

/**
 * 专题具体数据获取、封装接口
 * 目前有根据数据，封装，后续根据业务会有zid和ztype的数据获取和封装
 */
public interface SubjectServiceInterface {
    /**
     * 根据专题
     * @param contentPackage
     *            产品包
     * @param commonParam
     * @return
     */
    public SubjectInfoDto getSubjectInfo(ContentPackage contentPackage, CommonParam commonParam);
}
