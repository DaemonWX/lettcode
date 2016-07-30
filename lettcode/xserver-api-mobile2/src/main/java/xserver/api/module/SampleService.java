package xserver.api.module;

import org.springframework.stereotype.Component;

import xserver.lib.TpCacheUnit;
import xserver.lib.tp.SampleTpResponse;

/**
 * 样例Service
 */
@Component
public class SampleService extends BaseService {

    public String testTpDao(String url) {
        SampleTpResponse sampleTpResponse = this.facadeTpDao.getSampleTpDao().get(url);

        return sampleTpResponse.getData();
    }

    public TpCacheUnit testTpCache(String key) {
        return this.getTpCacheTemplate().get(key, TpCacheUnit.class);
    }

    // public String testMysqlDao(Integer minId, Integer maxId) {
    // List<SampleMysqlTable> list =
    // this.facadeLeadMysqlDao.getSampleMysqlDao().list(minId, maxId);
    //
    // StringBuilder sb = new StringBuilder();
    // for (SampleMysqlTable sampleMysqlTable : list) {
    // sb.append(sampleMysqlTable.getName());
    // sb.append(",");
    // }
    //
    // return sb.toString();
    // }
}
