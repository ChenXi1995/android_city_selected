package com.cxh.android_city_select;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author chenx
 */
public class NoScrollGridView extends GridView{
    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2 ,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,height);
    }
}