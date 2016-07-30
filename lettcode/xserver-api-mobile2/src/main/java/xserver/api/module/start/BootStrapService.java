package xserver.api.module.start;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import xserver.api.module.BaseService;
import xserver.api.module.start.dto.BootStrapDataDto;
import xserver.api.module.start.dto.BootStrapRequest;
import xserver.api.module.start.util.BootStrapUtil;
import xserver.api.module.start.util.DeviceLoggerUtil;
import xserver.api.response.Response;
import xserver.api.util.IpAddrUtil;
import xserver.common.cache.CbaseRmiClient;
import xserver.common.util.CalendarUtil;
import xserver.lib.tp.start.request.StartRequest;
import xserver.lib.tp.start.response.StartTpResponse;

@Component(value = "bootStrapService")
public class BootStrapService extends BaseService {
    private CbaseRmiClient cbaseRmiClient = CbaseRmiClient.getInstance();

    // private int series_app_id = 0;// 平台、型号和应用的关系表id

    /**
     * 激活验证，获取版本信息
     * @param request
     * @return
     */
    public Response<BootStrapDataDto> bootStrap(BootStrapRequest request) {
        long stime = System.currentTimeMillis();
        this.log.info("terminalAuth is start,requst:" + request);
        Response<BootStrapDataDto> res = new Response<BootStrapDataDto>();

        BootStrapDataDto data = getCacheData(request);
        if (data == null) {
            data = getStartData(request);
        }
        if (BootStrapConstant.CRACK_PHONE_IMEI.contains(request.getMac())) {
            data.setsKey(BootStrapUtil.getCrackKey(request.getTerminalBrand(), request.getTerminalSeries(),
                    request.getTerminalApplication(), request.getMac()));
        }
        data.setCityLevel(IpAddrUtil.getCityLevel(request.getIp()));
        // 记录设备信息
        DeviceLoggerUtil.getLogger().log(
                "[DeviceId=" + request.getMac() + ",Brand=" + request.getTerminalBrand() + ",App="
                        + request.getTerminalApplication() + ",Series=" + request.getTerminalSeries() + ",Version="
                        + request.getInstallVersion() + "," + data.toString() + "]");

        res.setData(data);
        this.log.info(new StringBuffer().append("ip:").append(request.getIp()).append(" citylevel:")
                .append(data.getCityLevel()).append(" imei:").append(request.getMac()).append(" version:")
                .append(request.getInstallVersion()).append(" series:").append(request.getTerminalSeries())
                .append("outter status:").append(data.getStatus()).append("  message:").append(data.getMessage())
                .toString());
        // this.log.info("terminalAuth is end,time:" +
        // (System.currentTimeMillis() - stime));
        return res;
    }

    private BootStrapDataDto getCacheData(BootStrapRequest request) {
        BootStrapDataDto data;
        try {
            data = tpCacheTemplate.get(
                    URLEncoder.encode(
                            BootStrapConstant.BOOTSTRAP_KEY + request.getTerminalBrand()
                                    + request.getTerminalApplication() + request.getTerminalSeries()
                                    + request.getInstallVersion(), "UTF-8"), BootStrapDataDto.class);
            return data;
        } catch (UnsupportedEncodingException e) {
            this.log.error(
                    "encode :" + BootStrapConstant.BOOTSTRAP_KEY + request.getTerminalBrand()
                            + request.getTerminalApplication() + request.getTerminalSeries()
                            + request.getInstallVersion() + "Exception", e);
            e.printStackTrace();
        }
        return null;
    }

