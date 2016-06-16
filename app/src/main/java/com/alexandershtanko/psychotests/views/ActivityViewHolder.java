package com.alexandershtanko.psychotests.views;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.viewmodels.ActivityViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aleksandr on 12.06.16.
 */
public class ActivityViewHolder extends AbstractViewHolder {
    private static final int DRAWER_ALL_TESTS = 0;
    private static final int DRAWER_CATEGORIES = 1;
    private static final int DRAWER_TESTS_DONE = 2;
    private static final int DRAWER_SETTINGS = 3;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer)
    ListView drawerList;
    private ActivityFragments fragments = ActivityFragments.getInstance();


    public ActivityViewHolder(AppCompatActivity activity, int layoutRes) {
        super(activity, layoutRes);
        fragments.init(activity.getSupportFragmentManager(), R.id.content_frame);
        initDrawer();
    }

    private void initDrawer() {
        drawerList.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.item_drawer, new Integer[]{R.string.all_tests, R.string.categories, R.string.tests_done, R.string.settings}));
        drawerList.setOnItemClickListener(((parent, view, position, id) -> selectItem(position)));
        drawerList.setSelection(0);
    }

    private void selectItem(int position) {
        switch (position) {
            case DRAWER_ALL_TESTS:
                fragments.openTests();
                break;
            case DRAWER_CATEGORIES:
                fragments.openCategories();
                break;
            case DRAWER_TESTS_DONE:
                fragments.openTestsDone();
                break;
            case DRAWER_SETTINGS:
                fragments.openSettings();
                break;
            default:
                return;
        }

        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }


    public static class ViewBinder extends AbstractViewBinder<ActivityViewHolder, ActivityViewModel> {

        public ViewBinder(ActivityViewHolder viewHolder, ActivityViewModel viewModel) {
            super(viewHolder, viewModel);
        }

        @Override
        protected void onBind(CompositeSubscription s) {
            s.add(Observable.create((Observable.OnSubscribe<Integer>) subscriber ->
                    viewHolder.drawerList.setOnItemClickListener((parent, view, position, id) ->
                            subscriber.onNext(position)))
                    .subscribe(viewHolder::selectItem));
        }

    }
}
