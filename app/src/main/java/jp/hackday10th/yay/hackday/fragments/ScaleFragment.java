package jp.hackday10th.yay.hackday.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.hackday10th.yay.hackday.R;
import jp.hackday10th.yay.hackday.databinding.FragmentScaleBinding;

public class ScaleFragment extends Fragment {
    private FragmentScaleBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scale, null, false);
        return mBinding.getRoot();
    }
}
