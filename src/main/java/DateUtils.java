import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
/**
 * Created by lszhen on 2017/6/27.
 */
public class DateUtils {

    private static Logger log = Logger.getLogger(DateUtils.class);

    /**当前毫秒值*/
    public static long NOW_DATE_MIS =getNowTime();

    /**根据方法获取**/
    public static long getNowTime(){
        return new Date().getTime();
    }

    /**
     * 获取当前时间的年月日时分秒+时间戳后4位  如:20140620123456   18位
     * @return
     */
    public static String ymdhms4(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = sdf.format(date);
        double a = Math.random() * 9000 + 1000;
        int result = (int)a;
        return s + result;
    }
    /**
     * 把一个字符串时间转换为毫秒值  如 2014-12-12 12:12:12 转换为 1406086513619
     * @param strDate
     * @return
     */
    public static long str2DateTime(String strDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            pe.getStackTrace();
        }
        return date.getTime() ;
    }
    /**
     * 把一个日期转换为指定的格式
     */
    public static Date str2Date(String strDate, String formater) {
        if (strDate == null) {
            return null;
        }
        if (formater == null) {
            formater = "yyyy-MM-dd";
        }
        SimpleDateFormat df = new SimpleDateFormat(formater);
        Date date = new Date();
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            pe.getStackTrace();
        }
        return date;
    }


    /**
     * 获取最大毫秒值
     * @param dateStr 2014-12-12 年-月-日 参数
     * @return
     */
    public static Long getMaxMillsByYMD(String dateStr){
        if(StringUtils.isBlank(dateStr)){
            log.error("getTodayMaxMills error , message :param is empty");
            return null;
        }
        dateStr = dateStr.trim() + " 23:59:59";
        Date date = ymdHmsStr2Date(dateStr);
        return date.getTime() ;
    }
    /**
     * 获取最小毫秒值
     * @param dateStr 2014-12-12 年月日参数
     * @return
     */
    public static Long getMinxMillsByYMD(String dateStr){
        if(StringUtils.isBlank(dateStr)){
            log.error("getTodayMaxMills error , message :param is empty");
            return null;
        }
        dateStr = dateStr.trim() + " 00:00:00";
        Date date = ymdHmsStr2Date(dateStr);
        return date.getTime() ;
    }


    public static String getNextMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        return formaterDate(c.getTime(), "yyyy-MM");
    }

    public static String getPreviousMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, -1);// 减一个月，变为上月的1号
        return formaterDate(c.getTime(), "yyyy-MM");
    }

    public static String getMonth(String date,int num) {
        Calendar c = Calendar.getInstance();
        if(StringUtils.isNotEmpty(date)){
            c.setTime(DateUtils.ymdStr2Date(date));
        }
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, num);// 减一个月，变为上月的1号
        return formaterDate(c.getTime(), "yyyy-MM-dd");
    }
    /**
     * 计算两个日期之间相差的月数
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonths(Date date1, Date date2) {
        int iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(date1);
            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(date2);
            if (objCalendarDate2.equals(objCalendarDate1))
                return 0;
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
                    .get(Calendar.DAY_OF_MONTH))
                flag = 1;

            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
                    .get(Calendar.YEAR))
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
                        .get(Calendar.YEAR))
                        * 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
                        - objCalendarDate1.get(Calendar.MONTH);
            else
                iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return iMonth;
    }

    @SuppressWarnings("unchecked")
    public static List getMonthList(String startDate,String endDate){
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return new ArrayList();
        }
        List<String> dateList = new ArrayList<String>();
        //得到两个日期之间有多少月
        int num = getMonths(ymdStr2Date(startDate),ymdStr2Date(endDate));
        Calendar c = Calendar.getInstance();
        c.setTime(DateUtils.ymdStr2Date(startDate));
        dateList.add(formaterDate(c.getTime(), "yyyy-MM"));
        for(int i=0; i<num; i++){
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            dateList.add(formaterDate(c.getTime(), "yyyy-MM"));
        }
        return dateList;
    }
    /**
     * 获取当前星期（中国, 如：星期日,星期一,星期二）
     */
    public static String cnWeek() {
        Calendar c = GregorianCalendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        String[] s = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        return s[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 得到当前日期 再加上你指定的天数 所得到的新日期
     * @param days
     * @return
     */
    public static Date getNewDayNumDate(int days){
        String today = getToday();
        String newDay = getNewDayNumDate(today,days);
        return ymdStr2Date(newDay);
    }

    // 计算当月最后一天,返回字符串
    public static String getMonthLastDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        c.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return ymd(c.getTime());
    }

    // 计算当月第一天,返回字符串
    public static String getMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        return ymd(c.getTime());
    }

    /**
     * 获得前十天时间
     *
     * @param date
     * @return
     */
    public static String getPrevTenDay(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - day);
        return ymdHms(c.getTime());
    }


    /**
     * 得到一个日期是周几
     */
    public static String getWeekCS(Date date) {

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        String[] s = { "sunday", "monday", "tuesday", "wednesday",
                "thursday", "friday", "saturday" };
        return s[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 得到一个日期是周几
     */
    public static String getWeekInNum(Date date) {

        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        int weekDayNum = c.get(Calendar.DAY_OF_WEEK)-1;
        if(weekDayNum == 0){
            weekDayNum = 7;
        }
        return String.valueOf(weekDayNum);
    }

    /**
     *  取传入时间的小时值
     * @param endDate
     * @return
     */
    public static String makeHour(Date endDate) {
        String hour = "";
        if (endDate != null) {
            DateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String after = format.format(endDate);
            hour = after.substring(11, 13);
        }
        return hour;
    }

    public static String makeMini(Date endDate) {
        String mini = "";
        if (endDate != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String after = format.format(endDate);
            mini = after.substring(14, 16);
        }
        return mini;
    }

    /**
     * 得到当前时间的 小时和分（hh:mm）
     * @param date
     * @return
     */
    public static String getHourAndMinute(Date date){
        String hour = makeHour(date);
        String minute = makeMini(date);

        return hour + ":" + minute;
    }

    @SuppressWarnings("unchecked")
    public static List getTomorrowList() {
        List<String> dateList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = sdf.format(calendar.getTime());
        dateList.add(tomorrow);
        for (int i = 0; i < 6; i++) {
            calendar.set(6, calendar.get(6) + 1);
            tomorrow = sdf.format(calendar.getTime());
            dateList.add(tomorrow);
        }

        return dateList;
    }

    public static String getWeekStartDateString(String startDate) {
        return getWeekDateString(startDate, 0);
    }

    public static String getWeekEndDateString(String endDate) {
        return getWeekDateString(endDate, 6);
    }

    public static String getWeekDateString(String currentDate, int day) {
        // 日历
        Calendar calendar = new GregorianCalendar();
        // 设置日历为当前时间
        calendar.setTime(ymdStr2Date(currentDate));
        // 得到今天是周几
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekofYear = calendar.get(Calendar.WEEK_OF_YEAR);
        // 加入不是周日，设置日期到下一周
        if (dayofweek == 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, weekofYear - 1);
        }
        int nextWeekDay = calendar.get(Calendar.DATE);
        // 设置日历为下周第一天,+2是下周从星期一开始，星期一是每周的第2天
        calendar.set(Calendar.DATE, nextWeekDay - dayofweek + 2);
        calendar.add(Calendar.DATE, day);
        return ymd(calendar.getTime());
    }

    public static Date[] getWeekStartAndEndDate(Date currentDate, int start) {
        // 将要返回的日期
        Date[] days = new Date[2];
        if(null != currentDate){
            // 日历
            Calendar calendar = new GregorianCalendar();
            // 设置日历为当前时间
            calendar.setTime(currentDate);
            // 得到今天是周几
            int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
            int weekofYear = calendar.get(Calendar.WEEK_OF_YEAR);
            // 加入不是周日，设置日期到下一周
            if (dayofweek == 1) {
                calendar.set(Calendar.WEEK_OF_YEAR, weekofYear - 1);
            }
            // 得到当前是全年的第几周
            int nextWeekDay = calendar.get(Calendar.DATE);
            // 设置日历为下周第一天,+5是下周从星期四开始，星期四是每周的第5天
            calendar.set(Calendar.DATE, nextWeekDay - dayofweek + 2);
            calendar.add(Calendar.DATE, start);
            days[0] = calendar.getTime();
            days[0] = ymdHmsStr2Date(ymd(days[0]) + " 00:00:00");
            calendar.add(Calendar.DATE, 6);
            days[1] = calendar.getTime();
            days[1] = ymdHmsStr2Date(ymd(days[1]) + " 23:59:59");
        }
        return days;
    }

    public static String[] getWeekStartAndEndDate(String currentDate, int start) {
        // 将要返回的日期
        String[] days = new String[2];
        if(StringUtils.isEmpty(currentDate)){
            return days;
        }
        // 日历
        Calendar calendar = new GregorianCalendar();
        // 设置日历为当前时间
        calendar.setTime(ymdStr2Date(currentDate));
        // 得到今天是周几
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekofYear = calendar.get(Calendar.WEEK_OF_YEAR);
        // 加入不是周日，设置日期到下一周
        if (dayofweek == 1) {
            if(weekofYear == 1){
                weekofYear = 53;
            }
            calendar.set(Calendar.WEEK_OF_YEAR, weekofYear - 1);
        }
        // 得到当前是全年的第几周
        int nextWeekDay = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, nextWeekDay - dayofweek + 2);
        calendar.add(Calendar.DATE, start);
        days[0] = ymd(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        days[1] = ymd(calendar.getTime());
        return days;
    }

    public static String[] getThisWeekStartEndDays() {
        return getWeekStartAndEndDate(ymd(new Date()), 0);
    }

    public static String[] getLastWeekStartEndDays() {
        return getWeekStartAndEndDate(ymd(new Date()), -7);
    }

    public static String[] getNextWeekStartEndDays() {
        return getWeekStartAndEndDate(ymd(new Date()), 7);
    }

    @SuppressWarnings("unchecked")
    public static List getWeekDateList(String date) throws ParseException {
        List<String> dateList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        Date dateVar = ymd.parse(date);
        calendar.setTime(dateVar);
        dateList.add(date);
        for (int i = 1; i <= 6; i++) {
            calendar.set(6, calendar.get(6) + 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tomorrow = sdf.format(calendar.getTime());
            dateList.add(tomorrow);
        }
        return dateList;
    }

    public static String getNextDay(String currentDate) {
        Date d = ymdStr2Date(currentDate);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
        return ymd(c.getTime());
    }

    /**
     * 获得开始时间和结束时间之间的时间列表
     * @param startDate
     * @param endDate
     * @return List<String>
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public static List getDateList(String startDate, String endDate) throws ParseException {
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // 设定格式
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);
        long day = ((end.getTime() - start.getTime()) / 1000);// 秒数
        day = day / (60 * 60 * 24); // 天数
        for (int i = 0; i <= day; i++) {
            String date = getAfterSomeDayYms(startDate, i);
            dateList.add(date);
        }
        return dateList;
    }

    /**
     * 判断所给时间在月的第几周
     * @param date 输入时间
     * @return 第几周
     */
    public static int weekOfMonth(String date) {
        Date d = ymdStr2Date(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取当前星期几
     */
    public static int weekDay(String date) {
        Date d = ymdStr2Date(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        return c.get(Calendar.DAY_OF_WEEK)-1;

    }
    /**
     * 返回日期date 前推beforHour小时后的 日期
     * @param date
     * @param beforHour
     * @return
     */
    public static Date getBeforHourDate(Date date, int beforHour){

        if(date != null){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR, c.get(Calendar.HOUR)-beforHour);

            return c.getTime();
        }
        return null;
    }

    /**
     * 取给定日期的前一天
     * @param date
     * @return
     */
    public static Date getLastDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,
                calendar.get(Calendar.DAY_OF_YEAR) - 1);
        return calendar.getTime();
    }

    /**
     * 返回两时间的相差天数
     * @param d1
     * @param d2
     * @return
     */
    public static int getDayNumBetween2Date(Date d1, Date d2){

        if(isEqualsInYMD(d1, d2)){
            return 0;
        }

        long l = d2.getTime()-d1.getTime();
        int day = (int) (l/(24*60*60*1000));
        if(day < 0){
            day = (-day);
        }

        int m = (int) (l%(24*60*60*1000));
        Calendar c = Calendar.getInstance();
        if(d1.compareTo(d2)<=0){
            c.setTime(d1);
            c.add(Calendar.MILLISECOND, m);
            if(isEqualsInYMD(d1, c.getTime())){
                return day;
            }else{
                return day+1;
            }
        }else{
            c.setTime(d2);
            c.add(Calendar.MILLISECOND, m);
            if(isEqualsInYMD(d2, c.getTime())){
                return day;
            }else{
                return day+1;
            }
        }

    }

    /**
     * 两个时间是否是 同年 同月 同日
     * 如果是，则返回true，否则返回false
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isEqualsInYMD(Date d1, Date d2){

        Calendar c = Calendar.getInstance();

        c.setTime(d1);
        int year1 = c.get(Calendar.YEAR);
        int dayOfYear1 = c.get(Calendar.DAY_OF_YEAR);

        c.setTime(d2);
        int year2 = c.get(Calendar.YEAR);
        int dayOfYear2 = c.get(Calendar.DAY_OF_YEAR);

        if(year1 != year2){
            return false;
        }
        if(dayOfYear1 != dayOfYear2){
            return false;
        }

        return true;
    }

    /**
     * 获得当前月的前 i 个月
     * @param i (henrry)
     * return 月的数组
     */
    public static String[] getMonthArr(int i){
        String arr[] = new String[i];
        Calendar c = Calendar.getInstance();
        int temp = 0;
        int tempp = 0;
        int year = 0;
        int month = 0;
        int m = i-1;
        for(int j=0;j<m;j++){
            if(i>12){
                temp = (i-1)/12;
                tempp = (i-1)%12;
                year  = c.get(Calendar.YEAR)-temp;
                month = c.get(Calendar.MONTH)-tempp+1;
                if(month<10 && month >0){
                    arr[j] = year+"-0"+month;
                }else if(month<0){
                    month = 12+month;
                    if(month<10){
                        arr[j] = (year-1)+"-0"+month;
                    }else{
                        arr[j] = (year-1)+"-"+month;
                    }
                }else if(month == 0){
                    arr[j] = (year-1)+"-"+12;
                }else{
                    arr[j] = year+"-"+month;
                }
            }else{
                year  = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH)-i+2;
                if(month<10 && month >0){
                    arr[j] = year+"-0"+month;
                }else if(month<0){
                    month = 12+month;
                    if(month<10){
                        arr[j] = (year-1)+"-0"+month;
                    }else{
                        arr[j] = (year-1)+"-"+month;
                    }
                }else if(month==0){
                    arr[j] = (year-1)+"-"+12;
                }else{
                    arr[j] = year+"-"+month;
                }
            }
            i--;
        }
        int monthNow = c.get(Calendar.MONTH);
        if(monthNow>8){
            arr[m] = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1);
        }else{
            arr[m] = c.get(Calendar.YEAR) + "-0" + (c.get(Calendar.MONTH)+1);
        }
        return arr;
    }

    /**
     * 得到两个时间之间的差距，根据type可返回分别以天，时，分为单位的整数
     * @param d1
     * @param d2
     * @param type
     * @return
     */
    public static int getBetweenDateGap(Date d1, Date d2, String type){
        long time1 = d1.getTime();
        long time2 = d2.getTime();
        long m = (time1-time2)/1000;
        int nDay = (int)m/(24*60*60);
        int nHour = (int)(m-nDay*24*60*60)/(60*60);
        int nMinute = (int)(m-nDay*24*60*60-nHour*60*60)/60;
        int mSecond = (int)(m-nDay*24*60*60-nHour*60*60);
        if("day".equals(type)){
            return nDay;
        }else if("minute".equals(type)){
            return nMinute;
        }else if("hour".equals(type)){
            return nHour;
        }else if("second".equals(type)){
            return mSecond;
        }
        return nMinute;
    }

    /**
     * *
     * 通过月份计算季度 *
     * @param
     * month *
     * @return
     */
    public static int getQuarter(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month is invalid.");
        }
        return (month - 1) / 3 + 1;
    }
}
}
