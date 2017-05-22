package com.example.yuda.findaplace;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by yuda on 17/05/2017.
 */

public class SettingsActivity extends PreferenceActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        ListPreference list = (ListPreference) findPreference("UnitConversionLP");
        String[] option = new String[]{"km", "miles"};
        String[] optionvalues = new String[]{"km", "miles"};

        list.setEntries(option);
        list.setEntryValues(optionvalues);

        list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(SettingsActivity.this, "choose "+newValue , Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Preference deleteprefernce = (Preference)findPreference("DeleteFavoritesP");
        deleteprefernce.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mySQLHelper mySqlHelper = new mySQLHelper(SettingsActivity.this);
                mySqlHelper.getWritableDatabase().delete(DBConstants.tableName, null , null );
                Toast.makeText(SettingsActivity.this, "deleted all favorities" , Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Preference exitPrefrance = (Preference)findPreference("ExitSettingsP");
        exitPrefrance.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                finish();
                return true;
            }
        });
    }
}
