package com.xiaosuokeji.framework.android.xsrefreshlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.xiaosuokeji.framework.android.xsrefreshlayout.loadview.XSDefaultFrameLoadView;
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
public class XSRefreshLayout extends FrameLayout{

    /**
     * XSRefreshLayout的子View，只允许存在一个
     */
    private View contentView;

    /**
     * 下拉刷新时展示的刷新View
     */
    private XSDefaultFrameLoadView headerView;

    /**
     * 上拉加载时，展示的刷新View
     */
    private XSDefaultFrameLoadView footerView;


    /**
     * 能否进行下拉刷新,默认为true
     */
    private boolean canRefresh = true;

    /**
     * 能否进行上拉加载，默认为true
     */
    private boolean canLoadMore = true;

    /**
     * 是否正在加载，既可以是下拉的刷新，也可以是上拉加载更多的刷新
     */
    private boolean isRefreshing = false;


    /**
     * 加载视图最终显示的高度
     */
    private float loadingViewFinalHeight;

    /**
     * 加载视图最大高度，超过即回弹
     */
    private float loadingViewOverHeight;


    /**
     * 记录滑动的上一个坐标X
     */
    private float preX;

    /**
     * 记录滑动的上一个坐标Y
     */
    private float preY;

    /**
     * 滑动操作是否确定
     */
    private boolean isActionDetermined = false;


    /**
     * 记录当前的操作
     */
    private int currentAction = -1;

    /**
     * 下拉刷新
     */
    private int ACTION_REFRESH = 1;

    /**
     * 上拉加载
     */
    private int ACTION_LOAD_MORE = 2;

    /**
     * 点击移动距离的误差值（点击操作可能会导致轻微的滑动）
     */
    private static final int CLICK_TOUCH_DEVIATION = 4;


    private XSRefreshListener xsRefreshListener;

    private XSRefreshController xsRefreshController;


    public XSRefreshLayout(Context context) {
        super(context);
    }

