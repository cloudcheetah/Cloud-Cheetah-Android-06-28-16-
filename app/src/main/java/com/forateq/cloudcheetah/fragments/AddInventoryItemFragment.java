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

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CameraActivity;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Units;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddInventoryItemResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * Created by PC1 on 7/21/2016.
 */
public class AddInventoryItemFragment extends Fragment {

    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
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
    @Bind(R.id.add_item)
    Button addItemButton;
    static final int REQUEST_TAKE_PHOTO = 11111;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_inventory_item_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
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
        ArrayAdapter<String> unitAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Units.getAllUnits());
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitTypeSP.setAdapter(unitAdapter);
        ArrayAdapter<String> vendorAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Vendors.getAllVendorName());
        vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSP.setAdapter(vendorAdapter);
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.add_item)
    public void addItem(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Adding inventory item...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            MultipartBuilder multipartBuilder = new MultipartBuilder();
            File file = new File(itemImageFileNameTV.getText().toString());
            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
            multipartBuilder.addFormDataPart("image", file.getName(), image);
            Log.e("Unit Id", ""+Units.getUnitId(unitTypeSP.getSelectedItem().toString()));
            RequestBody fileRequestBody = multipartBuilder.build();
            Observable<AddInventoryItemResponseWrapper> observable = cloudCheetahAPIService.addInventoryItem(resourceNameET.getText().toString(),
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
                    sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddInventoryItemResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("AddInventory", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(AddInventoryItemResponseWrapper addInventoryItemResponseWrapper) {
                            Resources resources = new Resources();
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
                            InventoryFragment.inventoryItemsAdapter.addItem(resources);
                        }
                    });
        }
        else{
            Resources resources = new Resources();
            resources.setResource_id(0);
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
            resources.setImage(itemImageFileNameTV.getText().toString());
            resources.setOn_hand_qty(0);
            resources.setReserved_qty(0);
            resources.setIn_transit_qty(0);
            resources.save();
            InventoryFragment.inventoryItemsAdapter.addItem(resources);
            MainActivity.popFragment();
        }
    }

    @OnClick(R.id.add_item_image)
    public void addImage(){
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.item_image_filename)
    public void showImage(){
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
