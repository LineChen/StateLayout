package com.line.statelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.line.lib.StateLayout;

public class MainActivity extends AppCompatActivity {

    private StateLayout stateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void load(){
        stateLayout.showLoading();
        stateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                stateLayout.showContent();
            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_content:
                stateLayout.showContent();
                break;
            case R.id.menu_empty:
                stateLayout.showEmpty();
                break;
            case R.id.menu_error:
                stateLayout.showError();
                break;
            case R.id.menu_loading:
                stateLayout.showLoading();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