    private BootStrapDataDto getStartData(BootStrapRequest request) {
        log.info("grade key =====" + BootStrapConstant.BOOTSTRAP_KEY + request.getTerminalBrand()
                + request.getTerminalApplication() + request.getTerminalSeries() + request.getInstallVersion());
        StartRequest tpReq = new StartRequest(request.getMac(), request.getTerminalSeries(),
                request.getTerminalBrand(), request.getpCode(), request.getInstallVersion(), request.getIp(),
                request.getLangcode(), request.getTerminalApplication());

        if (!StringUtils.isEmpty(request.getTerminalSeries()) && request.getTerminalSeries().contains("+")) {
            tpReq.setTerminalSeries(request.getTerminalSeries().replace("+", ""));
        }

        BootStrapDataDto data = new BootStrapDataDto();
        StartTpResponse rep = this.facadeTpDao.getStartTpDao().startInfo(tpReq);
        if (rep.getResultStatus().equals("1")) {

            data.setDescription(rep.getData().getDescription());
            data.setMessage(rep.getData().getMessage());
            if (!StringUtils.isEmpty(rep.getData().getPublishTime())) {
                String date = CalendarUtil.getDateString(
                        CalendarUtil.parseDate(rep.getData().getPublishTime(), CalendarUtil.SIMPLE_DATE_FORMAT),
                        "yyyy年MM月dd日");
                data.setPublishTime(date);
            }
            data.setStatus(rep.getData().getStatus());
            data.setVersionName(rep.getData().getVersionName());
            data.setVersionUrl(rep.getData().getVersionUrl());
        } else {
            this.log.error("启动信息异常:mac=" + request.getMac());
        }
        try {
            long updateSyncStart = System.currentTimeMillis();
            cbaseRmiClient.updateSync(
                    URLEncoder.encode(
                            BootStrapConstant.BOOTSTRAP_KEY + request.getTerminalBrand()
                                    + request.getTerminalApplication() + request.getTerminalSeries()
                                    + request.getInstallVersion(), "UTF-8"), data, 5 * 60);
            long updateSyncEnd = System.currentTimeMillis();
            log.info("updateSync waste:" + (updateSyncEnd - updateSyncStart));
        } catch (UnsupportedEncodingException e) {
            this.log.error(
                    "encode :" + BootStrapConstant.BOOTSTRAP_KEY + request.getTerminalBrand()
                            + request.getTerminalApplication() + request.getTerminalSeries()
                            + request.getInstallVersion() + "Exception", e);
            e.printStackTrace();
        }
        return data;
    }

    // public Response<BootStrapResultsDto> bootStrap(BootStrapRequest request)
    // {
    // try {
    // /************* 激活认证 **************/
    // data = this.doRegister(request, data);
    // /************ 获取升级信息 ************/
    // data = this.getVerionInfo(request, data);
    //
    // } catch (Exception e) {
    // this.log.error(e.getMessage(), e);
    // } finally {
    // this.log.info("ip:" + request.getIp());
    // this.log.info("outter status:" + data.getStatus() + "  message:" +
    // data.getMessage());
    // this.log.info("terminalAuth is end,time:" + (System.currentTimeMillis() -
    // stime));
    // }
    // res.setData(data);
    // this.log.info("ip:" + request.getIp());
    // this.log.info("outter status:" + data.getStatus() + "  message:" +
    // data.getMessage());
    // this.log.info("terminalAuth is end,time:" + (System.currentTimeMillis() -
    // stime));
    // return res;
    // }

    // private BootStrapResultsDto doRegister(BootStrapRequest request,
    // BootStrapResultsDto data) {
    // SeriesAppRelationMysqlTable seriesAppRelation =
    // this.facadeMysqlDao.getTerminalInfoDao().getSeriesAppInfo(
    // request.getTerminalBrand().trim(), "android",
    // request.getTerminalSeries(),
    // request.getTerminalApplication().trim());
    //
    // if (seriesAppRelation != null) {
    // this.series_app_id = seriesAppRelation.getSeries_app_id();
    // }
    //
    // String terminalUuid =
    // StringUtil.getMd5TerminalUuid(request.getTerminalBrand().trim(), request
    // .getTerminalSeries().trim(), "a");
    // TerminalMysqlTable terminal =
    // this.facadeMysqlDao.getTerminalInfoDao().getTerminalByUuidAndSeriesAppId(
    // terminalUuid, this.series_app_id);
    //
    // if (terminal == null) {
    // terminal = new TerminalMysqlTable();
    // terminal.setTerminalUUId(terminalUuid);
    // terminal.setSeriesId((seriesAppRelation != null &&
    // seriesAppRelation.getSeriesId() != null) ? seriesAppRelation
    // .getSeriesId().longValue() : 0);
    // terminal.setMac(request.getMac());
    // terminal.setIp(request.getIp());
    // terminal.setActivateTime(Calendar.getInstance().getTime());
    // terminal.setCreateTime(Calendar.getInstance().getTime());
    // terminal.setUpdateTime(Calendar.getInstance().getTime());
    // terminal.setStatus(0);
    // terminal.setSeries_app_id(this.series_app_id);
    // terminal.setVersionName(request.getInstallVersion());
    // terminal.setBrandName(request.getTerminalBrand());
    // terminal.setSeriesName(request.getTerminalSeries());
    // this.facadeMysqlDao.getTerminalInfoDao().insertTerminal(terminal);
    // this.log.info("===============first active." + request);
    // this.log.info("=====terminalUuid:" + terminalUuid + " 首次激活成功！=====");
    // } else {
    // this.facadeMysqlDao.getTerminalInfoDao().updateTerminalUpdateTimeById(terminal.getId(),
    // request.getTerminalBrand(), request.getTerminalSeries(),
    // request.getInstallVersion());
    // this.log.info("===============first update." + request);
    // this.log.info("=====terminalUuid:" + terminalUuid + " 激活验证成功！=====");
    // }
    //
    // // 播控、码流、config信息
    // data.setTerminalUuid(terminalUuid);
    // return data;
    // }

