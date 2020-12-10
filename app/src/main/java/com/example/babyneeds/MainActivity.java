package com.example.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);


        // check if items were added
        List<Item> items = db.getAllItems();

//        for (Item item : items) {
//            Log.d("name", "onCreate: " + item.getName() + item.getQuantity());
//        }

        bypassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void bypassActivity() {
        if(db.getItemsCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void saveItem(View view) {
        // Todo: save each item to db
        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int newQuantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        String newSize = itemSize.getText().toString().trim();

        Log.d("quantity", "saveItem: " + newQuantity);

        item.setName(newItem);
        item.setColor(newColor);
        item.setQuantity(newQuantity);
        item.setSize(newSize);

        db.addItem(item);

        Snackbar.make(view, "Item Saved!", Snackbar.LENGTH_LONG)
                .show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                // Todo: move to next screen - details screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200);
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.item_input);
        itemQuantity = view.findViewById(R.id.quantity_input);
        itemColor = view.findViewById(R.id.color_input);
        itemSize = view.findViewById(R.id.size_input);

        saveButton = view.findViewById(R.id.save_button);

        if (saveButton != null) {

            saveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!babyItem.getText().toString().isEmpty() &&
                            !itemColor.getText().toString().isEmpty() &&
                            !itemQuantity.getText().toString().isEmpty() &&
                            !itemSize.getText().toString().isEmpty()) {
                        saveItem(v);
                    } else {
                        Snackbar.make(v, "Fill All The Fields", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.d("null", "onCreate: Null");
        }

        builder.setView(view);

        dialog = builder.create(); // create our dialog object
        dialog.show(); // important step1
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}