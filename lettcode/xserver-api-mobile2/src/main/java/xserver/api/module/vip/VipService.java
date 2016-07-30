package xserver.api.module.vip;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import xserver.api.constant.DataConstants;
import xserver.api.constant.ErrorCodeConstants;
import xserver.api.dto.ValueDto;
import xserver.api.module.BaseService;
import xserver.api.module.vip.builder.ConsumptionRecordBuilder;
import xserver.api.module.vip.builder.PaymentActivityBuilder;
import xserver.api.module.vip.builder.VipPackageBuilder;
import xserver.api.module.vip.dto.CheckPhoneDto;
import xserver.api.module.vip.dto.CheckoutCounterDto;
import xserver.api.module.vip.dto.CheckoutFocusDto;
import xserver.api.module.vip.dto.DeviceBindDto;
import xserver.api.module.vip.dto.DeviceBindTextDto;
import xserver.api.module.vip.dto.LiveTicketDto;
import xserver.api.module.vip.dto.LiveTicketPackageInfoDto;
import xserver.api.module.vip.dto.LiveTicketPackageInfoDto.LiveTicketInfoDto;
import xserver.api.module.vip.dto.MovieTicketInfoDto;
import xserver.api.module.vip.dto.OrderInfoDto;
import xserver.api.module.vip.dto.PaymentActivityDto;
import xserver.api.module.vip.dto.PresentDeviceBindDto;
import xserver.api.module.vip.dto.PurchaseVipDto;
import xserver.api.module.vip.dto.ReceiveDeviceBindDto;
import xserver.api.module.vip.dto.VipBlockContentDto;
import xserver.api.module.vip.dto.VipInfoDto;
import xserver.api.module.vip.dto.VipPackageDto;
import xserver.api.module.vip.dto.VipPrivilegeDto;
import xserver.api.module.vip.util.VipUtil;
import xserver.api.response.PageResponse;
import xserver.api.response.Response;
import xserver.lib.tp.cms.response.CmsBlockContent;
import xserver.lib.tp.cms.response.CmsBlockTpResponse;
import xserver.lib.tp.vip.VipMsgCodeConstant;
import xserver.lib.tp.vip.VipTpConstant;
import xserver.lib.tp.vip.VipTpConstantUtils;
import xserver.lib.tp.vip.request.ActiveMovieTicketRequest;
import xserver.lib.tp.vip.request.AutoPayCheckInfoResquest;
import xserver.lib.tp.vip.request.AutoPayOnekeyRequest;
import xserver.lib.tp.vip.request.AutoPayStatusResquest;
import xserver.lib.tp.vip.request.AutoPaySwitchResquest;
import xserver.lib.tp.vip.request.CheckPhoneRequest;
import xserver.lib.tp.vip.request.CheckUserOrderRequest;
import xserver.lib.tp.vip.request.ConsumptionRecordRequest;
import xserver.lib.tp.vip.request.GetDeviceBindRequest;
import xserver.lib.tp.vip.request.GetMovieTicketListRequest;
import xserver.lib.tp.vip.request.GetPaymentActivityListRequest;
import xserver.lib.tp.vip.request.GetPresentDeviceBindRequest;
import xserver.lib.tp.vip.request.GetVipAccountInfoRequest;
import xserver.lib.tp.vip.request.GetVipPackageListRequest;
import xserver.lib.tp.vip.request.LiveTicketCommonRequest;
import xserver.lib.tp.vip.request.PurchaseVipCommonRequest;
import xserver.lib.tp.vip.request.ReceiveDeviceBindRequest;
import xserver.lib.tp.vip.request.ReceivePresentDeviceBindRequest;
import xserver.lib.tp.vip.request.RouteActiveMovieTicketRequest;
import xserver.lib.tp.vip.request.UserLiveCommonRequest;
import xserver.lib.tp.vip.response.ActiveMovieTicketTpResponse;
import xserver.lib.tp.vip.response.ActiveUserLiveTicketTpResponse;
import xserver.lib.tp.vip.response.AliAppPayTpResponse;
import xserver.lib.tp.vip.response.AutoPayCheckInfoResponse;
import xserver.lib.tp.vip.response.AutoPayOnekeyResponse;
import xserver.lib.tp.vip.response.AutoPayStatusResponse;
import xserver.lib.tp.vip.response.AutoPaySwitchResponse;
import xserver.lib.tp.vip.response.CheckPhoneTpResponse;
import xserver.lib.tp.vip.response.CheckUserLiveTicketTpResponse;
import xserver.lib.tp.vip.response.CheckUserLiveTicketTpResponse.UserLiveTicket;
import xserver.lib.tp.vip.response.CheckUserOrderTpResponse;
import xserver.lib.tp.vip.response.ConsumptionRecordTpResponse;
import xserver.lib.tp.vip.response.GetDeviceBindTpResponse;
import xserver.lib.tp.vip.response.GetDeviceBindTpResponse.DeviceBindInfoTpResponse;
import xserver.lib.tp.vip.response.GetLiveTicketPackagesResponse;
import xserver.lib.tp.vip.response.GetLiveTicketPackagesResponse.LiveTicketInfoResponse;
import xserver.lib.tp.vip.response.GetMovieTicketListTpResponse;
import xserver.lib.tp.vip.response.GetMovieTicketListTpResponse.MovieTicketInfoTpResponse;
import xserver.lib.tp.vip.response.GetPresentDeviceBindTpResponse;
import xserver.lib.tp.vip.response.GetPresentDeviceBindTpResponse.PresentDeviceBindTpResponse;
import xserver.lib.tp.vip.response.GetVipAccountInfoTpResponse;
import xserver.lib.tp.vip.response.GetVipAccountInfoTpResponse.VipAccountInfoTpResponse;
import xserver.lib.tp.vip.response.LetvPointPayTpResponse;
import xserver.lib.tp.vip.response.PaymentActivityTpResponse;
import xserver.lib.tp.vip.response.PaymentActivityTpResponse.PaymentActivityInfo;
import xserver.lib.tp.vip.response.PhonePayTpResponse;
import xserver.lib.tp.vip.response.ReceiveDeviceBindTpResponse;
import xserver.lib.tp.vip.response.ReceivePresentDeviceBindTpResponse;
import xserver.lib.tp.vip.response.RouteInfo;
import xserver.lib.tp.vip.response.UserOrderInfoTpResponse;
import xserver.lib.tp.vip.response.VipPackageListTpResponse;
import xserver.lib.tp.vip.response.VipPackageListTpResponse.VipPackageInfo;
import xserver.lib.tp.vip.response.WeixinAppPayTpResponse;
import xserver.lib.tpcache.CacheConstants;
import xserver.lib.util.ApplicationUtils;
import xserver.lib.util.CalendarUtil;
import xserver.lib.util.MessageUtils;

@Service
public class VipService extends BaseService {

    public VipInfoDto getVipAccountInfo(GetVipAccountInfoRequest getVipAccountInfoRequest, Locale locale) {
        VipInfoDto data = null;
        Response<VipInfoDto> response = this.getVipAccount(getVipAccountInfoRequest, locale);
        if (response != null && response.getStatus() != null
                && ErrorCodeConstants.RESPONSE_SUC_CODE == response.getStatus()) {
            data = response.getData();
        }

        return data;
    }

