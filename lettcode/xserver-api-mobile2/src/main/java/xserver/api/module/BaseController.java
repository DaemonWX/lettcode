package xserver.api.module;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller基类
 */
public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	protected FacadeService facadeService;
}
