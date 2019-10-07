package com.whr.user.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.whr.user.com.WHR.Utils.CustomStringRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationFindActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {
    Context context = this;
    private EditText mAutocompleteView;
    private ListView listView;
    ImageView delete;
    public static int lcnt = 0;
    public static String myLocation = "";
    public static double lat = 0.0f, lon = 0.0f;
    public static int locCnt = 0;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDIeuy9XoiR-Nc3LqJm4qqlGgufUbxmZzY";
    public static String receivedValue = "", un, mailUrl;
    int cnt = 0;
    ArrayAdapter<String> adapter;
    JSONArray peoples = null;
    private static final String TAG_RESULTS = "predictions";
    public static String title[];
    TextView currentAddress;
    private Gson gson;
    RequestQueue mQueue;
    String TAG = getClass().getSimpleName();

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    private ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    MaterialRippleLayout imgBack;

    public static String fullAddress;
    public static String postalCode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_find);
        findView();
        gson = new Gson();

        context = this;
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        delete.setOnClickListener(v -> mAutocompleteView.setText(""));
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());


        //  updateValuesFromBundle(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        currentAddress.setOnClickListener(v -> {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    mGoogleApiClient.connect();
                    buildGoogleApiClient();
                    checkLocationSettings();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(LocationFindActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };

            new TedPermission(LocationFindActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        });


        mAutocompleteView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mailUrl = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?sensor=false&key=" + API_KEY + "&input=" + mAutocompleteView.getText().toString();
                GlobalVar.errorLog(TAG, "mailUrl", mailUrl);
                try {
                    getAll();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Error=" + ex.toString(), Toast.LENGTH_SHORT).show();
                }

                if (count >= 1) {
                    listView.setVisibility(View.VISIBLE);
                } else if (count == 0) {
                    listView.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                locCnt = 1;
                String val = (String) parent.getItemAtPosition(position);
                progressBarFromAddress(val);

                GlobalVar.errorLog(TAG, "l", "Lat=" + lat + " Long=" + lon);
                String[] animalsArray = val.split(",");
                myLocation = animalsArray[0];
                lcnt = 1;
                GlobalVar.errorLog(TAG, "myLocation", myLocation);
                onBackPressed();
            } catch (Exception ex) {
                Toast.makeText(LocationFindActivity.this, "Error=" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void findView() {
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        HideSoft(coordinatorLayout);
        currentAddress = findViewById(R.id.currenLocation);
        mAutocompleteView = findViewById(R.id.autocomplete_places);
        delete = findViewById(R.id.cross);
        listView = findViewById(R.id.recyclerView);
        listView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progresBar);
        progressBar.incrementProgressBy(10);
        progressBar.setIndeterminate(true);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.e(TAG, "Building GoogleApiClient");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);

    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, "All location settings are satisfied.");
                //progressBar.setVisibility(View.VISIBLE);
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");

                try {
                    status.startResolutionForResult(LocationFindActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }


    protected void startLocationUpdates() {
        //progressBar.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
            }
        });

    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            try {
                if (Geocoder.isPresent()) {
                    Geocoder geocoder;
                    List<Address> addresses;
                    lat = mCurrentLocation.getLatitude();
                    lon = mCurrentLocation.getLongitude();
                    Log.e(TAG, "loca " + lat + ", " + lon);
                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses.size() > 0) {
                        String city = addresses.get(0).getSubLocality();
                        myLocation = city;

                        fullAddress = addresses.get(0).getAddressLine(0);
                        postalCode = addresses.get(0).getPostalCode();
                        Log.e("city", city);
                        GlobalVar.errorLog(TAG, "loca", city);
                        HideSoft(coordinatorLayout);
                        finish();
                        //   startActivity(new Intent(LocationFindActivity.this, NevigationDrawerDashBordActivity.class));
                    } else {
                        getAddressGoogleApi();
                    }
                } else {
                    getAddressGoogleApi();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                getAddressGoogleApi();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void getAddressGoogleApi() {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + LocationFindActivity.lat + "," + LocationFindActivity.lon + "&sensor=true";
        url = url.replace(" ", "%20");
        GlobalVar.errorLog(TAG, "latLng", url);

        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, url, response -> {
            //      GlobalVar.errorLog(TAG, "getAddressGoogleApi", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                String status = jsonObjectResponse.getString("status");
                if (status.equalsIgnoreCase("ok")) {
                    JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");
                    JSONObject jsonObject = jsonArrayResults.getJSONObject(0);
                    JSONArray jsonArrayAddressComponent = jsonObject.getJSONArray("address_components");
                    LocationFindActivity.myLocation = jsonArrayAddressComponent.getJSONObject(4).getString("long_name");
                    GlobalVar.errorLog(TAG, "long_name", LocationFindActivity.myLocation);
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                startActivity(new Intent(LocationFindActivity.this, NevigationDrawerDashBordActivity.class));

            }
        }, error -> {
            GlobalVar.errorLog(TAG, "getAddressGoogleApi", "VolleyError");
            startActivity(new Intent(LocationFindActivity.this, NevigationDrawerDashBordActivity.class));
        });
        mQueue.add(stringRequest);
    }

    public void getAll() {
        progressBar.setVisibility(View.VISIBLE);
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, mailUrl, response -> {
            progressBar.setVisibility(View.GONE);
            receivedValue = response;
            showList();
        }, error -> progressBar.setVisibility(View.GONE));
        mQueue.add(stringRequest);
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(receivedValue);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            cnt = 0;
            for (int i = 0; i < peoples.length(); i++) {
                cnt++;
            }
            title = new String[cnt];
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                title[i] = c.getString("description");
            }

            try {
                listView.setAdapter(null);
            } catch (Exception ex) {

            }
            adapter = new ArrayAdapter<>(context, R.layout.family_spinner_row,
                    R.id.tvCategory, title);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            mAutocompleteView.setText("");
        }
    }

    public String progressBarFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this, Locale.ENGLISH);
        List<Address> address;
        try {
            if (Geocoder.isPresent()) {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    getLatLngGoogleApi(strAddress);
                } else {
                    GlobalVar.errorLog(TAG, strAddress, String.valueOf(address.size()));
                    if (address.size() != 0) {
                        for (Address a : address) {
                            if (a != null) {
                                if (a.getLatitude() != 0.0 & a.getLongitude() != 0.0) {
                                    lat = a.getLatitude();
                                    lon = a.getLongitude();
                                    Log.e(TAG, "Lat=" + lat + " Long=" + lon);
                                    break;
                                }
                            }
                        }
                    } else {
                        getLatLngGoogleApi(strAddress);
                    }
                }
            } else {
                getLatLngGoogleApi(strAddress);
            }


        } catch (Exception ex) {
            getLatLngGoogleApi(strAddress);
        }
        return null;
    }

    public void getLatLngGoogleApi(final String address) {

        String latLngUrl = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";
        latLngUrl = latLngUrl.replace(" ", "%20");
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, latLngUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);
                    JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");
                    JSONObject jsonObjectLocation = jsonArrayResults.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    lat = jsonObjectLocation.getDouble("lat");
                    lon = jsonObjectLocation.getDouble("lng");
                    GlobalVar.errorLog(TAG, "getLatLngGoogleApi" + address + " lat", String.valueOf(lat));
                    GlobalVar.errorLog(TAG, "getLatLngGoogleApi " + address + " lon", String.valueOf(lon));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
        });

        mQueue.add(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransitionExit();
        return true;
    }

    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed() {
        HideSoft(coordinatorLayout);
        overridePendingTransitionExit();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    public void HideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
