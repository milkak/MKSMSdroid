package de.ub0r.android.smsdroid;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import de.ub0r.android.logg0r.Log;


/**
 * Created by xisberto on 08/11/14.
 */
public class PrefsActivity extends PreferenceActivity {
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(" Preference", "Krok 1" );

        prepareLayout();

        buildLegacyPreferences();
    }

    private void prepareLayout() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_prefs, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        mToolBar.setTitle(getTitle());
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        //int skin = p.getInt("primary_color", 111111);

       // mToolBar.setBackgroundColor(skin);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void buildLegacyPreferences() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            String action = getIntent().getAction();
         /*   if (action == null) {
                addPreferencesFromResource(R.xml.prefs_appearance_behavior);
            } else if (action.equals(getString(R.string.category_general))) {
                mToolBar.setTitle(getString(R.string.header_general));
                addPreferencesFromResource(R.xml.prefs_about);
            } else if (action.equals(getString(R.string.category_advanced))) {
                mToolBar.setTitle(getString(R.string.header_general));
                addPreferencesFromResource(R.xml.prefs_debug);
            }*/
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return  HeaderPreferenceFragment.class.getName().equals(fragmentName);
    }
}
