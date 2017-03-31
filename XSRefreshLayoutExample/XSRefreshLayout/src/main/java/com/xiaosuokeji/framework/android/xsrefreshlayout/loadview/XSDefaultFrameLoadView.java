package com.xiaosuokeji.framework.android.xsrefreshlayout.loadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xiaosuokeji.framework.android.xsrefreshlayout.R;
import com.xiaosuokeji.framework.android.xsrefreshlayout.progressbar.XSAbstractFrameProgressBar;

/**
 * 功能：
 * -------------------------------------------------------------------------------------------------
 * 创建者：陈佳润
 * -------------------------------------------------------------------------------------------------
 * 创建日期：17/3/31
 * -------------------------------------------------------------------------------------------------
 * 更新历史(日期/更新人/更新内容)
 */
public class XSDefaultFrameLoadView extends XSAbstractFrameLoadView {

    XSAbstractFrameProgressBar progressBar;

    public XSDefaultFrameLoadView(Context context) {
        super(context);
    }

    public XSDefaultFrameLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XSDefaultFrameLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XSDefaultFrameLoadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_default_frame_load_view;
    }

    @Override
    public void initView(View view) {
        progressBar = (XSAbstractFrameProgressBar) view.findViewById(R.id.pb_default);
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
