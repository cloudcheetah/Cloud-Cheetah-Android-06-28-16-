package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forateq.cloudcheetah.R;

import butterknife.ButterKnife;

/** This fragment is used to display the details of the selected project
 * Created by Vallejos Family on 5/18/2016.
 */
public class ProjectDetailsFragment extends Fragment {

    public static final String TAG = "ProjectDetailsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.project_details_fragment, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}
