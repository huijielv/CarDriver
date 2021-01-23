package com.ymx.driver.config;

/**
 * Created by wuwei
 * 2019/12/24
 * 佛祖保佑       永无BUG
 */
public class MqttConfig {
    //
//    public static final String MQTT_DEBUG_URL = "tcp://admin.xysc16.com";

    public static final String MQTT_DEBUG_URL = "tcp://39.101.196.81:1883";

    public static final String MQTT_RELEASE_URL = "tcp://scapp.xysc16.com";
    public static final String MQTT_TOPIC_USER_EVENT = "driver/event/d_";
    public static final String MQTT_TOPIC_SIDE_ID_LIST = "common/transferStation/";
    public static final String MQTT_TOPIC_GPS_UPLOAD = "common/uploadGps/";
    public static final String MQTT_CODE_PASSENGER_ORDER = "2000";    //乘客下单后，通知到司机
    public static final String MQTT_CODE_PASSENGER_CANCEL_ORDER = "2001";
    public static final String MQTT_CODE_PASSENGER_GOOD_FRIEND_ORDER = "2002";
    public static final String MQTT_CODE_PHONE_ORDER_PAY_SUCCESS = "2003";
    public static final String MQTT_CODE_ORDER_PRICE = "2004";
    //司机改派成功
    public static final String MQTT_CODE_TRANSFER_ORDER_SUCCESS = "2006";
    //司机远程订单
    public static final String MQTT_CODE_LONG_DRIVIER_NEW_ORDER = "2010";
    //司机取消远程订单
    public static final String MQTT_CODE_LONG_DRIVIER_SYSTEM_CANCAL_ORDER = "2011";
    //远程订单，乘客支付成功，通知到司机
    public static final String MQTT_CODE_LONG_DRIVIER_PAY_SUCCESS = "2012";
    //司机出车司机过期前5分钟，通知司机是否延长
    public static final String MQTT_CODE_EXTENSION_TIME = "2013";

    //远程司机出车时间过期提示
    public static final String MQTT_CODE_TIME_EXPIRED = "2014";
    // 更改司机是否时远程身份
    public static final String MQTT_CODE_UPDATE_DRIVER_TYPE = "2015";
    // 后台更改某些信息
    public static final String MQTT_CODE_UPDATE_ORDER_INFO_TYPE = "2016";
    //  乘客已确认/拒绝加价金额
    public static final String MQTT_CODE_ORDER_CUSTOMER_TRIPS_TYPE = "2017";
    // 订单流转
    public static final String MQTT_CODE_ORDER_CIRCULATION_TYPE = "2018";
    // 订单流转成功
    public static final String MQTT_CODE_ORDER_CIRCULATION_FAILE_TYPE = "2019";
    // 二维码扫描发过来的通知
    public static final String MQTT_CODE_ORDER_CIRCULATION_QCODE_TYPE = "2020";
    // 订单流转成功
    public static final String MQTT_CODE_ORDER_CIRCULATION_SUCCESS_TYPE = "2021";
    // 包车订单后台匹配成功，通知司机
    public static final String MQTT_CODE_CHARTTER_NEW_ORDER_TYPE = "2031";
    // 包车订单后台匹配成功，通知司机
    public static final String MQTT_CODE_CHARTTER_CANCAL_ORDER_TYPE = "2032";
    // 后台锁定登录，锁定派单，司机空闲中需要通知到司机
    public static final String MQTT_CODE_DRIVER_LOCK_TYPE = "2033";
    // 包车订单费用通知司机
    public static final String MQTT_CODE_CHARTTER_FEE_ORDER_TYPE = "2034";
    // 包车跨天行程中订单变化通知司机
    public static final String MQTT_CODE_CHARTTER_ORDER__UPDATE_TYPE = "2035";
    //时间失效
    public static final String MQTT_CODE_TRANSFER_ORDER_TIME_INVALID_TYPE = "2040";
    //乘客取消订单
    public static final String MQTT_CODE_TRANSFER_ORDER_CANCAL_TYPE = "2042";
    //乘客同意司机修改接驾时间，通知司机
    public static final String MQTT_CODE_TRANSFER_ORDER_UPDATE_TIME_TYPE = "2041";
    //新订单通知
    public static final String MQTT_CODE_ORDER_UPDATE_ADDRESS_TYPE = "2007";
    //接送站新订单通知
    public static final String MQTT_CODE_TRANSFER_NEW_ORDER_TYPE = "2043";
    // 网约车、出租车拼车取消订单
    public static final String MQTT_CODE_CAR_POOP_CANCAL_ORDER_TYPE = "2050";

    public static final String MQTT_CODE_CAR_POOL_NEW_ORDER_TYPE = "2008";

    public static final String MQTT_CODE_RECOVER_ORDER_TYPE = "2051";

}