    public XSRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public XSRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XSRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }


    /**
     * 加载自定义属性，并设置初始值
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {

        loadingViewFinalHeight = DpUtils.dipToPx(context, 60);
        loadingViewOverHeight = loadingViewFinalHeight * 2f;

    }

    /**
     * 在XML加载完成时进行校验，当且仅当XSRefreshLayout拥有一个子View时，才开始进行View的初始化
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1) {
            throw new RuntimeException("XSRefreshLayout只允许存在一个子视图");
        }

        if (getChildCount() == 0) {
            throw new RuntimeException("必须为XSRefreshLayout设置子视图");
        }

        contentView = getChildAt(0);

        initViews();
    }


    /**
     * 对View进行初始化，
     */
    private void initViews() {
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化 headerView
     */
    private void initHeaderView() {
        if (canRefresh) {

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

            headerView = new XSDefaultFrameLoadView(getContext());

//            headerView.setLoadText("下拉刷新");

            addView(headerView, lp);
        }
    }

    /**
     * 初始化footerView
     */
    private void initFooterView() {
        if (canLoadMore) {

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

            lp.gravity = Gravity.BOTTOM;

            footerView = new XSDefaultFrameLoadView(getContext());

//            footerView.setLoadText("上拉加载更多");

            addView(footerView, lp);
        }
    }

    /**
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isRefreshing) {
            return false;
        }


        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                if (xsRefreshController != null) {
                    canRefresh = xsRefreshController.canRefresh();
                    canLoadMore = xsRefreshController.canLoadMore();
                }


                preX = ev.getX();
                preY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                float curX = ev.getX();
                float curY = ev.getY();

                float dX = curX - preX;
                float dY = curY - preY;

                preX = curX;
                preY = curY;


                //是否满足下拉刷新的条件
                if (dY > 0 && !canChildScrollUp() && canRefresh) {
                    return true;
                }

                // 是否满足上拉加载的条件
                if (dY < 0 && !canChildScrollDown() && canLoadMore) {
                    return true;
                }

                break;
        }


        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isRefreshing) {
            return false;
        }

        switch (ev.getActionMasked()) {


            case MotionEvent.ACTION_DOWN:

                isActionDetermined = false;

                return super.onTouchEvent(ev);

            case MotionEvent.ACTION_MOVE:

                float currentX = ev.getX();
                float currentY = ev.getY();

                float dX = currentX - preX;
                float dY = currentY - preY;

                preX = currentX;
                preY = currentY;

                if (!isActionDetermined) {

                    isActionDetermined = true;


                    if (dY > 0 && !canChildScrollUp() && canRefresh) {
                        //是否满足下拉刷新的条件
                        currentAction = ACTION_REFRESH;
                    } else if (dY < 0 && !canChildScrollDown() && canLoadMore) {
                        // 是否满足上拉加载的条件
                        currentAction = ACTION_LOAD_MORE;
                    } else {
                        currentAction = -1;
                    }

                }

                if (currentAction != -1) {
                    handleScroll(dY);
                }

                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return releaseTouch();

        }

        return super.onTouchEvent(ev);
    }

    /**
     * 判断子视图能否下拉，若子视图不能下拉，则父视图可以下拉
     *
     * @return
     */
    private boolean canChildScrollUp() {

        // 没有子视图，当然就不能下拉了
        if (contentView == null) {
            return false;
        }

        // SDK 14 之后，Google提供了简便的判断方法，但是在此之前，需要用比较传统的办法
        if (Build.VERSION.SDK_INT < 14) {

            if (contentView instanceof AbsListView) {

                final AbsListView absListView = (AbsListView) contentView;


                boolean childMoreThan0 = absListView.getChildCount() > 0; // 必须有数据才可以下拉

                boolean isFirstVisiblePosition = absListView.getFirstVisiblePosition() > 0; // 可视区域的首条，不是列表的第一条数据，则可下拉

                boolean checkTop = absListView.getChildAt(0).getTop() < absListView.getPaddingTop(); // 未到顶，因为未必都是ListView，当第一个数据项的顶点坐标小于contentView的paddingTop时，则可以下拉

                // 当且仅当，有数据，且未到顶，contentView中的子View才可以下拉
                return childMoreThan0 && (isFirstVisiblePosition || checkTop);


            } else {

                //若不是AbsListView，还有一个判断方式为，getScrollY()>0,scrollY表示实际内容距离contentView上边界的距离，若大于0，则表情还可以往上滚动，即可以下拉，应用场景如ScrollView
                //若是AbsListView类型的View，则不行，因为getScrollY()永远为0
                return ViewCompat.canScrollVertically(contentView, -1) || contentView.getScrollY() > 0;
            }

        } else {

            // 第二个参数为负数时，表示可以向上滚动内容，即可以下拉，为正数时，表示可以向下滚动内容，即可以上拉
            return ViewCompat.canScrollVertically(contentView, -1);

        }
    }

    /**
     * 判断子视图能否上拉，若子视图不能上拉，则父视图可以上拉,详细解说参考canChildScrollUp()
     *
     * @return
     */
    private boolean canChildScrollDown() {

        if (contentView == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT < 14) {

            if (contentView instanceof AbsListView) {

                final AbsListView absListView = (AbsListView) contentView;


                boolean childMoreThan0 = absListView.getChildCount() > 0; // 必须有数据才可以下拉


                boolean isLastVisiblePosition = absListView.getLastVisiblePosition() != (absListView.getAdapter().getCount() - 1); // 可视区域的最后一条不是列表的最后一条数据，则可上拉

                // 计算最后一条数据底部坐标
                int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1)
                        .getBottom();

                boolean checkBottom = lastChildBottom <= absListView.getMeasuredHeight();// 若最后一条数据的底部坐标大于absListView的高度，则仍可上拉

                return childMoreThan0 && (isLastVisiblePosition || checkBottom);

            } else {

                return ViewCompat.canScrollVertically(contentView, 1) || contentView.getScrollY() > 0;

            }
        } else {
            return ViewCompat.canScrollVertically(contentView, 1);
        }
    }


    /**
     * 进行视图的滑动
     *
     * @param distanceY
     */
    private boolean handleScroll(float distanceY) {

        if (!canChildScrollUp() && currentAction == ACTION_REFRESH) {

            // 下拉刷新

            LayoutParams lp = (LayoutParams) headerView.getLayoutParams();


            if (lp.height >= loadingViewOverHeight) {
                return true;
            }

            lp.height += distanceY;


            if (lp.height < 0) {
                lp.height = 0;
            } else if (lp.height > loadingViewOverHeight) {
                lp.height = (int) loadingViewOverHeight;
            }

            headerView.setLayoutParams(lp);

            if (lp.height < loadingViewOverHeight) {

//                headerView.setLoadText("下拉刷新");

            } else {

//                headerView.setLoadText("放开刷新");
            }

            headerView.setProgressRotation(lp.height / loadingViewOverHeight);

            adjustContentViewHeight(lp.height);

            if (lp.height > 0) {
                return true;
            }

        } else if (!canChildScrollDown() && currentAction == ACTION_LOAD_MORE) {

            //上拉加载更多
            LayoutParams lp = (LayoutParams) footerView.getLayoutParams();

            if (lp.height >= loadingViewOverHeight) {
                return true;
            }

            lp.height -= distanceY;

            if (lp.height < 0) {
                lp.height = 0;
            } else if (lp.height > loadingViewOverHeight) {
                lp.height = (int) loadingViewOverHeight;
            }

            footerView.setLayoutParams(lp);

            if (lp.height < loadingViewOverHeight) {

//                footerView.setLoadText("上拉加载更多");

            } else {

//                footerView.setLoadText("释放加载更多");
            }

            footerView.setProgressRotation(lp.height / loadingViewOverHeight);

            adjustContentViewHeight(-lp.height);

            if (lp.height > 0) {
                return true;
            }
        }

        return false;

    }

    /**
     * 调整contentView的位置
     *
     * @param h
     */
    public void adjustContentViewHeight(float h) {
        contentView.setTranslationY(h);
    }

    /**
     * 释放时操作
     *
     * @return
     */
    private boolean releaseTouch() {

        boolean result = false;

        LayoutParams lp;

        if (canRefresh && currentAction == ACTION_REFRESH) {

            lp = (LayoutParams) headerView.getLayoutParams();

            if (lp.height >= loadingViewOverHeight) {


                startRefresh(lp.height);
                result = true;

            } else if (lp.height > 0) {

                resetRefresh(lp.height);
                result = lp.height >= CLICK_TOUCH_DEVIATION;

            } else {

                resetRefreshState();

            }


        } else if (canLoadMore && currentAction == ACTION_LOAD_MORE) {

            lp = (LayoutParams) footerView.getLayoutParams();

            if (lp.height >= loadingViewOverHeight) {
                startLoadMore(lp.height);
                result = true;

            } else if (lp.height > 0) {

                resetLoadMore(lp.height);
                result = lp.height >= CLICK_TOUCH_DEVIATION;

            } else {

                resetLoadMoreState();

            }

        }

        return result;
    }


    /**
     * 重置下拉刷新状态
     */
    private void resetRefreshState() {

        isRefreshing = false;

        isActionDetermined = false;

        currentAction = -1;

//        headerView.setLoadText("下拉刷新");

    }

    /**
     * 重置上拉加载更多状态
     */
    private void resetLoadMoreState() {
        isRefreshing = false;

        isActionDetermined = false;

        currentAction = -1;

//        headerView.setLoadText("上拉加载更多");
    }


    public void setXsRefreshListener(XSRefreshListener xsRefreshListener) {
        this.xsRefreshListener = xsRefreshListener;
    }

    public void setXsRefreshController(XSRefreshController xsRefreshController) {
        this.xsRefreshController = xsRefreshController;
    }


    /**
     * 开始刷新，先讲HeaderView移动到约定位置，随后开始下拉刷新回调
     *
     * @param heightOfView
     */
    private void startRefresh(int heightOfView) {

        isRefreshing = true;

        ValueAnimator animator = ValueAnimator.ofFloat(heightOfView, loadingViewFinalHeight);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                LayoutParams lp = (LayoutParams) headerView.getLayoutParams();

                lp.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();

                headerView.setLayoutParams(lp);

                adjustContentViewHeight(lp.height);
            }
        });

        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                headerView.start();
