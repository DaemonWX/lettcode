package xserver.api.module.error;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import xserver.lib.exception.LetvCommonException;
import xserver.lib.util.MessageUtils;

public class LetvExceptionResolver implements HandlerExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        String langCode = request.getParameter("langcode");

        /*
         * 这里设置默认值，防止调用MessageUtils.getMessage(String code, String lang)返回null
         * 而导致调用URLEncoder.encode(String s, String enc)抛出NullPointException
         */
        if (StringUtils.isBlank(langCode)) {
            langCode = MessageUtils.DEFAULT_FULL_LOCAL_ZH_CN;
        }

        String errorCode = "-100";
        String message = "";
        try {
            String msg = MessageUtils.getMessage("SYSTEM_ERROR", langCode);
            if (msg != null) {
                message = URLEncoder.encode(msg, "UTF-8");
            } else {
                this.logger.warn("not find errormsg in properites . errCode:SYSTEM_ERROR" + ",langCode:" + langCode);
            }
            if (StringUtils.isEmpty(message)) {
                message = URLEncoder.encode("服务异常请重试！", "UTF-8");
            }
        } catch (UnsupportedEncodingException e1) {
            this.logger.error(ex.getMessage(), ex);
            e1.printStackTrace();
        }
        String path = request.getRequestURI();
        if (ex instanceof LetvCommonException) {
            LetvCommonException exception = (LetvCommonException) ex;
            errorCode = exception.getErrorCode();
            try {
                String msg = MessageUtils.getMessage(errorCode, langCode);
                if (msg != null) {
                    message = URLEncoder.encode(msg, "UTF-8");
                } else {
                    this.logger
                            .warn("not find errormsg in properites . errCode:" + errorCode + ",langCode:" + langCode);
                }
                if (StringUtils.isEmpty(message)) {
                    message = URLEncoder.encode(exception.getMessage(), "UTF-8");
                }   
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ModelAndView mv = new ModelAndView();
        String errorURL = "/err/errorHandler.json" + "?path=" + path + "&resultStatus=0" + "&httpStatusCode="
                + HttpStatus.BAD_REQUEST.value() + "&message=" + message + "&errorCode=" + errorCode + "&exception="
                + ex;
        mv.setView(new InternalResourceView(errorURL));
        if (ex instanceof LetvCommonException) {
            this.logger.warn(ex.getMessage());
        } else {
            this.logger.error(ex.getMessage(), ex);
        }
        return mv;
    }
}
