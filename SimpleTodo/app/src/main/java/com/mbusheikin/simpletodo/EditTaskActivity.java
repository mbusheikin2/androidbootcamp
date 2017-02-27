package com.mbusheikin.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.id.message;

/**
 * Created by mbusheikin on 2/15/17.
 */

public class EditTaskActivity extends AppCompatActivity {

    private int position;

    private EditText titleTextView;

    public final static int REQUEST_CODE = 23498;

    public final static String EXTRA_TITLE = "com.mbusheikin.simpletodo.EDIT_TASK_TITLE";
    public final static String EXTRA_POSITION = "com.mbusheikin.simpletodo.EDIT_TASK_POSITION";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setTitle("Edit Task");

        titleTextView = (EditText)findViewById(R.id.titleTextView);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EditTaskActivity.EXTRA_TITLE);
        if (title != null) {
            titleTextView.setText(title);
        }

        position = intent.getIntExtra(EXTRA_POSITION, 0);

    }

    public void onSaveClicked(View v) {

        String title = titleTextView.getText().toString();

        Intent resultIntent = new Intent();

        resultIntent.putExtra(EXTRA_TITLE, title);
        resultIntent.putExtra(EXTRA_POSITION, position);

        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    public static void startActivity(Activity parent, String title, int position) {
        Intent intent = new Intent(parent, EditTaskActivity.class);

        intent.putExtra(EditTaskActivity.EXTRA_TITLE, title);
        intent.putExtra(EditTaskActivity.EXTRA_POSITION, position);

        parent.startActivityForResult(intent, REQUEST_CODE);
    }

}
