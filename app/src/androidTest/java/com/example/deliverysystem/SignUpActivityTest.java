package com.example.deliverysystem;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.ConstantValue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class SignUpActivityTest {

    private DBHelper dbHelper;

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testCreateAdminUser() {
        // Perform click action on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUp))
                .perform(ViewActions.click());
        // Enter user details
        Espresso.onView(withId(R.id.editTextName))
                .perform(ViewActions.typeText("AdminTest"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.editTextPassword))
                .perform(ViewActions.typeText("admin123"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.roleAdmin))
                .perform(ViewActions.click());

        // Click on the Sign Up button
        Espresso.onView(withId(R.id.buttonSignUp))
                .perform(ViewActions.click());

        // Check if success toast message is displayed
//        Espresso.onView(withText("New user added"))
//                .inRoot(new ToastMatcher())
//                .check(ViewAssertions.matches(isDisplayed()));
    }

//    @Test
//    public void testCreateDriverUser() {
//        // Perform click action on the login button
//        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUp))
//                .perform(ViewActions.click());
//
//        // Enter user details
//        Espresso.onView(withId(R.id.editTextName))
//                .perform(ViewActions.typeText("DriverTest"), ViewActions.closeSoftKeyboard());
//
//        Espresso.onView(withId(R.id.editTextPassword))
//                .perform(ViewActions.typeText("driver123"), ViewActions.closeSoftKeyboard());
//
//        Espresso.onView(withId(R.id.roleDriver))
//                .perform(ViewActions.click());
//
//        Espresso.onView(withId(R.id.editTextVehicle))
//                .perform(ViewActions.typeText("Vehicle123"), ViewActions.closeSoftKeyboard());
//
//        // Click on the Sign Up button
//        Espresso.onView(withId(R.id.buttonSignUp))
//                .perform(ViewActions.click());
//
//        // Check if success toast message is displayed
//        Espresso.onView(withText("New user added"))
//                .inRoot(new ToastMatcher())
//                .check(ViewAssertions.matches(isDisplayed()));
//    }
}

