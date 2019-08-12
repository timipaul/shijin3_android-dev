package retrofit.utils;

import com.google.gson.Gson;

import java.util.Map;

/***
 * 功能描述:请求和返回的加解密
 * 作者:qiujialiu
 * 时间:2017/6/12
 ***/

public class ParamsAndResultUtil {
    private static ParamsAndResultUtil instance;
    private Gson gson;

    private ParamsAndResultUtil() {
        gson = new Gson();
    }

    public static ParamsAndResultUtil getInstance() {
        if (instance == null) {
            instance = new ParamsAndResultUtil();
        }
        return instance;
    }

    public String getBase64Params(String params) {
        return Base64.encode(params);
    }

    public String getBase64Params(Map<String, Object> params) {
        String paramsStr = gson.toJson(params);
        return Base64.encode(paramsStr);
    }

}
