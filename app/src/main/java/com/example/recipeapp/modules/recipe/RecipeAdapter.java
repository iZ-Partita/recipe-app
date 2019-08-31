package com.example.recipeapp.modules.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.R;
import com.example.recipeapp.Utils.GlideApp;
import com.example.recipeapp.db.Recipe;
import com.example.recipeapp.pojo.RecipeObj;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<RecipeObj> mRecipeList;
    private Context mContext;
    private static RecipeObj mRecipe;

    public RecipeAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<RecipeObj> recipeObjList) {
        mRecipeList = recipeObjList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        mRecipe = mRecipeList.get(position);

        final RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
        recipeViewHolder.tvRecipeName.setText(mRecipe.getRecipeName());
        recipeViewHolder.tvRecipeType.setText(mRecipe.getRecipeType());

        GlideApp.with(mContext).asBitmap().load(mRecipe.getRecipeImage()).into(recipeViewHolder.ivRecipe);
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llRecipe)
        LinearLayout llRecipe;

        @BindView(R.id.ivRecipe)
        ImageView ivRecipe;

        @BindView(R.id.tvRecipeName)
        MaterialTextView tvRecipeName;

        @BindView(R.id.tvRecipeType)
        MaterialTextView tvRecipeType;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                NavDirections navDirections = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(mRecipe);
                Navigation.findNavController(view).navigate(navDirections);
            });
        }
    }
}
