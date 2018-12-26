package com.asus.zhechuankebiao.mvp.add;

import com.asus.zhechuankebiao.data.beanv2.CourseV2;



public class AddPresenter implements AddContract.Presenter {
    AddContract.View mView;

    public AddPresenter(AddContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        //do nothing
    }

    @Override
    public void addCourse(CourseV2 courseV2) {

    }

    @Override
    public void removeCourse(long courseId) {

    }

    @Override
    public void updateCourse(CourseV2 courseV2) {

    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
