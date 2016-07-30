package xserver.api.module.vip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xserver.api.constant.MutilLanguageConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseController;
import xserver.api.module.CommonParam;
import xserver.api.module.vip.dto.CheckPhoneDto;
import xserver.api.module.vip.dto.CheckoutCounterDto;
import xserver.api.module.vip.dto.DeviceBindDto;
import xserver.api.module.vip.dto.LiveTicketDto;
import xserver.api.module.vip.dto.LiveTicketPackageInfoDto;
import xserver.api.module.vip.dto.MovieTicketInfoDto;
import xserver.api.module.vip.dto.OrderInfoDto;
import xserver.api.module.vip.dto.PurchaseVipDto;
import xserver.api.module.vip.dto.ReceiveDeviceBindDto;
import xserver.api.module.vip.dto.VipInfoDto;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.tp.vip.request.CheckPhoneRequest;
import xserver.lib.tp.vip.request.CheckUserOrderRequest;
import xserver.lib.tp.vip.request.ConsumptionRecordRequest;
import xserver.lib.tp.vip.request.GetDeviceBindRequest;
import xserver.lib.tp.vip.request.GetMovieTicketListRequest;
import xserver.lib.tp.vip.request.GetPresentDeviceBindRequest;
import xserver.lib.tp.vip.request.GetVipAccountInfoRequest;
import xserver.lib.tp.vip.request.LiveTicketCommonRequest;
import xserver.lib.tp.vip.request.PurchaseVipCommonRequest;
import xserver.lib.tp.vip.request.ReceiveDeviceBindRequest;
import xserver.lib.tp.vip.request.ReceivePresentDeviceBindRequest;
import xserver.lib.tp.vip.request.UserLiveCommonRequest;
import xserver.lib.util.RequestUtil;

/**
 * 会员消费、支付、账户控制器
 */
