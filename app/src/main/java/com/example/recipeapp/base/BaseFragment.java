package com.example.recipeapp.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.example.recipeapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import butterknife.BindView;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment<T extends BaseViewModel> extends DaggerFragment {
    protected Unbinder mBinder;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected T mViewModel;

    @BindView(R.id.loading_container)
    @Nullable
    protected FrameLayout loadingContainer;

    @Override
    public void onDestroyView() {
        if (mBinder != null) {
            mBinder.unbind();
            mBinder = null;
        }
        super.onDestroyView();
    }

    protected void setUpToolbar(Toolbar toolbar, boolean displayHomeAsUpEnabled) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
            }
        }

        toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
    }

    protected void setUpToolbar(Toolbar toolbar, boolean displayHomeAsUpEnabled, String title) {
        setUpToolbar(toolbar, displayHomeAsUpEnabled);
        toolbar.setTitle(title);
    }

    protected void setUpToolbar(Toolbar toolbar, boolean displayHomeAsUpEnabled, @StringRes int resId) {
        setUpToolbar(toolbar, displayHomeAsUpEnabled);
        toolbar.setTitle(resId);
    }

    protected void showAlertDialog(@StringRes int resId) {
        showAlertDialog(getString(resId));
    }

    protected void showAlertDialog(String message) {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(message)
                .setTitle(R.string.app_name)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    protected void showConfirmationDialog(String title, String message, String positiveButton, DialogInterface.OnClickListener positiveOnClickListener, String negativeButton, DialogInterface.OnClickListener negativeOnClickListener) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());

        if (!TextUtils.isEmpty(title)) {
            alertDialog.setTitle(title);
        }

        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positiveButton, positiveOnClickListener);
        alertDialog.setNegativeButton(negativeButton, negativeOnClickListener);
        alertDialog.show();
    }

    protected void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}
