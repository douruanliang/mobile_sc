package com.mobile.stu.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobile.stu.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * @author: dourl
 * @date: 2020/4/5
 */
public class BaseFragment extends Fragment implements View.OnTouchListener {

    protected ProgressDialog mProgressDialog;
    protected View mNoNetView;
    protected View emptyView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    public void showLoadingProgress(String str) {
        showLoadingProgress(str, true, null);
    }

    public void showLoadingProgress(final String str, final boolean cancelable,
                                    final DialogInterface.OnCancelListener listener) {
        if (this.getActivity() != null && this.getActivity().isFinishing() || !isAdded()) {
            return;
        }

        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoadingProgress(str, cancelable, listener);
        }
    }


    public void showLoadingProgress() {
        showLoadingProgress("", true, null);
    }

    public void hideLoadingProgress() {
        if (getActivity() != null && isAdded()) {
            ((BaseActivity) getActivity()).hideLoadingProgress();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    public void showNoNetView() {
        showNoNetView(R.dimen.base_no_internet);
    }

    public void showNoNetView(int dimenId) {
        if (null == mNoNetView) {
            if (!isAdded()) return;

            int topMargin;
            try {
                topMargin = getResources().getDimensionPixelSize(dimenId);
            } catch (Exception e) {
                e.printStackTrace();
                topMargin = 225;
            }
            mNoNetView = getActivity().getLayoutInflater().inflate(R.layout.activity_base_no_net, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            getActivity().addContentView(mNoNetView, params);
            mNoNetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNoNetView.setVisibility(View.GONE);
                    reload();
                }
            });
        } else {
            mNoNetView.setVisibility(View.VISIBLE);
        }
    }

    public void hideNoNetView() {
        if (null != mNoNetView) {
            mNoNetView.setVisibility(View.GONE);
        }
    }

    protected void reload() {

    }

    protected void init() {
    }

    /**
     * 显示空View
     *
     * @param drawableId
     * @param textId
     */
    public void showEmptyView(int drawableId, int textId, int topMarginId) {
        if (emptyView == null) {
            if (!isAdded()) return;
            emptyView = getActivity().getLayoutInflater().inflate(R.layout.layout_empty_view, null);
            TextView textView = (TextView) emptyView.findViewById(R.id.empty_view);
            textView.setText(textId);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (topMarginId > 0) {
                params.topMargin = getResources().getDimensionPixelSize(topMarginId);
            } else {
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.empty_view_top_margin);
            }
            View view = getView();
            if (view instanceof ViewGroup) {
                ((ViewGroup) view).addView(emptyView, params);
            }
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void showEmptyView(int drawableId, int textId) {
        showEmptyView(drawableId, textId, 0);
    }

    public void showEmptyView() {
        showEmptyView(R.drawable.empty_box, R.string.tip_no_data);
    }

    /**
     * 隐藏空view
     */
    public void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }
}
