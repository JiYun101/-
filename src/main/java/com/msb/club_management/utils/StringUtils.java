package com.msb.club_management.utils;

/**
 * 字符串处理工具类
 */
public class StringUtils {
	
	 /**
     * 检查指定的字符串是否为 NUll
     * 返回true，指定的字符串是 NULL
     * 返回false，指定的字符串不是 NULL
     * @param str 要检查的字符串
     * @return
     */
    public static Boolean isNull(String str){

        if(str == null){

            return true;
        }

        return false;
    }

    /**
     * 检查指定的字符串是否不为 NUll
     * 返回true，指定的字符串不为 NULL
     * 返回false，指定的字符串是 NULL
     * @param str 要检查的字符串
     * @return
     */
    public static Boolean isNotNull(String str){

        Boolean flag = !isNull(str);

        return flag;
    }

    /**
     * 检查指定的字符串是否为 NULL 或者空字符串
     * 返回true，指定的字符串是 NULL 或者空字符串
     * 返回 false，指定的字符串不是 NULL 或者空字符粗
     * @param str 要检查的字符串
     * @return
     */
    public static Boolean isNullOrEmpty(String str){

        if(str == null){

            return true;
        }

        if(str.length() <= 0){

            return true;
        }

        return false;
    }

    /**
     * 检查指定的字符串是否不为 NULL 或者空字符串
     * 返回true，指定的字符串不是 NULL 或者空字符串
     * 返回 false，指定的字符串是 NULL 或者空字符粗
     * @param str
     * @return
     */
    public static Boolean isNotNullOrEmpty(String str){

        Boolean flag = !isNullOrEmpty(str);

        return flag;
    }
    
    /**
	 * 检查指定的字符串中
	 * @param str 要检查的字符串
	 * @param flag 指定的字符串
	 * @return
	 */
	public static boolean isExit(String str, String flag) {
		
		if(str.indexOf(flag) > 0) {
			
			return false;
		}
		
		return true;
	}
    
    /**
	 * 获取字符串的长度
	 * 如果字符串是 null，返回 0
	 * @param str
	 * @return
	 */
	public static int length(String str) {
		
		return str == null ? 0 : str.length();
	}

    /**
     * 判断给定的字符序列是否为空白。空白的定义是该字符序列要么是null，要么全部由空白字符（如空格、制表符、换行符等）组成。
     *
     * @param cs 待判断的字符序列。
     * @return 如果字符序列是null或全部由空白字符组成，则返回true；否则返回false。
     */
    public static boolean isBlank(CharSequence cs) {
        // 计算字符序列的长度
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            // 遍历字符序列中的每个字符，检查是否有非空白字符
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    // 只要发现一个非空白字符，即返回false
                    return false;
                }
            }

            // 字符串中所有字符都是空白字符，返回true
            return true;
        } else {
            // 字符序列为null或空，也返回true
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}
