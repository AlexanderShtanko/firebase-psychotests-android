package com.alexandershtanko.psychotests.views;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.fragments.ActivityFragments;
import com.alexandershtanko.psychotests.utils.IntentUtils;
import com.alexandershtanko.psychotests.utils.StringUtils;
import com.alexandershtanko.psychotests.viewmodels.ActivityViewModel;
import com.alexandershtanko.psychotests.vvm.AbstractViewBinder;
import com.alexandershtanko.psychotests.vvm.AbstractViewHolder;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by aleksandr on 12.06.16.
 */
public class ActivityViewHolder extends AbstractViewHolder implements NavigationView.OnNavigationItemSelectedListener {
    private final AppCompatActivity activity;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActivityFragments fragments = ActivityFragments.getInstance();
    private ActionBarDrawerToggle toggle;
    private DrawerLayout.DrawerListener listener;
    private boolean flgBackTwice;


    public ActivityViewHolder(AppCompatActivity activity, int layoutRes) {
        super(activity, layoutRes);
        this.activity = activity;
        fragments.init(activity.getSupportFragmentManager(), R.id.content_frame);
        initDrawer(activity);
    }

    private void initDrawer(AppCompatActivity activity) {

        toggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle(R.string.app_name);
        fragments.openTests();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        onDrawerClose(() -> {
            switch (item.getItemId()) {
                case R.id.nav_tests:
                    fragments.openTests();
                    break;
                case R.id.nav_categories:
                    fragments.openCategories();
                    break;
                case R.id.nav_done:
                    fragments.openPassedTests();
                    break;

                case R.id.nav_share:
                    IntentUtils.shareTheApp(getContext());
                    break;

                case R.id.nav_rate:
                    IntentUtils.rateTheApp(getContext());
                    break;
            }
        });

        return true;
    }

    private void onDrawerClose(Runnable runnable) {


        listener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                runnable.run();
                if (listener != null)
                    drawerLayout.removeDrawerListener(listener);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };
        drawerLayout.addDrawerListener(listener);
    }

    public boolean onBackPressed() {
        Boolean res = true;
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            res = false;
        } else {


            if (activity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
                activity.getSupportFragmentManager().popBackStackImmediate();
                flgBackTwice = false;
            } else if (flgBackTwice)
                res = false;
            else {
                flgBackTwice = true;
                Toast.makeText(activity, R.string.text_exit_on_back_button, Toast.LENGTH_LONG).show();
            }
        }

        return res;

    }

    public void updateToolbar(int stringRes) {
        toolbar.setTitle(stringRes);
    }

    public void updateToolbar(String string) {
        toolbar.setTitle(StringUtils.capitalizeSentences(string));
    }


    public static class ViewBinder extends AbstractViewBinder<ActivityViewHolder, ActivityViewModel> {

        public ViewBinder(ActivityViewHolder viewHolder, ActivityViewModel viewModel) {
            super(viewHolder, viewModel);

        }

        @Override
        protected void onBind(CompositeSubscription s) {
        }

    }
}
