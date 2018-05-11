/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.hackathon.clipfinder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hackathon.clipfinder.Service.ClipService;
import com.hackathon.clipfinder.models.ClipPlaylist;
import com.hackathon.clipfinder.models.ClipSearchRequest;
import com.hackathon.clipfinder.models.SceneMetadata;
import com.hackathon.clipfinder.models.SceneMetadataList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {


  private PlayerView playerView;
  private SimpleExoPlayer player;
  private boolean playWhenReady;
  private int currentWindow = 0;
  private long playbackPosition = 0;
  ClipService clipService = new ClipService();
  APIInterface apiInterface;
  List<SceneMetadata> sceneMetadataList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    playerView = findViewById(R.id.video_view);


  }

  private void initializePlayer() {
    player = ExoPlayerFactory.newSimpleInstance(
            new DefaultRenderersFactory(this),
            new DefaultTrackSelector(), new DefaultLoadControl());

    playerView.setPlayer(player);

    player.setPlayWhenReady(playWhenReady);
    player.seekTo(currentWindow, playbackPosition);

    MediaSource mediaSource = buildMediaSource();
    player.prepare(mediaSource, true, false);
  }

  private MediaSource buildMediaSource() {
      List<ExtractorMediaSource> playlistSource = new ArrayList<>();


    // these are reused for both media sources we create below
    DefaultExtractorsFactory extractorsFactory =
            new DefaultExtractorsFactory();
    DefaultHttpDataSourceFactory dataSourceFactory =
            new DefaultHttpDataSourceFactory( "user-agent");

      for(SceneMetadata sceneMetadata : sceneMetadataList){
          Uri sceneUri = Uri.parse(sceneMetadata.getUrl());
          ExtractorMediaSource videoSource =
                  new ExtractorMediaSource.Factory(
                          new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                          createMediaSource(sceneUri);
          playlistSource.add(videoSource);
      }
    ExtractorMediaSource [] sourceArray =  playlistSource.toArray(new ExtractorMediaSource[playlistSource.size()]);
    return new ConcatenatingMediaSource( sourceArray);
  }

  @Override
  public void onStart() {
    super.onStart();
      Bundle bundle =  getIntent().getExtras();
      ClipPlaylist clipPlaylist = (ClipPlaylist) bundle.getSerializable("playlist");
      sceneMetadataList = clipPlaylist.getSceneMetadataList();
      if (Util.SDK_INT > 23) {
          initializePlayer();
      }
  }

  @Override
  public void onResume() {
    super.onResume();
      Bundle bundle =  getIntent().getExtras();
      ClipPlaylist clipPlaylist = (ClipPlaylist) bundle.getSerializable("playlist");
      sceneMetadataList = clipPlaylist.getSceneMetadataList();
      if (Util.SDK_INT > 23) {
          initializePlayer();
      }


  }

  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      playWhenReady = player.getPlayWhenReady();
      player.release();
      player = null;
    }
  }

}
