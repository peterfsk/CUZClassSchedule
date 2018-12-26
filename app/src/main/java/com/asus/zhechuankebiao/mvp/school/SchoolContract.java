package com.asus.zhechuankebiao.mvp.school;


import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;



public interface SchoolContract {
    interface Presenter extends BasePresenter {
        void testUrl(String url);
    }

    interface View extends BaseView<Presenter> {
        void showNotice(String notice);

        void showInputDialog();

        void testingUrl(boolean bool);

        void testUrlFailed(String url);

        void testUrlSucceed(String url);
    }
}
