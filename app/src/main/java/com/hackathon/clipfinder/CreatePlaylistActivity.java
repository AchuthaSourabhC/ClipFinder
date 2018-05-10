package com.hackathon.clipfinder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hackathon.clipfinder.models.ClipPlaylist;
import com.hackathon.clipfinder.models.ClipSearchRequest;
import com.hackathon.clipfinder.models.SceneMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class CreatePlaylistActivity extends AppCompatActivity  {


    // UI references.
    private AutoCompleteTextView categoryVIew;
    private AutoCompleteTextView clipTagsView;
    private View mProgressView;
    private View mSavePlaylistForm;
    private Button saveButton;

    APIInterface apiInterface;
    List<SceneMetadata> sceneMetadataList;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    int[] covers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_playlist);

        covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};
        // Set up the login form.
        categoryVIew = (AutoCompleteTextView) findViewById(R.id.atv_category);
        clipTagsView = (AutoCompleteTextView) findViewById(R.id.atv_clip_tags);

        List<String> categories = Arrays.asList("ACTION", "ROMANCE","COMEDY", "THRILLER");
        List<String> tags = Arrays.asList("SUPERHEROES", "Baahubali", "Baazigar", "Split");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(CreatePlaylistActivity.this,
                        android.R.layout.simple_dropdown_item_1line, categories);
        ArrayAdapter<String> tagsAdapter =
                new ArrayAdapter<>(CreatePlaylistActivity.this,
                        android.R.layout.simple_dropdown_item_1line, tags);

        categoryVIew.setAdapter(adapter);
        clipTagsView.setAdapter(tagsAdapter);

        mProgressView = findViewById(R.id.save_progress);
        mSavePlaylistForm = findViewById(R.id.create_playlist_form);
        saveButton = findViewById(R.id.bt_create_playlist);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        sharedPref = getApplication().getSharedPreferences("Playlists", Context.MODE_PRIVATE);

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = categoryVIew.getText().toString();
                String tag = clipTagsView.getText().toString();
                showProgress(true);
                new CreatePlayListTask(category, tag).execute();
            }
        });

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSavePlaylistForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mSavePlaylistForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSavePlaylistForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSavePlaylistForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CreatePlayListTask extends AsyncTask<Void, Void, Boolean> {

        private final String category;
        private final String tag;

        CreatePlayListTask(String category, String tag) {
            this.category = category;
            this.tag = tag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Log.i("Task", "Category: "+ category + " Taq:" + tag );
            ClipSearchRequest clipSearchRequest = new ClipSearchRequest();
            clipSearchRequest.setCategory(category);
            clipSearchRequest.setMovieName(tag);
            Call<List<SceneMetadata>> sceneMetadataCall = apiInterface.searchClip(clipSearchRequest);
            try {
                int cover = R.drawable.action;
                if(category.equals("COMEDY")){
                     cover = R.drawable.comedy;
                }else if(category.equals("ACTION")){
                     cover = R.drawable.action;
                }else if(category.equals("ROMANCE")){
                     cover = R.drawable.romance;
                }else if(category.equals("THRILLER")){
                     cover = R.drawable.thriller;
                }

                List<SceneMetadata> sceneMetadataList = sceneMetadataCall.execute().body();
                ClipPlaylist clipPlaylist = new ClipPlaylist(tag, category,
                        sceneMetadataList.size(), cover, sceneMetadataList);

                String response = gson.toJson(clipPlaylist);
                Log.i("Scene", "Scenes: "+ response);

                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString(category+":"+tag, response);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                //finish();
                Intent intent = new Intent(CreatePlaylistActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }
}

