package com.example.nimira.ezrecipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

//import static android.support.test.espresso.*;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mashape.relocation.util.Asserts.check;
import static org.junit.Assert.*;

/**
 * Created by dkswe on 6/4/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private String ingred1, ingred2;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        //Specify valid strings
        ingred1 = "apples";
        ingred2 = "flour";
    }

    @Test
    public void input_ingredientsTest() {
        //Open text edit view
        onView(withId(R.id.addIngredients)).perform(click());
        //Type text then press the button.
        onView(withId(R.id.search)).perform(typeText(ingred1));
        onView(withId(R.id.add)).perform(click());
        //onView(withId(0)).perform(click());
        //Check that the checkbox was created
        onView(withId(0)).check(matches(withText(ingred1)));
        Log.i("What", "how");
    }

    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void displayCheckBoxes() throws Exception {
        onView(withId(0)).perform(click());
    }

    @Test
    public void getCheckBoxes() throws Exception {

    }

    @Test
    public void selectItems() throws Exception {

    }

    @Test
    public void onCreate1() throws Exception {

    }

    @Test
    public void setTheme() throws Exception {

    }

    @Test
    public void onPostCreate() throws Exception {

    }

}