package jp.hackday10th.yay.hackday.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.hackday10th.yay.hackday.R;
import jp.hackday10th.yay.hackday.WeightCalculator;
import jp.hackday10th.yay.hackday.databinding.FragmentScaleBinding;
import jp.hackday10th.yay.hackday.views.TouchHandleView;

public class ScaleFragment extends Fragment {
    private static final String TAG = ScaleFragment.class.getSimpleName();
    private static final String TYPE_FACE_PATH = "fonts/DINNextLTPro-Regular.otf";
    private FragmentScaleBinding mBinding;
    private WeightCalculator mWeightCalculator = new WeightCalculator();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scale, null, false);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), TYPE_FACE_PATH);
        setTypeFaceToTexts(type);
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
        clearWeight();
    }

    @Override
    public void onPause() {
        mBinding.scaleArea.removeTouchHandleListener();
        super.onPause();
    }

    private void setTypeFaceToTexts(Typeface typeface) {
        TextView[] texts = {
                mBinding.weight,
                mBinding.saveButton,
                mBinding.resetButton
        };
        for (TextView text : texts) {
            text.setTypeface(typeface);
        }
    }

    private boolean handleTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            clearWeight();
            return true;
        }
        StringBuilder builder = new StringBuilder();
        float[] footPrints = new float[event.getPointerCount()];
        for (int i = 0; i < event.getPointerCount(); ++i) {
            try {
                int id = event.getPointerId(i);
                float size = event.getPressure(id);
                footPrints[i] = size;
                builder.append("Pointer: ").append(i)
                        .append(" TouchEvent size: ").append(size)
                        .append("\n");
            } catch (IllegalArgumentException noSuchIndex) {
                footPrints[i] = -1.0f;
                // Do nothing
            }
        }
        displayWeight(footPrints);
        String sizeInfo = builder.toString();
        mBinding.area.setText(sizeInfo);
        Log.i(TAG, sizeInfo);
        return true;
    }

    private void displayWeight(float[] footPrints) {
        float weightInGrams = mWeightCalculator.computeWeight(footPrints);
        displayWeight(weightInGrams);
    }

    private void displayWeight(float weightInGrams) {
        String weightText = getContext().getResources().getString(R.string.weight_text, weightInGrams);
        mBinding.weight.setText(weightText);
    }

    private void clearWeight() {
        displayWeight(0.0f);
        mBinding.area.setText("");
    }
}
