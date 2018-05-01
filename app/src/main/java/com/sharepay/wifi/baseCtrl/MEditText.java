package com.sharepay.wifi.baseCtrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.EditText;

import com.sharepay.wifi.R;

import java.lang.reflect.Field;

@SuppressLint("AppCompatCustomView")
public class MEditText extends EditText {

    public MEditText(Context context) {
        this(context, null);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        modifyCursorDrawable(context, attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        modifyCursorDrawable(context, attrs);
    }

    private void modifyCursorDrawable(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HQEditText);
        int drawable = a.getResourceId(R.styleable.HQEditText_textCursorDrawable, 0);
        if (drawable != 0) {
            try {

                Field setCursor = TextView.class.getDeclaredField("mCursorDrawableRes");
                setCursor.setAccessible(true);
                setCursor.set(this, drawable);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        a.recycle();
    }
}
