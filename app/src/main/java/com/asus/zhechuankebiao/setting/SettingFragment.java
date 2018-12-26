package com.asus.zhechuankebiao.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.asus.zhechuankebiao.R;
import com.asus.zhechuankebiao.app.app;
import com.asus.zhechuankebiao.mvp.course.CourseActivity;
import com.asus.zhechuankebiao.utils.ActivityUtil;
import com.asus.zhechuankebiao.utils.DialogHelper;
import com.asus.zhechuankebiao.utils.DialogListener;
import com.asus.zhechuankebiao.utils.Preferences;
import com.asus.zhechuankebiao.utils.ScreenUtils;
import com.asus.zhechuankebiao.utils.spec.JiuMi;

import static com.asus.zhechuankebiao.app.Constant.themeColorArray;
import static com.asus.zhechuankebiao.app.Constant.themeNameArray;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.page_setting);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String title = (String) preference.getTitle();
        if (title.equals("颜色风格")) {
            showThemeDialog();
            return true;
        } else if (title.equals("赞一个")) {
            donate();
            return true;
        }
        return false;
    }

    int theme;

    private void showThemeDialog() {
        ScrollView scrollView = new ScrollView(getActivity());
        RadioGroup radioGroup = new RadioGroup(getActivity());
        scrollView.addView(radioGroup);
        int margin = ScreenUtils.dp2px(16);
        radioGroup.setPadding(margin / 2, margin, margin, margin);

        for (int i = 0; i < themeColorArray.length; i++) {
            AppCompatRadioButton arb = new AppCompatRadioButton(getActivity());

            RadioGroup.LayoutParams params =
                    new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

            arb.setLayoutParams(params);
            arb.setId(i);
            arb.setTextColor(getResources().getColor(themeColorArray[i]));
            arb.setText(themeNameArray[i]);
            arb.setTextSize(16);
            arb.setPadding(0, margin / 2, 0, margin / 2);
            radioGroup.addView(arb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                theme = checkedId;
            }
        });

        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showCustomDialog(getActivity(), scrollView,
                getString(R.string.theme_preference), new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        dialog.dismiss();
                        String key = getString(R.string.app_preference_theme);
                        int oldTheme = Preferences.getInt(key, 0);

                        if (theme != oldTheme) {
                            Preferences.putInt(key, theme);
                            ActivityUtil.finishAll();
                            startActivity(new Intent(app.mContext, CourseActivity.class));
                        }
                    }
                });
    }

    private void donate() {
        View view = View.inflate(getActivity(), R.layout.dialog_donate, null);
        view.setBackgroundColor(getResources().getColor(R.color.white));
        final Dialog dialog = new DialogHelper().buildBottomDialog(getActivity(), view);

        view.findViewById(R.id.iv_aininmen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JiuMi.jiumi(getActivity());
            }
        });
        view.findViewById(R.id.iv_heihei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JiuMi.jiumi(getActivity());
            }
        });

        dialog.show();
    }

}