    /**
     * 用于组织版本信息
     * @param request
     * @param dataMap
     *            返回参数
     * @return
     */
    // private BootStrapResultsDto getVerionInfo(BootStrapRequest request,
    // BootStrapResultsDto data) {
    // List<SeriesAppVersionInfoMysqlTable> highVersions =
    // this.facadeMysqlDao.getVersionDao().getHighVersionList(
    // this.series_app_id, request.getInstallVersion());
    // if (highVersions != null && highVersions.size() > 0) {
    // // highVersions里面存储了当前版本之上的所有强制和推荐升级版本，推荐升级的type=2，强制升级的type=1
    // // 如果所有条目的type之和=highVersions.size()*2，那么说明，highVersions中都是推荐升级
    // // 如果所有条目的type之和<highVersions.size()*2，那么说明，highVersions中都是推荐升级
    // int sumType = 0;
    // for (SeriesAppVersionInfoMysqlTable s : highVersions) {
    // sumType += s.getType();
    // }
    // if (sumType < highVersions.size() * 2 ||
    // highVersions.get(0).getEnforce_up_id() > 0) {// 强升逻辑
    // data.setStatus(TerminalConstants.STATUS_FORCE);
    // data.setMessage(getMsg(TerminalConstants.TERMINAL_VERSION_HAS_NEW,
    // request.getLangcode()));
    // } else {// 推荐升级
    // data.setStatus(TerminalConstants.STATUS_RECOMMEND);
    // data.setMessage(getMsg(TerminalConstants.TERMINAL_VERSION_HAS_NEW,
    // request.getLangcode()));
    // }
    // data.setVersionUrl(highVersions.get(0).getDownload_url());
    // data.setVersionName(highVersions.get(0).getVersion_name());
    // data.setDescription(highVersions.get(0).getDescription());
    // data.setPublishTime(CalendarUtil.getDateString(highVersions.get(0).getCreate_time(),
    // CalendarUtil.SIMPLE_DATE_FORMAT));// 升级包发布时间
    // } else {
    // data.setStatus(TerminalConstants.STATUS_NORMAL);
    // data.setMessage(getMsg(TerminalConstants.TERMINAL_VERSION_UNNEED_UPGRADE,
    // request.getLangcode()));
    // }
    // return data;
    // }

    // private String getMsg(String code, String lang) {
    // String msg = "";
    // try {
    // msg = MessageUtils.getMessage(code, lang);
    // if (msg == null || "".equals(msg)) {
    //
    // msg = TerminalConstants.MSG.get(code);
    // }
    // } catch (Exception e) {
    // }
    // return msg;
    // }
    public static void main(String[] args) {
        Date s = CalendarUtil.parseDate("2015-03-29 19:36:19", CalendarUtil.SIMPLE_DATE_FORMAT);
        String beginDate = CalendarUtil.getDateString(s, "yyyy年MM月dd日");
        System.out.println(beginDate);
    }

    public String getSth() {
        return this.facadeTpDao.getCmsTpDao().getSth();
    }

    public String getAsyncSth() {
        return this.facadeTpDao.getCmsTpDao().getAsyncSth();
        // return this.facadeTpDao.getCmsTpDao().getSth2();
    }
}
