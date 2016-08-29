package com.forateq.cloudcheetah.fragments;

/**
 * Created by PC1 on 8/18/2016.
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AccountsAdapter;
import com.forateq.cloudcheetah.adapters.ToDoAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.ToDo;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.AddResource;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.AddResourceView;
import com.forateq.cloudcheetah.views.AddTodoView;
import com.forateq.cloudcheetah.views.ResourceRowView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;




/**
 * Created by PC1 on 7/25/2016.
 */
public class ToDoFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_todo)
    RecyclerView listToDo;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static ToDoAdapter toDoAdapter;
    String date;
    long milliseconds;
    public static final String TAG = "ToDoFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.todo_fragment, container, false);
        date = getArguments().getString("date");
        milliseconds = getArguments().getLong("milliseconds");
        return v;
    }

    public void init(){
        toDoAdapter = new ToDoAdapter(ToDo.getToDoByDate(date), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listToDo.setAdapter(toDoAdapter);
        listToDo.setLayoutManager(mLinearLayoutManager);
        listToDo.setItemAnimator(new DefaultItemAnimator());
        setSearchAccounts();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void setSearchAccounts(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchString = searchEditText.getText().toString();

            }
        });
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.fab)
    void addNewToDo(){
        final AddTodoView addTodoView = new AddTodoView(getActivity(), date);
        final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                .title("Add To Do")
                .titleColorRes(R.color.colorText)
                .backgroundColorRes(R.color.colorPrimary)
                .widgetColorRes(R.color.colorText)
                .customView(addTodoView, true)
                .positiveText("Ok")
                .positiveColorRes(R.color.colorText)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                        String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                        String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        ToDo toDo = new ToDo();
                        toDo.setDate(date);
                        toDo.setNote(addTodoView.getNoteET().getText().toString());
                        toDo.setTime(addTodoView.getTimeET().getText().toString());
                        toDo.setUser_id(Users.getUserIdByUserName(userName));
                        toDo.save();
                        toDoAdapter.addItem(toDo);
                        Event event = new Event(R.color.colorPrimaryDark, milliseconds);
                        CalendarFragment.calendarView.addEvent(event, true);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                });
        final MaterialDialog addNoteDialog = createNoteDialog.build();
        addNoteDialog.show();
    }

}

