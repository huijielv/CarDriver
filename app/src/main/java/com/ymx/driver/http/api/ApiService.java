package com.ymx.driver.http.api;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetails;
import com.ymx.driver.entity.RangDrivingCancelOrder;
import com.ymx.driver.entity.SystemNoticeEntity;
import com.ymx.driver.entity.TodayOrderEntity;
import com.ymx.driver.entity.WalletInfoEntity;
import com.ymx.driver.entity.app.AcceptOrderCirculationBodyEntity;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.CarStateEntity;
import com.ymx.driver.entity.app.CharterOrderDetailsEntity;
import com.ymx.driver.entity.app.ConfirmExchangeCommodityEntity;
import com.ymx.driver.entity.app.ConfirmOrderEntity;
import com.ymx.driver.entity.app.ConfirmWithdrawalEntity;
import com.ymx.driver.entity.app.DayIncomeDetailEntity;
import com.ymx.driver.entity.app.DriverLockEntity;
import com.ymx.driver.entity.app.DriverLogoutEntity;
import com.ymx.driver.entity.app.DriverToeknEntity;
import com.ymx.driver.entity.app.FindCarpoolingSiteResultEntity;
import com.ymx.driver.entity.app.GetCharterSystemTime;
import com.ymx.driver.entity.app.GetCharteredInfoEntity;
import com.ymx.driver.entity.app.HeadDetailInfoEntity;
import com.ymx.driver.entity.app.IncomeDetailEntity;
import com.ymx.driver.entity.app.IntegralDetailModel;
import com.ymx.driver.entity.app.InterralCommodityEntity;
import com.ymx.driver.entity.app.MonthIncomeDetailEntity;
import com.ymx.driver.entity.app.MsgEntity;
import com.ymx.driver.entity.app.MsgInfoEntity;
import com.ymx.driver.entity.app.MyIntegralOrderListItemEntity;
import com.ymx.driver.entity.app.OrderCirculationEntity;
import com.ymx.driver.entity.app.OrderCirculationNoticeEntity;
import com.ymx.driver.entity.app.OrderDetailsEntity;
import com.ymx.driver.entity.app.QcodeEntity;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.SmsCodeEntity;
import com.ymx.driver.entity.app.TransferCancalOrderEntity;
import com.ymx.driver.entity.app.TransferFindSiteOrderListEntity;
import com.ymx.driver.entity.app.TransferOrderResultEntity;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.entity.app.TransferStationRecoverEntity;
import com.ymx.driver.entity.app.TransferStationTripOrderListItem;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.entity.app.UpdateCharterOrderBody;
import com.ymx.driver.entity.app.UpdateCharteredInfoEntity;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.entity.app.UpdateFerryRemoteInfoEntity;
import com.ymx.driver.entity.app.UpdateGpsEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.UpdateRemoteInfoBodyEntity;
import com.ymx.driver.entity.app.UpdateTransferStationActionEntity;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.entity.app.WechatPayEntity;
import com.ymx.driver.entity.app.WithdrawDetailEntity;
import com.ymx.driver.entity.app.WithdrawInfoEntity;
import com.ymx.driver.entity.app.WithdrawListModel;
import com.ymx.driver.entity.app.mqtt.GpsInfoEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.entity.app.postbody.BodyLoginEntity;
import com.ymx.driver.entity.http.HttpResult;
import com.ymx.driver.http.UpdateLongDrivingOrderActionBodyEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xuweihua
 * 2020/4/26
 */
public interface ApiService {
    /**
     * 用户登录
     *
     * @param entity
     * @return
     */
    @POST("v1/driver/user/login")
    Observable<HttpResult<UserEntity>> login(@Body BodyLoginEntity entity);

    /**
     * 获取司机消息列表
     *
     * @return
     */
    @POST("v1/driver/message/getAllMessageType")
    Observable<HttpResult<List<MsgEntity>>> getMsgList();

    /**
     * 获取消息详情列表
     *
     * @return
     */
    @POST("v1/driver/message/getAllMessageByMessageTypeId")
    Observable<HttpResult<List<MsgInfoEntity>>> getMsgDetailList(@Query("messageTypeId") int messageTypeId,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("titleSize") int titleSize);

    /**
     * 设为已读
     *
     * @param messageTypeId
     * @return
     */
    @POST("v1/driver/message/updateMessageReadState")
    Observable<HttpResult<BaseEntity>> setMsgRead(@Query("messageTypeId") int messageTypeId);

