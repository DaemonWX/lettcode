package xserver.api.module;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.response.BaseResponse;
import xserver.common.cache.CbaseRmiClient;
import xserver.lib.mysql.lead.FacadeLeadMysqlDao;
import xserver.lib.tp.FacadeTpDao;
import xserver.lib.tpcache.TpCacheTemplate;
import xserver.lib.util.MessageUtils;

/**
 * Service基类
 */
public class BaseService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    protected FacadeService facadeService;

    @Autowired(required = false)
    protected FacadeLeadMysqlDao facadeLeadMysqlDao;

    @Resource
    protected FacadeTpDao facadeTpDao;

    @Resource
    protected TpCacheTemplate tpCacheTemplate;

    protected CbaseRmiClient cbaseRmiClient = CbaseRmiClient.getInstance();

    public CbaseRmiClient getCbaseRmiClient() {
        return cbaseRmiClient;
    }

    public void setCbaseRmiClient(CbaseRmiClient cbaseRmiClient) {
        this.cbaseRmiClient = cbaseRmiClient;
    }

    public FacadeLeadMysqlDao getFacadeLeadMysqlDao() {
        return facadeLeadMysqlDao;
    }

    public FacadeTpDao getFacadeTpDao() {
        return facadeTpDao;
    }

    public Logger getLog() {
        return log;
    }

    public TpCacheTemplate getTpCacheTemplate() {
        return tpCacheTemplate;
    }

    /**
     * 设置接口处理失败时的返回值，其中错误信息的国际化文案，由errMsgCode指定
     * @param response
     * @param errCode
     * @param errMsgCode
     * @param locale
     * @return
     */
    protected void setErrorResponse(BaseResponse response, String errorCode, String errorMsgCode, Locale locale) {
        if (response != null) {
            response.setStatus(ErrorCodeConstants.RESPONSE_FAIL_CODE);
            response.setErrorCode(errorCode);
            response.setErrorMessage(MessageUtils.getMessage(errorMsgCode, locale));
        }
    }

    protected void setErrorResponse(BaseResponse response, String errorCode, String errorMsgCode, String langCode) {
        if (response != null) {
            response.setStatus(ErrorCodeConstants.RESPONSE_FAIL_CODE);
            response.setErrorCode(errorCode);
            response.setErrorMessage(MessageUtils.getMessage(errorMsgCode, langCode));
        }
    }
}
