package com.ymx.driver.util;

public class OrderStatusUtils {

    /**
     * 0-待确认
     */

    public static final int DRIVER_ORDER_N0_CONFIRN = 0;
    /**
     * 1-行程待开始
     */
    public static final int DRIVER_STATE_TO_START = 1;
    /**
     * 2-去接乘客
     */
    public static final int DRIVER_STATE_TO_PASSENGERS = 2;
    /**
     * 3-准备出发
     */
    public static final int DRIVER_STATE_READY_TO_GO = 3;
    /**
     * 4-行程中
     */
    public static final int DRIVER_STATE_ROADING = 4;
    /**
     * 5-确认费用
     */
    public static final int DRIVER_STATE_CONFIRM_COST = 5;
    /**
     * 6-待付款
     */
    public static final int DRIVER_STATE_TO_PAY = 6;

    /**
     * 7 已完成
     */
    public static final int DRIVER_ORDER_FINISH = 7;

    /**
     * 8 已关闭
     */
    public static final int DRIVER_ORDER_CLOSED = 8;

}
