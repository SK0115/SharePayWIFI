package com.sharepay.wifi.util;

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sharepay.wifi.R;
import com.sharepay.wifi.SPApplication;

/**
 * Created on 2017/12/18. 吐司相关工具类
 */

public class ToastUtils {
    private static Toast mToast;
    private static int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private static int xOffset = 0;
    private static int yOffset = (int) (64 * SPApplication.getContext().getResources().getDisplayMetrics().density + 0.5);

    /**
     * 显示短时吐司
     *
     * @param text
     *            文本
     */
    public static void showShort(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId
     *            资源Id
     */
    public static void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司
     *
     * @param resId
     *            资源Id
     * @param duration
     *            显示时长
     */
    private static void show(@StringRes int resId, int duration) {
        show(SPApplication.getContext().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param text
     *            文本
     * @param duration
     *            显示时长
     */
    private static void show(CharSequence text, int duration) {
        cancel();
        // 加载Toast布局
        View customView = LayoutInflater.from(SPApplication.getContext()).inflate(R.layout.layout_toast, null);
        // 初始化布局控件
        TextView mTextView = customView.findViewById(R.id.tv_toast_text);
        // 为控件设置属性
        mTextView.setText(text);
        mToast = new Toast(SPApplication.getContext());
        mToast.setView(customView);
        mToast.setDuration(duration);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
