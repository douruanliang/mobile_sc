package com.mobile.stu.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mobile.stu.utils.StorageUtils;

/**
 * author: dourl
 * created on: 2018/10/24 6:32 PM
 * description:
 */
public class XSanWebView extends WebView {

    public XSanWebView(Context context) {
        super(context);
        init();
    }


    public XSanWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XSanWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        if (isInEditMode()) {
            return;
        }
        WebSettings localWebSettings = this.getSettings();
        localWebSettings.setLoadWithOverviewMode(true);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setSupportZoom(true);
        localWebSettings.setBuiltInZoomControls(false);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        localWebSettings.setDomStorageEnabled(true);
        localWebSettings.setAppCacheEnabled(true);
        localWebSettings.setAppCachePath(StorageUtils.getOwnCacheDirectory(getContext(), "webcache").getPath());
        localWebSettings.setDatabaseEnabled(true);
        localWebSettings.setDefaultTextEncodingName("utf-8");
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
    }
}
