package com.mynimef.swiracle.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mynimef.swiracle.models.PostInfo;

import java.util.List;

@Dao
public interface PostDao {
    @Insert
    void insertPostInfo(PostInfo postInfo);

    @Query("DELETE FROM post_table")
    void deleteAllPosts();

    @Transaction
    @Query("SELECT * FROM post_table")
    LiveData<List<Post>> getAllPosts();
}