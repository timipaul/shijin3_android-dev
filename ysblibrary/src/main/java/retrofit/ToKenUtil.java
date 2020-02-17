package retrofit;


import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBLibrary;

/***
 * 功能描述: 授权Token相关工具类
 * 作者:
 * 时间:2017/05/31
 * 版本:
 ***/
public class ToKenUtil {

    public static String getToken() {
        return ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_TOKEN);
    }

    public static String getRefreshToken() {
        return ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_REFRESH_TOKEN);
    }

    public static void saveToken(String token) {
        ShareDataManager.getInstance().save(YSBLibrary.getLibrary().getContext(), SharedPreferencesKey.KEY_TOKEN, token);
    }

    public static void saveRefreshToken(String token) {
        ShareDataManager.getInstance().save(YSBLibrary.getLibrary().getContext(), SharedPreferencesKey.KEY_REFRESH_TOKEN, token);
    }

    public static void deleteToken() {
        ShareDataManager.getInstance().delete(YSBLibrary.getLibrary().getContext(), SharedPreferencesKey.KEY_TOKEN);
    }

    public static void deleteRefreshToken() {
        ShareDataManager.getInstance().delete(YSBLibrary.getLibrary().getContext(), SharedPreferencesKey.KEY_REFRESH_TOKEN);
    }
}
