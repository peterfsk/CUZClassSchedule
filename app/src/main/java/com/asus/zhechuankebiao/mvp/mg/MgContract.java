package com.asus.zhechuankebiao.mvp.mg;

import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;



public interface MgContract {
    interface Presenter extends BasePresenter {
        void deleteCsName(long csNameId);
        void switchCsName(long csNameId);
        void reloadCsNameList();
        void addCsName(String csName);
        void editCsName(long id, String newCsName);
    }

    interface View extends BaseView<Presenter> {
        void showList();
        void showNotice(String notice);
        void gotoCourseActivity();
        void deleteFinish();
        void addCsNameSucceed();
        void editCsNameSucceed();
    }

}