    /**
     * 司机/开始去接乘客/到达上车点/开始行程/行程结束
     *
     * @param
     * @return
     */
    @POST("v1/carOrder/driver/action")
    Observable<HttpResult<UpdateOrderStatusEntity>> updateOrderDetailsStatus(@Query("orderNo") String orderNo, @Query("currentCoord") String currentCoord, @Query("actionType") String actionType, @Query("otherCharge") String otherCharge);

    @POST("v1/carOrder/driver/cancelOrder")
    Observable<HttpResult<CancelOrderEntity>> cancelOrder(@Query("orderNo") String orderNo);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    @GET("v1/driver/center/orderDetail")
    Observable<HttpResult<OrderDetailsEntity>> getOrderDetails(@Query("orderNo") String orderNo);


    @GET("v1/driver/center/itineraryOrder")
    Observable<HttpResult<List<TripOrderList>>> getTripOrderList(@Query("orderState") String orderState, @Query("pageSize") int pageSize, @Query("currentPage") int currentPage);

    @POST("v1/carOrder/driver/recoverOrder")
    Observable<HttpResult<PassengerInfoEntity>> recoverOrderDetails(@Query("orderNo") String orderNo);

    /**
     * 司机确认接单
     *
     * @param orderNo
     * @return
     */
    @POST("v1/carOrder/driver/confirmOrder")
    Observable<HttpResult<ConfirmOrderEntity>> confirmOrder(@Query("orderNo") String orderNo, @Query("sendType") int sendType);

    /**
     * 司机首页
     */
    @GET("v1/carOrder/driver/getHeadDetailInfo")
    Observable<HttpResult<HeadDetailInfoEntity>> getHeadDetailInfo();

    /**
     * 司机收车
     */
    @POST("v1/driver/user/stopWork")
    Observable<HttpResult<CarStateEntity>> stopWork();

    /**
     * 司机收车
     */
    @POST("v1/driver/user/startWork")
    Observable<HttpResult<CarStateEntity>> startWork();

    /**
     * 我的钱包接口
     */

    @GET("v1/driver/center/walletInfo")
    Observable<HttpResult<WalletInfoEntity>> getWalletInfo();


    @POST("v1/wx/app/order/pay")
    Observable<HttpResult<String>> orderPay(@Query("wxPayType") int wxPayType, @Query("orderNo") String orderNo, @Query("clientType") int clientType);

    /**
     * 版本升级
     *
     * @param bundleId
     * @return
     */
    @GET("v1/driver/user/version")
    Observable<HttpResult<UpdateEntity>> update(@Query("bundleId") String bundleId);


    @GET("v1/driver/center/incomeDetail")
    Observable<HttpResult<List<IncomeDetailEntity>>> getIncomeDetailList(@Query("tradeType") String tradeType, @Query("pageSize") int pageSize, @Query("currentPage") int currentPage);


    /**
     * 查询提现余额、银行卡、手续费等信息
     */
    @GET("v1/driver/center/withdrawInfo")
    Observable<HttpResult<WithdrawInfoEntity>> getWithdrawInfo();


    @POST("v1/driver/center/confirmWithdrawal")
    Observable<HttpResult<ConfirmWithdrawalEntity>> confirmWithdrawal(@Query("amount") String amount);

    /**
     * 提现列表
     */
    @GET("v1/driver/center/withdrawList")
    Observable<HttpResult<List<WithdrawListModel>>> getWithdrawList(@Query("pageSize") int pageSize, @Query("currentPage") int currentPage);

    /**
     * 提现列表
     */
    @GET("v1/driver/center/withdrawDetail")
    Observable<HttpResult<WithdrawDetailEntity>> getWithdrawDetail(@Query("id") int id);

    /**
     * 司机退出
     */
    @POST("v1/driver/user/logout")
    Observable<HttpResult<DriverLogoutEntity>> driverLogout();

    /**
     * 司机二维码接口（用户扫描 ）
     */
    @POST("v1/driver/user/qrcode")
    Observable<HttpResult<QcodeEntity>> driverQrcode();

    /**
     * 发送短信
     */
    @GET("v1/passenger/user/getSmsCode")
    Observable<HttpResult<SmsCodeEntity>> getSmsCode(@Query("userPhone") String userPhone, @Query("smsType") int smsType);


