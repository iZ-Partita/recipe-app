package com.example.recipeapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeObj implements Parcelable {

    private String id;

    private String recipeName;

    private String recipeType;

    private String recipeImage;

    private String ingredients;

    private String steps;

    public RecipeObj() {

    }

    protected RecipeObj(Parcel in) {
        id = in.readString();
        recipeName = in.readString();
        recipeType = in.readString();
        recipeImage = in.readString();
        ingredients = in.readString();
        steps = in.readString();
    }

    public static final Creator<RecipeObj> CREATOR = new Creator<RecipeObj>() {
        @Override
        public RecipeObj createFromParcel(Parcel in) {
            return new RecipeObj(in);
        }

        @Override
        public RecipeObj[] newArray(int size) {
            return new RecipeObj[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(recipeName);
        parcel.writeString(recipeType);
        parcel.writeString(recipeImage);
        parcel.writeString(ingredients);
        parcel.writeString(steps);
    }
}
