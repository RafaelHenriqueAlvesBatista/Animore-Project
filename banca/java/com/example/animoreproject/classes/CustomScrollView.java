package com.example.animoreproject.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;

public class CustomScrollView extends NestedScrollView {
    private boolean scrollingEnabled = true;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollingEnabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Bloqueia a interceptação do evento de toque se o scrollingEnabled for falso
        if (!scrollingEnabled) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Bloqueia o evento de toque se o scrollingEnabled for falso
        return scrollingEnabled && super.onTouchEvent(ev);
    }
}
