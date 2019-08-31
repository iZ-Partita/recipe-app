package com.example.recipeapp.modules.recipedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.GlideApp;
import com.example.recipeapp.Utils.TinyDB;
import com.example.recipeapp.base.BaseFragment;
import com.example.recipeapp.di.ViewModelFactory;
import com.example.recipeapp.modules.addrecipe.AddRecipeFragmentDirections;
import com.example.recipeapp.pojo.RecipeObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends BaseFragment<RecipeDetailViewModel> {

    @BindView(R.id.ivRecipe)
    ImageView ivRecipe;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvRecipeType)
    MaterialTextView tvRecipeType;

    @BindView(R.id.tvRecipeName)
    MaterialTextView tvRecipeName;

    @BindView(R.id.tvIngredients)
    MaterialTextView tvIngredients;

    @BindView(R.id.tvSteps)
    MaterialTextView tvSteps;

    @BindView(R.id.fabMenu)
    FloatingActionButton fabMenu;

    @Inject
    ViewModelFactory mViewModelFactory;

    private RecipeObj mRecipe;
    private TinyDB tinyDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mBinder = ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.title_recipe_detail);
        setUpToolbar(toolbar, true);

        tinyDB = new TinyDB(requireContext());

        mRecipe = RecipeDetailFragmentArgs.fromBundle(requireArguments()).getSelectedRecipe();

        if (mRecipe != null) {
            tvRecipeType.setText(mRecipe.getRecipeType());
            tvRecipeName.setText(mRecipe.getRecipeName());
            tvIngredients.setText(mRecipe.getIngredients());
            tvSteps.setText(mRecipe.getSteps());
            GlideApp.with(requireContext()).asBitmap().load(mRecipe.getRecipeImage()).into(ivRecipe);
        }

        fabMenu.setOnClickListener(view -> {
            showDetailOption();
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RecipeDetailViewModel.class);
    }

    private void showDetailOption() {
        new MaterialDialog.Builder(requireContext())
                .title(R.string.action_sheet_title)
                .items(R.array.detail_action)
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            NavDirections navDirections = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddRecipeFragment(mRecipe);
                            Navigation.findNavController(requireView()).navigate(navDirections);
                            break;
                        case 1:
                            showConfirmationDialog(
                                    getString(R.string.app_name),
                                    getString(R.string.confirm_delete, mRecipe.getRecipeName()),
                                    getString(R.string.ok),
                                    (dialogInterface, i) -> delete(),
                                    getString(R.string.cancel),
                                    (dialogInterface, i) -> dialogInterface.dismiss());
                            break;
                    }
                })
                .positiveText(R.string.cancel)
                .show();
    }

    private void delete() {

        ArrayList<Object> recipeObjArrayList = tinyDB.getListObject("recipeList", RecipeObj.class);

        ArrayList<Object> temp = recipeObjArrayList;

        if (temp != null && temp.size() > 0) {
            for (Object object : temp) {
                RecipeObj r = (RecipeObj) object;
                if (r.getId().equalsIgnoreCase(mRecipe.getId())) {
                    recipeObjArrayList.remove(r);
                }
            }
        }

        tinyDB.putListObject("recipeList", recipeObjArrayList);
        NavDirections navDirections = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeFragment();
        Navigation.findNavController(requireView()).navigate(navDirections);
        showToast(getString(R.string.desc_success_delete_recipe));
    }
}
