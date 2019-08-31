package com.example.recipeapp.modules.addrecipe;

import androidx.lifecycle.MutableLiveData;

import com.example.recipeapp.base.BaseViewModel;
import com.example.recipeapp.data.RecipeRepository;
import com.example.recipeapp.db.Recipe;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddRecipeViewModel extends BaseViewModel {


    private final MutableLiveData<Boolean> mIsSaved = new MutableLiveData<>();
    private final RecipeRepository mRepository;

    @Inject
    AddRecipeViewModel(RecipeRepository recipeRepository) {
        mRepository = recipeRepository;
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return mIsSaved;
    }

    void onSaveRecipe(String imagePath, String recipeName, String recipeType, String ingredients, String steps) {
        Recipe recipe = new Recipe(recipeName, recipeType, imagePath, ingredients, steps);
        mCompositeDisposable.addAll(
                mRepository.saveRecipe(recipe)
                        .doOnSubscribe(__ -> mIsLoadingLiveData.setValue(true))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    mIsSaved.setValue(true);
                                    mIsLoadingLiveData.setValue(false);
                                },
                                mThrowableMutableLiveData::setValue)
        );
    }
}
