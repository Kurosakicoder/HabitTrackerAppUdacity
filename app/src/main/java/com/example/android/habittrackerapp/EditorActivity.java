package com.example.android.habittrackerapp;
/**
 * This project is done by Khaidem Sandip Singha under the Udacity Android Foundations Nanodegree program.
 *
 * I confirm that this submission is my own work. I have not used code from any other Udacity student's or graduate's submission of the same project.
 * I understand that Udacity will check my submission for plagiarism, and that failure to adhere to the Udacity Honor Code may result in the cancellation of my
 * enrollment.
 */
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.PuzzleContract.PuzzleEntry;
import com.example.android.habittrackerapp.data.PuzzleDbHelper;

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the puzzle name */
    private EditText mNameEditText;

    /** EditText field to enter the puzzle author name */
    private EditText mAuthorEditText;

    /** EditText field to enter the time taken to solve the puzzle */
    private EditText mTimeEditText;

    /** EditText field to enter the author gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the author. The possible valid values are in the PuzzleContract.java file:
     * {@link PuzzleEntry#GENDER_UNKNOWN}, {@link PuzzleEntry#GENDER_MALE}, or
     * {@link PuzzleEntry#GENDER_FEMALE}.
     */
    private int mGender = PuzzleEntry.GENDER_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_puzzle_name);
        mAuthorEditText = findViewById(R.id.edit_author_name);
        mGenderSpinner = findViewById(R.id.spinner_gender);
        mTimeEditText = findViewById(R.id.edit_time_taken);

        setupSpinner();

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner(){
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals(getString(R.string.gender_male))){
                        mGender = PuzzleEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PuzzleEntry.GENDER_FEMALE;
                    } else {
                        mGender = PuzzleEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PuzzleEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    /**
     * * Get user input from editor and save new puzzle into database.
     */
    private void insertPuzzles(){
        //Read from input fields
        // Use trim to eliminate leading or trailing white space
        String puzzleString = mNameEditText.getText().toString().trim();
        String authorString = mAuthorEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        int time = Integer.parseInt(timeString);

        // Create database helper
        PuzzleDbHelper mDbHelper = new PuzzleDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PuzzleEntry.COLUMN_PUZZLE_NAME, puzzleString);
        values.put(PuzzleEntry.COLUMN_PUZZLE_AUTHOR, authorString);
        values.put(PuzzleEntry.COLUMN_AUTHOR_GENDER, mGender);
        values.put(PuzzleEntry.COLUMN_PUZZLE_DURATION, time);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(PuzzleEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving puzzle", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Puzzle saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertPuzzles();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (HabitActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
