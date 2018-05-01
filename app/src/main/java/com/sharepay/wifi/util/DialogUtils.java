package com.sharepay.wifi.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharepay.wifi.R;

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
        show(context, title, desc, false, mOnCancel, mOnSure);
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
        show(context, title, desc, false, null, mOnSure);
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
     * @param isEdit
     *            是否存在编辑框
     * @param mOnCancel
     *            取消点击监听
     * @param mOnSure
     *            确定点击监听
     */
    public static void showDialog(Context context, String title, String desc, boolean isEdit, final OnDialogClickListener mOnCancel,
            final OnDialogClickListener mOnSure) {
        show(context, title, desc, isEdit, mOnCancel, mOnSure);
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
     * @param isEdit
     *            是否存在编辑框
     * @param mOnCancel
     *            取消点击监听
     * @param mOnSure
     *            确定点击监听
     */
    private static void show(final Context context, String title, String desc, boolean isEdit, final OnDialogClickListener mOnCancel,
            final OnDialogClickListener mOnSure) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(new EditText(context));
        alertDialog.setCancelable(true);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.layout_dialog);
        window.setBackgroundDrawableResource(R.color.transparent);
        TextView tvTitle = window.findViewById(R.id.tv_dialog_title);
        TextView tvDesc = window.findViewById(R.id.tv_dialog_describe);
        final EditText etEdit = window.findViewById(R.id.et_dialog_edit);
        final ImageView ivEditSplitNormal = window.findViewById(R.id.image_edit_split_normal);
        final ImageView ivEditSplitFocus = window.findViewById(R.id.image_edit_split_focus);
        TextView tvCancel = window.findViewById(R.id.tv_dialog_cancel);
        TextView tvSure = window.findViewById(R.id.tv_dialog_sure);

        tvTitle.setText(title);
        if (isEdit) {
            etEdit.setVisibility(View.VISIBLE);
            ivEditSplitNormal.setVisibility(View.VISIBLE);
            tvDesc.setVisibility(View.GONE);
            WindowManager.LayoutParams windowParams = alertDialog.getWindow().getAttributes();
            windowParams.y = -150;
            alertDialog.getWindow().setAttributes(windowParams);
            etEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivEditSplitNormal.setVisibility(View.GONE);
                    ivEditSplitFocus.setVisibility(View.VISIBLE);
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            });
        } else {
            tvDesc.setText(desc);
            tvDesc.setVisibility(View.VISIBLE);
            etEdit.setVisibility(View.GONE);
        }

        if (mOnCancel == null) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.VISIBLE);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCancel.onClick(etEdit.getText().toString());
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
                    mOnSure.onClick(etEdit.getText().toString());
                    alertDialog.dismiss();
                }
            });
        }
    }

    public interface OnDialogClickListener {
        void onClick(String content);
    }
}
