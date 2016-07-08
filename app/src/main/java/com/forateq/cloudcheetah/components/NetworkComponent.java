package com.forateq.cloudcheetah.components;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.authenticate.AuthenticatorActivity;
import com.forateq.cloudcheetah.fragments.AddProjectmemberFragment;
import com.forateq.cloudcheetah.fragments.AddResourceFragment;
import com.forateq.cloudcheetah.fragments.AddTaskFragment;
import com.forateq.cloudcheetah.fragments.ERPFragment;
import com.forateq.cloudcheetah.fragments.ProjectUpdateFragment;
import com.forateq.cloudcheetah.fragments.ProjectsComponentsContainerFragment;
import com.forateq.cloudcheetah.modules.AppModule;
import com.forateq.cloudcheetah.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 * Created by Vallejos Family on 5/12/2016.
 */
@Singleton
@Component(modules={AppModule.class, NetworkModule.class})
public interface NetworkComponent {

    void inject(MainActivity activity);
    void inject(AuthenticatorActivity activity);
    void inject(ERPFragment erpFragment);
    void inject(ProjectUpdateFragment projectUpdateFragment);
    void inject(AddTaskFragment addTaskFragment);
    void inject(ProjectsComponentsContainerFragment projectsComponentsContainerFragment);
    void inject(AddProjectmemberFragment addProjectmemberFragment);
    void inject(AddResourceFragment addResourceFragment);
}
