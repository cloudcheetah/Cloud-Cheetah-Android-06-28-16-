package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.utils.SetTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 8/18/2016.
 */
public class AddTodoView extends ScrollView {

    @Bind(R.id.time)
    EditText timeET;
    @Bind(R.id.note)
    EditText noteET;
    String date;
    Context context;
    SetTime fromTime;

    public AddTodoView(Context context, String date) {
        super(context);
        this.date = date;
        this.context = context;
        init();
    }

    public AddTodoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddTodoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.add_to_do_view, this);
        ButterKnife.bind(this);
        fromTime = new SetTime(timeET, context);
    }

    public EditText getTimeET() {
        return timeET;
    }

    public void setTimeET(EditText timeET) {
        this.timeET = timeET;
    }

    public EditText getNoteET() {
        return noteET;
    }

    public void setNoteET(EditText noteET) {
        this.noteET = noteET;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
