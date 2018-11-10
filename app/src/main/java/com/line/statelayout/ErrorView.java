package com.line.statelayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by line on 2018/11/9.
 */

public class ErrorView extends ConstraintLayout {

    private OnClickListener clickListener;

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View ivError = findViewById(R.id.btn_refresh);
        final View progress = findViewById(R.id.error_progress);
        ivError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                progress.setVisibility(VISIBLE);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(GONE);
                        clickListener.onClick(v);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.clickListener = l;
    }
}
