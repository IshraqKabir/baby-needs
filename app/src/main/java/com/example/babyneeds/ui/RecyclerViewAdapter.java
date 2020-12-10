package com.example.babyneeds.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.name.setText("Name: " + item.getName());
        holder.color.setText("Color: " + item.getColor());
        holder.quantity.setText("Quantity: " + String.valueOf(item.getQuantity()));
        holder.size.setText("Size: " + item.getSize());
        holder.dateAdded.setText("Date: " + item.getDateItemAdded());
    }

    public RecyclerViewAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView color;
        public TextView quantity;
        public TextView size;
        public TextView dateAdded;
        public int id;


        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView, Context innerContext) {
            super(itemView);
            context = innerContext;

            name = itemView.findViewById(R.id.item_name);
            color = itemView.findViewById(R.id.item_color);
            quantity = itemView.findViewById(R.id.item_quantity);
            size = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.item_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Item item = items.get(position);

            switch (v.getId()) {
                case R.id.editButton:
                    // edit item
                    updateItem(item);
                    break;
                case R.id.deleteButton:
                    // delete item
                    deleteItem(item.getId());
                    break;
            }
        }

        private void updateItem(Item item) {
            builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.popup, null);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            TextView name = view.findViewById(R.id.item_input);
            TextView quantity = view.findViewById(R.id.quantity_input);
            TextView size = view.findViewById(R.id.size_input);
            TextView color = view.findViewById(R.id.color_input);

            name.setText(item.getName());
            quantity.setText(String.valueOf(item.getQuantity()));
            size.setText(item.getSize());
            color.setText(item.getColor());

            Button editButton = view.findViewById(R.id.save_button);
            editButton.setText("Edit");

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!name.getText().toString().isEmpty() &&
                            !color.getText().toString().isEmpty() &&
                            !quantity.getText().toString().isEmpty() &&
                            !size.getText().toString().isEmpty()) {

                        item.setId(item.getId());
                        item.setName(name.getText().toString().trim());
                        item.setColor(color.getText().toString().trim());
                        item.setSize(size.getText().toString().trim());
                        item.setQuantity(Integer.parseInt(quantity.getText().toString().trim()));
                        item.setDateItemAdded(item.getDateItemAdded());

                        DatabaseHandler db = new DatabaseHandler(context);
                        int id = db.updateItem(item);

                        notifyItemChanged(getAdapterPosition());
                        items.set(getAdapterPosition(), item);
                        dialog.dismiss();

                        Log.d("id", "onClick: " + String.valueOf(id));
                    } else {
                        Snackbar.make(v, "Fill All The Fields", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void deleteItem(int id) {
            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop, null);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            Button noButton = view.findViewById(R.id.conf_no_button);
            Button yesButton = view.findViewById(R.id.conf_yes_button);

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    items.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }
    }
}
