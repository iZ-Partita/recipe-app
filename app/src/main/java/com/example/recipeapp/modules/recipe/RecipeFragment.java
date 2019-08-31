package com.example.recipeapp.modules.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.R;
import com.example.recipeapp.Utils.TinyDB;
import com.example.recipeapp.Utils.XMLPullParserHandler;
import com.example.recipeapp.base.BaseFragment;
import com.example.recipeapp.di.ViewModelFactory;
import com.example.recipeapp.pojo.RecipeObj;
import com.example.recipeapp.pojo.RecipeType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends BaseFragment<RecipeViewModel> {

    @BindView(R.id.rvRecipe)
    RecyclerView rvRecipe;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spRecipeType)
    AppCompatSpinner spRecipeType;

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    @BindView(R.id.tvNoRecords)
    MaterialTextView tvNoRecords;

    @Inject
    ViewModelFactory mViewModelFactory;

    private List<String> recipeTypeStringList = new ArrayList<>();

    private RecipeAdapter mAdapter;
    private TinyDB tinyDB;

    private List<RecipeType> recipeTypeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RecipeViewModel.class);
        tinyDB = new TinyDB(requireContext());

        XMLPullParserHandler parser = new XMLPullParserHandler();
        InputStream inputStream = getResources().openRawResource(R.raw.recipetypes);
        recipeTypeList = parser.parse(inputStream);

        for (RecipeType recipeType : recipeTypeList) {
            recipeTypeStringList.add(recipeType.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        mBinder = ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.title_recipe);
        setUpToolbar(toolbar, false);

        mAdapter = new RecipeAdapter(requireContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (requireContext(), android.R.layout.simple_spinner_dropdown_item, recipeTypeStringList);
        spRecipeType.setAdapter(adapter);

        rvRecipe.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRecipe.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rvRecipe.setAdapter(mAdapter);

        spRecipeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String recipeType = spRecipeType.getSelectedItem().toString();

                ArrayList<Object> recipeObjArrayList = tinyDB.getListObject("recipeList", RecipeObj.class);

                List<RecipeObj> recipeObjList = new ArrayList<>();

                if (recipeType.equalsIgnoreCase("All")) {

                    if (recipeObjArrayList != null && recipeObjArrayList.size() > 0) {
                        for (Object obj : recipeObjArrayList) {
                            RecipeObj recipe = (RecipeObj) obj;
                            recipeObjList.add(recipe);
                        }

                        mAdapter.setData(recipeObjList);
                        hideNoRecord();
                    } else {
                        showNoRecord();
                    }
                } else {
                    if (recipeObjArrayList != null && recipeObjArrayList.size() > 0) {
                        for (Object obj : recipeObjArrayList) {
                            RecipeObj recipe = (RecipeObj) obj;
                            if (recipe.getRecipeType().equalsIgnoreCase(recipeType)) {
                                recipeObjList.add(recipe);
                            }
                        }

                        if (recipeObjList != null && recipeObjList.size() > 0) {
                            mAdapter.setData(recipeObjList);
                            hideNoRecord();
                        } else {
                            showNoRecord();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fabAdd.setOnClickListener(view -> {
            NavDirections navDirections = RecipeFragmentDirections.actionRecipeFragmentToAddRecipeFragment(null);
            Navigation.findNavController(view).navigate(navDirections);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showNoRecord() {
        tvNoRecords.setVisibility(View.VISIBLE);
        rvRecipe.setVisibility(View.GONE);
    }

    private void hideNoRecord() {
        tvNoRecords.setVisibility(View.GONE);
        rvRecipe.setVisibility(View.VISIBLE);
    }
}
