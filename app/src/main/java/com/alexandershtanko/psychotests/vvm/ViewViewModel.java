package com.alexandershtanko.psychotests.vvm;

/**
 * Created by aleksandr on 13.06.16.
 */
public interface ViewViewModel<H extends AbstractViewHolder,M extends AbstractViewModel> {
    H createViewHolder();
    M createViewModel();
    AbstractViewBinder<H,M> createViewBinder(H viewHolder, M viewModel);
}
