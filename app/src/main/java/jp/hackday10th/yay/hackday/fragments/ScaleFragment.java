package jp.hackday10th.yay.hackday.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import jp.hackday10th.yay.hackday.R;
import jp.hackday10th.yay.hackday.databinding.FragmentScaleBinding;
import jp.hackday10th.yay.hackday.views.TouchHandleView;

public class ScaleFragment extends Fragment {
    private FragmentScaleBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scale, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.scaleArea.setTouchHandleListener(new TouchHandleView.TouchHandleListener() {
            @Override
            public boolean handleTouchEvent(MotionEvent event) {
                return ScaleFragment.this.handleTouchEvent(event);
            }
        });
    }

    @Override
    public void onPause() {
        mBinding.scaleArea.removeTouchHandleListener();
        super.onPause();
    }

    private boolean handleTouchEvent(MotionEvent event) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < event.getPointerCount(); ++i) {
            try {
                int id = event.getPointerId(i);
                float size = event.getPressure(id);
                builder.append("Pointer: ").append(i)
                        .append(" TouchEvent size: ").append(size)
                        .append("\n");
            } catch (IllegalArgumentException noSuchIndex) {
                // Do nothing
            }
        }
        String sizeInfo = builder.toString();
        mBinding.area.setText(sizeInfo);
        return true;
    }
}
