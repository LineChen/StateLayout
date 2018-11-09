package com.line.lib;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by line on 2018/11/8.
 */

public class StateLayout extends FrameLayout {

    /**
     * 状态view集合
     */
    private Map<StateEnum, View> viewMap = new HashMap<>(4);

    /**
     * 当前状态
     */
    private StateEnum currentState = StateEnum.CONTENT;

    /**
     * 是否开启切换动画
     */
    private boolean enableAnimation;

    /**
     * view切换动画
     */
    private Animation showAnimation;
    private Animation hideAnimation;


    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void switchState(StateEnum targetState) {
        if (currentState == targetState) {
            return;
        }

        final View toBeHide = viewMap.get(currentState);
        final View toBeshow = viewMap.get(targetState);
        currentState = targetState;

        if (enableAnimation && hideAnimation != null && showAnimation != null) {
            if (toBeHide != null) {
                hideAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toBeHide.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                hideAnimation.setFillAfter(false);
                toBeHide.startAnimation(hideAnimation);
            }
            if (toBeshow != null) {
                showAnimation.setFillAfter(false);
                toBeshow.setVisibility(VISIBLE);
                toBeshow.startAnimation(showAnimation);
            }
            return;
        }


        if (toBeHide != null) {
            toBeHide.setVisibility(GONE);
        }
        if (toBeshow != null) {
            toBeshow.setVisibility(VISIBLE);
        }
    }

    public View getView(StateEnum stateEnum){
        if(viewMap.containsKey(stateEnum)){
            return viewMap.get(stateEnum);
        }
        return null;
    }

    public StateEnum getCurrentState() {
        return currentState;
    }

    /**
     * 显示加载中
     */
    public void showLoading() {
        switchState(StateEnum.LOADING);
    }

    /**
     * 显示内容页面
     */
    public void showContent() {
        switchState(StateEnum.CONTENT);
    }

    /**
     * 显示错误页面
     */
    public void showError() {
        switchState(StateEnum.ERROR);
    }

    /**
     * 显示空页面
     */
    public void showEmpty() {
        switchState(StateEnum.EMPTY);
    }

    public static class Builder{

        private StateLayout stateLayout;
        private boolean enableAnimation = true;
        private ViewAnimFactory factory;
        private Map<StateEnum, View> viewMap;
        private OnClickListener onEmptyViewClickListener;
        private OnClickListener onErrorViewClickListener;


        public Builder(Context context) {
            this.stateLayout = new StateLayout(context);
            viewMap = new HashMap<>(4);
        }

        public static Builder create(Context context){
            return new Builder(context);
        }

        /**
         * 设置空页面
         */
        public Builder setEmptyView(View emptyView) {
            viewMap.put(StateEnum.EMPTY, emptyView);
            return this;
        }

        /**
         * 设置错误页面
         */
        public Builder setErrorView(View errorView) {
            viewMap.put(StateEnum.ERROR, errorView);
            return this;
        }

        /**
         * 设置加载中页面
         */
        public Builder setLoadingView(View loadingView) {
            viewMap.put(StateEnum.LOADING, loadingView);
            return this;
        }

        public Builder setOnEmptyViewClickListener(OnClickListener onEmptyViewClickListener) {
            this.onEmptyViewClickListener = onEmptyViewClickListener;
            return this;
        }

        public Builder setOnErrorViewClickListener(OnClickListener onErrorViewClickListener) {
            this.onErrorViewClickListener = onErrorViewClickListener;
            return this;
        }

        /**
         * 是否开启切换动画
         */
        public Builder setAminationEnable(boolean enableAnimation) {
            this.enableAnimation = enableAnimation;
            return this;
        }

        /**
         * 设置动画
         */
        public Builder setViewAnimFactory(ViewAnimFactory factory) {
            this.factory = factory;
            return this;
        }

        public Builder attach(Object targetView) {
            ViewGroup content = null;
            if (targetView instanceof Activity) {
                content = ((Activity) targetView).findViewById(android.R.id.content);
            } else if (targetView instanceof Fragment) {
                content = (ViewGroup) ((Fragment) targetView).getView().getParent();
            } else if (targetView instanceof View) {
                content = (ViewGroup) ((View) targetView).getParent();
            }
            if (content != null) {
                int childCount = content.getChildCount();
                int index = 0;
                View oldContent = null;
                if (targetView instanceof View) {
                    oldContent = (View) targetView;
                    for (int i = 0; i < childCount; i++) {
                        if (content.getChildAt(i) == oldContent) {
                            index = i;
                            break;
                        }
                    }
                } else {
                    oldContent = content.getChildAt(0);
                }
                stateLayout.viewMap.put(StateEnum.CONTENT, oldContent);
                stateLayout.removeAllViews();
                content.removeView(oldContent);
                ViewGroup.LayoutParams params = oldContent.getLayoutParams();
                content.addView(stateLayout, index, params);
                stateLayout.addView(oldContent);
            }
            return this;
        }

        public StateLayout build(){
            init();
            return stateLayout;
        }

        private void init(){
            stateLayout.enableAnimation = enableAnimation;
            if (factory != null) {
                stateLayout.showAnimation = factory.createShowAnimation();
                stateLayout.hideAnimation = factory.createHideAnimation();
            }

            View errorView = getStateView(StateEnum.ERROR);
            if (errorView != null) {
                addStateView(stateLayout, errorView, StateEnum.ERROR);
                errorView.setOnClickListener(onErrorViewClickListener);
            }

            View emptyView = getStateView(StateEnum.EMPTY);
            if (emptyView != null) {
                addStateView(stateLayout, emptyView, StateEnum.EMPTY);
                emptyView.setOnClickListener(onEmptyViewClickListener);
            }

            View loadingView = getStateView(StateEnum.LOADING);
            if (loadingView != null) {
                addStateView(stateLayout, loadingView, StateEnum.LOADING);
            }
        }

        private View getStateView(StateEnum stateEnum){
            if(viewMap.containsKey(stateEnum)){
                return viewMap.get(stateEnum);
            }
            return null;
        }

        private void addStateView(StateLayout stateLayout, View targetView, StateEnum state) {
            int childCount = stateLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                //从子view列表中查找构造方法中设置的默认布局，并进行移除
                View childView = stateLayout.getChildAt(i);
                if (childView == stateLayout.viewMap.get(state)) {
                    stateLayout.removeView(childView);
                }
            }
            stateLayout.viewMap.put(state, targetView);
            stateLayout.addView(targetView);
            targetView.setVisibility(GONE);
        }
    }

    public interface ViewAnimFactory {
        Animation createShowAnimation();
        Animation createHideAnimation();
    }


    public enum StateEnum {
        /**
         * 加载中
         */
        LOADING,

        /**
         * 网络连接失败
         */
        ERROR,

        /**
         * 空页面
         */

        EMPTY,

        /**
         * 显示内容
         */
        CONTENT
    }
}
