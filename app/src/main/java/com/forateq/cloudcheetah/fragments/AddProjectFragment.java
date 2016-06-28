package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to display the add project view in the app
 *
 * Created by Vallejos Family on 5/20/2016.
 */
public class AddProjectFragment extends Fragment {

    @Bind(R.id.project_name)
    EditText projectNameET;
    @Bind(R.id.project_start_date)
    EditText projectStartDateET;
    @Bind(R.id.project_end_date)
    EditText projectEnddateET;
    @Bind(R.id.budget)
    EditText projectBudgetET;
    @Bind(R.id.project_details)
    EditText projectDetailsET;
    @Bind(R.id.project_objectives)
    EditText projectObjectivesET;
    @Bind(R.id.project_sponsor)
    Spinner projectSponsorSpinner;
    @Bind(R.id.project_manager)
    Spinner projectManagerSpinner;
    @Bind(R.id.add_project)
    Button addprojectButton;
    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_project_fragment, container, false);
        ButterKnife.bind(this, v);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Users.getUsersNames());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectManagerSpinner.setAdapter(nameAdapter);
        projectSponsorSpinner.setAdapter(nameAdapter);
        return v;
    }

    public AddProjectFragment() {
        super();
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.project_start_date)
    void setStartDate(){
        setDate(projectStartDateET);
    }

    @OnClick(R.id.project_end_date)
    void setEndDate(){
        setDate(projectEnddateET);
    }

    @OnClick(R.id.add_project)
    void addProject(){
        Projects projects = new Projects();
        projects.setProject_id(0);
        projects.setName(projectNameET.getText().toString());
        projects.setStart_date(projectStartDateET.getText().toString());
        projects.setEnd_date(projectEnddateET.getText().toString());
        projects.setStatus(AccountGeneral.STATUS_DRAFT);
        projects.setBudget(Double.parseDouble(projectBudgetET.getText().toString()));
        projects.setLatitude(0.0);
        projects.setLongitide(0.0);
        projects.setDescription(projectDetailsET.getText().toString());
        projects.setObjectives(projectObjectivesET.getText().toString());
        projects.setProject_manager(projectManagerSpinner.getSelectedItem().toString());
        projects.setProject_sponsor(projectSponsorSpinner.getSelectedItem().toString());
        projects.save();
        ProjectsFragment.projectAdapter.addItem(projects);
        Toast.makeText(ApplicationContext.get(), "Project successfully added.", Toast.LENGTH_SHORT).show();
        MainActivity.popFragment();
    }

    /**
     * This method is used to display and set the date of a selected the selected edittext
     * @param editText
     */
    public void setDate(final EditText editText){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

}
