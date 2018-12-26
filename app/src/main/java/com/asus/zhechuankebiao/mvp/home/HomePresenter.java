package com.asus.zhechuankebiao.mvp.home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.asus.zhechuankebiao.R;
import com.asus.zhechuankebiao.app.AppUtils;
import com.asus.zhechuankebiao.app.Cache;
import com.asus.zhechuankebiao.app.Url;
import com.asus.zhechuankebiao.app.app;
import com.asus.zhechuankebiao.data.beanv2.BaseBean;
import com.asus.zhechuankebiao.data.beanv2.CourseGroup;
import com.asus.zhechuankebiao.data.beanv2.CourseV2;
import com.asus.zhechuankebiao.data.beanv2.DownCourseWrapper;
import com.asus.zhechuankebiao.data.beanv2.ShareBean;
import com.asus.zhechuankebiao.data.beanv2.UserWrapper;
import com.asus.zhechuankebiao.data.greendao.CourseGroupDao;
import com.asus.zhechuankebiao.data.greendao.CourseV2Dao;
import com.asus.zhechuankebiao.data.http.HttpCallback;
import com.asus.zhechuankebiao.data.http.MyHttpUtils;
import com.asus.zhechuankebiao.utils.LogUtil;
import com.asus.zhechuankebiao.utils.Preferences;
import com.asus.zhechuankebiao.utils.ScreenUtils;
import com.asus.zhechuankebiao.utils.event.CourseDataChangeEvent;
import com.asus.zhechuankebiao.utils.spec.QRCode;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;

    private Map<String, Long> mCacheGroup;

    public HomePresenter(HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    /**
     * 显示所有课程表
     */
    @Override
    public void showGroup() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao().loadAll();
        if (groups == null || groups.isEmpty()) {
            mView.showMassage("没有课程数据");
            return;
        }
        mView.showGroupDialog(groups);
    }

    /**
     * 建立分享
     */
    @Override
    public void createShare(final long groupId, final String groupName) {
        mView.showLoading("建立分享中");
        Observable.create(new ObservableOnSubscribe<ShareBean>() {
            @Override
            public void subscribe(final ObservableEmitter<ShareBean> emitter) throws Exception {
                List<CourseV2> list = Cache.instance().getCourseV2Dao()
                        .queryBuilder()
                        .where(CourseV2Dao.Properties.CouCgId.eq(groupId))
                        .where(CourseV2Dao.Properties.CouDeleted.eq(false))
                        .list();

                if (list != null && !list.isEmpty()) {
                    String json = buildJsonOfGroups(list, groupName);
                    MyHttpUtils utils = new MyHttpUtils();
                    utils.uploadShare(json, new HttpCallback<ShareBean>() {
                        @Override
                        public void onSuccess(ShareBean bean) {
                            emitter.onNext(bean);
                        }

                        @Override
                        public void onFail(String errMsg) {
                            emitter.onError(new Exception(errMsg));
                        }
                    });
                } else {
                    emitter.onError(new Exception("该课表没有课程"));
                }
            }

        }).map(new Function<ShareBean, Bitmap>() {
            @Override
            public Bitmap apply(ShareBean bean) throws Exception {
                if (bean == null || TextUtils.isEmpty(bean.getData())) {
                    return null;
                }
                Bitmap logo = BitmapFactory.decodeResource(Cache.instance().getContext().getResources(),
                        R.mipmap.ic_launcher_round);
                final int width = ScreenUtils.dp2px(150);
                String content = Url.URL_SHARE + "?id=" + bean.getData();

                return new QRCode().makeQRCodeImage(content, width, width, logo);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (!mView.isActive()) {
                            return;
                        }
                        mView.stopLoading();
                        if (bitmap == null) {
                            mView.showMassage("分享失败！");
                            return;
                        }
                        mView.createQRCodeSucceed(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!mView.isActive()) {
                            return;
                        }
                        mView.stopLoading();
                        mView.showMassage(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 下载解析分享
     */
    @Override
    public void downShare(String url) {
        if (TextUtils.isEmpty(url)) {
            mView.showMassage("分享为空");
            return;
        }

        mView.showLoading("导入中");
        new MyHttpUtils().downShare(url, new HttpCallback<DownCourseWrapper>() {
            @Override
            public void onSuccess(DownCourseWrapper downCourseWrapper) {
                if (mView == null) {//view被销毁
                    return;
                }
                mView.stopLoading();
                if (downCourseWrapper == null || downCourseWrapper.getData() == null) {
                    mView.showMassage("导入数据为空");
                    return;
                }
                long newGroupId = writeShare(downCourseWrapper.getData());
                // 通知更新

                // 切换到当前课表
                Preferences.putLong(app.mContext.getString(
                        R.string.app_preference_current_cs_name_id), newGroupId);
                mView.showMassage("导入成功！");
                EventBus.getDefault().post(new CourseDataChangeEvent());
            }

            @Override
            public void onFail(String errMsg) {
                if (mView == null) {//view被销毁
                    return;
                }
                mView.stopLoading();
                mView.showMassage(errMsg);
            }
        });

    }

    /**
     * 分享写入本地
     */
    private long writeShare(List<DownCourseWrapper.DownCourse> data) {
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();

        CourseGroup group = new CourseGroup();
        group.setCgName("来自热心网友分享" + AppUtils.createUUID().substring(0, 8));
        long newGroupId = Cache.instance().getCourseGroupDao().insert(group);

        for (DownCourseWrapper.DownCourse downCourse : data) {
            CourseV2 courseV2 = new CourseV2()
                    .setCouOnlyId(AppUtils.createUUID()) //new only_id
                    .setCouCgId(newGroupId) //new group
                    .setCouName(downCourse.getName())
                    .setCouTeacher(downCourse.getTeacher())
                    .setCouLocation(downCourse.getLocation())
                    .setCouColor(downCourse.getColor())
                    .setCouWeek(downCourse.getWeek())
                    .setCouStartNode(downCourse.getStart_node())
                    .setCouNodeCount(downCourse.getNode_count())
                    .setCouAllWeek(downCourse.getAll_week());

            courseV2Dao.insert(courseV2);
        }

        return newGroupId;
    }

    /**
     * 建立json
     */
    private String buildJsonOfGroups(List<CourseV2> courseV2s, String groupName) {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CourseV2 course : courseV2s) {
            try {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("id", course.getCouId());
                jsonItem.put("name", course.getCouName());
                jsonItem.put("location", course.getCouLocation() == null ? "" : course.getCouLocation());
                jsonItem.put("week", course.getCouWeek());
                jsonItem.put("teacher", course.getCouTeacher() == null ? "" : course.getCouTeacher());
                jsonItem.put("all_week", course.getCouAllWeek());
                jsonItem.put("start_node", course.getCouStartNode());
                jsonItem.put("node_count", course.getCouNodeCount());
                jsonItem.put("color", course.getCouColor() == null ? "-1" : course.getCouColor());
                jsonItem.put("group_name", groupName);

                jsonArray.put(jsonItem);
            } catch (JSONException e) {
                LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toString());
                e.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 对所有course建立json
     */
    @NonNull
    private JSONObject buildJsonOfAllCourse() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao()
                .queryBuilder().list();
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();

        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CourseGroup group : groups) {
            List<CourseV2> courseV2s = courseV2Dao.queryBuilder()
                    .where(CourseV2Dao.Properties.CouCgId.eq(group.getCgId()))
                    .list();

            for (CourseV2 course : courseV2s) {
                try {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("id", course.getCouId());
                    jsonItem.put("name", course.getCouName());
                    jsonItem.put("location", course.getCouLocation() == null ? "" : course.getCouLocation());
                    jsonItem.put("week", course.getCouWeek());
                    jsonItem.put("teacher", course.getCouTeacher() == null ? "" : course.getCouTeacher());
                    jsonItem.put("all_week", course.getCouAllWeek());
                    jsonItem.put("start_node", course.getCouStartNode());
                    jsonItem.put("node_count", course.getCouNodeCount());
                    jsonItem.put("color", course.getCouColor() == null ? "-1" : course.getCouColor());
                    jsonItem.put("group_name", group.getCgName());
                    jsonItem.put("only_id", course.getCouOnlyId());
                    jsonItem.put("deleted", course.getCouDeleted());

                    jsonArray.put(jsonItem);
                } catch (JSONException e) {
                    LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toString());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    /**
     * 覆盖本地
     */
    private void overWriteLocal(List<DownCourseWrapper.DownCourse> downCourses) {
        mCacheGroup = new HashMap<>();
        CourseV2Dao courseDao = Cache.instance().getCourseV2Dao();

        for (DownCourseWrapper.DownCourse downCourse : downCourses) {
            Long groupId = getGroupId(downCourse);
            if (groupId != null) {
                CourseV2 oldCourse = courseDao.queryBuilder()
                        .where(CourseV2Dao.Properties.CouOnlyId.eq(downCourse.getOnly_id()))
                        .unique();
                if (oldCourse != null) {
                    // 删除手机上的数据 （覆盖）
                    courseDao.delete(oldCourse);
                }
                addCourse(downCourse, groupId);
            }
        }
    }

    /**
     * 添加
     */
    private void addCourse(DownCourseWrapper.DownCourse downCourse, Long groupId) {
        CourseV2Dao courseDao = Cache.instance().getCourseV2Dao();

        CourseV2 oldCourse = new CourseV2()
                .setCouOnlyId(downCourse.getOnly_id())
                .setCouCgId(groupId)
                .setCouName(downCourse.getName())
                .setCouTeacher(downCourse.getTeacher())
                .setCouLocation(downCourse.getLocation())
                .setCouColor(downCourse.getColor())
                .setCouWeek(downCourse.getWeek())
                .setCouStartNode(downCourse.getStart_node())
                .setCouNodeCount(downCourse.getNode_count())
                .setCouAllWeek(downCourse.getAll_week());

        courseDao.insert(oldCourse);
    }

    /**
     * 获取group_id
     */
    private Long getGroupId(DownCourseWrapper.DownCourse downCourse) {
        if (downCourse == null) {
            return null;
        }

        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();

        String groupName = downCourse.getGroup_name();
        if (!TextUtils.isEmpty(groupName)) {
            Long groupId = mCacheGroup.get(groupName);
            if (null == groupId) {
                CourseGroup dbGroup = groupDao.queryBuilder()
                        .where(CourseGroupDao.Properties.CgName.eq(groupName))
                        .unique();
                if (dbGroup == null) {
                    CourseGroup newGroup = new CourseGroup().setCgName(groupName);
                    groupId = groupDao.insert(newGroup);
                } else {
                    groupId = dbGroup.getCgId();
                }
                mCacheGroup.put(groupName, groupId);
            }
            return groupId;
        } else {
            LogUtil.e(this, "下载的数据未找到group_name");
            return null;
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
