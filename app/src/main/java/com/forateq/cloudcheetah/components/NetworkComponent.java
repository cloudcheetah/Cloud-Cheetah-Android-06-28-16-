package com.forateq.cloudcheetah.components;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.adapters.CashInOutAdapter;
import com.forateq.cloudcheetah.adapters.ContactAdapter;
import com.forateq.cloudcheetah.adapters.ConversationsAdapter;
import com.forateq.cloudcheetah.adapters.ProjectMembersAdapter;
import com.forateq.cloudcheetah.adapters.SubTaskAdapter;
import com.forateq.cloudcheetah.adapters.TaskInProgressViewPagerAdapter;
import com.forateq.cloudcheetah.adapters.TaskProgressAdapter;
import com.forateq.cloudcheetah.authenticate.AuthenticatorActivity;
import com.forateq.cloudcheetah.fragments.AccountViewFragment;
import com.forateq.cloudcheetah.fragments.AddAccountFragment;
import com.forateq.cloudcheetah.fragments.AddCashFlowFragment;
import com.forateq.cloudcheetah.fragments.AddCustomerFragment;
import com.forateq.cloudcheetah.fragments.AddEmployeeFragment;
import com.forateq.cloudcheetah.fragments.AddInventoryItemFragment;
import com.forateq.cloudcheetah.fragments.AddProjectmemberFragment;
import com.forateq.cloudcheetah.fragments.AddResourceFragment;
import com.forateq.cloudcheetah.fragments.AddTaskFragment;
import com.forateq.cloudcheetah.fragments.AddTaskProgressReportFragment;
import com.forateq.cloudcheetah.fragments.AddTaskResourceFragment;
import com.forateq.cloudcheetah.fragments.AddVendorFragment;
import com.forateq.cloudcheetah.fragments.CustomerViewFragment;
import com.forateq.cloudcheetah.fragments.ERPFragment;
import com.forateq.cloudcheetah.fragments.EditResourceFragment;
import com.forateq.cloudcheetah.fragments.MainFragment;
import com.forateq.cloudcheetah.fragments.MyTasksFragment;
import com.forateq.cloudcheetah.fragments.ProfileFragment;
import com.forateq.cloudcheetah.fragments.ProjectChatFragment;
import com.forateq.cloudcheetah.fragments.ProjectUpdateFragment;
import com.forateq.cloudcheetah.fragments.ProjectsComponentsContainerFragment;
import com.forateq.cloudcheetah.fragments.SingleChatFragment;
import com.forateq.cloudcheetah.fragments.UpdateDeleteInventoryItemFragment;
import com.forateq.cloudcheetah.fragments.UpdateEmployeeFragment;
import com.forateq.cloudcheetah.fragments.UpdateHREmployeeFragment;
import com.forateq.cloudcheetah.fragments.VendorViewFragment;
import com.forateq.cloudcheetah.modules.AppModule;
import com.forateq.cloudcheetah.modules.NetworkModule;
import com.forateq.cloudcheetah.service.NotificationOpenedHandler;

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
    void inject(EditResourceFragment editResourceFragment);
    void inject(ProjectMembersAdapter projectMembersAdapter);
    void inject(AddTaskProgressReportFragment addTaskProgressReportFragment);
    void inject(AddCashFlowFragment addCashFlowFragment);
    void inject(TaskProgressAdapter taskProgressAdapter);
    void inject(CashInOutAdapter cashInOutAdapter);
    void inject(AddTaskResourceFragment addTaskResourceFragment);
    void inject(SubTaskAdapter subTaskAdapter);
    void inject(AddInventoryItemFragment addInventoryItemFragment);
    void inject(UpdateDeleteInventoryItemFragment updateDeleteInventoryItemFragment);
    void inject(AddAccountFragment addAccountFragment);
    void inject(AccountViewFragment accountViewFragment);
    void inject(AddCustomerFragment addCustomerFragment);
    void inject(CustomerViewFragment customerViewFragment);
    void inject(AddVendorFragment addVendorFragment);
    void inject(VendorViewFragment vendorViewFragment);
    void inject(SingleChatFragment singleChatFragment);
    void inject(ContactAdapter contactAdapter);
    void inject(ConversationsAdapter conversationsAdapter);
    void inject(MainFragment mainFragment);
    void inject(UpdateEmployeeFragment updateEmployeeFragment);
    void inject(ProfileFragment profileFragment);
    void inject(UpdateHREmployeeFragment updateHREmployeeFragment);
    void inject(AddEmployeeFragment addEmployeeFragment);
    void inject(ProjectChatFragment projectChatFragment);
    void inject(NotificationOpenedHandler notificationOpenedHandler);
    void inject(TaskInProgressViewPagerAdapter taskInProgressViewPagerAdapter);
    void inject(MyTasksFragment myTasksFragment);
}
