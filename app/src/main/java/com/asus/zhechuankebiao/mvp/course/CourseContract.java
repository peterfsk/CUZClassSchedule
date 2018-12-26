package com.asus.zhechuankebiao.mvp.course;


import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;
import com.asus.zhechuankebiao.data.beanv2.CourseV2;

import java.util.List;



public interface CourseContract {
    interface Presenter extends BasePresenter {
        void updateCourseViewData(long csNameId);
        void deleteCourse(long courseId);
    }

    interface View extends BaseView<Presenter> {
        void initFirstStart();
        void setCourseData(List<CourseV2> courses);
        void updateCoursePreference();
    }


}
