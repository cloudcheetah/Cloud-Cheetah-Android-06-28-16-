package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.CashInOutAdapter;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display all the cash in and cash out of specific task
 * Created by Vallejos Family on 6/14/2016.
 */
public class TaskCashInCashOutView extends RelativeLayout {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_task_cash_in_out)
    RecyclerView listCashInOut;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    long task_offline_id;
    int task_id;
    String taskName;
    public static CashInOutAdapter cashInOutAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "TaskCashInCashOutView";

    public TaskCashInCashOutView(Context context, long task_offline_id, int task_id, String taskName) {
        super(context);
        this.task_offline_id = task_offline_id;
        this.task_id = task_id;
        this.taskName = taskName;
        init();
    }

    public TaskCashInCashOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskCashInCashOutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.task_cash_in_out_view, this);
        ButterKnife.bind(this);
        cashInOutAdapter = new CashInOutAdapter(TaskCashInCashOut.getOfflineCashInOut(task_offline_id), ApplicationContext.get());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listCashInOut.setAdapter(cashInOutAdapter);
        listCashInOut.setLayoutManager(mLinearLayoutManager);
        listCashInOut.setItemAnimator(new DefaultItemAnimator());
        setSearchForTasksReports();
    }

    public void setSearchForTasksReports(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.fab)
    public void addCashInOut(){

    }
}