    /**
     *
     */
    @POST("v1/driver/user/resetPassword")
    Observable<HttpResult<SmsCodeEntity>> resetPassWord(@Query("mobile") String mobile, @Query("password") String password, @Query("authCode") String authCode);


    /**
     * 月收入明细接口
     */
    @GET("v1/driver/center/monthIncomeDetail")
    Observable<HttpResult<MonthIncomeDetailEntity>> getMonthIncomeDetail(@Query("year") String year, @Query("month") String month ,@Query("incomeType") int    incomeType);


    /**
     * 日收入明细接口
     */
    @GET("v1/driver/center/dayIncomeDetail")
    Observable<HttpResult<DayIncomeDetailEntity>> getDayIncomeDetail(@Query("dayTime") String dayTime, @Query("pageSize") int pageSize, @Query("currentPage") int currentPage ,@Query("incomeType") int incomeType);


    /**
     * 上传推送注册TOKEN
     */
    @POST("v1/driver/deviceToken")
    Observable<HttpResult<DriverToeknEntity>> loadToken(@Query("deviceToken") String deviceToken);


    /**
     * 获取系统公告
     *
     * @return
     */
    @GET("v1/driver/user/notice")
    Observable<HttpResult<List<SystemNoticeEntity>>> getSystemNoticeList();


    /**
     * @return
     */
    @GET("v1/driver/center/orderToday")
    Observable<HttpResult<List<TodayOrderEntity>>> getOrderTodayList(@Query("pageSize") int pageSize, @Query("currentPage") int currentPage);


    /**
     * 上传经纬度处理
     */
    @POST("v1/driver/user/updateGps")
    Observable<HttpResult<UpdateGpsEntity>> updateGps(@Body GpsInfoEntity entity);


    /**
     * 查询远程司机出车线路时间信息
     */
    @POST("v1/driver/user/getRemoteInfo")
    Observable<HttpResult<RemoteInfoEntity>> getRemoteInfo();

    /**
     * 更改远程司机出车线路时间信息
     */
    @POST("v1/driver/user/updateRemoteInfo")
    Observable<HttpResult<RemoteInfoEntity>> updateRemoteInfo(@Body UpdateRemoteInfoBodyEntity updateRemoteInfoBodyEntity);


    /**
     * 远程司机出车取消订单
     */
    @POST("v2/driver/remoteOrder/cancelOrder")
    Observable<HttpResult<RangDrivingCancelOrder>> rangeDrivingCancalOrder(@Query("orderNo") String orderNo);


    /**
     * 远程订单司机/开始行程/结束行程
     */
    @POST("v2/driver/remoteOrder/action")
    Observable<HttpResult<PassengerInfoEntity>> rangeDrivingRemoteOrderAction(@Body UpdateLongDrivingOrderActionBodyEntity updateLongDrivingOrderActionBodyEntity);


    /**
     * 远程订单详情
     */
    @GET("v2/driver/remoteOrder/orderDetail")
    Observable<HttpResult<RangDriverPassgerFinishOederDetails>> rangeDrivingOrderDetails(@Query("orderNo") String orderNo);


    /**
     * 支付宝-订单支付
     *
     * @param orderNo
     * @return
     */
    @POST("v1/ali/order/pay")
    Observable<HttpResult<String>> getAliPayOrderInfo(@Query("orderNo") String orderNo, @Query("clientType") int clientType);

    /**
     * 微信-订单支付
     *
     * @return
     */
    @POST("v1/wx/app/order/pay")
    Observable<HttpResult<WechatPayEntity>> getWechatPayOrderInfo(@Query("wxPayType") int wxPayType, @Query("orderNo") String orderNo, @Query("clientType") int clientType);


    /**
     * 摆渡远程司机滑动出车（收车）、锁定（解锁）
     *
     * @param lockState
     * @param state
     */

    @POST("v1/driver/user/updateFerryRemoteInfo")
    Observable<HttpResult<UpdateFerryRemoteInfoEntity>> updateFerryRemoteInfo(@Query("state") int state, @Query("lockState") int lockState);


    @POST("v1/driver/user/lockOrUnlock")
    Observable<HttpResult<DriverLockEntity>> lockOrUnlock(@Query("state") int state);

