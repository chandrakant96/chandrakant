package com.whr.user.first_aid.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.whr.user.com.WHR.Utils.CustomJSONObjectRequest;
import com.whr.user.com.WHR.Utils.CustomVolleyRequestQueue;
import com.whr.user.R;
import com.whr.user.activities.NoInternetConnectionActivity;
import com.whr.user.com.WHR.Utils.PreferenceUtils;
import com.whr.user.com.WHR.Utils.ConnectionDector;
import com.whr.user.com.WHR.Utils.VolleyErrorHelper;
import com.whr.user.first_aid.adapters.FirstAidDetailCommentAdapter;
import com.whr.user.first_aid.model.FirstAidDetailPojo;
import com.whr.user.pojo.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FirstDetailActivity extends AppCompatActivity {
    private TextView title;
    String id, type;
    PreferenceUtils pref;
    Context context;
    String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;
    private AppCompatActivity activity;
    CoordinatorLayout coordinatorLayout;
    private String url = GlobalVar.ServerAddress +"AndroidNew/FirstAidDetails";
    public static final String DEVELOPER_KEY = "AIzaSyDkNZ3XH9BPlhxxqWT3TX99a9S5oUPOYCY";
    private Uri uri;
    int pau = 0;
    private ProgressDialog progressBar;
    private String symptoms, precautions, videopath, audiopath, doctorname, specialization, experience, education, uploadedon, likes, comments;
    private TextView txtSymptoms, txtPrecautions, txtdoctorName, txtSpecilization, txtExperience, txtQualification, txtUploadDate, txtLikes, txtComments;
    private TextView txtAddcomment, txtPostComment;
    private EditText etxComment;
    private RelativeLayout relativeLayoutOverYouTubeThumbnailView, layoutComment;
    private TextView layout_downloas;
    private YouTubeThumbnailView youTubeThumbnailView;
    private ImageView playButton;
    private ImageButton stop_audio;
    private ImageView play_audio;
    private RecyclerView recyclerView;
    private FirstAidDetailCommentAdapter adapter;
    private List<FirstAidDetailPojo> list;
    private LinearLayoutManager lLayout;


    private MediaPlayer audioPlayer = null;
    private Uri audioFileUri = null;
    public static final String TAG_PLAY_AUDIO = "PLAY_AUDIO";
    private boolean audioIsPlaying = false;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private Runnable mRunnable;
    private TextView txtDuration, txtTotalDuration;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private ImageView imgBack;

    ConnectionDector dector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_detail);

        context = FirstDetailActivity.this;
        activity = FirstDetailActivity.this;
        pref = new PreferenceUtils(context);
        mQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();
        dector=new ConnectionDector(context);

        progressBar = new ProgressDialog(context);
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(false);

        imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtSymptoms = findViewById(R.id.txtSymptoms);
        txtPrecautions = findViewById(R.id.txtPrecautions);
        txtdoctorName = findViewById(R.id.txtdoctorName);
        txtSpecilization = findViewById(R.id.txtSpecilization);
        txtExperience = findViewById(R.id.txtExperience);
        txtQualification = findViewById(R.id.txtQualification);
        txtUploadDate = findViewById(R.id.txtUploadDate);
        txtLikes = findViewById(R.id.txtLikes);
        txtComments = findViewById(R.id.txtComments);
        play_audio = findViewById(R.id.play_audio);
        stop_audio = findViewById(R.id.stop_audio);
        txtAddcomment = findViewById(R.id.txtaddComment);
        txtPostComment = findViewById(R.id.txtPostComment);
        etxComment = findViewById(R.id.etxComment);
        layoutComment = findViewById(R.id.layoutComment);
        layout_downloas = findViewById(R.id.layout_downloas);

        relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) findViewById(R.id.relativeLayout_over_youtube_thumbnail);
        playButton = (ImageView) findViewById(R.id.btnYoutube_player);
        relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) findViewById(R.id.relativeLayout_over_youtube_thumbnail);
        youTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtube_thumbnail);


        list = new ArrayList<>();
        recyclerView = findViewById(R.id.commentRecycleview);
        adapter = new FirstAidDetailCommentAdapter(context, list, activity);
        lLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            type = bundle.getString("type");
        }
        title.setText("First Aid Tips For" + type);

        if (dector.isConnectingToInternet()) {
            firstDetail();
        } else {
            startActivityForResult(new Intent(context,NoInternetConnectionActivity.class),GlobalVar.NO_INTERNET_REQUEST_CODE);
            overridePendingTransitionExit();
        }


        txtAddcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutComment.setVisibility(View.VISIBLE);
            }
        });

        txtPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxComment.getText().toString().isEmpty())
                {
                    showSnackBar("Enter Your Comment ");
                }
                postComment();
            }
        });


        txtDuration = findViewById(R.id.textDuratio);
        txtTotalDuration = findViewById(R.id.textTotalDuration);
        mSeekBar=findViewById(R.id.seekbar);

        mHandler = new Handler();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(FirstDetailActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(FirstDetailActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();



        layout_downloas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (audioPlayer != null && b) {
                    audioPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVar.NO_INTERNET_REQUEST_CODE) {
            firstDetail();
        }
    }
    private void firstDetail() {
        JSONObject obj = new JSONObject();
        try {

            obj.put("id", id);
            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "obj", obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonRootobj = new JSONObject(response.toString());
                    Log.e("response", response.toString());
                    JSONObject jsonObject = jsonRootobj.getJSONObject("FirstAid");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("firstaidinfo");
                    symptoms = jsonObject1.getString("symptoms");
                    precautions = jsonObject1.getString("precautions");
                    videopath = jsonObject1.getString("videopath");
                    audiopath = jsonObject1.getString("audiopath");
                    doctorname = jsonObject1.getString("doctorname");
                    specialization = jsonObject1.getString("specialization");
                    experience = jsonObject1.getString("experience");
                    education = jsonObject1.getString("education");
                    uploadedon = jsonObject1.getString("uploadedon");
                    likes = jsonObject1.getString("likes");
                    comments = jsonObject1.getString("comments");

                    txtSymptoms.setText(symptoms);
                    txtPrecautions.setText(precautions);
                    txtdoctorName.setText(doctorname);
                    txtSpecilization.setText(specialization);
                    txtExperience.setText(experience + "\t" + "Yrs Experience");
                    txtQualification.setText(education);
                    txtUploadDate.setText("Uploaded On : " + uploadedon);
                    txtLikes.setText(likes + "\t" + "likes");
                    txtComments.setText(comments + "\t" + "comments");

                    getComment();


                    final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        }

                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailView.setVisibility(View.VISIBLE);
                            relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                        }
                    };

                    youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(videopath);
                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        }
                    });

                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(context).equals(YouTubeInitializationResult.SUCCESS)) {
                                Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity, DEVELOPER_KEY, videopath);
                                activity.startActivity(intent);
                            } else {
                                Toast.makeText(context, "You tube is not installed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    play_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            play_audio.setVisibility(View.GONE);
                            stop_audio.setVisibility(View.VISIBLE);



                            if (!TextUtils.isEmpty(audiopath)) {
                                stopCurrentPlayAudio();
                                initAudioPlayer();
                                audioPlayer.start();
                                audioIsPlaying = true;

                                getAudioStats();
                                initializeSeekBar();


                            } else {
                                Toast.makeText(getApplicationContext(), "Please specify an audio file to play.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    stop_audio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (audioIsPlaying) {
                                audioPlayer.stop();
                                audioPlayer.release();
                                audioPlayer = null;
                                play_audio.setVisibility(View.VISIBLE);
                                stop_audio.setVisibility(View.GONE);
                                audioIsPlaying = false;

                                if (mHandler != null) {
                                    mHandler.removeCallbacks(mRunnable);
                                }
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red
                );
            }
        });
        customJSONObjectRequest.setTag("get");
        mQueue.add(customJSONObjectRequest);
    }

    private void postComment() {
        String url = GlobalVar.ServerAddress +"AndroidNew/AddFirstReview";
        GlobalVar.showProgressDialog(this, "Loading....", false);
        JSONObject params = new JSONObject();
        try {

            params.put("userid", pref.getUID());
            params.put("comment", etxComment.getText().toString().trim());
            params.put("firstaidtypeid", id);


            GlobalVar.errorLog(TAG, "url", url);
            GlobalVar.errorLog(TAG, "params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GlobalVar.hideProgressDialog();
                        GlobalVar.errorLog(TAG, "Review Submit :=>", response.toString());
                        try {
                            JSONObject jsonRootObject = new JSONObject(response.toString());
                            if (jsonRootObject.length() > 0) {
                                boolean result = jsonRootObject.getBoolean("result");
                                //boolean result = jsonRootObject.getBoolean("result");
                                Log.e("Comment_result", Boolean.toString(result));
                                if (result) {
                                    Log.e("Comment_result", Boolean.toString(result));
                                    GlobalVar.showSnackBar(coordinatorLayout, "Thank You For Your Comment", context, R.color.green);
                                    startActivity(new Intent(context,FirstAidActivity.class));
                                    layoutComment.setVisibility(View.GONE);


                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalVar.hideProgressDialog();
                GlobalVar.showSnackBar(coordinatorLayout, VolleyErrorHelper.getMessage(error, context), context, R.color.red);
            }
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void getComment() {
        GlobalVar.showProgressDialog(this, "Loading.....", true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalVar.errorLog(TAG, "url", url);
        GlobalVar.errorLog(TAG, "obj", obj.toString());

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST,
                url, obj,
                response -> {
                    GlobalVar.errorLog(TAG, "GetCommentList", response.toString());
                    GlobalVar.hideProgressDialog();
                    Gson gson = new Gson();
                    list.clear();
                    JSONArray jsonArray1 = null;
                    JSONObject jsonRootobj = null;
                    JSONObject jsonObject = null;
                    try {
                        jsonRootobj = new JSONObject(response.toString());
                        jsonObject = jsonRootobj.getJSONObject("FirstAid");
                        jsonArray1 = jsonObject.getJSONArray("comments");
                        if (jsonArray1 != null && jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject json = jsonArray1.getJSONObject(i);
                                FirstAidDetailPojo pojo = gson.fromJson(json.toString(), FirstAidDetailPojo.class);
                                list.add(pojo);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                           // showSnackBar(getString(R.string.NoDataAvailable));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            GlobalVar.hideProgressDialog();
            showSnackBar(VolleyErrorHelper.getMessage(error, context));
        });
        jsonRequest.setTag("get");
        mQueue.add(jsonRequest);
    }

    private void showSnackBar(String message) {
        GlobalVar.showSnackBar(coordinatorLayout, message, context, R.color.red);
    }

    private void initAudioPlayer() {
        try {
            if (audioPlayer == null) {
                audioPlayer = new MediaPlayer();



                Log.d(TAG_PLAY_AUDIO, audiopath);

                if (audiopath.toLowerCase().startsWith("http://")) {
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    audioPlayer.setDataSource(audiopath);
                } else {
                    if (audioFileUri != null) {
                        audioPlayer.setDataSource(getApplicationContext(), audioFileUri);
                    }
                }
                audioPlayer.prepare();
            }
        } catch (IOException ex) {
            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
        }
    }

    private void stopCurrentPlayAudio() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    protected void initializeSeekBar() {
        mSeekBar.setMax(audioPlayer.getDuration() / 1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (audioPlayer != null) {
                    int mCurrentPosition = audioPlayer.getCurrentPosition() / 1000; // In milliseconds
                    mSeekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

    protected void getAudioStats() {
        int duration = audioPlayer.getDuration() / 1000;
        int due = (audioPlayer.getDuration() - audioPlayer.getCurrentPosition()) / 1000;
        int pass = duration - due;

        txtDuration.setText("" + pass + " Sec");
        txtTotalDuration.setText("" + duration + " Sec");

    }

    private void startDownload() {
        new DownloadAudio().execute(audiopath);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadAudio extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/audio.mp3");


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
