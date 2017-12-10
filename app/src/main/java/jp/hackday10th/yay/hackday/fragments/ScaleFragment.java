package jp.hackday10th.yay.hackday.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.hackday10th.yay.hackday.R;
import jp.hackday10th.yay.hackday.WeightCalculator;
import jp.hackday10th.yay.hackday.adapters.WeightMemoAdapter;
import jp.hackday10th.yay.hackday.databinding.FragmentScaleBinding;
import jp.hackday10th.yay.hackday.models.MemoItem;
import jp.hackday10th.yay.hackday.views.TouchHandleView;

public class ScaleFragment extends Fragment {
    private static final String TAG = ScaleFragment.class.getSimpleName();
    private static final String TYPE_FACE_PATH = "fonts/DINNextLTPro-Regular.otf";
    private FragmentScaleBinding mBinding;
    private WeightCalculator mWeightCalculator = new WeightCalculator();
    private WeightMemoAdapter mWeightMemoAdapter;
    private List<MemoItem> mMemoItems = new ArrayList<>();
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scale, null, false);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), TYPE_FACE_PATH);
        setTypeFaceToTexts(type);
        mWeightMemoAdapter = new WeightMemoAdapter(mMemoItems, type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.memoList.setLayoutManager(layoutManager);
        mBinding.memoList.setAdapter(mWeightMemoAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        mBinding.memoList.addItemDecoration(decoration);
        mHandler = new Handler(getContext().getMainLooper());
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
        mBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo(mBinding.weight.getText().toString());
            }
        });
        mBinding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMemo();
            }
        });
        clearWeight();
        startCounting();
    }

    @Override
    public void onPause() {
        stopCounting();
        mBinding.scaleArea.removeTouchHandleListener();
        super.onPause();
    }

    private void startCounting() {
        mBinding.fps.setText("0");
        countNext();
    }

    private void countNext() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long current = Long.parseLong(mBinding.fps.getText().toString());
                long next = (current + 1) % 30;
                mBinding.fps.setText(String.valueOf(next));
                Log.i(TAG, "Current count: " + current);
                countNext();
            }
        }, (long) (1000.0f / 30.0f));
    }

    private void stopCounting() {
        mHandler.removeCallbacksAndMessages(null);
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
//        Log.i(TAG, sizeInfo);
        return true;
    }

    private void saveMemo(String weight) {
        insertItemToList(new MemoItem(weight));
    }

    private void clearMemo() {
        List<MemoItem> items = new ArrayList<>(mMemoItems);
        for (MemoItem item : items) {
            removeItemFromList(item);
        }
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

    private void insertItemToList(MemoItem item) {
        if (mMemoItems == null) {
            return;
        }
        int index = mMemoItems.indexOf(item);
        if (-1 != index) {
            return;
        }
        mMemoItems.add(0, item);
        mWeightMemoAdapter.notifyItemInserted(0);
    }

    private void updateListItem(MemoItem item) {
        if (mMemoItems == null) {
            return;
        }
        int index = mMemoItems.indexOf(item);
        if (-1 != index) {
            mWeightMemoAdapter.notifyItemChanged(index, item);
        }
    }

    private void removeItemFromList(MemoItem item) {
        if (mMemoItems == null) {
            return;
        }
        int index = mMemoItems.indexOf(item);
        if (-1 == index) {
            return;
        }
        boolean isDelete = mMemoItems.remove(item);
        if (isDelete) {
            mWeightMemoAdapter.notifyItemRemoved(index);
        }
    }
}
