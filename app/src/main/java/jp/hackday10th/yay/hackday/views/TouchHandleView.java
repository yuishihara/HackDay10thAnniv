package jp.hackday10th.yay.hackday.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchHandleView extends View {
    public interface TouchHandleListener {
        public boolean handleTouchEvent(MotionEvent event);
    }

    private TouchHandleListener mListener;

    public TouchHandleView(Context context) {
        this(context, null);
    }

    public TouchHandleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchHandleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    public TouchHandleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener != null) {
            return mListener.handleTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setTouchHandleListener(TouchHandleListener listener) {
        mListener = listener;
    }

    public void removeTouchHandleListener() {
        mListener = null;
    }

    private void initializeView() {

    }
}