    /**
     * 摆渡远程订单司机发起订单流转
     */
    @POST("v2/driver/remoteOrder/orderCirculation")
    Observable<HttpResult<OrderCirculationEntity>> orderCirculation(@Query("orderNo") String orderNo, @Query("driverNo") String driverNo);

    /**
     * 摆渡远程订单司机拒绝订单流转
     */
    @POST("v2/driver/remoteOrder/refuseOrderCirculation")
    Observable<HttpResult<OrderCirculationEntity>> refuseOrderCirculation(@Query("orderNo") String orderNo);

    /**
     * 摆渡远程订单司机接受订单流转
     */
    @POST("v2/driver/remoteOrder/acceptOrderCirculation")
    Observable<HttpResult<OrderCirculationEntity>> acceptOrderCirculation(@Body AcceptOrderCirculationBodyEntity acceptOrderCirculationBodyEntity);

    /**
     * 摆渡远程订单司机接受订单流转
     */
    @POST("v2/driver/remoteOrder/orderCirculationQrcode")
    Observable<HttpResult<String>> orderCirculationQrcode(@Query("orderNo") String orderNo);


    /**
     * 摆渡远程订单司机接受订单流转
     */
    @POST("v2/driver/remoteOrder/scanCode")
    Observable<HttpResult<OrderCirculationNoticeEntity>> scanCode(@Query("orderNo") String orderNo);

    /**
     * 司机端加价确认
     */
    @POST("v2/driver/remoteOrder/confirmationPrice")
    Observable<HttpResult<PassengerInfoEntity>> confirmationPrice(@Query("orderNo") String orderNo, @Query("price") String price);

    /**
     * 司机端加价确认
     */
    @POST("v2/driver/remoteOrder/cancelPrice")
    Observable<HttpResult<PassengerInfoEntity>> cancelPrice(@Query("orderNo") String orderNo);


    /**
     * 查询包车司机出车城市信息
     */
    @POST("v1/driver/user/getCharteredInfo")
    Observable<HttpResult<GetCharteredInfoEntity>> getCharteredInfo();


    /**
     * 查询包车司机出车城市信息
     */
    @POST("v1/driver/user/updateCharteredInfo")
    Observable<HttpResult<UpdateCharteredInfoEntity>> updateCharteredInfo(@Query("state") int state);

    /**
     * 恢复包车详情
     */
    @POST("v2/driver/charteredOrder/recoverOrder")
    Observable<HttpResult<CharterOrderDetailsEntity>> recoverCharteredOrderDetails(@Query("orderNo") String orderNo);

    /**
     * 查询包车详情
     */
    @POST("v2/driver/charteredOrder/action")
    Observable<HttpResult<CharterOrderDetailsEntity>> updateCharterOrderStatus(@Body UpdateCharterOrderBody entity);


    /**
     * 查询包车详情
     */
    @POST("v2/driver/charteredOrder/orderDetail")
    Observable<HttpResult<CharterOrderDetailsEntity>> charteredOrderDetails(@Query("orderNo") String orderNo);


    /**
     * 查询包车详情
     */
    @POST("v2/driver/charteredOrder/findTimeOutFee")
    Observable<HttpResult<CharterOrderDetailsEntity>> findTimeOutFee(@Query("orderNo") String orderNo);

    /**
     * 查询包车详情
     */
    @POST("v2/driver/charteredOrder/getOrderNextStartTime")
    Observable<HttpResult<GetCharterSystemTime>> getCharterSystemTime(@Query("orderNo") String orderNo);


    /**
     * 积分明细
     */
    @POST("v2/driver/integral/findIntegralDetailList")
    Observable<HttpResult<IntegralDetailModel>> getIntegralDetailList(@Query("pageSize") int pageSize, @Query("currentPage") int currentPage);


    /**
     * 积分明细
     */
    @POST("v2/driver/integral/findInterralCommodityList")
    Observable<HttpResult<InterralCommodityEntity>> getInterralCommodityList(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);


    /**
     * 积分确认兑换商品
     */
    @POST("v2/driver/integral/confirmExchangeCommodity")
    Observable<HttpResult<ConfirmExchangeCommodityEntity>> confirmExchangeCommodity(@Query("commodityId") int commodityId);

