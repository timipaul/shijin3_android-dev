//package com.hongchuang.hclibrary.storage;
//
//
//import android.os.Build;
//
//import com.hongchuang.hclibrary.utils.TextUtil;
//
//import org.xutils.DbManager;
//import org.xutils.ex.DbException;
//import org.xutils.x;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///***
// * 功能描述:数据库操作管理类（基于xutil3）
// * 作者:qiujialiu
// * 时间:2017/05/31
// * 版本:
// ***/
//public class MDBManager {
//    private static final String TAG = "MDBManager";
//    private static MDBManager mdbManager;
//    private static DbManager.DaoConfig daoConfig;
//    private Map<String, DbManager.DaoConfig> configMap;
//
//    private MDBManager() {
//        configMap = new HashMap<>();
//    }
//
//    public static synchronized MDBManager getInstance() {
//        return MDBManagerHolder.INSTANCE;
//    }
//
//    /**
//     * create database
//     *
//     * @param name database name
//     */
//    public void createDatabase(String name) {
//        if (TextUtil.isNotEmpty(name)) {
//            init(name);
//        } else {
//            init(Build.ID);
//        }
//    }
//
//    public void createDatabase(String tag, String name) {
//        if (TextUtil.isNotEmpty(name)) {
//            init(tag, name);
//        } else {
//            init(tag, Build.ID);
//        }
//    }
//
//    /**
//     * 2017/05/31 版本1
//     */
//    private void init(String tag, String mob) {
//        if (mdbManager == null) {
//            mdbManager = new MDBManager();
//        }
//        String mobDb = tag + mob + ".db";
//        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
//                .setDbName(mobDb)
//                // 不设置dbDir时, 默认存储在app的私有目录.
//                // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
//                .setDbVersion(1)
////                .setDbDir(new File("mnt/sdcard"))
//                .setDbOpenListener(new DbManager.DbOpenListener() {
//                    @Override
//                    public void onDbOpened(DbManager db) {
//                        // 开启WAL, 对写入加速提升巨大
//                        db.getDatabase().enableWriteAheadLogging();
//                    }
//                })
//                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                    @Override
//                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//
//                    }
//                });
//        configMap.put(tag, daoConfig);
//    }
//
//    /**
//     * 2017/05/31 版本1
//     */
//    private void init(String mob) {
//        if (mdbManager == null) {
//            mdbManager = new MDBManager();
//        }
//        String mobDb = mob + ".db";
//        daoConfig = new DbManager.DaoConfig()
//                .setDbName(mobDb)
//                // 不设置dbDir时, 默认存储在app的私有目录.
//                // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
//                .setDbVersion(1)
////                .setDbDir(new File("mnt/sdcard"))
//                .setDbOpenListener(new DbManager.DbOpenListener() {
//                    @Override
//                    public void onDbOpened(DbManager db) {
//                        // 开启WAL, 对写入加速提升巨大
//                        db.getDatabase().enableWriteAheadLogging();
//                    }
//                })
//                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                    @Override
//                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//
//                    }
//                });
//    }
//
//    /**
//     * 获取数据库管理类
//     *
//     * @return
//     */
//    public DbManager getDbManager() {
//        return x.getDb(daoConfig);
//    }
//
//    /**
//     * 获取数据库管理类
//     *
//     * @return
//     */
//    public DbManager getDbManager(String tag) {
//        if (tag != null && configMap.containsKey(tag)) {
//            return x.getDb(configMap.get(tag));
//        }
//        return x.getDb(daoConfig);
//    }
//
//    /**
//     * 清除对应表数据
//     *
//     * @param cls 要清除对应表的映射类
//     */
//    public void clean(Class cls) {
//        DbManager db = x.getDb(daoConfig);
//        try {
//            db.delete(cls);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 修改数据
//     */
//    public void updateEntity(Object data) {
//        DbManager db = x.getDb(daoConfig);
//        try {
//            db.saveOrUpdate(data);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 批量修改
//     */
//    public void updateEntityList(List datas) {
//
//        if (datas != null && datas.size() > 0) {
//            for (Object data : datas) {
//                updateEntity(data);
//            }
//        }
//    }
//
//    /**
//     * 删除单条数据实体
//     */
//    public void delEntity(Object data) {
//        DbManager db = x.getDb(daoConfig);
//        try {
//            db.delete(data);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 存储单实体数据（如果有主键相同的数据，存储失败）
//     */
//    public void saveEntity(Object data) {
//        DbManager db = x.getDb(daoConfig);
//        try {
//            db.save(data);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 存储数据列表（如果有主键相同的数据，存储失败）
//     */
//    public void saveEntityList(List datas) {
//        if (datas != null && datas.size() > 0) {
//            for (Object data : datas) {
//                saveEntity(data);
//            }
//        }
//    }
//
//    /**
//     * 获取列表数据
//     */
//    public <T> List<T> getList(Class<T> cls) {
//        DbManager db = x.getDb(daoConfig);
//        List result = null;
//        try {
//            result = db.findAll(cls);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public static class MDBManagerHolder {
//        public static final MDBManager INSTANCE = new MDBManager();
//    }
//
//}
