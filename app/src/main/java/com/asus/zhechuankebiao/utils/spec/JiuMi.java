package com.asus.zhechuankebiao.utils.spec;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import com.asus.zhechuankebiao.R;
import com.longsh.optionframelibrary.OptionMaterialDialog;

public class JiuMi {
    public static void jiumi(Activity activity) {
        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(activity);
        mMaterialDialog.setTitle("感谢您的喜欢")
                .setTitleTextColor(R.color.bilibili_pink)
//                .setTitleTextSize((float) 22.5)
                .setMessage("啾咪(^.<)")
//                .setMessageTextColor(R.color.colorPrimary)
//                .setMessageTextSize((float) 16.5)
                .setPositiveButtonTextColor(R.color.primary_red)
//                .setNegativeButtonTextColor(R.color.colorPrimary)
//                .setPositiveButtonTextSize(15)
//                .setNegativeButtonTextSize(15)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //对话框消失后回调
                            }
                        })
                .show();


    }
}
