package com.asus.zhechuankebiao.mvp.home;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;
import com.asus.zhechuankebiao.data.beanv2.CourseGroup;
import com.asus.zhechuankebiao.data.beanv2.UserWrapper;

import java.util.List;

public interface HomeContract {
    interface Presenter extends BasePresenter {

        void showGroup();

        void createShare(long groupId, String groupName);

        void downShare(String url);


    }

    interface View extends BaseView<HomeContract.Presenter> {
        boolean isActive();

        void showMassage(String msg);


        void showLoading(String msg);

        void stopLoading();


        void showGroupDialog(List<CourseGroup> groups);

        void createQRCodeSucceed(Bitmap bitmap);

    }
}
