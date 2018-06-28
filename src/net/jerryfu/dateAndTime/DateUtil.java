
package net.jerryfu.dateAndTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * 日期操作类
 * @author jerryfu
 *
 */
public class DateUtil {
	/**
	 * 获取当前系统年份
	 * 
	 * @return 当前系统年份
	 */
	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * 将String 转为timestamp
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public static long strToTimestamp(String time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return new Timestamp(sdf.parse(time).getTime()).getTime();
	}

	public static String formatDateTime(Date date) {
		String dateStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateStr = sdf.format(date);
		return dateStr;
	}

	public static String DateTimeStr(Date date) {
		String dateStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dateStr = sdf.format(date);
		return dateStr;
	}

	public static String formatDateDay(Date date) {
		String dateStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dateStr = sdf.format(date);
		return dateStr;
	}

	public static String formatDateDay8(Date date) {
		String dateStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * Yvan 将yyyy-MM-dd转化成yyyyMMdd
	 * 
	 * @param str
	 *            yyyy-MM-dd
	 * @return yyyyMMdd
	 */
	public static String formatDate(String str) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMdd");
		String sfstr = "";
		try {
			sfstr = sf2.format(sf1.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	/**
	 * Yvan 将yyyyMMdd转化成yyyy-MM-dd
	 * 
	 * @param str
	 *            yyyyMMdd
	 * @return yyyy-MM-dd
	 */
	public static String formatDate2(String str) {
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		String sfstr = "";
		try {
			sfstr = sf2.format(sf1.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	/**
	 * 获取当前日期（yyyy-MM-dd）
	 * 
	 * @return
	 */
	public static String currentDate10() {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			tsStr = sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * 获取当前日期年月（yyyy-MM）
	 * 
	 * @return
	 */
	public static String dateYearMonth() {
		String tsStr = "";
		// DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// tsStr = sdf.format(new Date());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH) + 1;
			String temp = null;
			if (String.valueOf(m).length() == 1) {
				temp = "0" + String.valueOf(m);
			} else {
				temp = String.valueOf(m);
			}

			tsStr = y + "-" + temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * 
	 * TODO 把毫秒数转换成HH：mm:ss格式的时间
	 * 
	 * @param time
	 * @return
	 */
	public static String parseMillecondsToTime(Long time) {
		long milliSecond = time % 1000;
		String str = String.valueOf(milliSecond);
		while (str.length() < 3) {
			str = "0" + str;
		}
		time = time / 1000;
		long second = time % 60;
		time = time / 60;
		long minutes = time % 60;
		long hour = time / 60;
		String sh = (hour < 10) ? ("0" + String.valueOf(hour)) : String
				.valueOf(hour);
		String sm = (minutes < 10) ? ("0" + String.valueOf(minutes)) : String
				.valueOf(minutes);
		String ss = (second < 10) ? ("0" + String.valueOf(second)) : String
				.valueOf(second);
		return sh + ":" + sm + ":" + ss + "." + str;
	}

	/**
	 * 
	 * TODO 把毫秒数转换成以秒为单位的格式显示时间(1.324s)
	 * 
	 * @param time
	 * @return
	 */
	public static String parseMillecondsToSecond(Long time) {
		long milliSecond = time % 1000;
		String str = String.valueOf(milliSecond);
		while (str.length() < 3) {
			str = "0" + str;
		}
		long minutes = (time - milliSecond) / 1000;
		return minutes + "." + str;
	}

	/**
	 * 获取当前时间（HH:mm:ss）
	 * 
	 * @return
	 */
	public static String currentTime10() {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			tsStr = sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * 将Timestamp 转换为日期（yyyy-MM-dd）
	 * 
	 * @param time
	 * @return
	 */
	public static String parseTimestampDate(Timestamp time) {
		String tmpStr = "";
		if (null == time) {
			return tmpStr;
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			tmpStr = sdf.format(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmpStr;
	}

	/**
	 * 将Timestamp转为换日期+时间（yyyy-MM-dd HH:MM:SS） change by Yvan HH:MM:SS
	 * -->HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String parseTimestampDateTime(Timestamp time) {
		String tsStr = "";
		if (null == time) {
			return tsStr;
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			tsStr = sdf.format(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * 日期加天数
	 * 
	 * @param strDate
	 * @param dayCount
	 * @return
	 */
	public static Date dateStrAddDay(String strDate, int dayCount) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, dayCount);
			return calendar.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 日期加天
	 * 
	 * @param date
	 * @param dayCount
	 * @return
	 */
	public static String dateAddDay(String strDate, int dayCount) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateStrAddDay(strDate, dayCount);
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get 6 months date before nowdate
	 * 
	 * @return Date
	 */
	public static Date getDateBeforeSixMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -6);
		return c.getTime();
	}

	/**
	 * get 12 months date before nowdate
	 * 
	 * @return
	 */
	public static Date getDateBeforetwelveMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -12);
		return c.getTime();
	}

	/**
	 * get 18 months date before nowdate
	 * 
	 * @return Date
	 */
	public static Date getDateBeforeEighteenMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -18);
		return c.getTime();
	}

	/**
	 * 
	 * TODO get 20 minutea before nowdate
	 * 
	 * @return
	 */
	public static Date getDateBeforTwentyMinutes() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -20);
		return c.getTime();
	}

	/**
	 * 
	 * TODO get 40 minutea before nowdate
	 * 
	 * @return
	 */
	public static Date getDateBeforFourtyyMinutes() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -40);
		return c.getTime();
	}

