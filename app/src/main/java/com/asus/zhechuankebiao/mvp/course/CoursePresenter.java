package com.asus.zhechuankebiao.mvp.course;

import com.asus.zhechuankebiao.app.Cache;
import com.asus.zhechuankebiao.data.beanv2.CourseV2;
import com.asus.zhechuankebiao.data.greendao.CourseV2Dao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;




public class CoursePresenter implements CourseContract.Presenter {

    private CourseContract.View mView;

    public CoursePresenter(CourseContract.View mCourseView) {
        this.mView = mCourseView;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        //nothing
    }

    @Override
    public void updateCourseViewData(final long csNameId) {
        Observable.create(new ObservableOnSubscribe<List<CourseV2>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CourseV2>> emitter) throws Exception {
                final List<CourseV2> courses = Cache.instance().getCourseV2Dao()
                        .queryBuilder()
                        .where(CourseV2Dao.Properties.CouCgId.eq(csNameId))//根据当前课表组id查询
                        .where(CourseV2Dao.Properties.CouDeleted.eq(false))//查询没有删除的
                        .list();

                emitter.onNext(courses);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CourseV2>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CourseV2> courses) {
                        if (mView == null) {
                            //view被销毁
                            return;
                        }
                        mView.setCourseData(courses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteCourse(long courseId) {
        //Cache.instance().getCourseV2Dao().deleteByKey(courseId);
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();
        CourseV2 courseV2 = courseV2Dao.queryBuilder()
                .where(CourseV2Dao.Properties.CouId.eq(courseId))
                .unique();

        if (courseV2 != null) {
            courseV2.setCouDeleted(true);
            courseV2Dao.update(courseV2);
        }

        mView.updateCoursePreference(); //must be main thread
    }

    @Override
    public void onDestroy() {
        mView = null;
//        System.gc();
    }
}
