package com.mayank_amr.weather.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:53 PM
 */
class LocationPreferences(context: Context) {
    private val applicationContext = context.applicationContext
    private val locationDataStore: DataStore<Preferences>

    init {
        locationDataStore = applicationContext.createDataStore(
            name = "my_location_data_store"
        )

    }

    val latitude: Flow<String?>
        get() = locationDataStore.data.map { preferences ->
            preferences[KEY_LAT]
        }

    val longitude: Flow<String?>
        get() = locationDataStore.data.map { preferences ->
            preferences[KEY_LON]
        }

    suspend fun saveUserLocation(latitude: String, longitude: String) {
        locationDataStore.edit { preferences ->
            preferences[KEY_LAT] = latitude
            preferences[KEY_LON] = longitude
        }
    }

    suspend fun clearLocationData() {
        locationDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_LAT = preferencesKey<String>("key_lat")
        private val KEY_LON = preferencesKey<String>("key_lon")
    }

}