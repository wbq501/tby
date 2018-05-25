package com.baigu.dms.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baigu.dms.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/5 22:38
 */
public class DateUtils {

    public final static ThreadLocal<SimpleDateFormat> sYMDHMSFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sYMDHMSFormat2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };
    public final static ThreadLocal<SimpleDateFormat> sYMDHMSFormat3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sYMDHMSMFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sYMD_HMSFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sYMDHMFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sYMDFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sHMFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sEFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sEHMFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public final static ThreadLocal<SimpleDateFormat> sUTCFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf;
        }
    };

    public static final int SEMI_MONTH = 1001;
    private static final int MODIFY_TRUNCATE = 0;
    private static final int MODIFY_CEILING = 2;
    private static final int MODIFY_ROUND = 1;

    private static final int[][] fields = {
            {Calendar.MILLISECOND},
            {Calendar.SECOND},
            {Calendar.MINUTE},
            {Calendar.HOUR_OF_DAY, Calendar.HOUR},
            {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM
                /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
            },
            {Calendar.MONTH, DateUtils.SEMI_MONTH},
            {Calendar.YEAR},
            {Calendar.ERA}};

    /**
     * 字符串转日期
     * @param dateStr
     * @param dateFormat 请使用sYMDHMSFormat.get() 或 sYMDHMFormat.get() 或其他
     * @return
     */
    public static Date strToDate(String dateStr, DateFormat dateFormat) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  日期字符串转日期毫秒数
     * @param dateStr
     * @param sdf
     * @return
     */
    public static long strToLong(String dateStr, SimpleDateFormat sdf) {
        Date date = null;
        long timeStemp = 0;
        try {
            date = strToDate(dateStr, sdf);
            timeStemp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStemp;
    }

    /**
     *  日期毫秒数转日期字符串
     * @param timeMillis 毫秒数
     * @param sdf
     * @return
     */
    public static String longToStr(long timeMillis, SimpleDateFormat sdf) {
        String dateStr = "";
        try {
            dateStr = dateToStr(new Date(timeMillis), sdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 日期转字符串
     * @param date
     * @param dateFormat 请使用sYMDHMSFormat.get() 或 sYMDHMFormat.get() 或其他
     * @return
     */
    public static String dateToStr(Date date, DateFormat dateFormat) {
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**是否是同一天*/
    public static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**是否是同一天*/
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**增加amount年*/
    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if(date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**将时间截取到天*/
    public static Date truncateDate(Date date) {
        return truncate(date, Calendar.DATE);
    }

    public static Date truncateHour(Date date) {
        return truncate(date, Calendar.HOUR_OF_DAY);
    }

    public static Date truncateMinute(Date date) {
        return truncate(date, Calendar.MINUTE);
    }

    public static Date truncateSecond(Date date) {
        return truncate(date, Calendar.SECOND);
    }

    public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, MODIFY_TRUNCATE);
        return gval.getTime();
    }

    private static void modify(Calendar val, int field, int modType) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        if (field == Calendar.MILLISECOND) {
            return;
        }

        // ----------------- Fix for LANG-59 ---------------------- START ---------------
        // see http://issues.apache.org/jira/browse/LANG-59
        //
        // Manually truncate milliseconds, seconds and minutes, rather than using
        // Calendar methods.

        Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;

        // truncate milliseconds
        int millisecs = val.get(Calendar.MILLISECOND);
        if (MODIFY_TRUNCATE == modType || millisecs < 500) {
            time = time - millisecs;
        }
        if (field == Calendar.SECOND) {
            done = true;
        }

        // truncate seconds
        int seconds = val.get(Calendar.SECOND);
        if (!done && (MODIFY_TRUNCATE == modType || seconds < 30)) {
            time = time - (seconds * 1000L);
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }

        // truncate minutes
        int minutes = val.get(Calendar.MINUTE);
        if (!done && (MODIFY_TRUNCATE == modType || minutes < 30)) {
            time = time - (minutes * 60000L);
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        // ----------------- Fix for LANG-59 ----------------------- END ----------------

        boolean roundUp = false;
        for (int[] aField : fields) {
            for (int element : aField) {
                if (element == field) {
                    //This is our field... we stop looping
                    if (modType == MODIFY_CEILING || (modType == MODIFY_ROUND && roundUp)) {
                        if (field == DateUtils.SEMI_MONTH) {
                            //This is a special case that's hard to generalize
                            //If the date is 1, we round up to 16, otherwise
                            //  we subtract 15 days and add 1 month
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
// ----------------- Fix for LANG-440 ---------------------- START ---------------
                        } else if (field == Calendar.AM_PM) {
                            // This is a special case
                            // If the time is 0, we round up to 12, otherwise
                            //  we subtract 12 hours and add 1 day
                            if (val.get(Calendar.HOUR_OF_DAY) == 0) {
                                val.add(Calendar.HOUR_OF_DAY, 12);
                            } else {
                                val.add(Calendar.HOUR_OF_DAY, -12);
                                val.add(Calendar.DATE, 1);
                            }
// ----------------- Fix for LANG-440 ---------------------- END ---------------
                        } else {
                            //We need at add one to this field since the
                            //  last number causes us to round up
                            val.add(aField[0], 1);
                        }
                    }
                    return;
                }
            }
            //We have various fields that are not easy roundings
            int offset = 0;
            boolean offsetSet = false;
            //These are special types of fields that require different rounding rules
            switch (field) {
                case DateUtils.SEMI_MONTH:
                    if (aField[0] == Calendar.DATE) {
                        //If we're going to drop the DATE field's value,
                        //  we want to do this our own way.
                        //We need to subtrace 1 since the date has a minimum of 1
                        offset = val.get(Calendar.DATE) - 1;
                        //If we're above 15 days adjustment, that means we're in the
                        //  bottom half of the month and should stay accordingly.
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        //Record whether we're in the top or bottom half of that range
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (aField[0] == Calendar.HOUR_OF_DAY) {
                        //If we're going to drop the HOUR field's value,
                        //  we want to do this our own way.
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset >= 6;
                        offsetSet = true;
                    }
                    break;
            }
            if (!offsetSet) {
                int min = val.getActualMinimum(aField[0]);
                int max = val.getActualMaximum(aField[0]);
                //Calculate the offset from the minimum allowed value
                offset = val.get(aField[0]) - min;
                //Set roundUp if this is more than half way between the minimum and maximum
                roundUp = offset > ((max - min) / 2);
            }
            //We need to remove this field
            if (offset != 0) {
                val.set(aField[0], val.get(aField[0]) - offset);
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");

    }

    /**
     *
     * @param date 需要对比的时间
     * @param currDateStr 当前时间
     * @return
     */
    public static String relativeDate(Context context, Date date, String currDateStr) {
        try {
            Date currDate = strToDate(currDateStr, sYMDHMSFormat.get());
            return relativeDate(context, date, currDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param dateStr 需要对比的时间
     * @param currDateStr 当前时间
     * @return
     */
    public static String relativeDate(Context context, String dateStr, String currDateStr) {
        try {
            Date date = strToDate(dateStr, sYMDHMSFormat.get());
            Date currDate = strToDate(currDateStr, sYMDHMSFormat.get());
            return relativeDate(context, date, currDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String relativeDate(Context context, Date date1, Date date2) {
        if (isSameDay(date1, date2)) { //date1与date2是同一天
            return sHMFormat.get().format(date1);
        } else {
            Date date2Truncate = truncateDate(date2);
            Date yestoday = addDays(date2Truncate, -1);
            if (isSameDay(date1, yestoday)) { //date1与date2相差一天
                return context.getString(R.string.yestoday);
            }
            Date weekAgoDate = addDays(date2Truncate, -7); //七天前的日期
            if (date1.after(weekAgoDate)) { //date1相差date2七天之内
                return sEFormat.get().format(date1);
            }
            return sYMDFormat.get().format(date1);
        }
    }

    public static String relativeDateForMessage(Context context, Date date1, Date date2) {
        if (isSameDay(date1, date2)) { //date1与date2是同一天
            return sHMFormat.get().format(date1);
        } else {
            Date date2Truncate = truncateDate(date2);
            Date yestoday = addDays(date2Truncate, -1);
            if (isSameDay(date1, yestoday)) { //date1与date2相差一天
                return context.getString(R.string.yestoday) + " " + sHMFormat.get().format(date1);
            }
            Date weekAgoDate = addDays(date2Truncate, -7); //七天前的日期
            if (date1.after(weekAgoDate)) { //date1相差date2七天之内
                return sEHMFormat.get().format(date1);
            }
            return sYMDHMFormat.get().format(date1);
        }
    }

    public static String relativeDateForMessage(Context context, String dateStr, String currDateStr) {
        try {
            Date date = strToDate(dateStr, sYMDHMSFormat.get());
            Date currDate = strToDate(currDateStr, sYMDHMSFormat.get());
            return relativeDateForMessage(context, date, currDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * @param date1
     * @param date2
     * @return  天、时、分、秒 数组
     */
    public static long[] getDistanceTimes(String date1, String date2) {
        return getDistanceTimes(strToDate(date1, sYMDHMSFormat.get()), strToDate(date2, sYMDHMSFormat.get()));
    }

    /**
     *
     * @param date1
     * @param date2
     * @return  天、时、分、秒 数组
     */
    public static long[] getDistanceTimes(Date date1, Date date2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long time1 = date1.getTime();
            long time2 = date2.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

    /*
	 * 毫秒转化时分,格式为  h:mm
	 */
    public static String getMinuteAndSecond(long timeMillis) {
        DecimalFormat df = new DecimalFormat("#00");

        int ss = 1000;
        int mi = ss * 60;

        long minute = timeMillis / mi;
        long second = (timeMillis - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        if(minute > 0) {
            sb.append(minute);
        } else {
            sb.append("0");
        }
        if(second > 0) {
            sb.append(":");
            sb.append(df.format(second));
        } else {
            sb.append(":00");
        }
        return sb.toString();
    }

    public static String getCurrentTime() {
        return dateToStr(new Date(), sYMDHMSFormat.get());
    }

    public static long getCurrentTimeMillis() {
        return new Date().getTime();
    }


    public static long getDistance(String dateStr1, String dateStr2) {
        Date date1 = strToDate(dateStr1, sYMDHMSFormat.get());
        Date date2 = strToDate(dateStr2, sYMDHMSFormat.get());
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return time2 - time1;
    }

    /**
     * 判断给定字符串时间是否为今日(效率不是很高，不过也是一种方法)
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate){
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if(time != null){
            String nowDate = sYMDFormat.get().format(today);
            String timeDate = sYMDFormat.get().format(time);
            if(nowDate.equals(timeDate)){
                b = true;
            }
        }
        return b;
    }
    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return sYMDHMSFormat.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isThisYear(String sdate){
        String time = sdate.substring(0,4);
        Calendar c = Calendar.getInstance();
        int thisyear =c.get(Calendar.YEAR);
        if(TextUtils.equals(time,""+thisyear)){
            return true;
        }
        return false;
    }

    public static Date getGMTTime(String utc) {
        if(utc == null) {
            return null;
        } else {
            Date date = null;

            try {
                Calendar e = Calendar.getInstance();
                Date gmtDate = DateUtils.strToDate(utc, DateUtils.sYMDHMSFormat.get());
                e.setTime(new Date(gmtDate.getTime() + (long)e.getTimeZone().getOffset(e.getTimeInMillis())));
                date = e.getTime();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return date;
        }
    }

    public static void main(String[] args) throws Exception {
        String dateStr = "2017-04-07T07:18:00Z";
        Date date = strToDate(dateStr, sUTCFormat.get());
        dateStr = dateToStr(date, sYMDHMSFormat.get());
        date = getGMTTime(dateStr);
//        System.out.println(dateToStr(date, sYMDHMSFormat.get()));
    }
}
