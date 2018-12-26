package com.asus.zhechuankebiao.mvp.about;

import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;
import com.asus.zhechuankebiao.data.beanv2.VersionWrapper;



public interface AboutContract {
    interface Presenter extends BasePresenter {
        void checkUpdate();
    }

    interface View extends BaseView<AboutContract.Presenter> {
        void showMassage(String notice);
        void showUpdateVersionInfo(VersionWrapper.Version version);
    }
}
