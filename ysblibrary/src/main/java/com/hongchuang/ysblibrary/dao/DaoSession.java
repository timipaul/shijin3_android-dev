package com.hongchuang.ysblibrary.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hongchuang.ysblibrary.dao.AreaBean;
import com.hongchuang.ysblibrary.dao.CityBean;
import com.hongchuang.ysblibrary.dao.ProvinceBean;

import com.hongchuang.ysblibrary.dao.AreaBeanDao;
import com.hongchuang.ysblibrary.dao.CityBeanDao;
import com.hongchuang.ysblibrary.dao.ProvinceBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig areaBeanDaoConfig;
    private final DaoConfig cityBeanDaoConfig;
    private final DaoConfig provinceBeanDaoConfig;

    private final AreaBeanDao areaBeanDao;
    private final CityBeanDao cityBeanDao;
    private final ProvinceBeanDao provinceBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        areaBeanDaoConfig = daoConfigMap.get(AreaBeanDao.class).clone();
        areaBeanDaoConfig.initIdentityScope(type);

        cityBeanDaoConfig = daoConfigMap.get(CityBeanDao.class).clone();
        cityBeanDaoConfig.initIdentityScope(type);

        provinceBeanDaoConfig = daoConfigMap.get(ProvinceBeanDao.class).clone();
        provinceBeanDaoConfig.initIdentityScope(type);

        areaBeanDao = new AreaBeanDao(areaBeanDaoConfig, this);
        cityBeanDao = new CityBeanDao(cityBeanDaoConfig, this);
        provinceBeanDao = new ProvinceBeanDao(provinceBeanDaoConfig, this);

        registerDao(AreaBean.class, areaBeanDao);
        registerDao(CityBean.class, cityBeanDao);
        registerDao(ProvinceBean.class, provinceBeanDao);
    }
    
    public void clear() {
        areaBeanDaoConfig.clearIdentityScope();
        cityBeanDaoConfig.clearIdentityScope();
        provinceBeanDaoConfig.clearIdentityScope();
    }

    public AreaBeanDao getAreaBeanDao() {
        return areaBeanDao;
    }

    public CityBeanDao getCityBeanDao() {
        return cityBeanDao;
    }

    public ProvinceBeanDao getProvinceBeanDao() {
        return provinceBeanDao;
    }

}
