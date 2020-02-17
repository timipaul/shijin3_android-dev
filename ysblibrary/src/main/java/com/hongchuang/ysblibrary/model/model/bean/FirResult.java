package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by Administrator on 2018/6/7.
 */

public class FirResult {
//    name	:	新app
//
//    version	:	107
//
//    changelog	:	test
//
//    updated_at	:	1528285497
//
//    versionShort	:	1.0.7
//
//    build	:	107
//
//    installUrl	:	http://download.fir.im/v2/app/install/5b17c925ca87a877ec3318f0?download_token=e5a68d1a61a71e2e1be976bd9fb2a83f&source=update
//
//    install_url	:	http://download.fir.im/v2/app/install/5b17c925ca87a877ec3318f0?download_token=e5a68d1a61a71e2e1be976bd9fb2a83f&source=update
//
//    direct_install_url	:	http://download.fir.im/v2/app/install/5b17c925ca87a877ec3318f0?download_token=e5a68d1a61a71e2e1be976bd9fb2a83f&source=update
//
//    update_url	:	http://fir.im/cvtb
//
//    binary		{1}
//
//    fsize	:	11479011
    public String name;
    public String version;
    public String changelog;
    public String updated_at;
    public String versionShort;
    public String build;
    public String install_url;
    public String installUrl;
    public String direct_install_url;
    public String update_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public Fsize getBinary() {
        return binary;
    }

    public void setBinary(Fsize binary) {
        this.binary = binary;
    }

    public Fsize binary;

    public class Fsize{
        public String getFsize() {
            return fsize;
        }

        public void setFsize(String fsize) {
            this.fsize = fsize;
        }

        public String fsize;
    }
    public boolean isSuccess() {
        return true;
    }
}
