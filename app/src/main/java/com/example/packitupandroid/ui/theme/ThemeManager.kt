package com.example.packitupandroid.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Object containing keys used to store and retrieve theme preference data in DataStore.
 */
object ThemePreference {
    val DARK_THEME = booleanPreferencesKey("DARK_THEME")
}

/**
 * Manages the application's theme, allowing users to switch between light and dark modes.
 *
 * This class uses Android's DataStore to persist the user's preferred theme across app launches.
 * It also provides a way to observe the current theme state as a Flow.
 *
 * @property context The application context, used to access DataStore.
 * @property systemDarkTheme A boolean indicating whether the system's dark theme is enabled.
 *   This is used as the default theme if the user hasn't explicitly chosen one.
 */
class ThemeManager(private val context: Context, val systemDarkTheme: Boolean) {
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ThemePreference.DARK_THEME] ?: systemDarkTheme
        }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ThemePreference.DARK_THEME] = isDark
        }
    }
}

/**
 * Remembers and returns a [ThemeManager] instance.
 *
 * This function utilizes Compose's [remember] composable function to ensure that the
 * [ThemeManager] instance is created only once and reused across recompositions.
 * It provides a convenient way to manage the application's theme state,
 * including whether to use the system's dark theme preference.
 *
 * @param context The [Context] used by the [ThemeManager]. Defaults to the current
 *   [LocalContext].
 * @param systemDarkTheme A boolean indicating whether the system is currently in dark theme.
 *   Defaults to the result of [isSystemInDarkTheme()]. This parameter is used to initialize the
 *   [ThemeManager]'s initial theme state. Subsequent changes to the system's dark theme will
 *   be handled by the [ThemeManager] if it is configured to follow the system theme.
 *
 * @return A [ThemeManager] instance.
 *
 * @see ThemeManager
 * @see remember
 * @see LocalContext
 * @see isSystemInDarkTheme
 */
@Composable
fun rememberThemeManager(
    context: Context = LocalContext.current,
    systemDarkTheme: Boolean = isSystemInDarkTheme()
): ThemeManager {
    return remember { ThemeManager(context, systemDarkTheme) }
}
