package com.ymx.driver.config;

/**
 * Created by xuweihua
 * 2020/4/6
 */
public class MessageEvent {
    public int type;
    public Object src;

    public MessageEvent() {
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(int type, Object src) {
        this.type = type;
        this.src = src;
    }

    public static final int MSG_LOCATION_SUCCESS = 0;
    public static final int MSG_PASSENGER_ORDER = 1;
    public static final int MSG_PASSENGER_CANCEL_ORDER = 2;
    public static final int MSG_GOTO_ORDER_ACTIVITY = 3;
    public static final int MSG_PASSENGER_GOOD_FRIEND_ORDER = 4;
    public static final int MSG_GRAB_GOOD_FIREND_ORDER = 5;
    public static final int MSG_GRAB_GOOD_FIREND_ORDER_SUCCESS = 6;
    public static final int MSG_ORDER_SERVICE_COMPLETED = 7;
    public static final int MSG_PHONE_ORDER_PAY_SUCCESS = 8;
    public static final int MSG_WIDLLET_CODE = 9;
    public static final int MSG_REFRESH_MSG_READ_STATUS = 10;
    public static final int MSG_TO_MAIN = 11;
    public static final int MSG_TRIP_REFRESH_DATA_CODE = 12;
    public static final int MSG_MYMSG_REFRESH_DATA_CODE = 13;
    public static final int MSG_MYMSG_DAY_INCOME_DETAILS_CODE = 14;
    public static final int MSG_NET_CHANGE_CODE = 15;
    public static final int MSG_GO_TODAY_ORDER_CODE = 16;
    public static final int MSG_TRAVEL_ORDERS_FINSH_DATA_CODE = 17;
    public static final int MSG_HOME_FRAGMENT_DATA_CODE = 19;
    public static final int MSG_WITHDRAWAL_DATA_CODE = 20;
    public static final int MSG_PAYSECCESS_CODE = 21;
    public static final int MSG_SELECT_RANGGE_DRIVING_TIME_CODE = 22;
    //  远程用车时间弹窗确认关闭
    public static final int MSG_SELECT_RANGGE_DRIVING_CONFIRM_CODE = 24;

    //  远程选择时间天数
    public static final int MSG_SELECT_RANGGE_DRIVING_DAY_CONFIRM_CODE = 25;

    public static final int MSG_RANGGE_DRIVING_NEW_ORDER = 26;

    public static final int MSG_QUERY_PAY_RESULT = 27;

    public static final int MSG_QUERY_SHOW_PAY_DIALOG = 28;

    public static final int MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS = 29;

    public static final int MSG_QUERY_LONG_DRIVIER_FINISH_DETAILS_PAY = 30;


    public static final int MSG_QUERY_LONG_DRIVIER_SYSTEM_CALCAL_ORDER = 31;


    public static final int MSG_GO_TO_LONG_DRVING = 32;
    // 后台更改某些消息
    public static final int MSG_UPDATE_ORDER_INFO = 33;
    // 远程出车时间过期
    public static final int MSG_CODE_TIME_EXPIRED = 34;
    // 刷新消息
    public static final int MSG_HOME_MESSAGE_DATA_CODE = 35;
    // 按钮变更消息
    public static final int MSG_NAVI_UPDATE_BUTTON_CODE = 36;

    // 行程中的价格
    public static final int MSG_CODE_UPDATE_ORDER_PRICE_CODE = 37;

    //
    public static final int MSG_GOTO_SCAN_CODE = 38;


    // 流转订单司机编码
    public static final int MSG_DRIVER_ID_CODE = 39;
    // 流转订单拒绝
    public static final int MSG_ORDER_CIRCULATION_FAILE_NOTICE = 40;
    // 流转订单确认
    public static final int MSG_ORDER_CIRCULATION_SUCCESS_NOTICE = 41;
    // 流转订单确认
    public static final int MSG_ORDER_CIRCULATION_NOTICE_QCODE_CODE = 42;

    public static final int MSG_ORDER_CIRCULATION_NOTICE_QCODE_TO_DETAILS_CODE = 43;

    public static final int MSG_ORDER_LONG_RANGE_COMFIRM_PRICE_CODE = 44;

    public static final int MSG_ORDER_LONG_RANGE_CANCAL_PRICE_CODE = 45;
    //乘客已确认/拒绝加价金额
    public static final int MSG_ORDER_LONG_RANGE_CUSTOMER_TRIPS_CODE = 46;
    //恢复远程订单
    public static final int MSG_ORDER_LONG_RANGE_RECOVER_ORDER_DETAILSCODE = 47;

    public static final int MSG_GOTO_CHARTERCAR_CODE = 48;
    //包车订单选择时间
    public static final int MSG_GOTO_CHARTTER_SELECT_TIME_CODE = 49;
    //包车新订单匹配
    public static final int MSG_GOTO_CHARTTER_NEW_ORDER_CODE = 50;

    public static final int MSG_GOTO_CHARTTER_CANCAL_ORDER_CODE = 51;
    // 包车费用超时
    public static final int MSG_CHARTTER_ORDER_TIME_OUT_FEE_CODE = 52;

    public static final int MSG_DRIVER_LOCK_CODE = 54;

    public static final int MSG_CHARTTER_ORDER_STATUS_UPDATE_CODE = 55;

    public static final int MSG_CHARTTER_TODAY_INCOME_CODE = 56;

    public static final int MSG_SHOW_CONFIRM_EXCHANGE_DIALOG_CODE = 57;

    public static final int MSG_SHOW_CONFIRM_EXCHANGE_SUCCESS_CODE = 58;
    // 改派成功
    public static final int MSG_TRANSFER_ORDER_SUCCESS_CODE = 59;

    public static final int MSG_INTEGRAL_BACK_CODE = 60;

    public static final int MSG_NEW_ORDER_CODE = 61;
    // 接站类型
    public static final int MSG_SELECT_TYPE_CODE = 62;
    // 接站接单界面
    public static final int MSG_GO_TO_TRANSFER_SIDE_ORDER_CODE = 63;
    // 接站接单抢单
    public static final int MSG_GO_TO_TRANSFER_GRAP_ORDER_CODE = 64;
    // 接站加入行程
    public static final int MSG_GO_TO_TRANSFER_JOIN_ORDER_CODE = 65;
    // 时间失效
    public static final int MSG_GO_TO_TRANSFER_TIME_INVALID_CODE = 66;
    // 接送站失效
    public static final int MSG_TRANSFER_ORDER_CANCAL_CODE = 67;
    // 接送站详情
    public static final int MSG_GOTO_TRANSFER_ORDER_DETAILS_CODE = 68;
    // 接送站详情打電話
    public static final int MSG_GOTO_TRANSFER_ORDER_DETAILS_PASSENGER_PHONE_CODE = 68;
    // 接送站详情取消訂單
    public static final int MSG_GOTO_TRANSFER_ORDER_DETAILS_CANCAL_ORDER_CODE = 69;

    public static final int MSG_MINE_GOTO_TRANSFER_ORDER_CODE = 70;
    // 接送站详情时间处理
     public static final int MSG_MINE_TRANSFER_ORDER_SELECT_TIME_CODE = 71;
    // 接送站司机选择更改时间处理
    public static final int MSG_MINE_TRANSFER_ORDER_UPDATE_TIME_CODE = 72;
    // 接送站司机导航处理
    public static final int MSG_MINE_TRANSFER_ORDER_NAVI_CODE = 73;
    // 接送站详情更改乘客订单状态
    public static final int MSG_MINE_TRANSFER_ORDER_UPDATE_PASSENGER_TYPE_CODE = 75;
    // 弹起付款支付的弹窗
    public static final int MSG_MINE_TRANSFER_ORDER_SHOW_PAY_TYPE_CODE = 76;
    // 弹起付款支付的弹窗
    public static final int MSG_MINE_ORDER_UPDATE_ADDRESSS_TYPE_CODE = 77;
    // 弹起付款支付的弹窗
    public static final int MSG_TRANSFER_NEW_ORDER_CODE = 78;
    // 主订单界面取消订单消失
    public static final int MSG_MAIN_CANCAL_DIALOG_DISS_CODE = 79;

}
