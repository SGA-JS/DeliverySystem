package com.example.deliverysystem;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginButton() {
        // Input username and password
        Espresso.onView(ViewMatchers.withId(R.id.editTextUserName))
                .perform(ViewActions.typeText("AdminTest"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                .perform(ViewActions.typeText("admin123"), ViewActions.closeSoftKeyboard());

        // Perform click action on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin))
                .perform(ViewActions.click());
    }

//    @Test
//    public void testSignUpButton() {
//        // Perform click action on the login button
//        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUp))
//                .perform(ViewActions.click());
//
//        // Enter user details
//        Espresso.onView(withId(R.id.editTextName))
//                .perform(ViewActions.typeText("AdminTest"), ViewActions.closeSoftKeyboard());
//
//        Espresso.onView(withId(R.id.editTextPassword))
//                .perform(ViewActions.typeText("admin123"), ViewActions.closeSoftKeyboard());
//
//        Espresso.onView(withId(R.id.roleAdmin))
//                .perform(ViewActions.click());
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
