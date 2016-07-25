package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskResources;

import butterknife.Bind;
import butterknife.ButterKnife;

/** This class is used to add new resource
 * Created by Vallejos Family on 6/10/2016.
 */
public class AddResourceView extends ScrollView {

    @Bind(R.id.resource_name)
    Spinner resourceNameSP;
    @Bind(R.id.quantity)
    EditText resourceQtyET;
    long task_offline_id;
    int task_id;

    public AddResourceView(Context context, long task_offline_id, int task_id) {
        super(context);
        this.task_offline_id = task_offline_id;
        this.task_id = task_id;
        init();
    }

    public AddResourceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddResourceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.add_resource_view, this);
        ButterKnife.bind(this);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, TaskResources.getTaskResourceOnline(task_id));
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resourceNameSP.setAdapter(nameAdapter);
    }

    public Spinner getResourceNameSP() {
        return resourceNameSP;
    }

    public void setResourceNameSP(Spinner resourceNameSP) {
        this.resourceNameSP = resourceNameSP;
    }

    public EditText getResourceQtyET() {
        return resourceQtyET;
    }

    public void setResourceQtyET(EditText resourceQtyET) {
        this.resourceQtyET = resourceQtyET;
    }
}
