package com.example.recipeapp.di;

import android.content.Context;

import com.example.recipeapp.RecipeApplication;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract Context bindContext(RecipeApplication application);
}
