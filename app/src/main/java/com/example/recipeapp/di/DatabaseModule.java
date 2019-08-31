package com.example.recipeapp.di;

import androidx.room.Room;

import com.example.recipeapp.RecipeApplication;
import com.example.recipeapp.db.RecipeDao;
import com.example.recipeapp.db.RecipeDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class DatabaseModule {

    @Singleton
    @Provides
    static RecipeDatabase provideRecipeApplication(RecipeApplication recipeApplication) {
//        final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//            @Override
//            public void migrate(@NonNull SupportSQLiteDatabase database) {
//                database.execSQL("ALTER TABLE product ADD COLUMN operation TEXT");
////                database.execSQL("ALTER TABLE product ADD COLUMN last_update TEXT");
//            }
//        };
//
//        final Migration MIGRATION_2_3 = new Migration(2, 3) {
//
//            @Override
//            public void migrate(@NonNull SupportSQLiteDatabase database) {
//                database.execSQL("ALTER TABLE product ADD COLUMN last_update INTEGER");
//            }
//        };

        return Room.databaseBuilder(recipeApplication,
                RecipeDatabase.class, "recipe.db")
                .fallbackToDestructiveMigration()
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
    }

    @Singleton
    @Provides
    static RecipeDao provideShowDao(RecipeDatabase recipeDatabase) {
        return recipeDatabase.recipeDao();
    }
}
