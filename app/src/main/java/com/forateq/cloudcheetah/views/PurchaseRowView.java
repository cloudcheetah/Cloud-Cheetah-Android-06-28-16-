package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
public class PurchaseRowView extends LinearLayout {

    @Bind(R.id.resource_name)
    TextView resourceNameET;
    @Bind(R.id.resource_quantity)
    TextView resourceQuantityET;
    @Bind(R.id.price)
    TextView resourcePriceET;
    @Bind(R.id.total_price)
    TextView totalPriceET;
    @Bind(R.id.row_layout)
    LinearLayout rowLayout;
    @Bind(R.id.ripple)
    MaterialRippleLayout rippleLayout;

    public PurchaseRowView(Context context) {
        super(context);
        init();
    }

    public PurchaseRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PurchaseRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.purchase_row, this);
        ButterKnife.bind(this);
    }

    public TextView getResourceNameET() {
        return resourceNameET;
    }

    public TextView getResourceQuantityET() {
        return resourceQuantityET;
    }

    public TextView getResourcePriceET() {
        return resourcePriceET;
    }

    public TextView getTotalPriceET() {
        return totalPriceET;
    }

    public LinearLayout getRowLayout() {
        return rowLayout;
    }

    public MaterialRippleLayout getRippleLayout() {
        return rippleLayout;
    }
}
