package com.sharepay.wifi.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sharepay.wifi.R;

/**
 * Created on 2017/12/18.
 */

public class DialogUtils {

    /**
     * 显示一个通用的dialog框
     * 
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param desc
     *            描述
     * @param mOnCancel
     *            取消点击监听
     * @param mOnSure
     *            确定点击监听
     */
    public static void showDialog(Context context, String title, String desc, final OnDialogClickListener mOnCancel, final OnDialogClickListener mOnSure) {
        show(context, title, desc, mOnCancel, mOnSure);
    }

    /**
     * 显示一个通用的dialog框
     * 
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param desc
     *            描述
     * @param mOnSure
     *            确定点击监听
     */
    public static void showDialog(Context context, String title, String desc, final OnDialogClickListener mOnSure) {
        show(context, title, desc, null, mOnSure);
    }

    /**
     * 显示通用的dialog框的基本方法
     * 
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param desc
     *            描述
     * @param mOnCancel
     *            取消点击监听
     * @param mOnSure
     *            确定点击监听
     */
    private static void show(Context context, String title, String desc, final OnDialogClickListener mOnCancel, final OnDialogClickListener mOnSure) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.layout_dialog);
        window.setBackgroundDrawableResource(R.color.transparent);
        TextView tvTitle = window.findViewById(R.id.tv_dialog_title);
        TextView tvDesc = window.findViewById(R.id.tv_dialog_describe);
        TextView tvCancel = window.findViewById(R.id.tv_dialog_cancel);
        TextView tvSure = window.findViewById(R.id.tv_dialog_sure);

        tvTitle.setText(title);
        tvDesc.setText(desc);
        if (mOnCancel == null) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.VISIBLE);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCancel.onClick();
                    alertDialog.dismiss();
                }
            });
        }
        if (mOnSure == null) {
            tvSure.setVisibility(View.GONE);
        } else {
            tvSure.setVisibility(View.VISIBLE);
            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSure.onClick();
                    alertDialog.dismiss();
                }
            });
        }
    }

    public interface OnDialogClickListener {
        void onClick();
    }
}
