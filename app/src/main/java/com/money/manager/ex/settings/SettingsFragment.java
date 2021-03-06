/*
 * Copyright (C) 2012-2016 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.money.manager.ex.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import com.money.manager.ex.Constants;
import com.money.manager.ex.DonateActivity;
import com.money.manager.ex.R;
import com.money.manager.ex.about.AboutActivity;

import timber.log.Timber;

/**
 * Root settings fragment.
 */
public class SettingsFragment
    extends PreferenceFragmentCompat {

    public static final int REQUEST_GENERAL_PREFERENCES = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        initGeneralSettings();

        // Per-Database settings
        Preference perDbPreference = findPreference(getString(R.string.pref_per_database));
        if (perDbPreference != null) {
            perDbPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    intent.putExtra(SettingsActivity.EXTRA_FRAGMENT, PerDatabaseFragment.class.getSimpleName());
                    startActivity(intent);
                    return true;
                }
            });
        }

        final Preference lookAndFeelPreference = findPreference(getString(PreferenceConstants.PREF_LOOK_FEEL));
        if (lookAndFeelPreference != null) {
            lookAndFeelPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), LookFeelSettingsActivity.class));
                    return true;
                }
            });
        }

        final Preference behaviourPreference = findPreference(getString(R.string.pref_behaviour));
        if (behaviourPreference != null) {
            behaviourPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), BehaviourSettingsActivity.class));
                    return true;
                }
            });
        }

        final Preference investmentPreference = findPreference(getString(R.string.pref_investment));
        if (investmentPreference != null) {
            investmentPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), InvestmentSettingsActivity.class));
                    return true;
                }
            });
        }

        final Preference passcodePreference = findPreference(getString(PreferenceConstants.PREF_SECURITY));
        if (passcodePreference != null) {
            passcodePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), SecuritySettingsActivity.class));
                    return true;
                }
            });
        }

        final Preference databasesPreference = findPreference(getString(PreferenceConstants.PREF_DATABASE));
        if (databasesPreference != null) {
            databasesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), DatabaseSettingsActivity.class));
                    return true;
                }
            });
        }

        // Synchronisation
        final Preference dropboxPreference = findPreference(getString(R.string.pref_dropbox_how_it_works));
        if (dropboxPreference != null) {
            dropboxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), SyncPreferencesActivity.class));
                    return true;
                }
            });
        }

        //donate
        final Preference pDonate = findPreference(getString(PreferenceConstants.PREF_DONATE));
        if (pDonate != null) {
            pDonate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), DonateActivity.class));
                    return false;
                }
            });
        }

        final Preference infoPreference = findPreference(getString(PreferenceConstants.PREF_VERSION_NAME));
        if (infoPreference != null) {
            infoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    return true;
                }
            });
        }

        // manage intent
        if (getActivity().getIntent() != null) {
            if (!TextUtils.isEmpty(getActivity().getIntent()
                    .getStringExtra(Constants.INTENT_REQUEST_PREFERENCES_SCREEN))) {
                try {
                    PreferenceScreen screen = getPreferenceScreen();
                    Preference preference = findPreference(getActivity().getIntent()
                            .getStringExtra(Constants.INTENT_REQUEST_PREFERENCES_SCREEN));
                    if (preference != null) {
                        //screen.onItemClick(null, null, preference.getOrder(), 0);
                        screen.performClick();
                    }
                } catch (Exception e) {
                    Timber.e(e, "opening preferences screen");
                }
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Timber.d("creating preferences");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GENERAL_PREFERENCES:
                // always recreate activity when returning from general settings, instead of
                // trying to figure out if something has changed.
                getActivity().recreate();
                break;
        }
    }

    private void initGeneralSettings() {
        // General Settings

        final Preference generalPreference = findPreference(getString(PreferenceConstants.PREF_GENERAL));
        if (generalPreference != null) {
            generalPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    GeneralSettingsFragment fragment = new GeneralSettingsFragment();
//                    ((SettingsActivity) getActivity()).setSettingFragment(fragment);

                    Intent intent = new Intent(getActivity(), GeneralSettingsActivity.class);
//                    startActivity(intent);
                    startActivityForResult(intent, REQUEST_GENERAL_PREFERENCES);
                    return true;
                }
            });
        }
    }
}