    /**
     * 按token或用户id获取账户信息
     * 按规定获取数据要求按token
     * 目前只为兼容boss接口
     * @param token
     * @param userId
     * @return
     */
    public Response<VipInfoDto> getVipAccount(GetVipAccountInfoRequest getVipAccountInfoRequest, Locale locale) {
        Response<VipInfoDto> response = new Response<VipInfoDto>();
        String logPrefix = "getVipAccount_" + getVipAccountInfoRequest.getUsername() + "_"
                + getVipAccountInfoRequest.getUserid() + "_" + getVipAccountInfoRequest.getMac() + "_"
                + getVipAccountInfoRequest.getDeviceKey();
        String errorCode = null;

        int validCode = getVipAccountInfoRequest.assertValid();
        // 参数校验
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.USER_ACCOUNT, validCode), locale);
        } else {

            GetVipAccountInfoTpResponse tpResponse = this.facadeTpDao.getVipTpDao().getVipAccount(
                    getVipAccountInfoRequest);
            if (tpResponse == null || !tpResponse.isSucceed()) {
                errorCode = ErrorCodeConstants.PAY_GET_VIP_ACCOUNT_INFO_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get vip account info failure.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                List<VipAccountInfoTpResponse> vipAccountResponseList = tpResponse.getData();
                VipInfoDto data = null;
                for (VipAccountInfoTpResponse vipAccountInfoTpResponse : vipAccountResponseList) {
                    if (vipAccountInfoTpResponse != null) {
                        data = new VipInfoDto();
                        data.setCancelTime(CalendarUtil.getDateStringFromLong(vipAccountInfoTpResponse.getCanceltime(),
                                CalendarUtil.SHORT_DATE_FORMAT));
                        data.setSeniorCancelTime(CalendarUtil.getDateStringFromLong(
                                vipAccountInfoTpResponse.getSeniorendtime(), CalendarUtil.SHORT_DATE_FORMAT));
                        data.setVipType(vipAccountInfoTpResponse.getVipType());
                        // 对会员类型设置缓存
                        this.tpCacheTemplate.set(
                                CacheConstants.VIP_USER_VIP_TYPE + getVipAccountInfoRequest.getUserid(),
                                vipAccountInfoTpResponse.getVipType(), CalendarUtil.SECONDS_OF_PER_DAY);

                        data.setVipTypeName(getVipTypeName(vipAccountInfoTpResponse.getVipType(), locale));
                        break;
                    }
                }

                // 连续包月套餐的开通信息
                this.addVipAutoStatus(getVipAccountInfoRequest.getUserid(), VipTpConstant.SVIP_1, data);
                this.addVipAutoStatus(getVipAccountInfoRequest.getUserid(), VipTpConstant.SVIP_9, data);

                response.setData(data);
            }
        }

        return response;
    }

    /**
     * 获取会员类型文案
     * @return
     */
    private String getVipTypeName(Integer vipType, Locale locale) {
        String code = null;
        if (vipType == null || 0 == vipType) {
            code = "VIP_TYPE_NAME_NO_VIP";
        } else if (1 == vipType) {
            code = "VIP_TYPE_NAME_MOBILE_VIP";
        } else if (2 == vipType) {
            code = "VIP_TYPE_NAME_ALL_VIP";
        } else {
            code = "VIP_TYPE_NAME_NO_VIP";
        }
        return MessageUtils.getMessage(code, locale);
    }

    /**
     * 获取消费记录
     * @param consumptionRecordRequest
     * @param locale
     * @return
     */
    public PageResponse<OrderInfoDto> getConsumptionRecord(ConsumptionRecordRequest consumptionRecordRequest,
            Locale locale) {
        PageResponse<OrderInfoDto> response = new PageResponse<OrderInfoDto>();
        String logPrefix = "getConsumptionRecord_" + consumptionRecordRequest.getUserid();
        String errorCode = null;

        int validCode = consumptionRecordRequest.assertValid();
        // 参数校验
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            String errorMsgCode = errorCode + VipUtil.parseErrorMsgCode(VipConstants.CONSUMPTION_RECORD, validCode);
            this.setErrorResponse(response, errorCode, errorMsgCode, locale);
        } else {
            // 调用第三方接口
            ConsumptionRecordTpResponse tpResult = this.facadeTpDao.getVipTpDao().getConsumptionRecord(
                    consumptionRecordRequest);
            if (tpResult == null || tpResult.getTotal() == null) {
                errorCode = ErrorCodeConstants.PAY_GET_CONSUMPTION_RECORD_FAILURE;
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: get consume record failure.");
                this.setErrorResponse(response, errorCode, errorCode + DataConstants.COMMON_HYPHEN_UNDERLINE
                        + consumptionRecordRequest.getStatus(), locale);
            } else {
                List<UserOrderInfoTpResponse> recList = tpResult.getData();
                List<OrderInfoDto> dtoList = new ArrayList<OrderInfoDto>();

                if (!CollectionUtils.isEmpty(recList)) {
                    OrderInfoDto dto = null;
                    boolean typeIsNull = true;// 订单类型是否为空标志位
                    List<String> orderTypeList = consumptionRecordRequest.getOrderTypeList();
                    if (orderTypeList != null && orderTypeList.size() > 0) {
                        typeIsNull = false;
                    }
                    for (UserOrderInfoTpResponse consumptionRecord : recList) {
                        if (typeIsNull || orderTypeList.contains(consumptionRecord.getOrderType())) {
                            dto = ConsumptionRecordBuilder.build(consumptionRecord, locale);

                            if (dto != null) {
                                dtoList.add(dto);
                            }
                        }
                    }
                }

                response.setTotalCount(dtoList.size());
                response.setData(dtoList);
            }
        }

        return response;
    }

    private void addVipAutoStatus(String userid, Integer vipType, VipInfoDto data) {
        /**
         * 连续包月用户状态 获取response对应状态：
         *     移动会员： 0：已开通连续包月 1： 已暂停连续包月 2：未开通连续包月，有签约 3：未开通连续包月，没有签约
         */
        AutoPayStatusResquest userCheckRequest = new AutoPayStatusResquest(userid,vipType);
        AutoPayStatusResponse userCheckReponse = this.facadeTpDao.getVipTpDao()
                .getAutoPayStatus(userCheckRequest, AutoPayStatusResponse.class);

        /**
         * 连续包月提示信息显示 多种不同的提示信息
         */
        if (userCheckReponse == null) {
            this.log.error("get user status failed.");
        } else {
            if(vipType == VipTpConstant.SVIP_1){
                this.setStatus(userCheckReponse, VipTpConstant.SVIP_1, data);
            }else{
                this.setStatus(userCheckReponse, VipTpConstant.SVIP_9, data);
            }
        }
    }

    //userCheckReponse 状态信息 0未开通 1已开通 2以暂停
    private void setStatus(AutoPayStatusResponse userCheckReponse, int svip, VipInfoDto data) {
        if (userCheckReponse != null && userCheckReponse.getAutopay()!= null) {
            switch (userCheckReponse.getAutopay()) {
            case 0: // 未开通 // 没有签约
                    if(svip == VipTpConstant.SVIP_1) {
                        data.setMobileVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
                    }else{
                        data.setScreenVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
                    }
                    break;
            case 1: // 已开通 
		            if(svip == VipTpConstant.SVIP_1) {
		            	if(userCheckReponse.getSvip() == VipTpConstant.SVIP_1){
			                data.setMobileVipStatus(VipConstants.AUTO_PAY_USER_OPEN);
		            	}else {
			                data.setMobileVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
		            	}
		            }else{
		            	if(userCheckReponse.getSvip() == VipTpConstant.SVIP_9){
		            		data.setScreenVipStatus(VipConstants.AUTO_PAY_USER_OPEN);
		            	}else {
		            		data.setScreenVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
		            	}
		                
		            }
		            break;
            case 2: // 已暂停
            	    if(svip == VipTpConstant.SVIP_1) {
            	    	if(userCheckReponse.getSvip() == VipTpConstant.SVIP_1){
            	    		data.setMobileVipStatus(VipConstants.AUTO_PAY_USER_PAUSE);
            	    	}else{
            	    		data.setMobileVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
            	    	}
                    }else{
                    	if(userCheckReponse.getSvip() == VipTpConstant.SVIP_9){
                    		data.setScreenVipStatus(VipConstants.AUTO_PAY_USER_PAUSE);
                    	}else{
                    		data.setScreenVipStatus(VipConstants.AUTO_PAY_USER_UNOPEN_UNSINGED);
                    	}
                    }
                    break;
            default:
                    break;
            }
        } else {
            this.log.error("get auto pay status failed.");
        }
    }


    /**
     * 获取用户观影券列表
     * @param getMovieTicketListRequest
     * @param locale
     * @return
     */
    public PageResponse<MovieTicketInfoDto> getMovieTicketList(GetMovieTicketListRequest getMovieTicketListRequest,
            Locale locale) {
        PageResponse<MovieTicketInfoDto> response = new PageResponse<MovieTicketInfoDto>();
        String logPrefix = "getrMovieTicketList_" + getMovieTicketListRequest.getUserid();
        String errorCode = null;

        int validCode = getMovieTicketListRequest.assertValid();
        // 参数校验
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.MOVIE_TICKET, validCode), locale);
        } else {
            GetMovieTicketListTpResponse tpResponse = this.facadeTpDao.getVipTpDao().getMovieTicketList(
                    getMovieTicketListRequest);
            if (tpResponse == null || !tpResponse.isSucceed()) {
                // 失败
                errorCode = ErrorCodeConstants.PAY_GET_MOVIE_TICKET_LIST_FAILURE;
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: get movie ticket list failure.");
                // DataConstants.COMMON_HYPHEN_UNDERLINE
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                List<MovieTicketInfoDto> data = new LinkedList<MovieTicketInfoDto>();

                List<MovieTicketInfoTpResponse> ticketShows = tpResponse.getValues().getTicketShows();
                for (MovieTicketInfoTpResponse ticketShow : ticketShows) {
                    if (ticketShow != null) {
                        data.add(new MovieTicketInfoDto(ticketShow));
                    }
                }

                response.setTotalCount(data.size());
                response.setData(data);
            }
        }

        return response;
    }

    /**
     * 校验手机号时候满足手机支付条件
     * @param checkPhoneRequest
     * @param locale
     * @return
     */
    public Response<CheckPhoneDto> checkPhone(CheckPhoneRequest checkPhoneRequest, Locale locale) {
        Response<CheckPhoneDto> response = new Response<CheckPhoneDto>();
        String logPrefix = "checkPhone_" + checkPhoneRequest.getPhone();
        String errorCode = null;

        int validCode = checkPhoneRequest.assertValid();// 参数校验返回码
        // 参数校验
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            // 手机号是否为空，错误消息编码由“请求参数不合法编码”和“手机号为空编码”拼接而成
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.CHECK_PHONE, validCode), locale);
        } else {
            CheckPhoneTpResponse tpResponse = this.facadeTpDao.getVipTpDao().checkPhone(checkPhoneRequest);
            if (tpResponse == null || !"0".equals(tpResponse.getCode())) {
                errorCode = ErrorCodeConstants.PAY_CHECK_PHONE_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: phone check failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                CheckPhoneDto data = new CheckPhoneDto();
                data.setPaymentChannel(String.valueOf(tpResponse.getPayType()));
                data.setCommonVipPrice(tpResponse.getPrice());
                data.setSeniorVipPrice(tpResponse.getGjprice());

                response.setStatus(ErrorCodeConstants.RESPONSE_SUC_CODE);
                response.setData(data);
            }
        }

        return response;
    }

    /**
     * 获取收银台相关信息，目前包含套餐包（套餐基本信息，套餐活动，支付渠道活动），会员权益文案，收银台焦点图
     * @param locale
     * @param appVersion 版本信息
     * @return
     */
    public Response<CheckoutCounterDto> getCheckoutCounter(String userid, Integer vipType, Locale locale, String appVersion) {
        Response<CheckoutCounterDto> response = new Response<CheckoutCounterDto>();
        String logPrefix = "getCheckoutCounter_" + vipType + "_" + userid;
        String errorCode = null;

        // 0.参数校验
        if (!VipTpConstantUtils.isVipTypeDefined(vipType)) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        } else {
            // 1.获取套餐信息
            GetVipPackageListRequest getVipPackageListRequest = new GetVipPackageListRequest(vipType);
            VipPackageListTpResponse vipPackageListResponse = this.facadeTpDao.getVipTpDao().getVipPackage(
                    getVipPackageListRequest);

            // 剩余操作基于套餐信息进行，如果获取套餐失败，则操作结束；否则，后续操作失败，静默（日志）处理
            if (vipPackageListResponse == null || !vipPackageListResponse.isSucceed()) {
                errorCode = ErrorCodeConstants.PAY_GET_VIP_PACKAGE_LIST_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get vip package failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                // 整理套餐包信息
                List<VipPackageDto> vipPackageList = this
                        .parseVipPackageFromTp(vipPackageListResponse, vipType, locale, userid, appVersion);

                if (CollectionUtils.isEmpty(vipPackageList)) {
                    // 将筛选之后得到VipPackageDto空列表也作为一种业务异常
                    errorCode = ErrorCodeConstants.PAY_GET_VIP_PACKAGE_LIST_EMPTY;
                    this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get vip package empty.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                } else {
                    CheckoutCounterDto data = new CheckoutCounterDto();

                    // 在设置之前可以进行附加的排序操作；目前在boss可以有一次排序
                    data.setVipPackages(vipPackageList);

                    // 获取付费活动信息，支持未登录状态下获取活动信息
                    GetPaymentActivityListRequest getPaymentActivityListRequest = new GetPaymentActivityListRequest(
                            userid, vipType, VipTpConstant.PRICE_ZHIFU_TERMINAL_MOBILE);
                    PaymentActivityTpResponse paymentActivityTpResponse = this.facadeTpDao.getVipTpDao()
                            .getPaymentActivity(getPaymentActivityListRequest);
                    if (paymentActivityTpResponse == null || !paymentActivityTpResponse.isSucceed()) {
                        this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get payment activity failed.");
                    } else {
                        Map<String, PaymentActivityDto> paymentActivityMap = this.parsePaymentActivityFromTp(
                                paymentActivityTpResponse, vipType, locale);

                        // 组装付费活动数据
                        this.fillVipPackageWithPaymentActivityes(vipPackageList, paymentActivityMap);
                        data.setActivities(paymentActivityMap);
                    }

                    // 获取收银台焦点图信息
                    CheckoutFocusDto focusInfo = null;
                    VipPrivilegeDto vipPrivilege = null;
                    // TODO 后期需要修改成从配置文件中读取
                    String focusInfoBlockId = VipTpConstant.SVIP_1 == vipType ? VipTpConstant.VIP_CHECKOUT_FOCUS_INFO_BLOCK_ID_1
                            : VipTpConstant.VIP_CHECKOUT_FOCUS_INFO_BLOCK_ID_9;

                    // String focusInfoBlockId =
                    // MessageUtils.getMessage(VipConstants.VIP_CHECKOUT_FOCUS_INFO_BLOCK_ID_KEY
                    // + vipType, locale);
                    CmsBlockTpResponse focusInfoCmsBlockTpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(
                            focusInfoBlockId);
                    if (focusInfoCmsBlockTpResponse != null) {
                        focusInfo = this.parseCheckoutFocusDtoFromTp(focusInfoCmsBlockTpResponse, locale);
                        data.setFocusInfo(focusInfo);
                    } else {
                        this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get focus info cms block failed.");
                    }

                    // 获取收银台会员权益文件信息
                    String vipPrivilegeBlockId = VipTpConstant.SVIP_1 == vipType ? VipTpConstant.VIP_CHECKOUT_PRIVILEGE_BLOCK_ID_1
                            : VipTpConstant.VIP_CHECKOUT_PRIVILEGE_BLOCK_ID_9;
                    CmsBlockTpResponse vipPrivilegeCmsBlockTpResponse = this.facadeTpDao.getCmsTpDao().getCmsBlock(
                            vipPrivilegeBlockId);
                    if (vipPrivilegeCmsBlockTpResponse != null) {
                        vipPrivilege = this.parseVipPrivilegeDtoFromTp(vipPrivilegeCmsBlockTpResponse, locale);
                        data.setVipPrivilege(vipPrivilege);
                    } else {
                        this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get vip privilege cms block failed.");
                    }
                    response.setData(data);
                }
            }
        }

        return response;
    }

    /**
     * 鉴别该机器是否有未领取的机卡绑定套餐及套餐时长
     * @param getDeviceBindRequest
     * @param locale
     * @return
     */
    public Response<DeviceBindDto> getDeviceBind(GetDeviceBindRequest getDeviceBindRequest,
            GetPresentDeviceBindRequest getPresentDeviceBindRequest, Locale locale) {
        Response<DeviceBindDto> response = new Response<DeviceBindDto>();
        String logPrefix = "getDeviceBind_";
        String errorCode = null;
        String errorMsgCode = null;// 针对有的错误码并不是参数校验错误，所以需要一个错误码记录器
        DeviceBindDto data = null;

        if (getDeviceBindRequest == null && getPresentDeviceBindRequest == null) {
            // 查询类型错误
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameter.");
            errorMsgCode = errorCode
                    + VipUtil.parseErrorMsgCode(VipConstants.DEVICE_BIND,
                            VipMsgCodeConstant.VIP_GET_DEVICE_BIND_REQUEST_QUERY_TYPE_ERROR);
        } else {
            if (getDeviceBindRequest != null) {
                logPrefix = "getDeviceBind_" + getDeviceBindRequest.getDeviceKey() + "_"
                        + getDeviceBindRequest.getMac();

                int validCode = getDeviceBindRequest.assertValid();
                if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
                    errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameter.");
                    errorMsgCode = errorCode + VipUtil.parseErrorMsgCode(VipConstants.DEVICE_BIND, validCode);
                } else {
                    // 调用第三方接口，鉴别机卡绑定套餐信息
                    GetDeviceBindTpResponse deviceBindTp = this.facadeTpDao.getVipTpDao().getDeviceBind(
                            getDeviceBindRequest);
                    if (deviceBindTp == null || deviceBindTp.getValues() == null || deviceBindTp.getCode() == null
                            || deviceBindTp.getCode() != 0) {
                        // 调用接口失败（异常或返回失败码）
                        errorCode = ErrorCodeConstants.PAY_GET_DEVICE_BIND_FAILURE;
                        this.log.info(logPrefix + "[errorCode=" + errorCode + "]: get device bind info failed.");
                        errorMsgCode = errorCode;
                    } else {
                        data = new DeviceBindDto();
                        DeviceBindInfoTpResponse bindContent = deviceBindTp.getValues();
                        Boolean isDeviceActive = bindContent.getIsDeviceActive();
                        Integer bindDay = bindContent.getBindtime();
                        if (isDeviceActive == null || bindDay == null || bindDay <= 0) {
                            // 无效数据
                            data.setIsDeviceActive(VipConstants.DEVICE_BIND_STATUS_ILLEGAL);
                        } else if (Boolean.TRUE == isDeviceActive) {
                            // 已领取
                            data.setIsDeviceActive(VipConstants.DEVICE_BIND_STATUS_ACTIVATED);
                        } else {
                            // 未领取
                            data.setIsDeviceActive(VipConstants.DEVICE_BIND_STATUS_UNACTIVATED);
                            int bingMonth = this.parseDayToMonth(bindDay.intValue(), 31);
                            data.setBindMonths(Integer.valueOf(bingMonth));

                            // TODO 文案信息最好从配置文件读取
                            DeviceBindTextDto deviceBindText = new DeviceBindTextDto();
                            // 领取移动影视会员
                            deviceBindText.setTitle(MessageUtils.getMessage(VipConstants.VIP_DEVICE_BIND_TEXT_TITLE,
                                    locale));
                            String content = MessageUtils.getMessage(VipConstants.VIP_DEVICE_BIND_TEXT_CONTENT, locale);
                            deviceBindText.setContent(content.replace("$", String.valueOf(bingMonth)));
                            
                            String title = MessageUtils.getMessage(VipConstants.VIP_DEVICE_BIND_TEXT_TIPS,locale);
                            deviceBindText.setTips(title.replace("$", String.valueOf(bingMonth)));
                            data.setDeviceBindText(deviceBindText);
                        }
                    }
                }
            }

            if (errorCode == null && getPresentDeviceBindRequest != null) {
                logPrefix = "getDeviceBind_" + getPresentDeviceBindRequest.getPresentDeviceKey() + "_"
                        + getPresentDeviceBindRequest.getPresentDeviceInfo() + "_getPresentDeviceBindFrom_" + "_"
                        + getPresentDeviceBindRequest.getUserid();

                if (VipConstants.VIP_SHOW_PRESENT_DEVICE_BIND) {
                    // 如果未关闭领取赠送机卡入口，则显示，否则直接报错
                    int validCode = getPresentDeviceBindRequest.assertValid();
                    if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
                        errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
                        this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                        errorMsgCode = errorCode + VipUtil.parseErrorMsgCode(VipConstants.DEVICE_BIND, validCode);
                    } else {
                        GetPresentDeviceBindTpResponse tpResponse = this.facadeTpDao.getVipTpDao()
                                .getPresentDeviceBind(getPresentDeviceBindRequest);
                        if (tpResponse == null || !tpResponse.isSucceed()) {
                            errorCode = ErrorCodeConstants.PAY_GET_PRESENT_DEVICE_BIND_FAILURE;
                            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: get present device bind failure.");
                            errorMsgCode = errorCode;
                        } else {
                            if (data == null) {
                                data = new DeviceBindDto();
                            }

                            List<PresentDeviceBindTpResponse> presentList = tpResponse.getValues().getPresentList();
                            List<PresentDeviceBindDto> presentDeviceBinds = new LinkedList<PresentDeviceBindDto>();
                            if (!CollectionUtils.isEmpty(presentList)) {
                                for (PresentDeviceBindTpResponse presentDeviceBindTp : presentList) {
                                    if (presentDeviceBindTp != null && presentDeviceBindTp.isAvailable()) {
                                        PresentDeviceBindDto dto = new PresentDeviceBindDto();
                                        dto.setId(presentDeviceBindTp.getId());
                                        // 领取全屏影视会员
                                        dto.setTitle(MessageUtils.getMessage(
                                                VipConstants.VIP_DEVICE_BIND_TEXT_TITLE_PRESENT, locale));
                                        dto.setActiveTime(CalendarUtil.getDateStringFromLong(
                                                presentDeviceBindTp.getCreateTime(), CalendarUtil.SHORT_DATE_FORMAT));
                                        dto.setAvailableTime(String.valueOf(presentDeviceBindTp.getPresentDuration()));
                                        dto.setPresentProductName(presentDeviceBindTp.getDeviceVersion());
                                        presentDeviceBinds.add(dto);
                                    }
                                }
                                data.setPresentDeviceBinds(presentDeviceBinds);

                                DeviceBindTextDto presentDeviceBindText = new DeviceBindTextDto();
                                // 领取全屏影视会员
                                presentDeviceBindText.setTitle(MessageUtils.getMessage(
                                        VipConstants.VIP_DEVICE_BIND_TEXT_TITLE_PRESENT, locale));
                                presentDeviceBindText.setContent(MessageUtils.getMessage(
                                        VipConstants.VIP_DEVICE_BIND_TEXT_CONTENT_PRESENT, locale));
                                presentDeviceBindText.setTips(MessageUtils.getMessage(
                                        VipConstants.VIP_DEVICE_BIND_TEXT_TIPS_PRESENT, locale));
                                data.setPresentDeviceBindText(presentDeviceBindText);
                            }
                        }
                    }
                } else {
                    this.log.info(logPrefix + "[errorCode=" + ErrorCodeConstants.PAY_GET_PRESENT_DEVICE_BIND_FAILURE
                            + "]: get present device bind CLOSED!");
                    if (data == null) {
                        data = new DeviceBindDto();
                    }
                }
            }
        }

        if (errorCode != null) {
            this.setErrorResponse(response, errorCode, errorMsgCode, locale);
        } else {
            // TODO 设置显示优先级，应该从配置文件中读取
            if (data.getBindMonths() != null) {
                data.setPriority(VipConstants.DEVICE_BIND_QUERY_TYPE_1);
            } else {
                data.setPriority(VipConstants.DEVICE_BIND_QUERY_TYPE_2);
            }

            response.setData(data);
        }

        return response;
    }

    /**
     * 领取（自有或赠送的）机卡时长
     * @param receiveDeviceBindRequest
     * @param receivePresentDeviceBindRequest
     * @param locale
     * @return
     */
    public Response<ReceiveDeviceBindDto> receiveDeviceBind(ReceiveDeviceBindRequest receiveDeviceBindRequest,
            ReceivePresentDeviceBindRequest receivePresentDeviceBindRequest, Locale locale) {
        Response<ReceiveDeviceBindDto> response = new Response<ReceiveDeviceBindDto>();
        String logPrefix = "receiveDeviceBind_";
        String errorCode = null;
        if (receiveDeviceBindRequest == null && receivePresentDeviceBindRequest == null) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameter.");
            this.setErrorResponse(
                    response,
                    errorCode,
                    errorCode
                            + VipUtil.parseErrorMsgCode(VipConstants.RECEIVE_DEVICE_BIND,
                                    VipMsgCodeConstant.VIP_RECEIVE_DEVICE_BIND_REQUEST_QUERY_TYPE_ERROR), locale);
        } else {
            if (receiveDeviceBindRequest != null) {
                logPrefix = "receiveDeviceBind_" + receiveDeviceBindRequest.getUserid() + "_"
                        + receiveDeviceBindRequest.getMac() + "_" + receiveDeviceBindRequest.getDeviceKey();

                int validCode = receiveDeviceBindRequest.assertValid();
                if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
                    errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                    this.setErrorResponse(response, errorCode,
                            errorCode + VipUtil.parseErrorMsgCode(VipConstants.RECEIVE_DEVICE_BIND, validCode), locale);
                } else {
                    ReceiveDeviceBindTpResponse tpResponse = this.facadeTpDao.getVipTpDao().receiveDeviceBind(
                            receiveDeviceBindRequest);
                    if (tpResponse == null || !tpResponse.isSuccess()) {
                        // 领取失败
                        errorCode = ErrorCodeConstants.PAY_RECEIVE_DEVICE_BIND_FAILURE;
                        this.log.info(logPrefix + "[errorCode=" + errorCode + "]: receive present device bind falied.");
                        this.setErrorResponse(response, errorCode, errorCode, locale);
                    } else {
                        // 领取成功
                        ReceiveDeviceBindDto data = new ReceiveDeviceBindDto();
                        data.setEndTime(tpResponse.getValues().getViptime());
                        response.setData(data);
                    }
                }
            }

            if (receivePresentDeviceBindRequest != null) {
                logPrefix = "receiveDeviceBind_" + receivePresentDeviceBindRequest.getUserid() + "_"
                        + receivePresentDeviceBindRequest.getPresentDeviceInfo() + "_"
                        + receivePresentDeviceBindRequest.getPresentDeviceKey() + "_"
                        + receivePresentDeviceBindRequest.getPresentId();

                int validCode = receivePresentDeviceBindRequest.assertValid();
                if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
                    errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                    this.setErrorResponse(response, errorCode,
                            errorCode + VipUtil.parseErrorMsgCode(VipConstants.RECEIVE_DEVICE_BIND, validCode), locale);
                } else {
                    ReceivePresentDeviceBindTpResponse tpResponse = this.facadeTpDao.getVipTpDao()
                            .receivePresentDeviceBind(receivePresentDeviceBindRequest);
                    if (tpResponse == null || !tpResponse.isSuccess()) {
                        // 领取失败
                        errorCode = ErrorCodeConstants.PAY_RECEIVE_PRESENT_DEVICE_BIND_FAILURE;
                        this.log.info(logPrefix + "[errorCode=" + errorCode + "]: receive present device bind falied.");
                        this.setErrorResponse(response, errorCode, errorCode, locale);
                    } else {
                        // 领取成功
                        ReceiveDeviceBindDto data = new ReceiveDeviceBindDto();
                        data.setEndTime(CalendarUtil.getDateStringFromLong(tpResponse.getValues().getCanceltime(),
                                CalendarUtil.SHORT_DATE_FORMAT));
                        response.setData(data);
                    }
                }
            }
        }

        return response;
    }

    /**
     * 会员消费
     * @param purchaseVipCommonRequest
     * @param locale
     * @return
     */
    public Response<PurchaseVipDto> purchaseVip(PurchaseVipCommonRequest purchaseVipCommonRequest, Locale locale, String returnUrl) {
        Response<PurchaseVipDto> response = new Response<PurchaseVipDto>();
        String errorCode = null;
        String logPrefix = "purchaseVip_" + purchaseVipCommonRequest.getUserid() + "_"
                + purchaseVipCommonRequest.getActualProductId() + purchaseVipCommonRequest.getPaymentChannel() + "_ "
                + purchaseVipCommonRequest.getMac();

        int validCode = purchaseVipCommonRequest.assertValid();
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.PURCHASE_VIP, validCode), locale);
        } else {
            PurchaseVipDto data = null;

            switch (purchaseVipCommonRequest.getPaymentChannel()) {
            case VipTpConstant.PAYMENT_CHANNEL_ALI_APP_QUICK_PAY:
                // 如果是连续包月，需要进行判断是否进行签约
                int type = Integer.parseInt(purchaseVipCommonRequest.getProductid());
                if (VipTpConstant.ORDER_TYPE_42 == type || VipTpConstant.ORDER_TYPE_52 == type) {
                    this.autoMonthlyAliPay(purchaseVipCommonRequest, errorCode, data, logPrefix,
                            response, locale, returnUrl);
                } else {
                    // 连续包月外的支付宝支付
                    AliAppPayTpResponse aliPayTpResponse = this.facadeTpDao.getVipTpDao()
                            .purchaseVip(purchaseVipCommonRequest, AliAppPayTpResponse.class);

                    if (aliPayTpResponse != null && aliPayTpResponse.isSuccess()) {
                        data = new PurchaseVipDto();
                        data.setCorderid(aliPayTpResponse.getCorderid());
                        data.setParentId(aliPayTpResponse.getPartnerId());
                        data.setInfo(aliPayTpResponse.getInfo());
                    } else {
                        errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_ALI_APP_PAY_FAILURE;
                        this.log.info(logPrefix + "[errorCode=" + errorCode + "]: ali app pay failed.");
                        this.setErrorResponse(response, errorCode, errorCode, locale);
                    }
                }
                break;
            case VipTpConstant.PAYMENT_CHANNEL_WEIXIN_APP:
            case VipTpConstant.PAYMENT_CHANNEL_WEIXIN_APP_SULER_LIVE:
                // 微信app
                WeixinAppPayTpResponse weixinAppPayTpResponse = this.facadeTpDao.getVipTpDao().purchaseVip(
                        purchaseVipCommonRequest, WeixinAppPayTpResponse.class);

                if (weixinAppPayTpResponse != null && weixinAppPayTpResponse.isSuccess()) {
                    data = new PurchaseVipDto();
                    data.setCorderid(weixinAppPayTpResponse.getCorderid());
                    data.setPrice(weixinAppPayTpResponse.getPrice());
                    data.setOrdernumber(weixinAppPayTpResponse.getOrdernumber());

                    data.setTimestamp(weixinAppPayTpResponse.getTimestamp());
                    data.setNoncestr(weixinAppPayTpResponse.getNoncestr());
                    data.setParentId(weixinAppPayTpResponse.getPartnerid());
                    data.setPrepayid(weixinAppPayTpResponse.getPrepayid());
                    data.setWeixinPackage(weixinAppPayTpResponse.getWeixinPackage());
                    data.setAppid(weixinAppPayTpResponse.getAppid());
                    data.setSign(weixinAppPayTpResponse.getSign());
                } else {
                    errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_WEIXIN_APP_PAY_FAILURE;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]: weixin app pay failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
                break;
            case VipTpConstant.PAYMENT_CHANNEL_LETV_POINT:
                // 乐点支付
                LetvPointPayTpResponse letvPointPayTpResponse = this.facadeTpDao.getVipTpDao().purchaseVip(
                        purchaseVipCommonRequest, LetvPointPayTpResponse.class);

                if (letvPointPayTpResponse != null && letvPointPayTpResponse.isSuccess()) {
                    data = new PurchaseVipDto();
                    data.setCorderid(letvPointPayTpResponse.getCorderid());
                } else {
                    errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_LETVPOINT_PAY_FAILURE;
                    this.log.error(logPrefix + "[errorCode=" + errorCode + "]: letvPoint pay failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
                break;
            case VipTpConstant.PAYMENT_CHANNEL_PHONEPAY_UNICOM:
            case VipTpConstant.PAYMENT_CHANNEL_PHONEPAY_TELECOM:
                // 拉卡拉二维码
                PhonePayTpResponse phonePayTpResponse = this.facadeTpDao.getVipTpDao().purchaseVip(
                        purchaseVipCommonRequest, PhonePayTpResponse.class);

                if (phonePayTpResponse != null && phonePayTpResponse.isSuccess()) {
                    data = new PurchaseVipDto();

                    data.setCorderid(phonePayTpResponse.getCorderid());
                    if (VipTpConstant.PAYMENT_CHANNEL_PHONEPAY_TELECOM == purchaseVipCommonRequest.getPaymentChannel()) {
                        // 电信支付特殊参数
                        data.setCommand(phonePayTpResponse.getCommand());
                        data.setServicecode(phonePayTpResponse.getServicecode());
                    }
                } else {
                    errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_PHONE_PAY_FAILURE;
                    this.log.error(logPrefix + "[errorCode=" + errorCode + "]: letvPoint pay failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
                break;
            default:
                // 非法支付方式
                errorCode = ErrorCodeConstants.PAY_ILLEGATE_PAYMENT_CHANNEL;
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal payment channel["
                        + purchaseVipCommonRequest.getPaymentChannel() + "].");
                break;
            }

            if (data != null) {
                response.setData(data);
            }
        }

        return response;
    }

    /**
     * 会员连续包月支付的处理：已签约直接支付，未签约则跳转签约
     * 
     * @param purchaseVipCommonRequest
     * @param errorCode
     * @param data
     * @param logPrefix
     * @param response
     * @param locale
     * @param returnUrl 
     */
    private void autoMonthlyAliPay(PurchaseVipCommonRequest purchaseVipCommonRequest, String errorCode,
            PurchaseVipDto data, String logPrefix, Response<PurchaseVipDto> response, Locale locale, String returnUrl) {
        // 如果是连续包月，需要进行判断是否有签约，
        AutoPayCheckInfoResquest checkInfoRequest = new AutoPayCheckInfoResquest(purchaseVipCommonRequest.getUserid(),
                purchaseVipCommonRequest.getVipType());
        AutoPayCheckInfoResponse checkinfoResponse = this.facadeTpDao.getVipTpDao().getCheckInfo(checkInfoRequest,
                AutoPayCheckInfoResponse.class);

        // 需要对移动、全屏影视会员都做判断才行
        if(checkinfoResponse.getIsOneKey()== null){
            Integer type = purchaseVipCommonRequest.getVipType() == VipTpConstant.SVIP_1 ? 
                    VipTpConstant.SVIP_9 : VipTpConstant.SVIP_1;
            checkInfoRequest = new AutoPayCheckInfoResquest(purchaseVipCommonRequest.getUserid(),type);
            checkinfoResponse = this.facadeTpDao.getVipTpDao().getCheckInfo(checkInfoRequest,
                    AutoPayCheckInfoResponse.class);
        }

        if (checkinfoResponse != null) {
            if (checkinfoResponse.getStatus()!=null && checkinfoResponse.getStatus() == 1) {
                // 如果已有连续包月
            } else if (checkinfoResponse.getIsOneKey() != null && checkinfoResponse.getIsOneKey() == false) {
                // 如果没有签约，则得到地址并返回给客户端
                String price = purchaseVipCommonRequest.getPrice();
                if(price.contains(".")){
                    price = price.substring(0, price.indexOf("."));
                }

                AutoPayOnekeyRequest oneKeySignRequest = new AutoPayOnekeyRequest(
                        purchaseVipCommonRequest.getCorderid(), purchaseVipCommonRequest.getProductid(),
                        Integer.valueOf(price), purchaseVipCommonRequest.getVipType(),
                        purchaseVipCommonRequest.getUserid(), returnUrl, purchaseVipCommonRequest.getActivityIds());
                oneKeySignRequest.setREQ_URL(ApplicationUtils.get(VipTpConstant.BOSS_AUTOPAY_ONEKEY_SIGN_URL));
                AutoPayOnekeyResponse oneKeySignResponse = this.facadeTpDao.getVipTpDao()
                        .getOneKeySignInfo(oneKeySignRequest, AutoPayOnekeyResponse.class);

                if (oneKeySignResponse != null && oneKeySignResponse.getStatus()!=null
                        && oneKeySignResponse.isSignSuccess()) {
                    data = new PurchaseVipDto();
                    data.setCorderid(oneKeySignResponse.getCorderid());
                    data.setParentId(oneKeySignResponse.getPartnerId());
                    data.setInfo(oneKeySignResponse.getInfo());
                    data.setOnekeySign(false);
                    response.setData(data);
                } else {
                    // 一键签约失败
                    errorCode = VipConstants.AUTO_PAY_ONKEY_SIGN_FAILURE;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]:onekey sign failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
            }else if (checkinfoResponse.getIsOneKey() != null && checkinfoResponse.getIsOneKey() == true
                    && checkinfoResponse.getPaytype() != null) {
                // 如果是连续包月，调用连续包月的接口AutoPayOnekeyRequest
                String price = purchaseVipCommonRequest.getPrice();
                if(price.contains(".")){
                    price = price.substring(0, price.indexOf("."));
                }
                AutoPayOnekeyRequest oneKeyPayRequest = new AutoPayOnekeyRequest(purchaseVipCommonRequest.getCorderid(),
                        purchaseVipCommonRequest.getProductid(), Integer.valueOf(price),
                        purchaseVipCommonRequest.getVipType(), purchaseVipCommonRequest.getUserid(), returnUrl,
                        purchaseVipCommonRequest.getActivityIds());
                oneKeyPayRequest.setREQ_URL(ApplicationUtils.get(VipTpConstant.BOSS_AUTOPAY_ONEKEY_PAY_URL)
                        + checkinfoResponse.getPaytype()+"?");
                AutoPayOnekeyResponse oneKeyPayResponse = this.facadeTpDao.getVipTpDao().
                        getOneKeySignInfo(oneKeyPayRequest, AutoPayOnekeyResponse.class);

                if (oneKeyPayResponse != null && oneKeyPayResponse.isPaySuccess()) {
                    data = new PurchaseVipDto();
                    data.setCorderid(oneKeyPayResponse.getCorderid());
                    data.setOrdernumber(oneKeyPayResponse.getOrdernumber());
                    data.setOnekeySign(true);
                    response.setData(data);
                } else {
                    errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_ALI_APP_PAY_FAILURE;
                    this.log.info(logPrefix + "[errorCode=" + errorCode + "]: ali app pay failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
            } else {
                // 获取用户签约续费信息失败
                errorCode = VipConstants.AUTO_PAY_USERS_INFO_FAILURE;
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: get user sign info failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            }
        }else{
            // 支付失败
            errorCode = ErrorCodeConstants.PAY_PURCHASE_VIP_ALI_APP_PAY_FAILURE;
            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: ali app pay failed.");
            this.setErrorResponse(response, errorCode, errorCode, locale);
        }
    }

    /**
     * 查询用户订单信息
     * @param checkUserOrderRequest
     * @param locale
     * @return
     */
    public Response<OrderInfoDto> checkOrder(CheckUserOrderRequest checkUserOrderRequest, Locale locale) {
        Response<OrderInfoDto> response = new Response<OrderInfoDto>();
        String logPrefix = "checkOrder_" + checkUserOrderRequest.getUserid() + "_" + checkUserOrderRequest.getOrderid();
        String errorCode = null;

        int validCode = checkUserOrderRequest.assertValid();
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.info(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.CHECK_ORDER_STATUS, validCode), locale);
        } else {
            // 查询订单详情
            CheckUserOrderTpResponse userOrderTp = this.facadeTpDao.getVipTpDao().checkOrder(checkUserOrderRequest);

            if (userOrderTp == null || userOrderTp.getTotal() == null) {
                // 获取订单信息失败
                errorCode = ErrorCodeConstants.PAY_CHECK_USER_ORDER_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: check roder failed.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else if (0 == userOrderTp.getTotal() || userOrderTp.getData() == null
                    || userOrderTp.getData().size() == 0) {
                // 订单不存在
                errorCode = ErrorCodeConstants.PAY_USER_ORDER_NULL;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: empty roder info.");
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                // 订单信息存在，设置返回
                OrderInfoDto orderDto = new OrderInfoDto();
                UserOrderInfoTpResponse orderInfoTp = userOrderTp.getData().get(0);
                if (orderInfoTp != null) {
                    orderDto.setOrderId(orderInfoTp.getOrderId());
                    orderDto.setOrderName(orderInfoTp.getOrderName());
                    orderDto.setMoney(orderInfoTp.getMoney());

                    // 单点支付新增逻辑，需要通过orderType判断当前订单是否为单点订单
                    String orderCreateAndCancelTimeFormat = String.valueOf(VipTpConstant.ORDER_TYPE_0).equals(
                            orderInfoTp.getOrderType()) ? CalendarUtil.SIMPLE_DATE_HOUR_MINUTE_FORMAT
                            : CalendarUtil.SHORT_DATE_FORMAT;
                    orderDto.setCancelTime(CalendarUtil.getDateStringFromLong(orderInfoTp.getCancelTime(),
                            orderCreateAndCancelTimeFormat));

                    if (VipTpConstantUtils.isOrderPaySuccess(orderInfoTp.getStatusName())) {
                        orderDto.setPayStatus(VipConstants.ORDER_PAY_STATUS_SUCCESS);
                    } else {
                        orderDto.setPayStatus(VipConstants.ORDER_PAY_STATUS_NOT_PAY);
                    }

                    response.setData(orderDto);
                } else {
                    errorCode = ErrorCodeConstants.PAY_CHECK_USER_ORDER_FAILURE;
                    this.log.error(logPrefix + "[errorCode=" + errorCode + "]: check roder failed.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                }
            }
        }

        return response;
    }

    /**
     * 使用直播券
     * @param activeMovieTicketRequest
     * @param locale
     * @return
     */
    public Response<ValueDto<Boolean>> activeMovieTicket(String movieId, String movieName, String uid, Locale locale,
            String routeInfo) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String errorCode = null;

        RouteInfo route = this.facadeService.getPlayService().transRouteInfo(routeInfo);
        if (route.isRoute()) {
            RouteActiveMovieTicketRequest activeMovieTicketRequest = new RouteActiveMovieTicketRequest(uid, movieName,
                    movieId, route.getRouteUserid(), route.getRouteMac(), route.getRouteSn());
            activeMovieTicketRequest.routeUserId(route.getRouteUserid()).routeMac(route.getRouteMac())
                    .routeSN(route.getRouteSn());
            activeMovieTicketRequest.movieId(movieId).userid(uid).movieName(movieName);

            ActiveMovieTicketTpResponse tpResponse = this.facadeTpDao.getVipTpDao().activeMovieTicket(
                    activeMovieTicketRequest);

            if (tpResponse == null || !tpResponse.isSucceed()) {
                errorCode = ErrorCodeConstants.PAY_ACTIVE_MOVIE_TICKET_FAILURE;
                this.setErrorResponse(response, errorCode, errorCode, locale);
            } else {
                response.setData(new ValueDto<Boolean>(true));
            }
        } else {
            ActiveMovieTicketRequest activeMovieTicketRequest = new ActiveMovieTicketRequest(uid, movieName, movieId);

            String logPrefix = "activeMovieTicket_" + activeMovieTicketRequest.getUserid() + "_"
                    + activeMovieTicketRequest.getMovieId() + "_" + activeMovieTicketRequest.getMovieName();
            int validCode = activeMovieTicketRequest.assertValid();// 参数校验返回码

            // 参数校验
            if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
                errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                this.setErrorResponse(response, errorCode,
                        errorCode + VipUtil.parseErrorMsgCode(VipConstants.MOVIE_TICKET, validCode), locale);
            } else {
                ActiveMovieTicketTpResponse tpResponse = this.facadeTpDao.getVipTpDao().activeMovieTicket(
                        activeMovieTicketRequest);

                if (tpResponse == null || !tpResponse.isSucceed()) {
                    errorCode = ErrorCodeConstants.PAY_ACTIVE_MOVIE_TICKET_FAILURE;
                    this.log.error(logPrefix + "[errorCode=" + errorCode + "]: active movie ticket failure.");
                    this.setErrorResponse(response, errorCode, errorCode, locale);
                } else {
                    response.setData(new ValueDto<Boolean>(true));
                }
            }
        }

        return response;
    }

    /**
     * 查询用户的直播券
     * @param userLiveCommonRequest
     * @param locale
     * @return
     */
    public PageResponse<LiveTicketDto> checkUserLiveTicket(UserLiveCommonRequest userLiveCommonRequest, Locale locale) {
        PageResponse<LiveTicketDto> response = new PageResponse<LiveTicketDto>();
        String logPrefix = "checkUserLiveTicket_" + userLiveCommonRequest.getUserid() + "_"
                + userLiveCommonRequest.getPid();
        String errorCode = null;
        String errorMsgCode = null;

        // 参数校验
        int validCode = userLiveCommonRequest.assertValid();
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET, validCode), locale);
        } else {
            // 查询直播券；
            CheckUserLiveTicketTpResponse checkTicketTpResponse = this.facadeTpDao.getVipTpDao().checkUserLiveTicket(
                    userLiveCommonRequest);
            if (checkTicketTpResponse == null) {
                errorCode = ErrorCodeConstants.PAY_GET_USER_LIVE_TICKET_FAILURE;
                errorMsgCode = VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_TP_INVOKE_FAILURE);
            } else if (!checkTicketTpResponse.hasBoughtLiveTicket()) {
                // TODO 是否购买或是否有可用直播券时是否应该报错还需要考虑
                errorCode = ErrorCodeConstants.PAY_GET_USER_LIVE_TICKET_FAILURE;
                errorMsgCode = VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_BUSSINESS_FAILURE);
            } else {
                List<UserLiveTicket> userLiveTicketList = checkTicketTpResponse.getPackages();
                List<LiveTicketDto> liveTicketList = new LinkedList<LiveTicketDto>();
                for (UserLiveTicket ticket : userLiveTicketList) {
                    LiveTicketDto liveTicketDto = new LiveTicketDto();
                    liveTicketDto.setStatus(ticket.getStatus());
                    liveTicketDto.setCount(ticket.getCount());
                    liveTicketDto.setType(ticket.getType());

                    liveTicketList.add(liveTicketDto);
                }
                response.setData(liveTicketList);
            }
            if (errorCode != null) {
                this.setErrorResponse(response, errorCode, errorMsgCode, locale);
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: check user live ticket failure.");
            }
        }

        return response;
    }

    /**
     * 激活直播券
     * @param userLiveCommonRequest
     * @param locale
     * @return
     */
    public Response<ValueDto<Boolean>> activeLiveTicket(UserLiveCommonRequest userLiveCommonRequest, Locale locale) {
        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        String logPrefix = "activeLiveTicket_" + userLiveCommonRequest.getUserid() + "_"
                + userLiveCommonRequest.getPid();
        String errorCode = null;
        String errorMsgCode = null;

        // 参数校验
        int validCode = userLiveCommonRequest.assertValid();
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET, validCode), locale);
        } else {
            // 激活直播券
            ActiveUserLiveTicketTpResponse activeTicketTpResponse = this.facadeTpDao.getVipTpDao()
                    .activeUserLiveTicket(userLiveCommonRequest);
            if (activeTicketTpResponse == null) {
                // 接口使用失败，如无响应
                errorCode = ErrorCodeConstants.PAY_ACTIVE_LIVE_TICKET_FAILURE;
                errorMsgCode = VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_TP_INVOKE_FAILURE);
            } else if (!activeTicketTpResponse.isSucceed()) {
                // 业务逻辑处理失败
                errorCode = ErrorCodeConstants.PAY_ACTIVE_LIVE_TICKET_FAILURE;
                errorMsgCode = VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_BUSSINESS_FAILURE);
            } else {
                response.setData(new ValueDto<Boolean>(true));
            }

            if (errorCode != null) {
                this.setErrorResponse(response, errorCode, errorMsgCode, locale);
                this.log.info(logPrefix + "[errorCode=" + errorCode + "]: check user live ticket failure.");
            }
        }

        return response;
    }

    /**
     * 查询用户直播券详细信息
     * @param liveTicketCommonRequest
     * @param locale
     * @return
     */
    public Response<LiveTicketPackageInfoDto> getLiveTicketInfo(LiveTicketCommonRequest liveTicketCommonRequest,
            Locale locale) {
        Response<LiveTicketPackageInfoDto> response = new Response<LiveTicketPackageInfoDto>();
        String logPrefix = "getLiveTicketInfo_" + liveTicketCommonRequest.getPid();
        String errorCode = null;

        int validCode = liveTicketCommonRequest.assertValid();
        if (VipMsgCodeConstant.VIP_COMMON_REQUEST_SUCCESS != validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    errorCode + VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET, validCode), locale);
        } else {
            GetLiveTicketPackagesResponse getLiveTicketPackagesResponse = this.facadeTpDao.getVipTpDao()
                    .getLiveTicketInfo(liveTicketCommonRequest);
            if (getLiveTicketPackagesResponse == null) {
                errorCode = ErrorCodeConstants.PAY_GET_LIVE_TICKETINFO_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                this.setErrorResponse(response, errorCode, VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_TP_INVOKE_FAILURE), locale);
            } else if ("0".equals(getLiveTicketPackagesResponse.getStatus())) {
                errorCode = ErrorCodeConstants.PAY_GET_LIVE_TICKETINFO_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
                this.setErrorResponse(response, errorCode, VipUtil.parseErrorMsgCode(VipConstants.USER_LIVE_TICKET,
                        VipMsgCodeConstant.VIP_COMMON_RESPONSE_BUSSINESS_FAILURE), locale);
            } else {
                LiveTicketPackageInfoDto liveTicketPackageInfoDto = new LiveTicketPackageInfoDto();

                this.initLiveTicketPackageInfoDto(liveTicketCommonRequest, getLiveTicketPackagesResponse,
                        liveTicketPackageInfoDto);

                response.setData(liveTicketPackageInfoDto);
            }
        }
        return response;
    }

    /**
     * 根据调用第三方接口的返回值，初始化接口的返回信息
     * @param getLiveTicketPackagesResponse
     * @param liveTicketPackageInfoDto
     * @return
     */
    private LiveTicketPackageInfoDto initLiveTicketPackageInfoDto(LiveTicketCommonRequest liveTicketCommonRequest,
            GetLiveTicketPackagesResponse getLiveTicketPackagesResponse,
            LiveTicketPackageInfoDto liveTicketPackageInfoDto) {
        liveTicketPackageInfoDto.setMatchId(getLiveTicketPackagesResponse.getMatchId());
        liveTicketPackageInfoDto.setItemId(getLiveTicketPackagesResponse.getItemId());
        liveTicketPackageInfoDto.setSessionId(getLiveTicketPackagesResponse.getSessionId());
        liveTicketPackageInfoDto.setSessionEndDate(getLiveTicketPackagesResponse.getSessionEndDate());

        List<LiveTicketInfoResponse> list = getLiveTicketPackagesResponse.getPackages();
        List<LiveTicketInfoDto> liveTicketList = new LinkedList<LiveTicketInfoDto>();

        // 是否是音乐直播
        boolean isMusicLive = VipTpConstant.BOSS_LIVE_CHANNEL_MUSIC.equals(liveTicketCommonRequest.getChannel());

        for (LiveTicketInfoResponse res : list) {
            if ("1".equals(res.getStatus()) || res.getType() != 1) {
                // 不可用的状态或者不是直播券，不不返回数据
                continue;
            }

            if (isMusicLive
                    && !(StringUtils.equals(liveTicketCommonRequest.getTurn(), res.getRounds()) && StringUtils.equals(
                            liveTicketCommonRequest.getGame(), res.getPlay_number()))) {
                // 针对音乐直播进行过滤
                continue;
            }

            // 需要过滤已过期直播券时
            if (VipConstants.VIP_FILTER_EXPRIED_LIVE_TICKET && this.isLiveTicketExpried(res)) {
                continue;
            }

            LiveTicketInfoDto liveTicketInfoDto = new LiveTicketInfoDto();
            liveTicketInfoDto.setCounts(res.getCounts());
            liveTicketInfoDto.setMobileImg(res.getMobileImg());
            liveTicketInfoDto.setName(res.getName());
            liveTicketInfoDto.setPlayDesc(res.getPlayDesc());
            liveTicketInfoDto.setPlayName(res.getPlayName());
            liveTicketInfoDto.setOriginalPrice(res.getRegular_price());
            liveTicketInfoDto.setEndTime(res.getSession_end_time());
            liveTicketInfoDto.setSessionName(res.getSessionname());
            liveTicketInfoDto.setSuperMobileImg(res.getSuperMobileImg());
            liveTicketInfoDto.setType(res.getType());
            liveTicketInfoDto.setValidateDays(res.getValidate_days());
            liveTicketInfoDto.setVipPrice(res.getVip_price());

            liveTicketList.add(liveTicketInfoDto);
        }
        liveTicketPackageInfoDto.setPackages(liveTicketList);

        return liveTicketPackageInfoDto;
    }

    /**
     * 从第三方响应中解析出VipPackageDto信息；
     * 返回List格式数据，因为VipPackageListTpResponse中的套餐已经排好序，如果使用Map会打乱顺序
     * @param vipPackageListResponse
     * @param appVersion 
     * @return
     */
    private List<VipPackageDto> parseVipPackageFromTp(VipPackageListTpResponse vipPackageListResponse, Integer vipType,
            Locale locale, String userid, String appVersion) {
        List<VipPackageDto> vipPackageList = null;

        List<VipPackageInfo> packageList = vipPackageListResponse.getPackageList();
        if (!CollectionUtils.isEmpty(packageList)) {
            vipPackageList = new ArrayList<VipPackageDto>();
            for (VipPackageInfo vipPackageInfo : packageList) {
            	// 1.6.9版本以下不推连续包月
            	if(vipPackageInfo != null
            			&& VipTpConstant.ORDER_TYPE_42 == Integer.parseInt(vipPackageInfo.getMonthType())
                		|| VipTpConstant.ORDER_TYPE_52 == Integer.parseInt(vipPackageInfo.getMonthType())){
            		if(VipPackageBuilder.compareVersion(appVersion, "1.7.0")){
            			continue;
            		}
            	}
            	
                VipPackageDto dto = VipPackageBuilder.build(vipPackageInfo, vipType);
                if (dto != null) {
                    // 连续包月处理
                    if (dto.getId() != null && VipTpConstant.ORDER_TYPE_42 == Integer.parseInt(dto.getId())
                    		|| VipTpConstant.ORDER_TYPE_52 == Integer.parseInt(dto.getId())) {
                        // 增加连续包月提示信息
                        AutoPayCheckInfoResquest checkInfoRequest = new AutoPayCheckInfoResquest(userid, vipType);
                        AutoPayCheckInfoResponse checkinfoResponse = this.facadeTpDao.getVipTpDao()
                                .getCheckInfo(checkInfoRequest, AutoPayCheckInfoResponse.class);

                        // 有连续包月则不加入套餐
                        if(checkinfoResponse != null && checkinfoResponse.getStatus()!=null &&
                                checkinfoResponse.getStatus() == 1){
                            continue;
                        }else{
                            if(checkinfoResponse != null && checkinfoResponse.getMsg() != null){
                                try {
                                    String msg = URLDecoder.decode(checkinfoResponse.getMsg(),"UTF-8");
                                    dto.setAutoPayMsg(msg);
                                } catch (UnsupportedEncodingException e) {
                                    log.error("URLDecoder error");
                                }
                            }
                            // 增加连续包月文案信息
                            dto.setAutoPayInfo(VipConstants.AUTO_PAY_MONTHLY_INFO);
                            vipPackageList.add(dto);
                        }

                    }else{
                        vipPackageList.add(dto);
                    }
                }
            }
        }

        return vipPackageList;
    }

    /**
     * 从第三方响应中解析出PaymentActivityDto信息；
     * 返回Map格式数据，key--活动id，value--整理后的活动信息，PaymentActivityDto；
     * @param paymentActivityTpResponse
     * @return
     */
    private Map<String, PaymentActivityDto> parsePaymentActivityFromTp(
            PaymentActivityTpResponse paymentActivityTpResponse, Integer vipType, Locale locale) {
        Map<String, PaymentActivityDto> paymentActivityMap = null;
        Map<String, List<PaymentActivityInfo>> activitys = paymentActivityTpResponse.getValues().getActivitys();
        if (!CollectionUtils.isEmpty(activitys)) {
            paymentActivityMap = new HashMap<String, PaymentActivityDto>();
            for (Entry<String, List<PaymentActivityInfo>> entry : activitys.entrySet()) {
                List<PaymentActivityInfo> paymentActivityInfoList = entry.getValue();
                if (!CollectionUtils.isEmpty(paymentActivityInfoList)) {
                    for (PaymentActivityInfo paymentActivityInfo : paymentActivityInfoList) {
                        PaymentActivityDto dto = PaymentActivityBuilder.build(paymentActivityInfo, vipType, locale);
                        if (dto != null) {
                            paymentActivityMap.put(dto.getId(), dto);
                        }
                    }
                }
            }
        }

        return paymentActivityMap;
    }

    /**
     * 将付费活动相关信息，填充到套餐信息中
     * @param vipPackageMap
     * @param paymentActivityMap
     */
    private void fillVipPackageWithPaymentActivityes(List<VipPackageDto> vipPackageList,
            Map<String, PaymentActivityDto> paymentActivityMap) {
        if (CollectionUtils.isEmpty(paymentActivityMap) || CollectionUtils.isEmpty(vipPackageList)) {
            return;
        }

        Map<String, VipPackageDto> vipPackageMap = new HashMap<String, VipPackageDto>();
        for (VipPackageDto vipPackageDto : vipPackageList) {
            vipPackageMap.put(vipPackageDto.getId(), vipPackageDto);
        }

        for (Entry<String, PaymentActivityDto> paymentActivityEntry : paymentActivityMap.entrySet()) {
            String activityId = paymentActivityEntry.getKey();
            PaymentActivityDto paymentActivity = paymentActivityEntry.getValue();
            VipPackageDto vipPackage = vipPackageMap.get(paymentActivity.getMonthType());
            if (vipPackage != null && paymentActivity.getType() != null) {
                if (VipTpConstant.PAYMENT_ACTIVITY_TYPE_VIP_PACKAGE == paymentActivity.getType()) {
                    // 套餐活动
                    vipPackage.setPackageActivityId(activityId);
                } else if (VipTpConstant.PAYMENT_ACTIVITY_TYPE_PAYMENT_CHANNEL == paymentActivity.getType()) {
                    // 支付渠道活动
                    List<Integer> payTypeList = paymentActivity.getPayTypeList();
                    if (!CollectionUtils.isEmpty(payTypeList)) {
                        Map<String, String> paymentChannelActivitys = vipPackage.getPaymentChannelActivitys();
                        if (paymentChannelActivitys == null) {
                            paymentChannelActivitys = new HashMap<String, String>();
                            vipPackage.setPaymentChannelActivitys(paymentChannelActivitys);
                        }
                        for (Integer payType : payTypeList) {
                            paymentChannelActivitys.put(String.valueOf(payType), activityId);
                        }
                    }
                }
            }
        }
    }

    /**
     * 从CmsBlockTpResponse中解析出会员权益文案和图片
     * @param cmsBlockTpResponse
     * @return
     */
    private VipPrivilegeDto parseVipPrivilegeDtoFromTp(CmsBlockTpResponse cmsBlockTpResponse, Locale locale) {
        VipPrivilegeDto vipPrivilegeDto = null;
        List<CmsBlockContent> blockContentList = cmsBlockTpResponse.getBlockContent();
        if (!CollectionUtils.isEmpty(blockContentList)) {
            vipPrivilegeDto = new VipPrivilegeDto();
            vipPrivilegeDto.setContentStyle(VipTpConstant.VIP_CHECKOUT_FOCUS_CONTENT_STYLE);
            vipPrivilegeDto.setTitle(cmsBlockTpResponse.getName());

            // TODO 用户会员协议文案及跳转链接应当从CMS板块读取，目前写死，但不在配置文件中标注
            vipPrivilegeDto.setUserAggrementText(MessageUtils.getMessage(
                    VipConstants.VIP_CHECKOUT_PRIVILEGE_BLOCK_USER_AGGREMENT_TEXT, locale));
            vipPrivilegeDto.setUserAggrementUrl(VipTpConstant.VIP_CHECKOUT_PRIVILEGE_BLOCK_USER_AGGREMENT_URL);
            List<VipBlockContentDto> privilegeList = new ArrayList<VipBlockContentDto>();

            for (CmsBlockContent blockContent : blockContentList) {
                if (blockContent != null
                        && (StringUtils.contains(blockContent.getPushflag(), DataConstants.MOBILE_COPYRIGHT) || StringUtils
                                .contains(blockContent.getPushflag(), DataConstants.ANDROID_COPYRIGHT))
                        && StringUtils.isNotBlank(blockContent.getTitle())) {

                    VipBlockContentDto privilegeDto = new VipBlockContentDto();
                    privilegeDto.setTitle(blockContent.getTitle());
                    privilegeDto.setMobilePic(blockContent.getMobilePic());
                    privilegeDto.setSkipUrl(blockContent.getSkipUrl());

                    privilegeList.add(privilegeDto);
                }
            }
            vipPrivilegeDto.setPrivilegeList(privilegeList);
        }

        return vipPrivilegeDto;
    }

    /**
     * 从CmsBlockTpResponse中解析出收银台焦点图信息
     * @param cmsBlockTpResponse
     * @return
     */
    private CheckoutFocusDto parseCheckoutFocusDtoFromTp(CmsBlockTpResponse cmsBlockTpResponse, Locale locale) {
        CheckoutFocusDto checkoutFocusDto = null;
        List<CmsBlockContent> blockContentList = cmsBlockTpResponse.getBlockContent();
        if (!CollectionUtils.isEmpty(blockContentList)) {
            checkoutFocusDto = new CheckoutFocusDto();
            checkoutFocusDto.setContentStyle(VipTpConstant.VIP_PRIVILEGE_CONTENT_STYLE);
            List<VipBlockContentDto> focusList = new ArrayList<VipBlockContentDto>();

            for (CmsBlockContent blockContent : blockContentList) {
                if (blockContent != null
                        && (StringUtils.contains(blockContent.getPushflag(), DataConstants.MOBILE_COPYRIGHT) || StringUtils
                                .contains(blockContent.getPushflag(), DataConstants.ANDROID_COPYRIGHT))
                        && StringUtils.isNotBlank(blockContent.getMobilePic())) {

                    VipBlockContentDto blockDto = new VipBlockContentDto();
                    blockDto.setMobilePic(blockContent.getMobilePic());
                    blockDto.setSkipUrl(blockContent.getSkipUrl());

                    focusList.add(blockDto);
                }
            }
            checkoutFocusDto.setFocusList(focusList);
        }

        return checkoutFocusDto;
    }

    /**
     * 将天数转换为月数，按每月conversionFactor天计算，并四舍五入
     * @param day
     * @param conversionFactor
     * @return
     *         conversionFactor为0时返回0
     */
    private int parseDayToMonth(int days, int daysOfPerMonth) {
        if (daysOfPerMonth == 0) {
            return 0;
        }
        BigDecimal day = new BigDecimal(String.valueOf(days));
        BigDecimal divisor = new BigDecimal(String.valueOf(daysOfPerMonth));
        BigDecimal month = day.divide(divisor, 0, BigDecimal.ROUND_HALF_UP);

        return month.intValue();
    }

    /**
     * 判断直播券是否已过期
     * @return
     */
    private boolean isLiveTicketExpried(LiveTicketInfoResponse liveTicket) {
        if (StringUtils.isNotBlank(liveTicket.getSession_end_time())) {
            try {
                Calendar calendar = CalendarUtil.parseCalendar(liveTicket.getSession_end_time(),
                        CalendarUtil.SIMPLE_DATE_FORMAT);

                // 大于系统当前时间才能算未过期
                return System.currentTimeMillis() >= calendar.getTimeInMillis();
            } catch (Exception e) {
                this.log.info(
                        "isLiveTicketExpried_" + liveTicket.getPlayName() + "_" + liveTicket.getSession_end_time()
                                + " parse error:", e);
                // 报错则认为已过期失效
                return true;
            }
        }

        // 有效期为空，认为是过期
        return true;
    }
    
    
    /**
     * 维护用户续约信息 恢复或者暂停预约
     * 
     * @param autoPayAutoRenewalResquest
     * @param locale
     * @return
     */
    public Response<ValueDto<Boolean>> autoPaySwitch(String userid, Integer act, Locale locale) {
        String type = "";
        if (act == 1) {
            type = VipConstants.AUTO_PAY_OPEN;
        } else if (act == 0) {
            type = VipConstants.AUTO_PAY_CLOSE;
        }

        AutoPaySwitchResquest request = new AutoPaySwitchResquest(userid, type);
        String errorCode = null;
        String logPrefix = "userid_" + request.getUserid();
        int validCode = request.assertValid();

        Response<ValueDto<Boolean>> response = new Response<ValueDto<Boolean>>();
        if (VipMsgCodeConstant.AUTO_PAY_REQUEST_USERID_EMPTY == validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    VipUtil.parseErrorMsgCode(VipConstants.ONEKEY_PAY_SWITCH_STATUS, validCode), locale);
        } else if (VipMsgCodeConstant.AUTO_PAY_AUTORENEW_TYPE_EMPTY == validCode) {
            errorCode = ErrorCodeConstants.PAY_ILLEGAL_PARAMETER;
            this.log.error(logPrefix + "[errorCode=" + errorCode + "]: illegal parameters.");
            this.setErrorResponse(response, errorCode,
                    VipUtil.parseErrorMsgCode(VipConstants.ONEKEY_PAY_SWITCH_STATUS, validCode), locale);
        } else {
            AutoPaySwitchResponse tpResponse = this.facadeTpDao.getVipTpDao().autoPaySwitch(request,
                    AutoPaySwitchResponse.class);

            if (tpResponse != null) {
                if (VipConstants.AUTO_PAY_OPEN.equals(tpResponse.getStatus())) {
                    response.setData(new ValueDto<Boolean>(true));
                    response.setErrorMessage("恢复成功！");
                } else if (VipConstants.AUTO_PAY_CLOSE.equals(tpResponse.getStatus())) {
                    response.setData(new ValueDto<Boolean>(true));
                    response.setErrorMessage("暂停成功！");
                }
            } else {
                errorCode = VipConstants.AUTO_PAY_SWITCH_INFO_FAILURE;
                this.log.error(logPrefix + "[errorCode=" + errorCode + "]: get auto pay switch info error.");
                this.setErrorResponse(response, errorCode,
                        errorCode + VipUtil.parseErrorMsgCode(VipConstants.ONEKEY_PAY_SWITCH_STATUS, validCode),
                        locale);
                }
        }

        return response;
    }
}
