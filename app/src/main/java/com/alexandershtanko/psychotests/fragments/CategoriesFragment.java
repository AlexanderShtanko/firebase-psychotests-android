package com.alexandershtanko.psychotests.fragments;

import android.support.v4.app.Fragment;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.activities.MainActivity;
import com.alexandershtanko.psychotests.helpers.AmplitudeHelper;
import com.alexandershtanko.psychotests.viewmodels.CategoriesViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.views.CategoriesViewHolder;
import com.alexandershtanko.psychotests.vvm.AbstractFragment;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesFragment extends AbstractFragment<CategoriesViewHolder,CategoriesViewModel> {

    @Override
    public CategoriesViewHolder createViewHolder() {
        return new CategoriesViewHolder(getContext(), R.layout.fragment_categories);
    }

    @Override
    public CategoriesViewModel createViewModel() {
        AmplitudeHelper.onOpenCategories();

        return new CategoriesViewModel();
    }

    @Override
    public AbstractViewBinder<CategoriesViewHolder, CategoriesViewModel> createViewBinder(CategoriesViewHolder viewHolder, CategoriesViewModel viewModel) {
        return new CategoriesViewHolder.ViewBinder(viewHolder,viewModel);
    }

    public static Fragment getInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getMainActivity()).updateToolbar(R.string.categories);

    }
}
