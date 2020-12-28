package com.ymx.driver.util;

import android.text.TextUtils;

import com.ymx.driver.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by wuwei
 * 2018/6/15
 * 佛祖保佑       永无BUG
 */
public class DateUtils {

    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;

    public static final String normal_formats = "yyyy-MM-dd HH:mm:ss";
    public static final String chat_msg_top_bar_formats = "MM-dd HH:mm";

    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }




        /**
         * 取得当前时间戳（精确到秒）
         *
         * @return nowTimeStamp
         */
        public static String getNowTimeStamp () {
            long time = System.currentTimeMillis();
            String nowTimeStamp = String.valueOf(time / 1000);
            return nowTimeStamp;
        }

        public static String longToDate ( long longTime){

            Date date = new Date(longTime);

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return sd.format(date);
        }


        public static String getAlia ( long date){
            //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
            int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
            long today = (System.currentTimeMillis() + offSet) / 86400000;
            long start = (date + offSet) / 86400000;
            long intervalTime = start - today;
            //-2:前天,-1：昨天,0：今天,1：明天,2：后天
            String strDes = "";
            if (intervalTime == 0) {
                strDes = UIUtils.getString(R.string.today);
            } else if (intervalTime == 1) {
                strDes = UIUtils.getString(R.string.tomorrow);
            } else if (intervalTime == 2) {
                strDes = UIUtils.getString(R.string.the_day_after_tomorrow);
            } else {
                String s = DateUtils.timeStamp2Date(String.valueOf(date / 1000), "yyyy-MM-dd HH:mm:ss").replaceAll(" ", "");
                strDes = s.substring(5, 10);
            }
            return strDes;
        }

    public static String getDayString(Date startDate) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (startDate.getTime() + offSet) / 86400000;

        if (start - today == 0) {
            return "今天";
        } else if (start - today == 1) {
            return "明天";
        } else if (start - today == 2) {
            return "后天";
        } else {
            return "";
        }

    }


        public static String getTimeShow ( long milliseconds){
            String dataString;
            Date currentTime = new Date(milliseconds * 1000);
            Calendar todayStart = Calendar.getInstance();
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            Date todaybegin = todayStart.getTime();
            Date tomorrowbegin = new Date(todaybegin.getTime() + 3600 * 24 * 1000);
            Date afterTomorrow = new Date(tomorrowbegin.getTime() + 3600 * 24 * 1000);

            if (!currentTime.before(todaybegin)) {
                dataString = UIUtils.getString(R.string.today);
            } else if (!currentTime.before(tomorrowbegin)) {
                dataString = UIUtils.getString(R.string.tomorrow);
            } else if (!currentTime.before(afterTomorrow)) {
                dataString = UIUtils.getString(R.string.the_day_after_tomorrow);
            } else {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM月dd日", Locale.CHINA);
                dataString = dateformatter.format(currentTime);
            }
            return dataString;
        }


        /**
         * 获取当前日期几月几号
         */
        public static String getDateString () {

            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            return mMonth + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.month) : "/")
                    + mDay + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.day) : "");
        }

        /**
         * 获取当前年月日
         *
         * @return
         */
        public static String StringData () {

            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            return mYear + "-" + mMonth + "-" + mDay;
        }

        public static String getDate () {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            return simpleDateFormat.format(date);
        }

        public static String getYear () {
            Calendar c = Calendar.getInstance();
            return String.valueOf(c.get(Calendar.YEAR));
        }

        /**
         * 获取当前是周几
         */
        public static String getWeekString () {
            final Calendar c = Calendar.getInstance();
            mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            if ("1".equals(mWay)) {
                mWay = UIUtils.getString(R.string.sunday);
            } else if ("2".equals(mWay)) {
                mWay = UIUtils.getString(R.string.monday);
            } else if ("3".equals(mWay)) {
                mWay = UIUtils.getString(R.string.tuesday);
            } else if ("4".equals(mWay)) {
                mWay = UIUtils.getString(R.string.wednesday);
            } else if ("5".equals(mWay)) {
                mWay = UIUtils.getString(R.string.thursday);
            } else if ("6".equals(mWay)) {
                mWay = UIUtils.getString(R.string.friday);
            } else if ("7".equals(mWay)) {
                mWay = UIUtils.getString(R.string.saturday);
            }
            return mDay;
        }

        /**
         * 根据当前日期获得是星期几
         *
         * @return
         */
        public static String getWeek (String time){
            String Week = "";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {

                c.setTime(format.parse(time));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                Week += UIUtils.getString(R.string.sunday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 2) {
                Week += UIUtils.getString(R.string.monday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 3) {
                Week += UIUtils.getString(R.string.tuesday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 4) {
                Week += UIUtils.getString(R.string.wednesday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 5) {
                Week += UIUtils.getString(R.string.thursday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 6) {
                Week += UIUtils.getString(R.string.friday);
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 7) {
                Week += UIUtils.getString(R.string.saturday);
            }
            return Week;
        }

        /**
         * 获取今天往后一周的日期（年-月-日）
         */
        public static List<String> get7date () {
            List<String> dates = new ArrayList<String>();
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            String date = sim.format(c.getTime());
            dates.add(date);
            for (int i = 0; i < 6; i++) {
                c.add(Calendar.DAY_OF_MONTH, 1);
                date = sim.format(c.getTime());
                dates.add(date);
            }
            return dates;
        }

        /**
         * 获取今天往后一周的日期（几月几号）
         */
        public static List<String> getSevendate () {
            List<String> dates = new ArrayList<String>();
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

            for (int i = 0; i < 7; i++) {
                mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
                mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
                String date = mMonth + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.month) : "/") +
                        mDay + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.day) : "");
                dates.add(date);
            }
            return dates;
        }

        public static String getHour () {
            Calendar c = Calendar.getInstance();
            return String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        }

        public static String getMinute () {
            Calendar c = Calendar.getInstance();
            return String.valueOf(c.get(Calendar.MINUTE));
        }


        /**
         * 获取今天往后一周的集合
         */
        public static List<String> get7week () {
            String week = "";
            List<String> weeksList = new ArrayList<String>();
            List<String> dateList = get7date();
            for (String s : dateList) {
                if (s.equals(StringData())) {
                    week = UIUtils.getString(R.string.today);
                } else {
                    week = getWeek(s);
                }
                weeksList.add(week);
            }
            return weeksList;
        }

        // 根据年月日计算年龄,birthTimeString:"1994-11-14"
        public static int getAgeFromBirthTime (Date date){
            // 得到当前时间的年、月、日
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH) + 1;
                int dayNow = cal.get(Calendar.DATE);
                //得到输入时间的年，月，日
                cal.setTime(date);
                int selectYear = cal.get(Calendar.YEAR);
                int selectMonth = cal.get(Calendar.MONTH) + 1;
                int selectDay = cal.get(Calendar.DATE);
                // 用当前年月日减去生日年月日
                int yearMinus = yearNow - selectYear;
                int monthMinus = monthNow - selectMonth;
                int dayMinus = dayNow - selectDay;
                int age = yearMinus;// 先大致赋值
                if (yearMinus <= 0) {
                    age = 0;
                }
                if (monthMinus < 0) {
                    age = age - 1;
                } else if (monthMinus == 0) {
                    if (dayMinus < 0) {
                        age = age - 1;
                    }
                }
                return age;
            }
            return 0;
        }

        public static Date parse (String strDate){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Date getDate (String str){
            try {
//            SimpleDateFormat formatter = new SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Date date = formatter.parse(str);
                return date;
            } catch (Exception e) {

            }
            return null;
        }

        public static Date getHourDate (String str){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");

                Date date = formatter.parse(str);
                return date;
            } catch (Exception e) {

            }
            return null;
        }


        /**
         * 获取某年某月有多少天
         *
         * @param year
         * @param month
         * @return
         */
        public static int getDayOfMonth ( int year, int month){
            Calendar c = Calendar.getInstance();
            c.set(year, month, 0); //输入类型为int类型
            return c.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * 根据当前的日期获取下个月的日期
         *
         * @param date
         * @return
         */
        // 待优化
        public static String nextMonth (String date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = null;
            try {
                dt = sdf.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.MONTH, 1);
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            return reStr;
        }

        // 待优化
        public static String priousMonth (String date){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = null;
            try {
                dt = sdf.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.MONTH, -1);
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            return reStr;
        }


        /**
         * 根据当前的日期年
         *
         * @param mYear
         * @return
         */
        public static int getYear (String mYear){
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mYear);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar now = Calendar.getInstance();
            now.setTime(date);

            int year = now.get(Calendar.YEAR);
            return year;
        }

        /**
         * 根据当前的月份
         *
         * @param mMonth
         * @return
         */

        public static int getMonth (String mMonth){
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar now = Calendar.getInstance();
            now.setTime(date);

            int month = now.get(Calendar.MONTH) + 1;
            return month;
        }


        public static String setDay (String date,int setday){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = null;
            try {
                dt = sdf.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DAY_OF_MONTH, setday);
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            return reStr;
        }

        /**
         * Description: 判断一个时间是否在一个时间段内 </br>
         *
         * @param nowTime   当前时间 </br>
         * @param beginTime 开始时间 </br>
         * @param endTime   结束时间 </br>
         */
        public static boolean belongCalendar (Date nowTime, Date beginTime, Date endTime){
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            return date.after(begin) && date.before(end);
        }

        /**
         * 判断时间是否在时间段内 开始时间大于结束时间返回true
         *
         * @param beginTime
         * @param endTime
         * @return true
         */

        public static boolean belongCalendar (Date beginTime, Date endTime){

            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);
            if (begin.after(end)) {
                return true;
            } else {
                return false;
            }
        }


        public static String toTime ( long second){
            long days = second / 86400;
            second = second % 86400;
            long hours = second / 3600;
            second = second % 3600;
            long minutes = second / 60;
            second = second % 60;
            if (days > 0) {
                return days + "天" + hours + "小时" + minutes + "分" + second + "秒";
            } else if (hours > 0) {
                return hours + "小时" + minutes + "分" + second + "秒";
            } else if (minutes > 0) {
                return minutes + "分" + second + "秒";
            } else {
                return second + "秒";
            }
        }

    }