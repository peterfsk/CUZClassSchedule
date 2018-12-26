package com.asus.zhechuankebiao.mvp.about;

import com.asus.zhechuankebiao.R;
import com.asus.zhechuankebiao.app.app;
import com.asus.zhechuankebiao.data.beanv2.VersionWrapper;
import com.asus.zhechuankebiao.data.http.HttpCallback;
import com.asus.zhechuankebiao.utils.LogUtil;
import com.asus.zhechuankebiao.utils.ToastUtils;
import com.asus.zhechuankebiao.utils.spec.VersionUpdate;

public class AboutPresenter implements AboutContract.Presenter {
    private AboutContract.View mView;

    public AboutPresenter(AboutContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void checkUpdate() {
        if (mView == null) {
            //view被销毁
            return;
        }
        mView.showMassage(app.mContext.getString(R.string.already_the_latest_version));
//        mView.showMassage(app.mContext.getString(R.string.checking_for_updates));

//        final VersionUpdate versionUpdate = new VersionUpdate();
//        versionUpdate.checkUpdate(new HttpCallback<VersionWrapper>() {
//            @Override
//            public void onSuccess(VersionWrapper versionWrapper) {
//                if (mView == null) {
//                    //view被销毁
//                    return;
//                }
//                if (versionWrapper == null || versionWrapper.getData() == null) {
//                    mView.showMassage("返回数据为空");
//                    return;
//                }
//
//                if (versionWrapper.getCode() != 1) {
//                    mView.showMassage(versionWrapper.getMsg());
//                    return;
//                }
//
//                int localVersion = versionUpdate.getLocalVersion(app.mContext);
//
//                if (versionWrapper.getData().getCode() > localVersion) {
//                    mView.showUpdateVersionInfo(versionWrapper.getData());
//                } else {
//                    mView.showMassage(app.mContext.getString(R.string.already_the_latest_version));
//                }
//            }
//
//            @Override
//            public void onFail(String errMsg) {
//                if (mView == null) {
//                    //view被销毁
//                    return;
//                }
//                LogUtil.e(this, errMsg);
//                ToastUtils.show(app.mContext.getString(R.string.access_err));
//            }
//        });
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
