package com.example.recipeapp.data;

import com.example.recipeapp.db.Recipe;
import com.example.recipeapp.db.RecipeDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

@Singleton
public class RecipeRepository {

    private final RecipeDao mRecipeDao;
    private final Scheduler mScheduler;

    @Inject
    RecipeRepository(RecipeDao recipeDao, @Named("network_scheduler") Scheduler scheduler) {
        mRecipeDao = recipeDao;
        mScheduler = scheduler;
    }

    public Completable saveRecipe(Recipe recipe) {
        return mRecipeDao.insert(recipe);
    }

    public Single<List<Recipe>> getRecipe() {
        return mRecipeDao.getAllRecipe()
                .subscribeOn(mScheduler);
    }

    public Flowable<Integer> getRecipeCount() {
        return mRecipeDao.getRecipeCount()
                .subscribeOn(mScheduler);
    }

    public Single<Integer> deleteRecipe(Recipe recipe) {
        return mRecipeDao.delete(recipe)
                .subscribeOn(mScheduler);
    }
}
