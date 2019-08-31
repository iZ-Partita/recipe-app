package com.example.recipeapp.di;

import com.example.recipeapp.modules.addrecipe.AddRecipeFragment;
import com.example.recipeapp.modules.recipe.RecipeFragment;
import com.example.recipeapp.modules.recipedetail.RecipeDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract RecipeFragment recipeFragment();

    @ContributesAndroidInjector
    abstract RecipeDetailFragment recipeDetailFragment();

    @ContributesAndroidInjector
    abstract AddRecipeFragment addRecipeFragment();
}
