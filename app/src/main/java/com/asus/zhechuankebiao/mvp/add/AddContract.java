package com.asus.zhechuankebiao.mvp.add;

import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;
import com.asus.zhechuankebiao.data.beanv2.CourseV2;



public interface AddContract {
    interface Presenter extends BasePresenter {
        void addCourse(CourseV2 courseV2);
        void removeCourse(long courseId);
        void updateCourse(CourseV2 courseV2);
    }

    interface View extends BaseView<AddContract.Presenter> {
        void showAddFail(String msg);
        void onAddSucceed(CourseV2 courseV2);
        void onDelSucceed();
        void onUpdateSucceed(CourseV2 courseV2);
    }
}
