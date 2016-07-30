package xserver.api.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 样例Servlet监听器
 */
public class SampleServletListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(SampleServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug(this.getClass().getName() + " start.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
