package com.mynimef.swiracle.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mynimef.swiracle.logic.ClothesInfo;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "clothes_element_table")
public class ClothesElement {
    @PrimaryKey(autoGenerate = true)
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    private String postId;
    @NotNull
    public String getPostId() { return postId; }
    public void setPostId(@NotNull String postId) { this.postId = postId; }

    @Embedded
    private ClothesInfo info;

    private String url;

    public ClothesElement(@NotNull String postId, ClothesInfo info, String url) {
        this.postId = postId;
        this.info = info;
        this.url = url;
    }

    public ClothesInfo getInfo() { return info; }
    public String getUrl() { return url; }

    public void setInfo(ClothesInfo info) { this.info = info; }
    public void setUrl(String url) { this.url = url; }
}