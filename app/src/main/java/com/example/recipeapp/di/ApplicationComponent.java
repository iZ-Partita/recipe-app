package com.example.recipeapp.di;

import com.example.recipeapp.RecipeApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        DatabaseModule.class,
        RecipeServiceModule.class,
        FragmentBindingModule.class,
        ViewModelModule.class
})
public interface ApplicationComponent extends AndroidInjector<RecipeApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeApplication> {
    }

}
