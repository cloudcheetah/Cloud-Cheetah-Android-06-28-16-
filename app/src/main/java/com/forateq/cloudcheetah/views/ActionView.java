package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vallejos Family on 7/7/2016.
 */
public class ActionView extends LinearLayout {

    @Bind(R.id.ripple_edit)
    MaterialRippleLayout rippleEdit;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;

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

    public MaterialRippleLayout getRippleEdit() {
        return rippleEdit;
    }

    public void setRippleEdit(MaterialRippleLayout rippleEdit) {
        this.rippleEdit = rippleEdit;
    }

    public MaterialRippleLayout getRippleDelete() {
        return rippleDelete;
    }

    public void setRippleDelete(MaterialRippleLayout rippleDelete) {
        this.rippleDelete = rippleDelete;
    }
}
