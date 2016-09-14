package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Resources;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
public class AddItemView extends ScrollView {

    @Bind(R.id.item_name)
    Spinner itemNameSP;
    @Bind(R.id.quantity)
    EditText quantityET;
    List<String> resourceList;

    public AddItemView(Context context) {
        super(context);
        init();
    }

    public AddItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.add_item_view, this);
        ButterKnife.bind(this);
        resourceList = Resources.getAllResourcesList();
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, resourceList);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemNameSP.setAdapter(nameAdapter);
    }

    public Spinner getItemNameSP() {
        return itemNameSP;
    }

    public EditText getQuantityET() {
        return quantityET;
    }

}
