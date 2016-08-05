package yoyocom.fancy.yoyolibrary.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by db on 15-7-8.
 */
public class DateUtils {
    public static final int FORMAT_DATA_YYYY_MM_DD_HH_MM_SS = 0;
    public static final int FORMAT_DATA_YYYY_MM_DD = FORMAT_DATA_YYYY_MM_DD_HH_MM_SS + 1;
    public static final int FORMAT_DATA_YYYY_MM_DD_A = FORMAT_DATA_YYYY_MM_DD + 1;
    public static final int FORMAT_DATA_MM_DD_A = FORMAT_DATA_YYYY_MM_DD + 4;
    public static final int FORMAT_DATA_A_CN = FORMAT_DATA_YYYY_MM_DD_A+1;
    public static final int FORMAT_DATA_YYYY_MM_DD_HH_MM = FORMAT_DATA_A_CN+1;
    public static final long ONE_DAY=86400000;


    public static final String[] monthS = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    /**
     * 获取本月第一天的开始
     *
     * @return
     */
    public static Date getFristMonth() {
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return cale.getTime();
    }

    public static Date getFirstMonthNew(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        try {
            Date date = sdf.parse(monthStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getLastMonthNew(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        try {
            Date date = sdf.parse(monthStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            calendar.set(Calendar.MONTH, month + 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取本月最后一天的结束
     *
     * @return
     */
    public static Date getLashMonth() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        cale.set(Calendar.SECOND, 59);
        return cale.getTime();
    }

    /**
     * 获取指定月数第一天的开始  0开始
     *
     * @return
     */
    public static Date getFristMonth(int month) {
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.MONTH, month);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return cale.getTime();
    }

    /**
     * 获取指定月数最后一天的结束  0开始
     *
     * @return
     */
    public static Date getLashMonth(int month) {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.MONTH, month + 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        cale.set(Calendar.SECOND, 59);
        return cale.getTime();
    }


    /**
     * 获取当前月  从0开始
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH);
        return month;
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar cale = Calendar.getInstance();
        return cale.get(Calendar.DATE);
    }

    /**
     * 获取今日 00:00:00 时间
     *
     * @return
     */
    public static String getCurrentDayStartTime() {
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        Date date = new Date(cale.getTimeInMillis());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 获取本周第一天
     *
     * @return
     */
    public static Date getFristDayOfWeek() {
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.DAY_OF_WEEK);
        if(day==1){
            day=-6;
            cale.add(Calendar.DAY_OF_YEAR,day );
        }else{
            cale.add(Calendar.DAY_OF_YEAR, 2-day);
        }
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        return cale.getTime();
    }


    /**
     * 获取本周第一天  从星期2算起
     *
     * @return
     */
    public static Date getFristDayOfWeek2() {
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.DAY_OF_WEEK);
        if(day==1){
            day=-6;
            cale.add(Calendar.DAY_OF_YEAR,day );
        }else{
            cale.add(Calendar.DAY_OF_YEAR, 2-day);
        }
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        long time =  cale.getTimeInMillis()+ ONE_DAY;
        if(time> System.currentTimeMillis()){
            time = time-ONE_DAY*7;
        }
        return new Date(time);
    }
    public static Date StringToDate(String str,int type){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (type) {
            case FORMAT_DATA_YYYY_MM_DD_HH_MM_SS:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case FORMAT_DATA_YYYY_MM_DD:
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case FORMAT_DATA_YYYY_MM_DD_A:
                format = new SimpleDateFormat("yyyy-MM-dd a", Locale.US);
                break;
        }
        try {
            return format.parse(str);
        }catch (Exception e){

        }
        return  null;
    }


    public static String dataToString(Date date, int type) {
        if(date==null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (type) {
            case FORMAT_DATA_YYYY_MM_DD_HH_MM_SS:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case FORMAT_DATA_YYYY_MM_DD:
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case FORMAT_DATA_YYYY_MM_DD_A:
                format = new SimpleDateFormat("yyyy-MM-dd a", Locale.US);
                break;
            case FORMAT_DATA_A_CN:
                format = new SimpleDateFormat("a",new Locale("zh","CN"));
                break;
            case FORMAT_DATA_YYYY_MM_DD_HH_MM:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            case FORMAT_DATA_MM_DD_A:
                format = new SimpleDateFormat("MM-dd HH:mm");
                break;
        }

        String str =  format.format(date);
        if(FORMAT_DATA_A_CN==type){
            if(str.equalsIgnoreCase("pm")){
                return "下午";
            }else if(str.equalsIgnoreCase("am")){
                return "上午";
            }
        }

        return str;

    }

    /**
     * 获取最近6个月月份
     */
    public static ArrayList<String> getLastSixMonths() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        ArrayList<String> result = new ArrayList<String>();
        Date date = new Date();
        result.add(sdf.format(date));
        for (int i = 1; i < 6; i ++) {
            result.add(sdf.format(getLastDate(date, i)));
        }
        return result;
    }

    public static String getCurrentMonthNew() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        ArrayList<String> result = new ArrayList<String>();
        Date date = new Date();
        return sdf.format(date);
    }

    public static Date getLastDate(Date date, int index) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0 - index);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }


}
