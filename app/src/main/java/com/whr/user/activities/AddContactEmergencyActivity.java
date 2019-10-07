package com.whr.user.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.whr.user.com.WHR.Utils.CommanUtils;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.adapter.AddEmergencyContactAdapter;
import com.whr.user.model.EmergencyNumberMode;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddContactEmergencyActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 123;
    private static final int MY_PERMISSIONS_REQUEST_GALARY = 120;
    private EditText name, cno;
    private TextView SaveButton;
    RequestQueue mQueue;
    private Context context;
    private RecyclerView recyclerView;
    private AddEmergencyContactAdapter addEmergencyContactAdapter;
    private LinearLayoutManager lLayout;
    private TextView AddBtn, ContactBtn;
    private CardView addingLAyout;
    private List<EmergencyNumberMode> rowListItem;
    private PreferenceUtils pref;
    private LinearLayout coordinatorLayout, selectimage;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private Spinner relationShipSpinner;
    int REQUEST_CAMERA = 200, SELECT_FILE = 100;
    private String relationWithContact = "";
    private ImageView emergencyImage;
    private String image_url = null;
    private Bitmap bitmap;
    private String contactNamestr, contcatNumberstr;
    private AppCompatActivity activity;
    private MaterialRippleLayout imgBack;
    private TextView txtTitle;

    private ImageView imgClose;
    CardView cardList;
    ConnectionDector connectionDector;

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_add_emergency_contact_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        activity = AddContactEmergencyActivity.this;
        context = AddContactEmergencyActivity.this;
        connectionDector = new ConnectionDector(context);
        init();
    }

    private void init() {
        imgClose = findViewById(R.id.imgClose);
        cardList = findViewById(R.id.cardList);
        context = AddContactEmergencyActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        rowListItem = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        recyclerView = findViewById(R.id.addemergencyrecyclelist);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        if (connectionDector.isConnectingToInternet()) {
            getAllEmergencyList();
        } else {
            startActivityForResult(new Intent(context, NoInternetConnectionActivity.class), GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionEnter();
        }
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);
        name = findViewById(R.id.EnterName);
        cno = findViewById(R.id.enterPhone);
        SaveButton = findViewById(R.id.savebtton);
        relationShipSpinner = findViewById(R.id.relationShipSpinner);
        AddBtn = findViewById(R.id.txtAdd);
        ContactBtn = findViewById(R.id.txtContact);
        selectimage = findViewById(R.id.selectimage);
        emergencyImage = findViewById(R.id.emergencyImage);
        addingLAyout = findViewById(R.id.addingLAyout);
        addingLAyout.setVisibility(View.GONE);

        imgClose.setOnClickListener(v -> {
            addingLAyout.setVisibility(View.GONE);
            cardList.setVisibility(View.VISIBLE);
            AddBtn.setVisibility(View.VISIBLE);
            ContactBtn.setVisibility(View.VISIBLE);

        });


        txtTitle.setText("Emergency Contacts");
        imgBack.setOnClickListener(v -> onBackPressed());
        selectimage.setOnClickListener(v -> selectImage());

        AddBtn.setOnClickListener(v -> {
            if (rowListItem.size() < 3) {
                emergencyImage.setImageResource(R.drawable.ic_profile);
                name.setText("");
                cno.setText("");
                final List<String> categories = new ArrayList<String>();
                categories.clear();
                categories.add(getString(R.string.PleaseSelectRelationship));
                categories.add("Mother");
                categories.add("Father");
                categories.add("Brother");
                categories.add("Sister");
                categories.add("Relatives");
                categories.add("husband");
                categories.add("Wife");
                categories.add("Office Colleague");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.family_spinner_row, R.id.tvCategory, categories);
                dataAdapter.setDropDownViewResource(R.layout.family_spinner_row);
                relationShipSpinner.setAdapter(dataAdapter);
                relationShipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView,
                                               View view, int i, long l) {
                        if (i == 0) {
                        } else {
                            relationWithContact = categories.get(i);
                            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, categories.get(i), context, R.color.green);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

                addingLAyout.setVisibility(View.VISIBLE);
                AddBtn.setVisibility(View.GONE);
                ContactBtn.setVisibility(View.GONE);
                cardList.setVisibility(View.GONE);


            } else {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.YouCanAddOnlyEmergencyContact), context, R.color.red);
            }
        });

        SaveButton.setOnClickListener(v -> {
            if (cno.getText().toString().trim().length() < 10) {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseEnterValidEmergencyContactNumber), context, R.color.red);
                return;
            } else if (CommanUtils.isEmpty(name.getText().toString().trim()) && CommanUtils.isEmpty(cno.getText().toString().trim())) {
                addingLAyout.setVisibility(View.VISIBLE);
                AddBtn.setVisibility(View.VISIBLE);
                ContactBtn.setVisibility(View.VISIBLE);
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseEnterNameAndEmergencyNumber), context, R.color.red);
                return;
            } else if (name.getText().toString().length() < 1) {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseenterName), context, R.color.red);
            } else if (relationWithContact.equals("Please") || relationWithContact.equals("")) {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseSelectRelationship), context, R.color.red);
                return;
            } else {
                addingLAyout.setVisibility(View.GONE);
                AddBtn.setVisibility(View.VISIBLE);
                ContactBtn.setVisibility(View.VISIBLE);
                AddContact(name.getText().toString().trim(), cno.getText().toString().trim());
            }
        });

        InputFilter[] nameTextfilters = new InputFilter[1];
        nameTextfilters[0] = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                            'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};
                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.SpecialSymbolAreNotAllowed), context, R.color.red);
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        name.setFilters(nameTextfilters);

        ContactBtn.setOnClickListener(v -> {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    GlobalVar.showSnackBarLinearLayout(coordinatorLayout, "Permission Denied\n" + deniedPermissions.toString(), context, R.color.red);
                }
            };
            new TedPermission(context)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_CONTACTS)
                    .check();

            final List<String> categories = new ArrayList<String>();
            categories.clear();
            categories.add(getString(R.string.PleaseSelectRelationship));
            categories.add("Mother");
            categories.add("Father");
            categories.add("Brother");
            categories.add("Sister");
            categories.add("Relatives");
            categories.add("husband");
            categories.add("Wife");
            categories.add("Office Colleague");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.family_spinner_row, R.id.tvCategory, categories);
            dataAdapter.setDropDownViewResource(R.layout.family_spinner_row);
            relationShipSpinner.setAdapter(dataAdapter);
            relationShipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView,
                                           View view, int i, long l) {
                    if (i == 0) {
               /* Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Select relationship", Snackbar.LENGTH_LONG);
                snackbar.show();*/
                    } else {
                        relationWithContact = categories.get(i);
                        GlobalVar.showSnackBarLinearLayout(coordinatorLayout, categories.get(i), context, R.color.green);
                    }
                }

                // If no option selected
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        });
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.TakePhoto),
                getString(R.string.ChooseFromLibrary),
                getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(GlobalVar.changeFont(context, getString(R.string.AddPhoto)));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = Utility.checkPermission(context);
                // String userChoosenTask;
                if (items[item].toString().equals(getString(R.string.TakePhoto))) {
                    boolean result = checkPermission();
                    if (result) {
                        cameraIntent();
                    }
                } else if (items[item].equals(getString(R.string.ChooseFromLibrary))) {
                    boolean result = checkPermissionForGalary();
                    if (result) {
                        galleryIntent();
                    }
                } else if (items[item].equals(getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d("AddEmerg", "Response: " + data.toString());
            uriContact = data.getData();
            contactNamestr = retrieveContactName();
            contcatNumberstr = retrieveContactNumber();
            String contcatNumberstrcontact = contcatNumberstr.replace("+", "").replaceAll("\\s", "").replace("(", "").replace(")", "").replace("-", "");

            //contcatNumberstrcontact.replace("(","").replace(")","").replace("-","")
            // retrieveContactPhoto();
            if (rowListItem.size() < 3) {
                name.setText(contactNamestr.trim());
                cno.setText(contcatNumberstrcontact + "");
                addingLAyout.setVisibility(View.VISIBLE);
                AddBtn.setVisibility(View.GONE);
                ContactBtn.setVisibility(View.GONE);
            } else {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.CanNotAddMoreThanEmergencyNumbers), context, R.color.red);
            }
        }
    }

    private String retrieveContactName() {
        String contactName = null;
        Cursor cursor = context.getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.e("contactName", "Contact Name: " + contactName);
        return contactName;
    }

    private String retrieveContactNumber() {

        String contactNumber = null;
        Cursor cursorID = context.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.e("contactID", "Contact ID: " + contactID);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        Cursor cursorPhone = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);

        if (cursorPhone.moveToNext()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e("phoneNumber", contactNumber + "");
        }
        cursorPhone.close();
        return contactNumber;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            int nh = (int) (bitmap.getHeight() * (700.0 / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 700, nh, true);
            emergencyImage.setImageBitmap(bitmap);
            emergencyImage.setTag("ok");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            image_url = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        int nh = (int) (bitmap.getHeight() * (700.0 / bitmap.getWidth()));
        bitmap = Bitmap.createScaledBitmap(bitmap, 700, nh, true);
        emergencyImage.setImageBitmap(bitmap);
        emergencyImage.setTag("ok");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image_url = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void AddContact(String namestr, String contactNumber) {
        if (CommanUtils.isEmpty(contactNumber)) {
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseEnterValidContactNumber), context, R.color.red);
            return;
        } else if (CommanUtils.isEmpty(contactNumber) && CommanUtils.isEmpty(namestr)) {
            //Toast.makeText(context, "Please Enter Name And Contact Number ", Toast.LENGTH_SHORT).show();
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseEnterNameAndEmergencyNumber), context, R.color.red);
            return;
        } else if (relationWithContact.equals("Please Select relationship") || relationWithContact.equals("")) {
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.PleaseSelectRelationship), context, R.color.red);
            return;
        } else {
            if (rowListItem.size() < 3) {
                sendToServer(namestr, contactNumber);
            } else {
                GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.YouCanAddOnlyEmergencyContact), context, R.color.red);
            }
        }
    }

    String TAG = getClass().getSimpleName();

    public void sendToServer(String emgname, String emgnumber) {

        JSONObject obj = new JSONObject();
        try {
            GlobalVar.showProgressDialog(this, "Adding Emergency Contact...", false);
            String image = getStringImage(bitmap);
            obj.put("uid", pref.getUID());
            obj.put("emergencyContactName", emgname);
            //  obj.put("emgNumber",Long.parseLong(emgnumber)+"");
            obj.put("emgNumber", emgnumber);
            obj.put("relationShip", relationWithContact);
            obj.put("emgContactimage", image);
        } catch (Exception ex) {
            Log.e("UrlException", ex.getMessage().toString());
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/AddEmergency";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    GlobalVar.errorLog(TAG, "sendToServer", response.toString());
                    relationWithContact = "";
                    GlobalVar.hideProgressDialog();
                    JSONObject jsonRootobj = new JSONObject(response.toString());
                    boolean result = response.getBoolean("result");
                    if (result) {
                        cno.setText("");
                        name.setText("");
                        Gson gson = new Gson();
                        EmergencyNumberMode pojo = gson.fromJson(jsonRootobj.toString(), EmergencyNumberMode.class);
                        rowListItem.add(pojo);
                        GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.EmergencyNumberSuccessfullyAdded), context, R.color.green);
                    } else {
                        GlobalVar.showSnackBarLinearLayout(coordinatorLayout, getString(R.string.EmergencyNumberCanNotAddedPleaseTryAgain), context, R.color.green);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addEmergencyContactAdapter = new AddEmergencyContactAdapter(context, rowListItem, activity, coordinatorLayout);
                recyclerView.setAdapter(addEmergencyContactAdapter);
                addingLAyout.setVisibility(View.GONE);
                cardList.setVisibility(View.VISIBLE);
                AddBtn.setVisibility(View.VISIBLE);
                ContactBtn.setVisibility(View.VISIBLE);

            }
        }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public void getAllEmergencyList() {
        JSONObject obj = new JSONObject();
        try {
            GlobalVar.showProgressDialog(this, "Getting Emergency Contacts", false);
            obj.put("uid", pref.getUID());
        } catch (Exception ex) {
            Log.e("UrlException", ex.getMessage().toString());
        }
        String url = GlobalVar.ServerAddress + "AndroidNew/GetEmergency";
        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, obj, response -> {
            GlobalVar.errorLog(TAG, "getAllEmergencyList", response.toString());
            GlobalVar.hideProgressDialog();
            Gson gson = new Gson();
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = response.getJSONArray("emergency");
                if (jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject json = jsonArray1.getJSONObject(i);
                        EmergencyNumberMode pojo = gson.fromJson(json.toString(), EmergencyNumberMode.class);
                        if (rowListItem.size() < 3) {
                            rowListItem.add(pojo);
                        }
                    }
                    addEmergencyContactAdapter = new AddEmergencyContactAdapter(context, rowListItem, activity, coordinatorLayout);
                    recyclerView.setAdapter(addEmergencyContactAdapter);
                } else {
                    startActivityForResult(new Intent(context, NoDataAvailableActivity.class), GlobalVar.NO_DATA_AVAILABLE);
                    overridePendingTransitionEnter();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            GlobalVar.hideProgressDialog();
            GlobalVar.showSnackBarLinearLayout(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    public String getStringImage(Bitmap bmp) {
        String encodedImage = "";
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        return encodedImage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write CAMERA permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMARA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_GALARY: {
                galleryIntent();
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermissionForGalary() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write EXTERNAL STORAGE permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GALARY));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_GALARY);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}