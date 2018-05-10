package com.hackathon.clipfinder.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lincoln on 18/05/16.
 */
public class ClipPlaylist implements Serializable{
    private String name;
    private String category;
    private int numOfSongs;
    private List<SceneMetadata> sceneMetadataList;
    private int thumbnail;

    public ClipPlaylist() {
    }

    public ClipPlaylist(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public ClipPlaylist(String name, String category, int numOfSongs, int thumbnail, List<SceneMetadata> sceneMetadataList) {
        this.name = name;
        this.category = category;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
        this.sceneMetadataList = sceneMetadataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<SceneMetadata> getSceneMetadataList() {
        return sceneMetadataList;
    }

    public void setSceneMetadataList(List<SceneMetadata> sceneMetadataList) {
        this.sceneMetadataList = sceneMetadataList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClipPlaylist{");
        sb.append("name='").append(name).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", numOfSongs=").append(numOfSongs);
        sb.append(", sceneMetadataList=").append(sceneMetadataList);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append('}');
        return sb.toString();
    }
}
