package com.example.compose.jetchat

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import java.util.*

class PermissionTest {

    private lateinit var device : UiDevice

    @get:Rule
    val composeTestRule = createAndroidComposeRule<NavActivity>()

    @Before
    fun setUp() {
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    // make sure to revoke the location permission manually on real phone or by adb command in emulator
    // !!! revoke permission during test leads to a restart of the app and test fails
    @Test
    @Ignore("not for CI usage")
    fun checkPermissionDialogShown()  {
        composeTestRule.onNodeWithTag("functionNotAvailable").assertDoesNotExist()
        composeTestRule.onNodeWithTag("selectorButtonMAP").performClick()

        val button: UiObject? = device.findObject(UiSelector().text(getAllowButtonText()))

        assertThat(button, notNullValue())

        button?.click()

        composeTestRule.onNodeWithTag("functionNotAvailable").assertIsDisplayed()

    }

    private fun getAllowButtonText(): String {
        return  when(android.os.Build.VERSION.SDK_INT) {
            android.os.Build.VERSION_CODES.S -> when (Locale.getDefault()) {
                Locale.GERMAN, Locale.GERMANY -> "Bei Nutzung der App"
                else -> "While using the app"
            }

            android.os.Build.VERSION_CODES.Q -> when (Locale.getDefault()) {
                Locale.GERMAN, Locale.GERMANY -> "Bei Nutzung der App"
                else -> "Allow only while using the app"
            }

            else -> when (Locale.getDefault()) {
                Locale.GERMAN, Locale.GERMANY -> "ZULASSEN"
                else -> "ALLOW"
            }
        }
    }
}