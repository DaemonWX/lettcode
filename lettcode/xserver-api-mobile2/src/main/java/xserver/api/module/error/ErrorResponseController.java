/*
 *  Copyright (c) 2011 乐视网（letv.com）. All rights reserved
 * 
 *  LETV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package xserver.api.module.error;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.constant.ErrorCodeConstants;
import xserver.api.module.BaseController;
import xserver.api.response.Response;
import xserver.lib.exception.LetvCommonException;

@Controller
public class ErrorResponseController extends BaseController {

    @RequestMapping(value = "/err/errorHandler", method = { RequestMethod.GET, RequestMethod.POST })
    public Response<Object> responseWithError(@RequestParam("path") String path,
            @RequestParam("resultStatus") String resultStatus, @RequestParam("httpStatusCode") int httpStatusCode,
            @RequestParam("message") String message, @RequestParam("exception") String exception,
            @RequestParam("errorCode") String errorCode, HttpServletRequest request) {
        Response<Object> commonError = new Response<Object>();
        commonError.setStatus(ErrorCodeConstants.RESPONSE_FAIL_CODE);
        commonError.setErrorCode(errorCode);
        try {
            commonError.setErrorMessage(URLDecoder.decode(message,"UTF-8"));
        } catch (Exception e) {}
        return commonError;
    }
    @RequestMapping("/test/exception")
    public String testException() {
        throw new LetvCommonException("000", "内部错误");
    }
}
