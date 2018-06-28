package net.jerryfu.string;/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文拼音转换工具类
 * @author jerryfu
 *
 */
public class PinyinTool {

    private static HanyuPinyinOutputFormat format = null;

    public static enum Type {
        UPPERCASE,              //全部大写
        LOWERCASE,              //全部小写
        FIRSTUPPER              //首字母大写
    }

    static{
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

//    public PinyinTool(){
//        format = new HanyuPinyinOutputFormat();
//        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//    }

    /**
     * 转大写的拼音
     * @param str
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toUppercasePinYin(String str) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, "", Type.UPPERCASE);
    }

    /**
     * 转小写的拼音
     * @param str
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toLowercasePinYin(String str) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, "", Type.LOWERCASE);
    }

    public static String toLOWERCASEPinYin(String str,String spera) throws BadHanyuPinyinOutputFormatCombination{
        return toPinYin(str, spera, Type.UPPERCASE);
    }

    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * 如： 明天 转换成 MINGTIAN
     * @param str
     * @param spera
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {
        if(str == null || str.trim().length()==0)
            return "";
        if(type == Type.UPPERCASE)
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        else
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        String py = "";
        String temp = "";
        String[] t;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if((int)c <= 128)
                py += c;
            else{
                t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if(t == null)
                    py += c;
                else{
                    temp = t[0];
                    if(type == Type.FIRSTUPPER)
                        temp = t[0].toUpperCase().charAt(0)+temp.substring(1);
                    py += temp+(i==str.length()-1?"":spera);
                }
            }
        }
        return py.trim();
    }

}
