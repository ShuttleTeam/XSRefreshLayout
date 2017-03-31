package com.xiaosuokeji.xsrefreshlayout.example;

import android.content.Context;
import android.util.AttributeSet;

import com.xiaosuokeji.framework.android.xsrefreshlayout.progressbar.XSAbstractFrameProgressBar;

/**
 * 功能：自定义进度指示器
 * -------------------------------------------------------------------------------------------------
 * 创建者：陈佳润
 * -------------------------------------------------------------------------------------------------
 * 创建日期：17/3/31
 * -------------------------------------------------------------------------------------------------
 * 更新历史(日期/更新人/更新内容)
 */
public class TestFrameProgressBar extends XSAbstractFrameProgressBar {

    public TestFrameProgressBar(Context context) {
        super(context);
    }

    public TestFrameProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestFrameProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getAnimationListId() {
        return R.anim.loading;
    }
}
