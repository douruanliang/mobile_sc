package com.mobile.stu.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobile.stu.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;


/**
 * @author: dourl
 * @date: 2020/4/5
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;
    protected Fragment mFragment;
    protected boolean mCustomStatusBar = false;
    protected View mNoNetView;
    protected View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (!mCustomStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //  StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.transparent));
            } else {
                // StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.statusbar_color));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 子类需要自定义通知栏颜色，
     * 则应该在setContentView前调用该方法
     */
    protected void useCustomStatusBar() {
        mCustomStatusBar = true;
    }


    public void showLoadingProgress(String str) {
        showLoadingProgress(str, true, null);
    }

    public void showLoadingProgress(final String str, final boolean cancelable,
                                    final DialogInterface.OnCancelListener listener) {
        if (this.isFinishing())
            return;
        runOnUiThread(() -> {
            if (getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                return;
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(BaseActivity.this);
                mProgressDialog.setMessage(str);
            }
            mProgressDialog.setCancelable(cancelable);
            if (listener != null) {
                mProgressDialog.setOnCancelListener(listener);
            }
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showLoadingProgress() {
        showLoadingProgress("");
    }

    public void showLoadingProgress(boolean cancelable) {
        showLoadingProgress(getString(R.string.loading), cancelable, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    public void hideLoadingProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 权限被拒绝后的提示信息
     */
    public void ShowPermissionDeniedToast() {
        // NHToastUtils.showLongToast(R.string.permission_error_unable_to_use_feature);
    }

    public void showNoNetView() {
        showNoNetView(R.dimen.base_no_internet);
    }

    public void hideNoNetView() {
        if (null != mNoNetView) {
            mNoNetView.setVisibility(View.GONE);
        }
    }

    public void showNoNetView(int dimenId) {
        if (null == mNoNetView) {
            int topMargin;
            try {
                topMargin = getResources().getDimensionPixelSize(dimenId);
            } catch (Exception e) {
                e.printStackTrace();
                topMargin = 225;
            }
            mNoNetView = getLayoutInflater().inflate(R.layout.activity_base_no_net, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            addContentView(mNoNetView, params);
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

    protected void reload() {
        showLoadingProgress();
    }

    public void showEmptyView() {
        showEmptyView(R.drawable.empty_box, R.string.tip_no_data);
    }

    public void showEmptyView(int drawableId, int textId) {
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(R.layout.layout_empty_view, null);
            TextView textView = (TextView) emptyView.findViewById(R.id.empty_view);
            if (textId > 0) {
                textView.setText(textId);
            }
            FrameLayout.LayoutParams params = null;
            if (drawableId > 0) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.empty_view_top_margin);
            }
            addContentView(emptyView, params);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    public void postNotifyDataChanged() {
    }
}
