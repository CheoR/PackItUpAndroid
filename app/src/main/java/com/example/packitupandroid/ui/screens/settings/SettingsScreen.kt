package com.example.packitupandroid.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.theme.ThemeManager
import com.example.packitupandroid.ui.theme.rememberThemeManager
import kotlinx.coroutines.launch


/**
 * Settings screen allows user to toggle settings like dark theme.
 *
 * This composable provides a simple UI with switch controls manually toggle application settings.
 * It utilizes a [ThemeManager] to manage the current settings state and persists
 * the user's preference.
 *
 * @param themeManager The [ThemeManager] instance responsible for managing the application's theme.
 *                     Defaults to a remembered instance if not provided.
 */
@Composable
fun SettingsScreen(
    themeManager: ThemeManager = rememberThemeManager(),
) {
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        themeManager.isDarkTheme.collect { isDark ->
            isDarkTheme = isDark
        }
    }

    val settings = listOf(
        Setting(
            text = R.string.dark_theme,
            icon =  if(isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
            isChecked = isDarkTheme,
            onCheckedChange = { isChecked ->
                isDarkTheme = isChecked
                scope.launch {
                    themeManager.setDarkTheme(isChecked)
                }
            },
        ),
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        settings.forEach { setting ->
            SettingsRow(setting = setting)
        }
    }
}

@Composable
private fun SettingsRow(
    setting: Setting,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = setting.icon,
            contentDescription = stringResource(R.string.set_setting, setting.text),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(setting.text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = setting.isChecked,
            onCheckedChange = setting.onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary),
        )
    }
}

private data class Setting(
    @StringRes val text: Int,
    val icon: ImageVector,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)


@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    SettingsScreen(themeManager)
}