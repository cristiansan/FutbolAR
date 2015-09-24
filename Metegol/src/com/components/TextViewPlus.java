package com.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pmovil.soccer.R;

import java.util.HashMap;
import java.util.Map;

public class TextViewPlus extends TextView {

    private static final String TAG = "TextViewPlus";
    private static Map<String, Typeface> typefaces = null;

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        if (typefaces == null)
            typefaces = new HashMap<String, Typeface>(3);

        TypedArray a = ctx.obtainStyledAttributes(attrs,
                R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        if (typefaces.containsKey(asset)) {
            tf = typefaces.get(asset);
        } else {
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), asset);
                typefaces.put(asset, tf);
            } catch (Exception e) {
                ////Log.e(TAG, "Could not get typeface: " + e.getMessage());
                return false;
            }
        }
        setTypeface(tf);
        return true;
    }
}