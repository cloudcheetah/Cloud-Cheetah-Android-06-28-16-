package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.forateq.cloudcheetah.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vallejos Family on 7/7/2016.
 */
public class ActionView extends CardView {

    @Bind(R.id.action_edit)
    ImageView actionEditIV;
    @Bind(R.id.action_delete)
    ImageView actionDeleteIV;

    public ActionView(Context context) {
        super(context);
        init();
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.action_view, this);
        ButterKnife.bind(this);
    }

    public ImageView getActionEditIV() {
        return actionEditIV;
    }


    public ImageView getActionDeleteIV() {
        return actionDeleteIV;
    }

}
