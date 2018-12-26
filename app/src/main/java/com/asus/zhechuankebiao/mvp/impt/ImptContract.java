package com.asus.zhechuankebiao.mvp.impt;


import android.widget.ImageView;

import com.asus.zhechuankebiao.BasePresenter;
import com.asus.zhechuankebiao.BaseView;
import com.asus.zhechuankebiao.data.bean.CourseTime;



public interface ImptContract {
    interface Presenter extends BasePresenter {

        void importCustomCourses(String courseTime, String term);

        void importDefaultCourses(String courseTime, String term);

        void loadCourseTimeAndTerm(String xh, String pwd, String captcha);

        void getCaptcha();
    }

    interface View extends BaseView<Presenter> {
        ImageView getCaptchaIV();

        void showImpting();

        void hideImpting();

        void captchaIsLoading(boolean isLoading);

        void showErrToast(String errMsg, boolean reLoad);

        void showSucceed();

        void showCourseTimeDialog(CourseTime times);
    }

    interface Model {
        void getCaptcha(ImageView iv);
    }
}
