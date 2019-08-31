package com.example.recipeapp.modules.recipe;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.base.BaseViewModel;
import com.example.recipeapp.data.RecipeRepository;
import com.example.recipeapp.db.Recipe;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecipeViewModel extends BaseViewModel {

    private final RecipeRepository mRepository;

    private final MutableLiveData<List<Recipe>> mRecipeListLiveData = new MutableLiveData<>();

    @Inject
    RecipeViewModel(RecipeRepository recipeRepository) {
        mRepository = recipeRepository;
        onGetRecipeList();
    }

    public List<Recipe> getRecipe() {
        return mRecipeListLiveData.getValue();
    }

    void onGetRecipeList() {
        mCompositeDisposable.addAll(
                mRepository.getRecipe()
                        .doOnSubscribe(__ -> mIsLoadingLiveData.setValue(true))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recipeList -> {
                            mIsLoadingLiveData.setValue(false);
                            mRecipeListLiveData.setValue(recipeList);
                        }, throwable -> {
                            mIsLoadingLiveData.setValue(false);
                        })

        );
    }

    void onGetRecipeCount() {
        mCompositeDisposable.addAll(
                mRepository.getRecipeCount()
                .doOnSubscribe(__ ->mIsLoadingLiveData.setValue(true))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Log.d("count", "onGetRecipeCount: " + integer);
                }, throwable -> {
                    mIsLoadingLiveData.setValue(false);
                    Log.d("asd", "onGetRecipeCount: " + throwable.getLocalizedMessage());
                })
        );
    }
}