package net.jerryfu.random;

import java.util.Random;

/**
 * 生成随机码工具类
 * @author jerryfu
 *
 */
public class RandomUtils {

    private static final String INT = "0123456789";
    private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random rd = new Random();

    private RandomUtils() {
    }

    /**
     * 函数功能说明：生成0-9的随机码. Administrator 2015-9-8 修改者名字 修改日期 修改内容
     * 
     * @参数： @param length
     * @参数： @return
     * @return String
     * @throws
     */
    public static String randomInt(int length) {
        return random(length, RndType.INT);
    }

    /**
     * 函数功能说明：生成a-z的随机码. Administrator 2015-9-8 修改者名字 修改日期 修改内容
     * 
     * @参数： @param length
     * @参数： @return
     * @return String
     * @throws
     */
    public static String randomStr(int length) {
        return random(length, RndType.STRING);
    }

    /**
     * 函数功能说明：成长数字加字母的随机码. Administrator 2015-9-8 修改者名字 修改日期 修改内容
     * 
     * @参数： @param length
     * @参数： @return
     * @return String
     * @throws
     */
    public static String randomAll(int length) {
        return random(length, RndType.ALL);
    }

    private static String random(int length, RndType rndType) {
        StringBuilder s = new StringBuilder();
        char num = 0;
        for (int i = 0; i < length; i++) {
            if (rndType.equals(RndType.INT)) {
                num = INT.charAt(rd.nextInt(INT.length()));// 产生数字0-9的随机数
            } else if (rndType.equals(RndType.STRING)) {
                num = STR.charAt(rd.nextInt(STR.length()));// 产生字母a-z的随机数
            } else {
                num = ALL.charAt(rd.nextInt(ALL.length()));
            }
            s.append(num);
        }
        return s.toString();
    }

    public static enum RndType {
        INT, STRING, ALL
    }
}
