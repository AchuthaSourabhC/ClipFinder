package com.hackathon.clipfinder;

import com.hackathon.clipfinder.models.ClipSearchRequest;
import com.hackathon.clipfinder.models.SceneMetadata;
import com.hackathon.clipfinder.models.SceneMetadataList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface APIInterface {


    @POST("/findScene")
    Call<List<SceneMetadata>> searchClip(@Body ClipSearchRequest clipSearchRequest);
}
