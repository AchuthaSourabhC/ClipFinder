package com.hackathon.clipfinder.models;

import java.util.ArrayList;
import java.util.List;

public class SceneMetadataList {


    List<SceneMetadata> sceneMetadataList = new ArrayList<>();

    public List<SceneMetadata> getSceneMetadataList() {
        return sceneMetadataList;
    }

    public void setSceneMetadataList(List<SceneMetadata> sceneMetadataList) {
        this.sceneMetadataList = sceneMetadataList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SceneMetadataList{");
        sb.append("sceneMetadataList=").append(sceneMetadataList);
        sb.append('}');
        return sb.toString();
    }
}