    /**
     * 积分确认兑换商品
     */
    @POST("v2/driver/integral/findIntegralExchangeList")
    Observable<HttpResult<List<MyIntegralOrderListItemEntity>>> getIntegralExchangeList(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

    /**
     * 网约车转单接口
     */
    @POST("v1/carOrder/driver/transferOrder")
    Observable<HttpResult<TransferOrderResultEntity>> transferOrder(@Query("orderNo") String orderNo);

    /**
     * 网约车转单接口
     */
    @POST("v1/carOrder/driver/cancelTransferOrder")
    Observable<HttpResult<TransferOrderResultEntity>> cancelTransferOrder(@Query("orderNo") String orderNo);

    /**
     * 查询接送站出车站点信息
     */
    @POST("v2/driver/transferStation/findTransferSiteInfo")
    Observable<HttpResult<FindCarpoolingSiteResultEntity>> findCarpoolingSite();

    /**
     * 查询接送站出车站点信息
     */
    @POST("v2/driver/transferStation/updateCarState")
    Observable<HttpResult<FindCarpoolingSiteResultEntity>> updateCarState(@Query("state") int state);

    /**
     * 查询司机行程订单
     */
    @POST("v2/driver/transferStation/findTripOrderList")
    Observable<HttpResult<List<TransferStationTripOrderListItem>>> findTripOrderList(@Query("orderType") int orderType);

    /**
     * 查询司机行程订单
     */
    @POST("v2/driver/transferStation/findSiteOrderList")
    Observable<HttpResult<TransferFindSiteOrderListEntity>> findSiteOrderList(@Query("pageSize") int pageSize, @Query("currentPage") int currentPage, @Query("siteId") int siteId, @Query("orderType") int orderType);


    /**
     * 司机抢单接口
     */
    @POST("v2/driver/transferStation/grabOrder")
    Observable<HttpResult<TransferStationGrabOrder>> transferStationGrabOrder(@Query("orderNo") String orderNo);

    /**
     * 行程订单列表
     */
    @POST("v2/driver/transferStation/findOptionalTripOrder")
    Observable<HttpResult<List<TransferStationTripOrderListItem>>> findOptionalTripOrder(@Query("orderNo") String orderNo);

    /**
     * 可加入的行程订单
     */
    @POST("v2/driver/transferStation/joinTripOrder")
    Observable<HttpResult<TransferStationRecoverEntity>> joinTripOrder(@Query("orderNo") String orderNo, @Query("orderId") String orderId);

    @POST("v2/driver/transferStation/giveUpOrder")
    Observable<HttpResult<BaseEntity>> giveUpOrder(@Query("orderNo") String orderNo);


    /**
     * 接送站详情
     */
    @POST("v2/driver/transferStation/recoverOrder")
    Observable<HttpResult<TransferStationRecoverEntity>> recoverTransferStationOrderDetails(@Query("orderNo") String orderNo);
    /**
     * 接送站司机取消订单
     */
    @POST("v2/driver/transferStation/cancelOrder")
    Observable<HttpResult<TransferCancalOrderEntity>> transferCancalOrder(@Query("orderNo") String orderNo);
    /**
     * 接送站司机修改接送时间
     */
    @POST("v2/driver/transferStation/chooseAfterDrivingTime")
    Observable<HttpResult<BaseEntity>> chooseAfterDrivingTime(@Query("orderNo") String orderNo,@Query("afterDrivingTime") long afterDrivingTime);
    /**
     * 接送站司机更改状态
     */
    @POST("v2/driver/transferStation/action")
    Observable<HttpResult<TransferStationRecoverEntity>> updateTransferStationAction(@Body UpdateTransferStationActionEntity updateTransferStationActionEntity);
    /**
     * 接送站子订单修改状态
     */
    @POST("v2/driver/transferStation/arrivePlace")
    Observable<HttpResult<TransferStationRecoverEntity>> transferArrivePlace(@Body UpdateTransferStationActionEntity updateTransferStationActionEntity);
    /**
     * 网约车订单修改状态
     */
    @POST("v1/carOrder/driver/updateStartAddress")
    Observable<HttpResult<BaseEntity>> updateStartAddress(@Query("orderNo") String orderNo ,@Query("state") int state);


    /**
     * 网约车订单完成订单
     */
    @POST("v2/driver/transferStation/orderDetail")
    Observable<HttpResult<RangDriverPassgerFinishOederDetails>> transferStationOrderDetails(@Query("orderNo") String orderNo );

}
