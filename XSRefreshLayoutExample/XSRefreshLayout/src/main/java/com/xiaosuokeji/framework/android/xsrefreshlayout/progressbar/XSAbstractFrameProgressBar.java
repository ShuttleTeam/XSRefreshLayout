package com.xiaosuokeji.framework.android.xsrefreshlayout.progressbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaosuokeji.framework.android.xsrefreshlayout.util.DpUtils;

/**
 * 功能：
 * -------------------------------------------------------------------------------------------------
 * 创建者：陈佳润
 * -------------------------------------------------------------------------------------------------
 * 创建日期：17/3/31
 * -------------------------------------------------------------------------------------------------
 * 更新历史(日期/更新人/更新内容)
 */
public abstract class XSAbstractFrameProgressBar extends LinearLayout implements XSProgressBar {

    private static final int DEFAULT_SIZE = 42;

    ImageView imageLoading;

    AnimationDrawable animationDrawable;

    public XSAbstractFrameProgressBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public XSAbstractFrameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public XSAbstractFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XSAbstractFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setGravity(Gravity.CENTER_HORIZONTAL);

        imageLoading = new ImageView(context);

        LayoutParams lp = new LayoutParams((int) DpUtils.dipToPx(getContext(), DEFAULT_SIZE),
                (int) DpUtils.dipToPx(getContext(), DEFAULT_SIZE));
        lp.rightMargin = (int) DpUtils.dipToPx(getContext(), 10);

        imageLoading.setImageResource(getAnimationListId());

        addView(imageLoading, lp);

        animationDrawable = (AnimationDrawable) imageLoading.getDrawable();
    }

    @Override
    public void setProgressRotation(float rotation) {
    }

    @Override
    public void start() {
        animationDrawable.start();
    }

    @Override
    public void stop() {
        animationDrawable.stop();
    }

    public abstract int getAnimationListId();

}
