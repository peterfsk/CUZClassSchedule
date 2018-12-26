package com.asus.zhechuankebiao.mvp.about;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asus.zhechuankebiao.BaseActivity;
import com.asus.zhechuankebiao.Html5Activity;
import com.asus.zhechuankebiao.R;
import com.asus.zhechuankebiao.app.app;
import com.asus.zhechuankebiao.data.beanv2.VersionWrapper;
import com.asus.zhechuankebiao.utils.DialogHelper;
import com.asus.zhechuankebiao.utils.RequestPermission;
import com.asus.zhechuankebiao.utils.ToastUtils;
import com.asus.zhechuankebiao.utils.spec.JiuMi;
import com.asus.zhechuankebiao.utils.spec.VersionUpdate;


public class AboutActivity extends BaseActivity implements AboutContract.View {

    private AboutContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initToolbar();
        initLinkTextView();
        initVersionName();
        initCheckUpdate();

        new AboutPresenter(this);
    }

    private void initToolbar() {
        initBackToolbar(getString(R.string.about));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_about, menu);
        return true;
    }

    private void initVersionName() {
        TextView tvVersionName = findViewById(R.id.tv_version);

        VersionUpdate vu = new VersionUpdate();
        String versionName = vu.getLocalVersionName(app.mContext);
        tvVersionName.setText(versionName);
    }

    private void initCheckUpdate() {
        TextView tv = findViewById(R.id.tv_check_update);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkUpdate();
            }
        });
    }

    private void initLinkTextView() {
        findViewById(R.id.tv_github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.github_zhechuankebiao));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }


    @Override
    public void showMassage(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void showUpdateVersionInfo(VersionWrapper.Version version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("有新版本")
                .setMessage(version.getDescribe())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            VersionUpdate.goToMarket(getBaseContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent = new Intent(AboutActivity.this, Html5Activity.class);
                            Bundle bundle = new Bundle();
//                            bundle.putString("url", Url.URL_UPDATE_WEB);
                            bundle.putString("title", "浙传课表");
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                        }
                    }
                });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_donate:
                donate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void donate() {
        View view = View.inflate(this, R.layout.dialog_donate, null);
        view.setBackgroundColor(getResources().getColor(R.color.white));
        final Dialog dialog = new DialogHelper().buildBottomDialog(this, view);
        final Activity activity = this;
        view.findViewById(R.id.iv_aininmen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JiuMi.jiumi(activity);

            }
        });
        view.findViewById(R.id.iv_heihei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JiuMi.jiumi(activity);
            }
        });

        dialog.show();
    }

    /**
     * 捐献权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
