package xserver.api.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 样例Spring拦截器
 */
public class SampleSpringInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(SampleSpringInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug(this.getClass().getName() + " preHandle.");

        return true;
    }
}
