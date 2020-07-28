package com.mobile.stu.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.stu.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author: dourl
 * created on: 2018/8/8 下午7:29
 * description: 自定义TitleBar
 */
public class XSanTitleBar extends FrameLayout {

    private View mLeftBtnLayout;
    private View mTitleLayout;
    private View mRightBtnLayout;
    private View mLine;

    private TextView mLeftBtnText;
    private TextView mTitleTv;
    private TextView mRightBtnText;

    private ImageView mLeftBtnImg;
    private ImageView mTitleImg;
    private ImageView mRightBtnImg;


    public XSanTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public XSanTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XSanTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_titlebar, this);
        mLeftBtnLayout = findViewById(R.id.title_bar_left_btn_layout);
        mRightBtnLayout = findViewById(R.id.title_bar_right_btn_layout);
        mLeftBtnText = (TextView) findViewById(R.id.title_bar_left_btn);
        mLeftBtnImg = (ImageView) findViewById(R.id.title_bar_left_btn_image);
        mRightBtnText = (TextView) findViewById(R.id.title_bar_right_btn);
        mRightBtnImg = (ImageView) findViewById(R.id.title_bar_right_btn_image);
        mTitleTv = (TextView) findViewById(R.id.title_bar_title_text);
        mTitleImg = (ImageView) findViewById(R.id.title_bar_title_logo);
        mTitleLayout = findViewById(R.id.title_bar_title_layout);
        mLine = findViewById(R.id.title_bar_line);

        if (attrs != null) {
            //自定义属性
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XSanTitleBar);
            init(array);
            array.recycle();
        }
    }

    private void init(TypedArray array) {
        //默认 共性
        ColorStateList defaultColor = getResources().getColorStateList(R.color.title_bar_right_text_color_sel);
        ColorStateList defaultCenterColor = getResources().getColorStateList(R.color.title_bar_center_text_color_sel);

        int defaultBtnTextSize = getResources().getDimensionPixelSize(R.dimen.default_title_btn_textsize); // 默认左右按键的字体大小 17sp
        int defaultTitleTextSize = getResources().getDimensionPixelSize(R.dimen.default_title_textsize); //  默认标题字体大小 15sp
        int defaultTabItemSpace = getResources().getDimensionPixelOffset(R.dimen.default_tab_item_space); // 默认TabItem的间距为 20dp

        //获取自定义属性
        Drawable leftDrawable = array.getDrawable(R.styleable.XSanTitleBar_leftBgDrawable);
        CharSequence leftText = array.getText(R.styleable.XSanTitleBar_leftText);
        ColorStateList leftTextColor = array.getColorStateList(R.styleable.XSanTitleBar_leftTextSize);
        float leftTextSize = array.getDimension(R.styleable.XSanTitleBar_leftTextSize, defaultBtnTextSize);
        int leftPadding = array.getDimensionPixelSize(R.styleable.XSanTitleBar_leftPadding, 0);


        Drawable titleDrawable = array.getDrawable(R.styleable.XSanTitleBar_centralBgDrawable);
        CharSequence titleText = array.getText(R.styleable.XSanTitleBar_centralText);
        ColorStateList titleTextColor = array.getColorStateList(R.styleable.XSanTitleBar_centralTextColor);
        int titleLeftMargin = array.getDimensionPixelSize(R.styleable.XSanTitleBar_centralLeftMargin, 0);
        int titleRightMargin = array.getDimensionPixelSize(R.styleable.XSanTitleBar_centralRightMargin, 0);
        float titleTextSize = array.getDimension(R.styleable.XSanTitleBar_centralTextSize, defaultTitleTextSize);


        Drawable rightDrawable = array.getDrawable(R.styleable.XSanTitleBar_rightBgDrawable);
        CharSequence rightText = array.getText(R.styleable.XSanTitleBar_rightText);
        ColorStateList rightTextColor = array.getColorStateList(R.styleable.XSanTitleBar_rightTextColor);
        float rightTextSize = array.getDimension(R.styleable.XSanTitleBar_rightTextSize, defaultBtnTextSize);
        int rightPadding = array.getDimensionPixelSize(R.styleable.XSanTitleBar_rightPadding, 0);


        if (leftTextColor == null) {
            leftTextColor = defaultColor;
        }
        if (titleTextColor == null) {
            titleTextColor = defaultCenterColor;
        }
        updateBtn(mLeftBtnLayout, mLeftBtnText, mLeftBtnImg, leftDrawable, leftText, leftTextColor, leftTextSize, leftPadding);
        updateBtn(mRightBtnLayout, mRightBtnText, mRightBtnImg, rightDrawable, rightText, rightTextColor, rightTextSize, rightPadding);
        updateTitleBtn(mTitleLayout, mTitleTv, mTitleImg, titleDrawable, titleText, titleTextColor, titleTextSize, titleLeftMargin, titleRightMargin);


    }

    private void updateBtn(View layout, TextView textView, ImageView iv, Drawable drawable, CharSequence text, ColorStateList color, float size, int padding) {
        if (drawable == null && TextUtils.isEmpty(text)) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                textView.setBackgroundDrawable(drawable);
                textView.setText(text);
                textView.setTextColor(color);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            } else {
                textView.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                iv.setImageDrawable(drawable);
            }

            MarginLayoutParams params = (MarginLayoutParams) layout.getLayoutParams();
            if (params != null && padding > 0) {
                params.setMargins(padding, 0, padding, 0);
            }
        }
    }

    private void updateTitleBtn(View layout, TextView textView, ImageView iv, Drawable drawable, CharSequence text, ColorStateList color, float size, int leftMargin, int rightMargin) {
        if (drawable == null && TextUtils.isEmpty(text)) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(text)) {
                textView.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                textView.setBackgroundDrawable(drawable);
                textView.setText(text);
                textView.setTextColor(color);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            } else {
                textView.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                iv.setImageDrawable(drawable);
            }

            MarginLayoutParams params = (MarginLayoutParams) layout.getLayoutParams();
            if (params != null) {
                params.setMargins(leftMargin, 0, rightMargin, 0);
            }
        }
    }
}
