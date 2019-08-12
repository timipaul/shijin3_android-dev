package com.hongchuang.hclibrary.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.hongchuang.hclibrary.utils.TextUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/***
 * 功能描述:应用数据存储（文件）管理类
 * 作者:qiujialiu
 * 时间:2016/12/26
 * 版本:1.0
 ***/

public class StorageManager {
    private static StorageManager instance;
    //外部目录统一在Android/data/包名下，除有不能和应用一起删除的外（多应用数据共享)
    //应用存储数据目录
    private static String FILE_DIR;
    //临时文件目录
    private static String TEMP_PATH;

    //媒体文件目录
    private static String MEDIA_PATH;
    //图片目录
    private static String MEDIA_IMAGE_PATH;
    //视频目录
    private static String MEDIA_VIDEO_PATH;
    //日志目录
    private static String LOG_PATH;

    private static String NIM_PATH;

    private Map<String, String> mVideoThumbnailMap;

    private StorageManager() {
        mVideoThumbnailMap = new HashMap<>();
    }

    public static synchronized StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public static void initFilePath(Context context, String name) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        try {
            FILE_DIR = context.getExternalFilesDir(null) + File.separator + name;
            MEDIA_PATH = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
        } catch (Exception e) {
            String docPath = context.getFilesDir() + "/" + name;
            File docFile = new File(docPath);
            if (!docFile.exists()) {
                docFile.mkdir();
            }
            FILE_DIR = docPath;
            MEDIA_PATH = FILE_DIR + "/DCIM";

        }
        TEMP_PATH = FILE_DIR + File.separator + "temp";
        MEDIA_IMAGE_PATH = MEDIA_PATH + File.separator + "images";
        MEDIA_VIDEO_PATH = MEDIA_PATH + File.separator + "videos";
        LOG_PATH = FILE_DIR + "/log";
        NIM_PATH = FILE_DIR + "/nim";
    }

    public static String getNimPath() {
        return NIM_PATH;
    }

    public static String getFileDir() {
        return FILE_DIR;
    }

    public static String getLogPath() {
        return LOG_PATH;
    }

    public static String getMediaVideoPath() {
        return MEDIA_VIDEO_PATH;
    }

    public static String getMediaImagePath() {
        return MEDIA_IMAGE_PATH;
    }

    public static String getMediaPath() {
        return MEDIA_PATH;
    }

    public static String getTempPath() {
        return TEMP_PATH;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (File file : files) {
            if (file.isFile()) {
                //删除子文件
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /*******目录获取**********/

    public void deleteTempFile() {
        deleteFolder(TEMP_PATH);
    }

    public boolean deleteFolder(String filePath) {
        if (TextUtil.isEmpty(filePath)) return false;
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

}
