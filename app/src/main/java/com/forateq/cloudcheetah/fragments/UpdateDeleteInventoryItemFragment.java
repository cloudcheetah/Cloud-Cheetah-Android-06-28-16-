package com.forateq.cloudcheetah.fragments;

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
import com.forateq.cloudcheetah.adapters.InventoryItemsAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Units;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddInventoryItemResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Created by PC1 on 7/22/2016.
 */
public class UpdateDeleteInventoryItemFragment extends Fragment {

    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;
    @Bind(R.id.ripple_update)
    MaterialRippleLayout rippleUpdate;
    @Bind(R.id.update_delete)
    ImageView updateSaveIV;
    @Bind(R.id.item_label)
    TextView itemLabelTV;
    @Bind(R.id.resource_name)
    EditText resourceNameET;
    @Bind(R.id.item_type)
    Spinner itemTypeSP;
    @Bind(R.id.description)
    EditText descriptionET;
    @Bind(R.id.account)
    Spinner accountSP;
    @Bind(R.id.unit_type)
    Spinner unitTypeSP;
    @Bind(R.id.unit_cost)
    EditText unitCostET;
    @Bind(R.id.sales_price)
    EditText salesPriceET;
    @Bind(R.id.reorder_point)
    EditText reorderPointET;
    @Bind(R.id.vendor)
    Spinner vendorSP;
    @Bind(R.id.notes)
    EditText notesET;
    @Bind(R.id.item_image_filename)
    TextView itemImageFileNameTV;
    @Bind(R.id.add_item_image)
    Button addItemImageButton;
    static final int REQUEST_TAKE_PHOTO = 11111;
    boolean isSave;
    boolean isPicChange;
    int resource_id;
    Resources resources;
    RequestBody fileRequestBody;
    int listPosition;
    int size;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_delete_inventory_item_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        resource_id = Integer.parseInt(getArguments().getString("resource_id"));
        listPosition = Integer.parseInt(getArguments().getString("position"));
        size = Integer.parseInt(getArguments().getString("size"));
        resources = Resources.getResource(resource_id);
        List<String> typeList = new ArrayList<>();
        typeList.add("Equipment");
        typeList.add("Supply");
        typeList.add("Item");
        typeList.add("BOM");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemTypeSP.setAdapter(typeAdapter);
        ArrayAdapter<String> accountAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Accounts.getAccountsName());
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSP.setAdapter(accountAdapter);
        int selectionPositionAccount= accountAdapter.getPosition(Accounts.getAccountName(resources.getAccount_id()));
        accountSP.setSelection(selectionPositionAccount);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Units.getAllUnits());
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeSP.setAdapter(unitAdapter);
        Log.e("Unit Id", ""+resources.getUnit_id());
        int selectionPositionUnit = unitAdapter.getPosition(Units.getUnitName(resources.getUnit_id()));
        unitTypeSP.setSelection(selectionPositionUnit);
        ArrayAdapter<String> vendorAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Vendors.getAllVendorName());
        vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSP.setAdapter(vendorAdapter);
        int selectionPositionVendor = vendorAdapter.getPosition(Vendors.getVendorName(resources.getVendor_id()));
        vendorSP.setSelection(selectionPositionVendor);
        disAbleEdit();
        init();
        return v;
    }

    public void init(){
        resourceNameET.setText(resources.getName());
        descriptionET.setText(resources.getDescription());
        unitCostET.setText(""+resources.getUnit_cost());
        salesPriceET.setText(""+resources.getSales_price());
        reorderPointET.setText(""+resources.getReorder_point());
        notesET.setText(resources.getNotes());
        itemLabelTV.setText(resources.getName());
        itemImageFileNameTV.setText(resources.getImage());
    }

    @OnClick(R.id.ripple_update)
    public void update(){
        if(isSave){
            if(isNetworkAvailable()){
                Log.e("Id", ""+resources.getResource_id());
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating inventory item...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if(isPicChange){
                    MultipartBuilder multipartBuilder = new MultipartBuilder();
                    File file = new File(itemImageFileNameTV.getText().toString());
                    RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                    multipartBuilder.addFormDataPart("image", file.getName(), image);
                    fileRequestBody = multipartBuilder.build();
                }
                else{
                    MultipartBuilder multipartBuilder = new MultipartBuilder();
                    multipartBuilder.addFormDataPart("image", "");
                    fileRequestBody = multipartBuilder.build();
                }
                Observable<AddInventoryItemResponseWrapper> observable = cloudCheetahAPIService.updateInventoryItem(resources.getResource_id(), resourceNameET.getText().toString(),
                        0,
                        descriptionET.getText().toString(),
                        0,
                        Accounts.getAccountId(accountSP.getSelectedItem().toString()),
                        1,
                        Units.getUnitId(unitTypeSP.getSelectedItem().toString()),
                        Double.parseDouble(unitCostET.getText().toString()),
                        Double.parseDouble(salesPriceET.getText().toString()),
                        Integer.parseInt(reorderPointET.getText().toString()),
                        Vendors.getVendorId(vendorSP.getSelectedItem().toString()),
                        notesET.getText().toString(),
                        fileRequestBody,
                        userName,
                        deviceid,
                        sessionKey,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddInventoryItemResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                updateSaveIV.setImageResource(R.mipmap.ic_mode_edit_white_24dp);
                                isSave = false;
                                disAbleEdit();
                                MainActivity.popFragment();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("UpdateInventory", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(AddInventoryItemResponseWrapper addInventoryItemResponseWrapper) {
                                if(addInventoryItemResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    resources.setResource_id(addInventoryItemResponseWrapper.getData().getId());
                                    resources.setName(resourceNameET.getText().toString());
                                    resources.setDescription(descriptionET.getText().toString());
                                    resources.setParent_id(0);
                                    resources.setAccount_id(Accounts.getAccountId(accountSP.getSelectedItem().toString()));
                                    resources.setType_id(0);
                                    resources.setUnit_id(Units.getUnitId(unitTypeSP.getSelectedItem().toString()));
                                    resources.setUnit_cost(Double.parseDouble(unitCostET.getText().toString()));
                                    resources.setSales_price(Double.parseDouble(salesPriceET.getText().toString()));
                                    resources.setReorder_point(Integer.parseInt(reorderPointET.getText().toString()));
                                    resources.setVendor_id(Vendors.getVendorId(vendorSP.getSelectedItem().toString()));
                                    resources.setNotes(notesET.getText().toString());
                                    resources.setImage(addInventoryItemResponseWrapper.getData().getImage());
                                    resources.setOn_hand_qty(0);
                                    resources.setReserved_qty(0);
                                    resources.setIn_transit_qty(0);
                                    resources.save();
                                    Toast.makeText(ApplicationContext.get(), "Item successfully updated.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(ApplicationContext.get(), "There is a problem updating the item. Please contact the administrator. Error "+addInventoryItemResponseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
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
            enabledEdit();
        }
    }

    @OnClick(R.id.add_item_image)
    public void addImage(){
        isPicChange = true;
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.ripple_delete)
    public void delete(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Delete")
                    .content("Are you sure you want to delete this item?")
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
                            mProgressDialog.setMessage("Deleting inventory item...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteInventoryItem(resource_id, userName, deviceid, sessionKey, AccountGeneral.METHOD_DELETE);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            MainActivity.popFragment();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("DeleteInventory", e.getMessage(), e);
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                new Delete().from(Resources.class).where("resource_id = ?", resource_id).execute();
                                                InventoryFragment.inventoryItemsAdapter.notifyItemRemoved(listPosition);
                                                InventoryFragment.inventoryItemsAdapter.notifyItemRangeChanged(listPosition,size);
                                                Toast.makeText(ApplicationContext.get(), "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(ApplicationContext.get(), "There is a problem updating the item. Please contact the administrator. Error code: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please connect to a network to delete this item.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    public void enabledEdit(){
        resourceNameET.setEnabled(true);
        itemTypeSP.setEnabled(true);
        descriptionET.setEnabled(true);
        accountSP.setEnabled(true);
        unitTypeSP.setEnabled(true);
        unitCostET.setEnabled(true);
        salesPriceET.setEnabled(true);
        reorderPointET.setEnabled(true);
        vendorSP.setEnabled(true);
        notesET.setEnabled(true);
        addItemImageButton.setEnabled(true);
    }

    public void disAbleEdit(){
        resourceNameET.setEnabled(false);
        itemTypeSP.setEnabled(false);
        descriptionET.setEnabled(false);
        accountSP.setEnabled(false);
        unitTypeSP.setEnabled(false);
        unitCostET.setEnabled(false);
        salesPriceET.setEnabled(false);
        reorderPointET.setEnabled(false);
        vendorSP.setEnabled(false);
        notesET.setEnabled(false);
        addItemImageButton.setEnabled(false);
    }

    @OnClick(R.id.item_image_filename)
    public void showImage(){
        if(isPicChange){
            if(!itemImageFileNameTV.getText().toString().equals("Attachment")){
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(itemImageFileNameTV.getText().toString())).resize(500, 500)
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
            Picasso.with(ApplicationContext.get()).load("http://"+itemImageFileNameTV.getText().toString()).placeholder( R.drawable.progress_animation ).resize(500, 500)
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
            itemImageFileNameTV.setText(activity.getCurrentPhotoPath());
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
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
