package com.xiaosuokeji.framework.android.xsrefreshlayout.progressbar;

import android.content.Context;
import android.util.AttributeSet;

import com.xiaosuokeji.framework.android.xsrefreshlayout.R;

/**
 * 功能：
 * -------------------------------------------------------------------------------------------------
 * 创建者：陈佳润
 * -------------------------------------------------------------------------------------------------
 * 创建日期：17/3/31
 * -------------------------------------------------------------------------------------------------
 * 更新历史(日期/更新人/更新内容)
 */
public class XSDefaultFrameProgressBar extends XSAbstractFrameProgressBar {

    public XSDefaultFrameProgressBar(Context context) {
        super(context);
    }

    public XSDefaultFrameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XSDefaultFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XSDefaultFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getAnimationListId() {
        return R.anim.default_loading;
    }
}
