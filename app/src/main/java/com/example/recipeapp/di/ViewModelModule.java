package com.example.recipeapp.di;

import androidx.lifecycle.ViewModel;

import com.example.recipeapp.modules.addrecipe.AddRecipeViewModel;
import com.example.recipeapp.modules.recipe.RecipeViewModel;
import com.example.recipeapp.modules.recipedetail.RecipeDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddRecipeViewModel.class)
    abstract ViewModel bindAddRecipeViewModel(AddRecipeViewModel viewModel);
}
