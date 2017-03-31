package com.xiaosuokeji.xsrefreshlayout.example;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosuokeji.framework.android.xsrefreshlayout.loadview.XSAbstractFrameLoadView;
import com.xiaosuokeji.framework.android.xsrefreshlayout.progressbar.XSAbstractFrameProgressBar;

/**
 * 功能：自定义加载框
 * -------------------------------------------------------------------------------------------------
 * 创建者：陈佳润
 * -------------------------------------------------------------------------------------------------
 * 创建日期：17/3/31
 * -------------------------------------------------------------------------------------------------
 * 更新历史(日期/更新人/更新内容)
 */
public class TestFrameLoadView extends XSAbstractFrameLoadView {

    XSAbstractFrameProgressBar progressBar;

    public TestFrameLoadView(Context context) {
        super(context);
    }

    public TestFrameLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestFrameLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestFrameLoadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_test_frame_load_view;
    }

    @Override
    public void initView(View view) {

        progressBar = (XSAbstractFrameProgressBar) view.findViewById(R.id.pb_test);

        start();

    }

    @Override
    public void setProgressRotation(float rotation) {
        progressBar.setProgressRotation(rotation);
    }

    @Override
    public void start() {
        progressBar.start();
    }

    @Override
    public void stop() {
        progressBar.stop();
    }
}
