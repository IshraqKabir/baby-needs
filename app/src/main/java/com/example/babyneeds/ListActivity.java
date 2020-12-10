package com.example.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> items;
    private DatabaseHandler db;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DatabaseHandler(this);

        recyclerView = findViewById(R.id.itemListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();

        // Get items from db;

        items = db.getAllItems();

        recyclerViewAdapter = new RecyclerViewAdapter(this, items);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

       fab = findViewById(R.id.fab_activity_list);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                createPopupDialog();
           }
       });

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
                        Snackbar.make(v, "Fill All The Fields", Snackbar.LENGTH_LONG);
                    }
                }
            });
        } else {
            Log.d("null", "onCreate: Null");
        }
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveItem(View view) {
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
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);
    }
}