//                headerView.setLoadText("刷新中");

                if (xsRefreshListener != null) {
                    xsRefreshListener.onRefresh();
                }
            }
        });
        animator.setDuration(300);
        animator.start();
    }


    /**
     * 开始刷新，先将FooterView移动到约定位置，随后开始下拉刷新回调
     *
     * @param heightOfView
     */
    private void startLoadMore(int heightOfView) {

        isRefreshing = true;

        ValueAnimator animator = ValueAnimator.ofFloat(heightOfView, loadingViewFinalHeight);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                LayoutParams lp = (LayoutParams) footerView.getLayoutParams();

                lp.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();

                footerView.setLayoutParams(lp);

                adjustContentViewHeight(-lp.height);
            }
        });

        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                footerView.start();
//                footerView.setLoadText("加载中");

                if (xsRefreshListener != null) {
                    xsRefreshListener.onLoadMore();
                }
            }
        });
        animator.setDuration(300);
        animator.start();
    }


    /**
     * 重置下拉刷新的动画
     *
     * @param heightOfView
     */
    private void resetRefresh(int heightOfView) {

        headerView.stop();

        ValueAnimator animator = ValueAnimator.ofFloat(heightOfView, 0);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                LayoutParams lp = (LayoutParams) headerView.getLayoutParams();

                lp.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();

                headerView.setLayoutParams(lp);

                adjustContentViewHeight(lp.height);
            }
        });

        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {

                resetRefreshState();

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    /**
     * 重置下拉刷新的动画
     *
     * @param heightOfView
     */
    private void resetLoadMore(int heightOfView) {

        footerView.stop();

        ValueAnimator animator = ValueAnimator.ofFloat(heightOfView, 0);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                LayoutParams lp = (LayoutParams) footerView.getLayoutParams();

                lp.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();

                footerView.setLayoutParams(lp);

                adjustContentViewHeight(-lp.height);
            }
        });

        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {

                resetLoadMoreState();

            }
        });
        animator.setDuration(300);
        animator.start();
    }


    /**
     * 完成下拉刷新动作
     */
    public void finishRefresh() {
        if (currentAction == ACTION_REFRESH) {
            resetRefresh(headerView == null ? 0 : headerView.getMeasuredHeight());
        }
    }

    /**
     * 完成上拉加载操作
     */
    public void finishLoadMore() {

        if (currentAction == ACTION_LOAD_MORE) {

            resetLoadMore(footerView == null ? 0 : footerView.getMeasuredHeight());

        }

    }

}