@Controller
public class VipController extends BaseController {
    /**
     * 根据token获取账户信息
     * @param request
     * @param token
     *            用户中心token 值
     * @return
     */
    // TODO
    // BOSS需要提供新接口，其接口：boss.yuanxian.vip.serivceinfo.get=http://yuanxian.letv.com/letv/tvServlet.ldo?type=cellVip
    @RequestMapping(value = "/vip/getAccount")
    public Response<VipInfoDto> getVipAccount(HttpServletRequest request,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "deviceKey", required = false) String deviceKey,
            @ModelAttribute CommonParam commonParam) {

        GetVipAccountInfoRequest getVipAccountInfoRequest = new GetVipAccountInfoRequest(username,
                commonParam.getUid(), commonParam.getDevId(), deviceKey);

        Response<VipInfoDto> response = this.facadeService.getVipService().getVipAccount(getVipAccountInfoRequest,
                MutilLanguageConstants.getLocale(commonParam));

        return response;
    }

    /**
     * 获取用户消费记录
     * @param status
     *            订单状态，0是全部，1待付款，2交易成功，3交易过期
     * @param day
     *            查询day天时间内的充值记录，0是全部，
     * @param page
     *            当前页
     * @param pageSize
     *            每页记录数
     * @param orderTypes
     *            要查询的订单类型列表，格式为多个orderType适用英文逗号拼接，为空则表示查询全部
     * @param commonParam
     *            通用参数，这里主要取uid
     * @return
     */
    @RequestMapping("/vip/consume/getConsumptionRecord")
    public PageResponse<OrderInfoDto> getConsumptionRecord(
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
            @RequestParam(value = "day", required = false, defaultValue = "365") Integer day,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "orderTypes", required = false) String orderTypes,
            @ModelAttribute CommonParam commonParam) {

        ConsumptionRecordRequest req = new ConsumptionRecordRequest(commonParam.getUid(), status, day, page, pageSize,
                orderTypes);

        return this.facadeService.getVipService().getConsumptionRecord(req,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 获取用户观影券列表
     * 注意boss逻辑为，当前账户会员过期后，将不再返回任何观影券信息
     * @param page
     *            当前页
     * @param pageSize
     *            每页记录数
     * @param commonParam
     *            通用参数，这里主要取uid
     * @return
     */
    @RequestMapping("/vip/consume/movieTicket/getList")
    public PageResponse<MovieTicketInfoDto> getMovieTicketList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @ModelAttribute CommonParam commonParam) {

        GetMovieTicketListRequest getMovieTicketListRequest = new GetMovieTicketListRequest(commonParam.getUid(), page,
                pageSize);

        return this.facadeService.getVipService().getMovieTicketList(getMovieTicketListRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 手机号验证接口,反馈手机号是否支持话费支付及套餐包价格信息
     * @param phone
     *            手机号
     * @param request
     * @return
     */
    @RequestMapping("/vip/pay/checkPhone")
    public Response<CheckPhoneDto> checkPhone(@RequestParam("phone") String phone,
            @ModelAttribute CommonParam commonParam) {

        CheckPhoneRequest checkPhoneRequest = new CheckPhoneRequest(phone);
        return this.facadeService.getVipService().checkPhone(checkPhoneRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 获取收银台相关信息，目前包含套餐包（套餐基本信息，套餐活动，支付渠道活动），会员权益文案，收银台焦点图
     * @param vipType
     *            套餐适用的会员类型，参见VipTpConstant#SVIP_*，当前可取1--普通会员，用于手机、PC端；9
     *            --高级会员，用于TV
     * @param commonParam
     *            通用参数，这里主要用于获取uid和语言信息
     * @return
     */
    @RequestMapping("/vip/pay/getCheckoutCounter")
    public Response<CheckoutCounterDto> getCheckoutCounter(@RequestParam("vipType") Integer vipType,
            @ModelAttribute CommonParam commonParam) {

        return this.facadeService.getVipService().getCheckoutCounter(commonParam.getUid(), vipType,
                MutilLanguageConstants.getLocale(commonParam), commonParam.getAppVersion());
    }

    /**
     * 查询设备机卡绑定信息（包含自带机卡时长和赠送机卡时长，是否可领，可领时长，或已领及领取时间等）
     * @param type
     *            0--查询全部（自带机卡和赠送机卡），1--只查询自带机卡，2--只查询赠送机卡
     * @param deviceKey
     *            机卡设备序列号
     * @param commonParam
     *            查询赠送时长（type=2或0）时必传uid
     * @return
     */
    @RequestMapping("/vip/devicebind/get")
    public Response<DeviceBindDto> getDeviceBind(@RequestParam(value = "type") String type,
            @RequestParam("deviceKey") String deviceKey, @ModelAttribute CommonParam commonParam) {

        GetDeviceBindRequest getDeviceBindRequest = null;
        GetPresentDeviceBindRequest getPresentDeviceBindRequest = null;

        if (VipConstants.DEVICE_BIND_QUERY_TYPE_0.equals(type) || VipConstants.DEVICE_BIND_QUERY_TYPE_1.equals(type)) {
            getDeviceBindRequest = new GetDeviceBindRequest(deviceKey, commonParam.getDevId(),
                    VipTpConstant.DEVICE_BIND_DEVICE_TYPE_MOBILE);
        }

        if (VipConstants.DEVICE_BIND_QUERY_TYPE_0.equals(type) || VipConstants.DEVICE_BIND_QUERY_TYPE_2.equals(type)) {
            getPresentDeviceBindRequest = new GetPresentDeviceBindRequest(commonParam.getUid(),
                    VipTpConstant.PRESENT_DEBICE_BIND_TYPE_MOBILE, deviceKey, commonParam.getDevId());
        }

        return this.facadeService.getVipService().getDeviceBind(getDeviceBindRequest, getPresentDeviceBindRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 领取（自带或赠送的）机卡绑定
     * @param type
     *            1--领取自带机卡绑定。2--领取赠送机卡绑定
     * @param deviceKey
     *            设备暗码
     * @param presentId
     *            领取赠送机卡绑定的id。领取赠送时长（type=2）时必传
     * @param commonParam
     * @return
     */
    @RequestMapping("/vip/devicebind/receive")
    public Response<ReceiveDeviceBindDto> receiveDeviceBind(@RequestParam(value = "type") Integer type,
            @RequestParam("deviceKey") String deviceKey,
            @RequestParam(value = "presentId", required = false) String presentId,
            @ModelAttribute CommonParam commonParam) {

        ReceiveDeviceBindRequest receiveDeviceBindRequest = null;
        ReceivePresentDeviceBindRequest receivePresentDeviceBindRequest = null;
        if (type != null) {
            if (VipConstants.DEVICE_BIND_RECEIVE_TYPE_1 == type) {
                receiveDeviceBindRequest = new ReceiveDeviceBindRequest(commonParam.getUid(), deviceKey,
                        commonParam.getDevId(), commonParam.getTerminalSeries(),
                        VipTpConstant.DEVICE_BIND_DEVICE_TYPE_MOBILE);
            } else if (VipConstants.DEVICE_BIND_RECEIVE_TYPE_2 == type) {
                receivePresentDeviceBindRequest = new ReceivePresentDeviceBindRequest(commonParam.getUid(),
                        VipTpConstant.PRESENT_DEBICE_BIND_TYPE_MOBILE, deviceKey, commonParam.getDevId(), presentId);
            }
        }

        return this.facadeService.getVipService().receiveDeviceBind(receiveDeviceBindRequest,
                receivePresentDeviceBindRequest, MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 下单
     * @param corderid
     * @param purchaseType
     *            消费类型，1--影片单点，2--套餐，3--直播单点，4--超级Live中的直播
     * @param productid
     * @param paymentChannel
     * @param price
     * @param vipType
     * @param activityIds
     * @param phone
     * @param commonParam
     * @param request
     * @return
     */
    @RequestMapping("/vip/pay/purchaseVip")
    public Response<PurchaseVipDto> purchaseVip(
            @RequestParam(value = "corderid", required = false, defaultValue = "0") String corderid,
            @RequestParam(value = "purchaseType") Integer purchaseType,
            @RequestParam(value = "productid") String productid,
            @RequestParam(value = "paymentChannel") Integer paymentChannel,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "vipType", required = false) Integer vipType,
            @RequestParam(value = "activityIds", required = false) String activityIds,
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            @RequestParam(value = "phone", required = false) String phone, @ModelAttribute CommonParam commonParam,
            HttpServletRequest request) {

        Integer av = VipTpConstant.BROADCAST_APK_VERSION_LETV; // 默认letv
        if (vipType == null) {
            vipType = VipTpConstant.SVIP_1;
        }

        PurchaseVipCommonRequest purchaseVipCommonRequest = new PurchaseVipCommonRequest(commonParam.getUid(),
                corderid, purchaseType, productid, paymentChannel, price, vipType, activityIds, av,
                commonParam.getDevId(), RequestUtil.getClientIp(request));
        purchaseVipCommonRequest.setPhone(phone);

        return this.facadeService.getVipService().purchaseVip(purchaseVipCommonRequest,
                MutilLanguageConstants.getLocale(commonParam), returnUrl);
    }

    /**
     * 查询订单信息
     * @param orderId
     *            订单号
     * @param commonParam
     *            传uid
     * @return
     */
    @RequestMapping("/vip/consume/checkOrder")
    public Response<OrderInfoDto> checkOrderStatus(@RequestParam("orderId") String orderId,
            @ModelAttribute CommonParam commonParam) {

        CheckUserOrderRequest checkUserOrderRequest = new CheckUserOrderRequest(commonParam.getUid(), orderId);

        return this.facadeService.getVipService().checkOrder(checkUserOrderRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 使用观影券
     * @param movieName
     * @param movieId
     * @param commonParam
     * @return
     */
    @RequestMapping("/vip/consume/movieTicket/active")
    public Response<ValueDto<Boolean>> activeMovieTicket(HttpServletRequest request,
            @RequestParam(value = "movieName") String movieName, @RequestParam(value = "movieId") String movieId,
            @ModelAttribute CommonParam commonParam) {
        String routerInfo = RequestUtil.getRouterInfo(request);
        log.info(routerInfo);

        return this.facadeService.getVipService().activeMovieTicket(movieId, movieName, commonParam.getUid(),
                MutilLanguageConstants.getLocale(commonParam), routerInfo);
    }

    /**
     * 查询用户的直播券信息
     * @param uid
     * @param screenings
     * @param commonParam
     * @return
     */
    @RequestMapping("/vip/live/ticket/check")
    public PageResponse<LiveTicketDto> checkUserLiveTicket(@RequestParam(value = "screenings") String screenings,
            @ModelAttribute CommonParam commonParam) {

        // 用户直播券公共请求参数构造函数依次为(userid，场次id，sign)，sign传对应的请求URL
        UserLiveCommonRequest userLiveCommonRequest = new UserLiveCommonRequest(commonParam.getUid(), screenings,
                VipTpConstant.CHECK_USER_LIVE_TICKET_URL);

        return this.facadeService.getVipService().checkUserLiveTicket(userLiveCommonRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 激活用户的直播券
     * @param uid
     * @param screenings
     * @param commonParam
     * @return
     */
    @RequestMapping("/vip/live/ticket/active")
    public Response<ValueDto<Boolean>> activeLiveTicket(@RequestParam(value = "screenings") String screenings,
            @ModelAttribute CommonParam commonParam) {

        // 用户直播券公共请求参数构造函数依次为(userid，场次id，sign)，sign传对应的请求URL
        UserLiveCommonRequest userLiveCommonRequest = new UserLiveCommonRequest(commonParam.getUid(), screenings,
                VipTpConstant.ACTIVE_USER_LIVE_TICKET_URL);

        return this.facadeService.getVipService().activeLiveTicket(userLiveCommonRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 查询用户直播券的详细信息
     * @param screenings
     * @param commonParam
     * @return
     */
    @RequestMapping("/vip/live/ticket/getInfo")
    public Response<LiveTicketPackageInfoDto> getLiveTicketInfo(@RequestParam(value = "screenings") String screenings,
            @ModelAttribute CommonParam commonParam) {

        LiveTicketCommonRequest liveTicketCommonRequest = new LiveTicketCommonRequest(screenings);

        return this.facadeService.getVipService().getLiveTicketInfo(liveTicketCommonRequest,
                MutilLanguageConstants.getLocale(commonParam));
    }

    /**
     * 维护用户续约 暂停开通 or 恢复开通 2种状态
     * 
     * @param userid 
     *            用户ID
     * @param act
     *            1:开通, 0:暂停
     * @return
     */
    @RequestMapping("/vip/autopay/switch")
    public Response<ValueDto<Boolean>> autoPaySwitch(@RequestParam(value = "act") Integer act, 
            @ModelAttribute CommonParam commonParam) {

        return this.facadeService.getVipService().autoPaySwitch(commonParam.getUid(), act,
                MutilLanguageConstants.getLocale(commonParam));
    }

}
