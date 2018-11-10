# StateLayout

page切换 空布局、错误布局、加载布局，不需要xml编写

思想来自于：https://github.com/Hankkin/PageLayoutDemo

用法：

和上面链接中的基本一致

```
stateLayout = StateLayout.Builder.create(this)
                .attach(this)
                .setAminationEnable(true)
                .setViewAnimFactory(new StateLayout.ViewAnimFactory() {
                    @Override
                    public Animation createShowAnimation() {
                        Animation animation = new AlphaAnimation(0.0f, 1.0f);
                        animation.setDuration(200);
                        animation.setInterpolator(new DecelerateInterpolator());
                        return animation;
                    }

                    @Override
                    public Animation createHideAnimation() {
                        Animation animation = new AlphaAnimation(1.0f, 0.0f);
                        animation.setDuration(200);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        return animation;
                    }
                })
                .setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_empty, null))
                .setLoadingView(LayoutInflater.from(this).inflate(R.layout.layout_loading, null))
                .setErrorView(LayoutInflater.from(this).inflate(R.layout.layout_error, null))
                .setOnErrorViewClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stateLayout.showContent();
                    }
                })
                .setOnEmptyViewClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        load();
                    }
                })
                .build();

```


### 效果图

![1](https://github.com/LineChen/StateLayout/blob/master/s1.png)
![1](https://github.com/LineChen/StateLayout/blob/master/s2.png)
![1](https://github.com/LineChen/StateLayout/blob/master/s3.png)
![1](https://github.com/LineChen/StateLayout/blob/master/s4.png)

