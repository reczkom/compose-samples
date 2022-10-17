package com.example.compose.jetchat

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication


@RunWith(RobolectricTestRunner::class)
@Config(minSdk = Build.VERSION_CODES.P)
class PermissionTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NavActivity>()

    @Test
    fun checkPermissionDialogShown() {
        val application: Application = ApplicationProvider.getApplicationContext()
        val app: ShadowApplication = Shadows.shadowOf(application)
        app.denyPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        composeTestRule.onNodeWithTag("functionNotAvailable").assertDoesNotExist()
        composeTestRule.onNodeWithTag("selectorButtonMAP").performClick()


        val intent = app.nextStartedActivity
        val requestedPermissions = intent.extras?.getStringArray("android.content.pm.extra.REQUEST_PERMISSIONS_NAMES")?.toList() ?: emptyList()
        assertThat(requestedPermissions, hasItem("android.permission.ACCESS_FINE_LOCATION"))

        // user clicks in dialog of allow...
        composeTestRule.activity.onRequestPermissionsResult(99, emptyArray(), arrayOf(PackageManager.PERMISSION_GRANTED).toIntArray())

        composeTestRule.onNodeWithTag("functionNotAvailable").assertIsDisplayed()
    }

    @Test
    fun checkPermissionNotDialogShown() {
        val application: Application = ApplicationProvider.getApplicationContext()
        val app: ShadowApplication = Shadows.shadowOf(application)
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        composeTestRule.onNodeWithTag("selectorButtonMAP").performClick()


        val intent = app.nextStartedActivity

        assertThat(intent, nullValue())

        composeTestRule.onNodeWithTag("functionNotAvailable").assertIsDisplayed()
    }

}