	/**
	 * 字符串转日期
	 * 
	 * @param str
	 * @return
	 * @author Anna
	 */
	public static Date convertStringToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = (java.util.Date) sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;

	}

	/**
	 * 比较两个日期大小
	 * 
	 * @param DATE1
	 *            日期1
	 * @param DATE2
	 *            日期2
	 * @return int 0：日期1大于等于日期2;;;1：日期1小于等于日期2
	 */
	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 0;
			} else if (dt1.getTime() <= dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return 1;
			} else {
				return -1;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	// /**
	// * 日期月份加减计算
	// * @param date 日期 格式yyyy-MM-dd
	// * @param months 加减的数
	// * @return 日期
	// * @throws ParseException
	// */
	// public static String dateMonthCount(String date,int months) throws
	// ParseException{
	// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	// Date dt=sdf.parse(date);
	// Calendar rightNow = Calendar.getInstance();
	// rightNow.setTime(dt);
	// rightNow.add(Calendar.MONTH,months);//日期加减
	// Date dt1=rightNow.getTime();
	// String reStr = sdf.format(dt1);
	// return reStr;
	// }

	/**
	 * 根据月份获取月的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * 判断日期为星期几
	 * 
	 * @param date
	 *            日期
	 * @throws ParseException
	 */
	public static String dateCheck(String date) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		try {
			Date d = sd.parse(date);
			String currentWeek = sdw.format(d);
			if (currentWeek.equals("星期一")) {
				return "1";
			} else if (currentWeek.equals("星期二")) {
				return "2";
			} else if (currentWeek.equals("星期三")) {
				return "3";
			} else if (currentWeek.equals("星期四")) {
				return "4";
			} else if (currentWeek.equals("星期五")) {
				return "5";
			} else if (currentWeek.equals("星期六")) {
				return "6";
			} else if (currentWeek.equals("星期日")) {
				return "7";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "0";
	}

	/**
	 * kelly 获取日期的年、月、日
	 * 
	 * @param date
	 * @param flag
	 *            0获取年，1获取月，2获取日
	 * @return 如2016-07-01，返回2016,7,1
	 */
	public static int getYearMonthDay(String date, int flag) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = cal.get(Calendar.YEAR); // 年
		int month = cal.get(Calendar.MONTH) + 1;// 月
		int day = cal.get(Calendar.DATE); // 日
		int temp = -1;
		switch (flag) {
		case 0:
			temp = year;
			break;
		case 1:
			temp = month;
			break;
		case 2:
			temp = day;
			break;
		}
		return temp;
	}

	/**
	 * kelly 获取指定两个日期之间的所有日期（支持跨年），省去日期拼接
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 */
	public static List<String> getAllDate(String startDate, String endDate) {
		List<String> date = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			start.setTime(sdf.parse(startDate));
			end.setTime(sdf.parse(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long startTIme = start.getTimeInMillis();
		Long endTime = end.getTimeInMillis();

		Long oneDay = 1000 * 60 * 60 * 24l;

		Long time = startTIme;

		while (time <= endTime) {
			Date d = new Date(time);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			date.add(df.format(d));
			time += oneDay;
		}
		return date;
	}

	/**
	 * kelly 获取指定两个日期之间的所有月份（支持跨年）
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getMonthBetween(String startDate, String endDate) {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			start.setTime(sdf.parse(startDate));
			start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1);
			end.setTime(sdf.parse(endDate));
			end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), 2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar curr = start;
		while (curr.before(end)) {
			result.add(sdf.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}
		return result;
	}
}
