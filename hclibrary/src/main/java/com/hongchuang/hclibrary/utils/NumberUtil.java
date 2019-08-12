package com.hongchuang.hclibrary.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.text.DecimalFormat;

/***
 * 功能描述:时间处理类
 * 作者:qiujialiu
 * 时间:2017/5/31
 ***/

public class NumberUtil {
    public static String count_double2String(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#");//格式化设置
        String str;
        if (value != null) {
            str = decimalFormat.format(value);
        } else {
            str = "0";
        }

        return str;
    }

    public static String double2String(Double value) {
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");//格式化设置
        String str;
        if (value != null) {
            str = decimalFormat.format(value);
        } else {
            str = "0.00";
        }
        if ("0.00".equals(str)) {
            str = "0.00";
        }
        return str;
    }

    public static String double2StringAdaptive(Double value) {
        if (value == null) {
            return "0";
        }
        if (value % 1 == 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#0");//格式化设置
            String result = decimalFormat.format(value);
            return result;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");//格式化设置
            String str;
            if (value != null) {
                str = decimalFormat.format(value);
            } else {
                str = "0";
            }
            if ("0.00".equals(str)) {
                str = "0";
            }
            if (str.matches("[\\d]+.00")) {
                str = str.substring(str.length() - 3);
            }
            return str;
        }
    }

    public static String double2StringComma(Double value) {
        if (value == null) {
            return "0";
        }
        if (value % 1 == 0) {
            DecimalFormat decimalFormat = new DecimalFormat(",##0");//格式化设置
            String result = decimalFormat.format(value);
            return result;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(",##0.00");//格式化设置
            String str;
            if (value != null) {
                str = decimalFormat.format(value);
            } else {
                str = "0";
            }
            if ("0.00".equals(str)) {
                str = "0";
            }
            if (str.matches("[\\d]+.00")) {
                str = str.substring(str.length() - 3);
            }
            return str;
        }
    }

    public static Double string2double(String str) {
        Double value;
        try {
            if (str != null && str.length() > 0) {
                str = str.replaceAll(",", "");
                value = Double.parseDouble(str);
            } else {
                value = 0d;
            }
        } catch (Exception e) {
            value = 0d;
        }
        return value;
    }


    public static int string2int(String str) {
        int value;
        try {
            if (str != null && str.length() > 0) {
                str = str.replaceAll(",", "");
                value = Integer.parseInt(str);
            } else {
                value = 0;
            }
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    public static InputFilter get2NumPoint(final int DECIMAL_DIGITS, final int MAX_LENGTH) {
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if (nullFilter(source)) return null;
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");//在点前后分开两段
                if (splitArray.length > 0) {
                    String intValue = splitArray[0];
                    int errorIndex = dValue.indexOf(".");
                    if (errorIndex == -1) {
                        errorIndex = dValue.length();
                    }
                    if (intValue.length() >= MAX_LENGTH - DECIMAL_DIGITS - 1 && dstart <= errorIndex) {
                        if (".".equals(source.toString())) {
                            return null;
                        }
                        return "";
                    }
                }
                //&&dstart==dValue.length()
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                    if (diff > 0) {
                        try {
                            return source.subSequence(start, end - diff);
                        } catch (IndexOutOfBoundsException e) {
                            return source;
                        }
                    }
                }
                if (dest.length() == MAX_LENGTH - 1 && ".".equals(source.toString())) {
                    return "";
                }
                if (dest.length() >= MAX_LENGTH) {
                    return "";
                }
                return null;
            }
        };
        return lengthfilter;
    }

    private static boolean nullFilter(CharSequence source) {
        return "".equals(source.toString());
    }

}
