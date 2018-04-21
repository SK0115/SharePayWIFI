package com.sharepay.wifi.baseCtrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ProgressView extends ImageView {

    private RotateAnimation mAnim;

    public ProgressView(Context context) {
        super(context);
        initAnimation();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAnimation();
    }

    private void initAnimation() {
        if (getVisibility() == View.VISIBLE) {
            startRotateAnimation();
        }
    }

    public void startRotateAnimation() {
        if (mAnim == null) {
            mAnim = new RotateAnimation(0, 36000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mAnim.setDuration(150000);
            mAnim.setInterpolator(new LinearInterpolator());
            mAnim.setRepeatCount(-1);
            setAnimation(mAnim);
            mAnim.start();
        }
    }

    public void stopRotateAnimation() {
        if (mAnim != null) {
            mAnim.cancel();
            mAnim = null;
        }
    }

    public void releaseAnimation() {
        releaseAnimation(true);
    }

    public void releaseAnimation(boolean releaseDrawble) {
        if (mAnim != null) {
            mAnim.cancel();
            clearAnimation();
            mAnim = null;
        }
        if (releaseDrawble) {
            setImageDrawable(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnim != null) {
            releaseAnimation();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            startRotateAnimation();
        } else {
            releaseAnimation(false);
        }
        super.setVisibility(visibility);
    }
}
