package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;


import java.util.List;

/**
 * Created by wuwei
 * 2020/4/23
 * 佛祖保佑       永无BUG
 */
public class TimePickEntity extends BaseEntity {
    private String date;
    private List<HourDate> hourDate;

    public TimePickEntity(String date, List<HourDate> hourDate) {
        this.date = date;
        this.hourDate = hourDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<HourDate> getHourDate() {
        return hourDate;
    }

    public void setHourDate(List<HourDate> hourDate) {
        this.hourDate = hourDate;
    }

    public static class HourDate extends BaseEntity {
        private String hour;
        private List<MinuteDate> minuteDate;

        public HourDate(String hour, List<MinuteDate> minuteDate) {
            this.hour = hour;
            this.minuteDate = minuteDate;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public List<MinuteDate> getMinuteDate() {
            return minuteDate;
        }

        public void setMinuteDate(List<MinuteDate> minuteDate) {
            this.minuteDate = minuteDate;
        }
    }

    public static class MinuteDate extends BaseEntity {
        private String minute;

        public MinuteDate(String minute) {
            this.minute = minute;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }
    }
}
