package com.mbusheikin.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOExceptionWithCause;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.id.message;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.mbusheikin.simpletodo.R.id.lvItems;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);

        readItems();

        this.setupListViewAdapter();
        this.setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        // Ignore if there's no text.
        if (itemText == null || itemText.isEmpty()) {
            return;
        }
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // Setup

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditTaskForItemAt(position);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private void setupListViewAdapter() {
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
    }

    // Helpers

    private void showEditTaskForItemAt(int position) {
        String title = items.get(position);
        EditTaskActivity.startActivity(this, title, position);
    }

    // Event handlers

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case (EditTaskActivity.REQUEST_CODE): {
                onEditTaskResult(resultCode, data);
                break;
            }
        }
    }

    private void onEditTaskResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra(EditTaskActivity.EXTRA_TITLE);
            int position = data.getIntExtra(EditTaskActivity.EXTRA_POSITION, 0);

            items.set(position, title);
            itemsAdapter.notifyDataSetChanged();

            writeItems();
        }
    }

    // Persistence

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
