package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class SelectIncomeTimePickEntity extends BaseEntity {

    private String year;
    private List<MonthDate> yearDate;

    public List<MonthDate> getYearDate() {
        return yearDate;
    }

    public void setYearDate(List<MonthDate> yearDate) {
        this.yearDate = yearDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SelectIncomeTimePickEntity(String year, List<MonthDate> yearDate) {
        this.year = year;
        this.yearDate = yearDate;
    }


    public static class MonthDate extends BaseEntity {
        private String month;
        private List<DayDate> monthDate;

        public List<DayDate> getMonthDate() {
            return monthDate;
        }

        public void setMonthDate(List<DayDate> monthDate) {
            this.monthDate = monthDate;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }


        public MonthDate(String month, List<DayDate> monthDate) {
            this.month = month;
            this.monthDate = monthDate;
        }


    }

    public static class DayDate extends BaseEntity {
        private String day;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public DayDate(String day) {
            this.day = day;
        }


    }
}
