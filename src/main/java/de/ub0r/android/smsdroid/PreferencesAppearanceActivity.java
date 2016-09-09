/*
 * Copyright (C) 2010-2012 Felix Bechstein
 * 
 * This file is part of SMSdroid.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */
package de.ub0r.android.smsdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import de.ub0r.android.lib.IPreferenceContainer;

/**
 * Preferences.
 * 
 * @author flx
 */
public final class PreferencesAppearanceActivity extends PreferenceActivity implements
		IPreferenceContainer {
	/**
	 * {@inheritDoc}
	 */

    public static android.support.v7.widget.Toolbar mToolbar;
    SharedPreferences Sp;

    @Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_appearance);

        String skin = ConversationListActivity.Sp.getString("skin_color", "#5677fc");


        setTheme(R.style.Mizuu_Theme_Preference);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Resources.Theme pp = getTheme();

        /*int[] textSizeAttr = new int[] { android.R.attr. .textSize };
        int indexOfAttrTextSize = 0;
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int textSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();*/


        //getActivity().setActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PreferencesActivity.registerOnPreferenceClickListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in Action Bar clicked; go home
			Intent intent = new Intent(this, ConversationListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Activity getActivity() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Context getContext() {
		return this;
	}
}
