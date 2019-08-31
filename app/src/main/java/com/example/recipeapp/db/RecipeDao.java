package com.example.recipeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface RecipeDao {

    @Insert
    Completable insert(Recipe recipe);

    @Update
    Completable update(Recipe recipe);

    @Query("SELECT * FROM recipe")
    Single<List<Recipe>> getAllRecipe();

    @Query("SELECT count(*) FROM recipe")
    Flowable<Integer> getRecipeCount();

    @Delete
    Single<Integer> deleteAll(List<Recipe> recipeList);

    @Delete
    Single<Integer> delete(Recipe recipe);
}
