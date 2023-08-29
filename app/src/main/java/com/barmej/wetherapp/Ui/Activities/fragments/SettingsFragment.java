package com.barmej.wetherapp.Ui.Activities.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.barmej.wetherapp.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onStart() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onStart();
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // Show preferences defined in the XML file
        addPreferencesFromResource(R.xml.settings);
        //a loop is used to iterate through each preference item in a PreferenceScreen and set its summary using the setPreferenceSummary() function.
        // The PreferenceScreen likely contains multiple preference items (such as checkboxes, list items, etc.) that the user can interact with. Instead of manually setting the summary for each preference item one by one, a loop allows you to automate the process and apply the same operation to all preferences
        PreferenceScreen preferenceScreen=getPreferenceScreen();
        int count= preferenceScreen.getPreferenceCount();
        for(int i =0; i<count;i++){
            Preference preference=preferenceScreen.getPreference(i);
            setPreferenceSummary(preference);
        }
    }
    private void setPreferenceSummary(Preference preference){
        // Get the value associated with the preference key from the SharedPreferences file
        String value = preference.getSharedPreferences().getString(preference.getKey(), "");
        // If the value is empty that means it's not set yet so we should not update the summary
        if (TextUtils.isEmpty(value)) {
            return;
        }
        // if preference is ListPreference find the position of itemSelected on ArrayPosition
        //we use this position to acces to the title of this item in ArrayTitle
        //set this title as a summary
        if (preference instanceof androidx.preference.ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the value's string as a summary.
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference =findPreference(key);
        if (preference != null){
            setPreferenceSummary(preference);
        }
        if(getActivity() != null){
            getActivity().setResult(Activity.RESULT_OK);
        }
    }

    @Override
    public void onStop() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }
}
