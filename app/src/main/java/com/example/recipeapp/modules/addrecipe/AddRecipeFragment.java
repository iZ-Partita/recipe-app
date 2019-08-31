package com.example.recipeapp.modules.addrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.GlideApp;
import com.example.recipeapp.Utils.TinyDB;
import com.example.recipeapp.Utils.XMLPullParserHandler;
import com.example.recipeapp.base.BaseFragment;
import com.example.recipeapp.di.ViewModelFactory;
import com.example.recipeapp.pojo.RecipeObj;
import com.example.recipeapp.pojo.RecipeType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddRecipeFragment extends BaseFragment<AddRecipeViewModel> {

    @BindView(R.id.ivRecipe)
    ImageView ivRecipe;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spRecipeType)
    AppCompatSpinner spRecipeType;

    @BindView(R.id.etRecipeName)
    TextInputEditText etRecipeName;

    @BindView(R.id.etIngredients)
    TextInputEditText etIngredients;

    @BindView(R.id.etSteps)
    TextInputEditText etSteps;

    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;

    @Inject
    ViewModelFactory mViewModelFactory;

    private List<String> recipeTypeStringList = new ArrayList<>();
    private TinyDB tinyDB;
    private RecipeObj selectedRecipe;
    private Image mImage;

    private ArrayAdapter<String> mSpinnerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddRecipeViewModel.class);
        tinyDB = new TinyDB(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mBinder = ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.title_recipe);
        setUpToolbar(toolbar, true);

        selectedRecipe = AddRecipeFragmentArgs.fromBundle(requireArguments()).getSelectedRecipe();

        List<RecipeType> recipeTypeList = null;

        XMLPullParserHandler parser = new XMLPullParserHandler();
        InputStream inputStream = getResources().openRawResource(R.raw.recipetypes);
        recipeTypeList = parser.parse(inputStream);

        for (RecipeType recipeType : recipeTypeList) {
            if(!recipeType.getName().equalsIgnoreCase("All")) {
                recipeTypeStringList.add(recipeType.getName());
            }
        }

        mSpinnerAdapter = new ArrayAdapter<>
                (requireContext(), android.R.layout.simple_spinner_dropdown_item, recipeTypeStringList);
        spRecipeType.setAdapter(mSpinnerAdapter);

        if (selectedRecipe != null) {
            updateUI();
        }

        return rootView;
    }

    private void updateUI() {
        GlideApp.with(requireContext()).asBitmap().load(selectedRecipe.getRecipeImage()).into(ivRecipe);
        etRecipeName.setText(selectedRecipe.getRecipeName());
        etIngredients.setText(selectedRecipe.getIngredients());
        etSteps.setText(selectedRecipe.getSteps());

        String compareValue = selectedRecipe.getRecipeType();

        if (compareValue != null) {
            int spinnerPosition = mSpinnerAdapter.getPosition(compareValue);
            spRecipeType.setSelection(spinnerPosition);
        }
    }

    private RecipeObj saveRecipe() {
        String imagePath = mImage.getPath();
        String recipeName = etRecipeName.getText().toString();
        String recipeType = spRecipeType.getSelectedItem().toString();
        String ingredients = etIngredients.getText().toString();
        String steps = etSteps.getText().toString();

        RecipeObj recipeObj = new RecipeObj();
        recipeObj.setId(UUID.randomUUID().toString());
        recipeObj.setRecipeImage(imagePath);
        recipeObj.setRecipeName(recipeName);
        recipeObj.setRecipeType(recipeType);
        recipeObj.setIngredients(ingredients);
        recipeObj.setSteps(steps);

        return recipeObj;
    }

    private RecipeObj editRecipe(RecipeObj recipeObj) {
        String imagePath = mImage != null? mImage.getPath() : recipeObj.getRecipeImage();
        String recipeName = etRecipeName.getText().toString();
        String recipeType = spRecipeType.getSelectedItem().toString();
        String ingredients = etIngredients.getText().toString();
        String steps = etSteps.getText().toString();

        recipeObj.setId(recipeObj.getId());
        recipeObj.setRecipeImage(imagePath);
        recipeObj.setRecipeName(recipeName);
        recipeObj.setRecipeType(recipeType);
        recipeObj.setIngredients(ingredients);
        recipeObj.setSteps(steps);

        return recipeObj;
    }

    private boolean isInputValid() {
        if (mImage == null && selectedRecipe == null) {
            showToast("Please select an image.");
            return false;
        } else if (TextUtils.isEmpty(etRecipeName.getText().toString())) {
            showToast("Please enter the Recipe Name.");
            return false;
        } else if (TextUtils.isEmpty(etIngredients.getText().toString())) {
            showToast("Please enter the Ingredients.");
            return false;
        } else if (TextUtils.isEmpty(etSteps.getText().toString())) {
            showToast("Please enter the Steps.");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ivRecipe.setOnClickListener(view -> {
            showImagePicker();
        });

        fabDone.setOnClickListener(view -> {
            if (isInputValid()) {

                if (selectedRecipe != null) {

                    showConfirmationDialog(getString(R.string.app_name),
                            getString(R.string.confirm_edit),
                            getString(R.string.ok),
                            (dialogInterface, i) -> {
                                RecipeObj recipeObj = editRecipe(selectedRecipe);

                                ArrayList<Object> recipeObjArrayList = tinyDB.getListObject("recipeList", RecipeObj.class);

                                ArrayList<Object> temp = recipeObjArrayList;

                                if (temp != null && temp.size() > 0) {
                                    for (Object object : temp) {
                                        RecipeObj r = (RecipeObj) object;
                                        if (r.getId().equalsIgnoreCase(selectedRecipe.getId())) {
                                            recipeObjArrayList.remove(r);
                                        }
                                    }
                                }

                                recipeObjArrayList.add(recipeObj);

                                tinyDB.putListObject("recipeList", recipeObjArrayList);
                                NavDirections navDirections = AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment();
                                Navigation.findNavController(requireView()).navigate(navDirections);
                                showToast(getString(R.string.desc_success_update_recipe));
                            },
                            getString(R.string.cancel),
                            (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }
                    );
                } else {

                    showConfirmationDialog(getString(R.string.app_name),
                            getString(R.string.confirm_add),
                            getString(R.string.ok),
                            (dialogInterface, i) -> {
                                RecipeObj recipeObj = saveRecipe();
                                ArrayList<Object> recipeObjArrayList = tinyDB.getListObject("recipeList", RecipeObj.class);

                                if (recipeObjArrayList != null && recipeObjArrayList.size() > 0) {
                                    recipeObjArrayList.add(recipeObj);
                                } else {
                                    recipeObjArrayList = new ArrayList<>();
                                    recipeObjArrayList.add(recipeObj);
                                }

                                tinyDB.putListObject("recipeList", recipeObjArrayList);
                                NavDirections navDirections = AddRecipeFragmentDirections.actionAddRecipeFragmentToRecipeFragment();
                                Navigation.findNavController(requireView()).navigate(navDirections);
                                showToast(getString(R.string.desc_success_save_recipe));
                            },
                            getString(R.string.cancel),
                            (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }
                    );
                }
            }
        });
    }

    private void showImagePicker() {
        ImagePicker.create(this).single().start();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            mImage = ImagePicker.getFirstImageOrNull(data);
            GlideApp.with(this).load(mImage.getPath()).into(ivRecipe);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
