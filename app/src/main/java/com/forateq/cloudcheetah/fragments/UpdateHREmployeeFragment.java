package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CameraActivity;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Units;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddInventoryItemResponseWrapper;
import com.forateq.cloudcheetah.pojo.EmployeeResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.utils.MonthYearPickerDialog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC1 on 8/9/2016.
 */
public class UpdateHREmployeeFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;
    @Bind(R.id.ripple_update)
    MaterialRippleLayout rippleUpdate;
    @Bind(R.id.update_save)
    ImageView updateSaveIV;
    @Bind(R.id.first_name)
    EditText firstNameET;
    @Bind(R.id.last_name)
    EditText lastNameET;
    @Bind(R.id.middle_name)
    EditText middleNameET;
    @Bind(R.id.gender)
    Spinner genderSP;
    @Bind(R.id.date_of_birth)
    EditText birthDayET;
    @Bind(R.id.address)
    EditText addressET;
    @Bind(R.id.email)
    EditText emailET;
    @Bind(R.id.contact_no)
    EditText contactNoET;
    @Bind(R.id.status)
    Spinner statusSP;
    @Bind(R.id.title)
    EditText titleET;
    @Bind(R.id.employment_type)
    Spinner employmentTypeSP;
    @Bind(R.id.zip_code)
    EditText zipCodeET;
    @Bind(R.id.tin_no)
    EditText tinNoET;
    @Bind(R.id.sss_no)
    EditText sssNoET;
    @Bind(R.id.driver_license_no)
    EditText driverLicenseNoET;
    @Bind(R.id.civil_status)
    Spinner civilStatusSP;
    @Bind(R.id.notes)
    EditText notesET;
    @Bind(R.id.profile_image_filename)
    TextView profileImageFileNameTV;
    @Bind(R.id.add_profile_image)
    Button addProfileImageBT;
    @Bind(R.id.update_profile)
    Button updateProfileBT;
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String EMPLOYMENT_TYPE_CONTRACTUAL = "Contractual";
    public static final String EMPLOYMENT_TYPE_REGULAR = "Regular";
    public static final String EMPLOYMENT_TYPE_PROBATION = "Probationary";
    public static final String CIVIL_STATUS_MARRIED = "Married";
    public static final String CIVIL_STATUS_SINGLE = "Single";
    public static final String CIVIL_STATUS_WIDOWED = "Widowed";
    public List<String> genderList;
    public List<String> employmentList;
    public List<String> civilStatusList;
    Employees employees;
    int employee_id;
    static final int REQUEST_TAKE_PHOTO = 11111;
    boolean isPicChange;
    boolean isSave;
    int position;
    int size;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    RequestBody fileRequestBody;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_hr_employee_fragment, container, false);
        employee_id = getArguments().getInt("employee_id");
        position = getArguments().getInt("position");
        size = getArguments().getInt("size");
        employees = Employees.getEmployee(employee_id);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    public void disAbleUpdate(){
        firstNameET.setEnabled(false);
        lastNameET.setEnabled(false);
        middleNameET.setEnabled(false);
        genderSP.setEnabled(false);
        birthDayET.setEnabled(false);
        addressET.setEnabled(false);
        emailET.setEnabled(false);
        contactNoET.setEnabled(false);
        statusSP.setEnabled(false);
        titleET.setEnabled(false);
        employmentTypeSP.setEnabled(false);
        zipCodeET.setEnabled(false);
        tinNoET.setEnabled(false);
        sssNoET.setEnabled(false);
        driverLicenseNoET.setEnabled(false);
        civilStatusSP.setEnabled(false);
        notesET.setEnabled(false);
        addProfileImageBT.setEnabled(false);
    }

    public void enableUpdate(){
        firstNameET.setEnabled(true);
        lastNameET.setEnabled(true);
        middleNameET.setEnabled(true);
        genderSP.setEnabled(true);
        birthDayET.setEnabled(true);
        addressET.setEnabled(true);
        emailET.setEnabled(true);
        contactNoET.setEnabled(true);
        statusSP.setEnabled(true);
        titleET.setEnabled(true);
        employmentTypeSP.setEnabled(true);
        zipCodeET.setEnabled(true);;
        tinNoET.setEnabled(true);
        sssNoET.setEnabled(true);
        driverLicenseNoET.setEnabled(true);
        civilStatusSP.setEnabled(true);
        notesET.setEnabled(true);
        addProfileImageBT.setEnabled(true);
    }

    @OnClick(R.id.ripple_update)
    public void update(){
        if(isSave){
            if(isNetworkAvailable()){
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating Employee...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if(isPicChange){
                    MultipartBuilder multipartBuilder = new MultipartBuilder();
                    File file = new File(profileImageFileNameTV.getText().toString());
                    RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                    multipartBuilder.addFormDataPart("image", file.getName(), image);
                    fileRequestBody = multipartBuilder.build();
                }
                else{
                    MultipartBuilder multipartBuilder = new MultipartBuilder();
                    multipartBuilder.addFormDataPart("image", "");
                    fileRequestBody = multipartBuilder.build();
                }
                Observable<EmployeeResponseWrapper> observable = cloudCheetahAPIService.updateEmployee(employee_id,
                        firstNameET.getText().toString(),
                        middleNameET.getText().toString(),
                        lastNameET.getText().toString(),
                        genderSP.getSelectedItemPosition(),
                        birthDayET.getText().toString(),
                        addressET.getText().toString(),
                        emailET.getText().toString(),
                        contactNoET.getText().toString(),
                        1,
                        titleET.getText().toString(),
                        employmentTypeSP.getSelectedItemPosition(),
                        zipCodeET.getText().toString(),
                        tinNoET.getText().toString(),
                        sssNoET.getText().toString(),
                        driverLicenseNoET.getText().toString(),
                        civilStatusSP.getSelectedItemPosition(),
                        notesET.getText().toString(),
                        fileRequestBody,
                        userName,
                        deviceid,
                        sessionKey,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<EmployeeResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                updateSaveIV.setImageResource(R.mipmap.ic_mode_edit_white_24dp);
                                isSave = false;
                                disAbleUpdate();
                                MainActivity.popFragment();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                Log.e("UpdateEmployee", e.getMessage(), e);
                            }

                            @Override
                            public void onNext(EmployeeResponseWrapper employeeResponseWrapper) {
                                if(employeeResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    Log.e("Name", employeeResponseWrapper.getData().getFirst_name());
                                    employees.setFirst_name(firstNameET.getText().toString());
                                    employees.setMiddle_name(middleNameET.getText().toString());
                                    employees.setLast_name(lastNameET.getText().toString());
                                    employees.setDate_of_birth(birthDayET.getText().toString());
                                    employees.setGender_id(genderSP.getSelectedItemPosition());
                                    employees.setAddress(addressET.getText().toString());
                                    employees.setEmail_address(emailET.getText().toString());
                                    employees.setContact_no(contactNoET.getText().toString());
                                    employees.setTitle(titleET.getText().toString());
                                    employees.setEmployment_type_id(employmentTypeSP.getSelectedItemPosition());
                                    employees.setZip_code(zipCodeET.getText().toString());
                                    employees.setTin_no(tinNoET.getText().toString());
                                    employees.setSss_no(sssNoET.getText().toString());
                                    employees.setDrivers_license_no(driverLicenseNoET.getText().toString());
                                    employees.setCivil_status_id(civilStatusSP.getSelectedItemPosition());
                                    employees.setNotes(notesET.getText().toString());
                                    employees.setImage(employeeResponseWrapper.getData().getImage());
                                    employees.save();
                                }
                                else{
                                    Toast.makeText(getActivity(), "There is a problem updating the employee please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(ApplicationContext.get(), "Please connect to a network to update the item.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            updateSaveIV.setImageResource(R.mipmap.ic_save_white_24dp);
            isSave = true;
            enableUpdate();
        }
    }


    @OnClick(R.id.ripple_delete)
    public void delete(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Delete")
                    .content("Are you sure you want to delete this employee?")
                    .contentColorRes(R.color.colorText)
                    .titleColorRes(R.color.colorText)
                    .backgroundColorRes(R.color.colorPrimary)
                    .widgetColorRes(R.color.colorText)
                    .positiveText("Ok")
                    .negativeText("Cancel")
                    .positiveColorRes(R.color.colorText)
                    .negativeColorRes(R.color.colorText)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Deleting Employee...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteEmployee(employee_id, userName, deviceid, sessionKey, AccountGeneral.METHOD_DELETE);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            new Delete().from(Employees.class).where("employee_id = ?", employee_id).execute();
                                            EmployeesFragment.employeesAdapter.notifyItemRemoved(position);
                                            EmployeesFragment.employeesAdapter.notifyItemRangeChanged(position, size);
                                            Toast.makeText(ApplicationContext.get(), "Employee deleted successfully.", Toast.LENGTH_SHORT).show();
                                            MainActivity.popFragment();
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            Log.e("DeleteEmployee", e.getMessage(), e);
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){

                                            }
                                            else{
                                                Toast.makeText(getActivity(), "There is an error deleting the employee. Error Code: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                            }
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    });
            final MaterialDialog addNoteDialog = createNoteDialog.build();
            addNoteDialog.show();
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to delete this employee.", Toast.LENGTH_SHORT).show();
        }
    }

    public void init(){
        genderList = new ArrayList<>();
        genderList.add(GENDER_MALE);
        genderList.add(GENDER_FEMALE);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSP.setAdapter(genderAdapter);
        int selectionPositionGender= genderAdapter.getPosition(genderList.get(employees.getGender_id()));
        genderSP.setSelection(selectionPositionGender);
        employmentList = new ArrayList<>();
        employmentList.add(EMPLOYMENT_TYPE_CONTRACTUAL);
        employmentList.add(EMPLOYMENT_TYPE_PROBATION);
        employmentList.add(EMPLOYMENT_TYPE_REGULAR);
        ArrayAdapter<String> employmentAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, employmentList);
        employmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employmentTypeSP.setAdapter(employmentAdapter);
        int selectionPositionEmployment= employmentAdapter.getPosition(employmentList.get(employees.getEmployment_type_id()));
        employmentTypeSP.setSelection(selectionPositionEmployment);
        civilStatusList = new ArrayList<>();
        civilStatusList.add(CIVIL_STATUS_SINGLE);
        civilStatusList.add(CIVIL_STATUS_MARRIED);
        civilStatusList.add(CIVIL_STATUS_WIDOWED);
        ArrayAdapter<String> civilStatusAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, civilStatusList);
        civilStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        civilStatusSP.setAdapter(civilStatusAdapter);
        int selectionPositionCivilStatus= civilStatusAdapter.getPosition(civilStatusList.get(employees.getCivil_status_id()));
        civilStatusSP.setSelection(selectionPositionCivilStatus);
        firstNameET.setText(employees.getFirst_name());
        lastNameET.setText(employees.getLast_name());
        middleNameET.setText(employees.getMiddle_name());
        birthDayET.setText(employees.getDate_of_birth());
        addressET.setText(employees.getAddress());
        emailET.setText(employees.getEmail_address());
        contactNoET.setText(employees.getContact_no());
        zipCodeET.setText(employees.getZip_code());
        tinNoET.setText(employees.getTin_no());
        sssNoET.setText(employees.getSss_no());
        driverLicenseNoET.setText(employees.getDrivers_license_no());
        notesET.setText(employees.getNotes());
        titleET.setText(employees.getTitle());
        profileImageFileNameTV.setText(employees.getImage());
        disAbleUpdate();
    }

    @OnClick(R.id.date_of_birth)
    public void setBirthDay(){
        setDate(birthDayET);
    }

    public void setDate(final EditText editText){
        Calendar calendar = Calendar.getInstance();
        MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.YEAR));

        pd.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                String currentDateFormat = "";

                if(selectedYear == 1904)
                {
                    currentDateFormat = selectedMonth + "/" + selectedDay;// + "/" + selectedYear;  //"MM/dd/yyyy"
                }
                else{
                    currentDateFormat = selectedMonth + "/" + selectedDay + "/" + selectedYear;  //"MM/dd/yyyy"

                }

                editText.setText(currentDateFormat);
            }
        });
        pd.show(getFragmentManager(), "MonthYearPickerDialog");
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Error", ex.getMessage(), ex);
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/CloudCheetah/Pictures");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity)getActivity();
        Uri contentUri = Uri.fromFile(image);
        activity.setCurrentPhotoPath("file:" + image.getPath());
        activity.setCapturedImageURI(contentUri);
        return image;
    }

    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == MainActivity.RESULT_OK) {
            addPhotoToGallery();
            CameraActivity activity = (CameraActivity)getActivity();
            Log.e("Photo Path", activity.getCurrentPhotoPath());
            profileImageFileNameTV.setText(activity.getCurrentPhotoPath());
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @OnClick(R.id.profile_image_filename)
    public void showImage(){
        if(isPicChange){
            if(!profileImageFileNameTV.getText().toString().equals("Attachment")){
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(profileImageFileNameTV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Item Image")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
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
        else{
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load("http://"+profileImageFileNameTV.getText().toString()).placeholder( R.drawable.progress_animation ).resize(500, 500)
                    .centerCrop().into(imageView);
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Item Image")
                    .titleColorRes(R.color.colorText)
                    .backgroundColorRes(R.color.colorPrimary)
                    .widgetColorRes(R.color.colorText)
                    .customView(imageView, true)
                    .positiveText("Ok")
                    .positiveColorRes(R.color.colorText)
                    .negativeColorRes(R.color.colorText)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
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
    @OnClick(R.id.add_profile_image)
    public void addImage(){
        isPicChange = true;
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.update_profile)
    public void updateProfile(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Updating profile...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if(isPicChange){
                MultipartBuilder multipartBuilder = new MultipartBuilder();
                File file = new File(profileImageFileNameTV.getText().toString());
                RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                multipartBuilder.addFormDataPart("image", file.getName(), image);
                fileRequestBody = multipartBuilder.build();
            }
            else{
                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.addFormDataPart("image", "");
                fileRequestBody = multipartBuilder.build();
            }
            Observable<EmployeeResponseWrapper> observable = cloudCheetahAPIService.updateEmployee(employee_id,
                    firstNameET.getText().toString(),
                    middleNameET.getText().toString(),
                    lastNameET.getText().toString(),
                    genderSP.getSelectedItemPosition(),
                    birthDayET.getText().toString(),
                    addressET.getText().toString(),
                    emailET.getText().toString(),
                    contactNoET.getText().toString(),
                    1,
                    titleET.getText().toString(),
                    employmentTypeSP.getSelectedItemPosition(),
                    zipCodeET.getText().toString(),
                    tinNoET.getText().toString(),
                    sssNoET.getText().toString(),
                    driverLicenseNoET.getText().toString(),
                    civilStatusSP.getSelectedItemPosition(),
                    notesET.getText().toString(),
                    fileRequestBody,
                    userName,
                    deviceid,
                    sessionKey,
                    AccountGeneral.METHOD_PUT);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<EmployeeResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            Log.e("UpdateEmployee", e.getMessage(), e);
                        }

                        @Override
                        public void onNext(EmployeeResponseWrapper employeeResponseWrapper) {
                            if(employeeResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Log.e("Name", employeeResponseWrapper.getData().getFirst_name());
                                employees.setFirst_name(firstNameET.getText().toString());
                                employees.setMiddle_name(middleNameET.getText().toString());
                                employees.setLast_name(lastNameET.getText().toString());
                                employees.setDate_of_birth(birthDayET.getText().toString());
                                employees.setGender_id(genderSP.getSelectedItemPosition());
                                employees.setAddress(addressET.getText().toString());
                                employees.setEmail_address(emailET.getText().toString());
                                employees.setContact_no(contactNoET.getText().toString());
                                employees.setTitle(titleET.getText().toString());
                                employees.setEmployment_type_id(employmentTypeSP.getSelectedItemPosition());
                                employees.setZip_code(zipCodeET.getText().toString());
                                employees.setTin_no(tinNoET.getText().toString());
                                employees.setSss_no(sssNoET.getText().toString());
                                employees.setDrivers_license_no(driverLicenseNoET.getText().toString());
                                employees.setCivil_status_id(civilStatusSP.getSelectedItemPosition());
                                employees.setNotes(notesET.getText().toString());
                                employees.setImage(employeeResponseWrapper.getData().getImage());
                                employees.save();
                            }
                            else{
                                Toast.makeText(getActivity(), "There is a problem updating your profile please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to update your profile.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
