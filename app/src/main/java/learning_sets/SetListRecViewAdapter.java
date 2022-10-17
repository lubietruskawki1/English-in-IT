package learning_sets;

import android.app.AlertDialog;

import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RecView of the CreateOwnTermSets activity.
 */
public class SetListRecViewAdapter extends RecyclerView.Adapter<SetListRecViewAdapter.ViewHolder> {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ArrayList<Set> items = new ArrayList<>();
    private Context context;
    private ConnectionHandlerUtils connectionHandlerUtils;

    public SetListRecViewAdapter(Context context, ConnectionHandlerUtils connectionHandlerUtils) {
        this.context = context;
        this.connectionHandlerUtils = connectionHandlerUtils;
    }

    @NonNull
    @Override
    public SetListRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SetListRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setName.setText(items.get(position).getName());
        if (items.get(position).getTerms_number() == 1) {
            holder.termsNumber.setText(items.get(position).getTerms_number() + " term");
        } else {
            holder.termsNumber.setText(items.get(position).getTerms_number() + " terms");
        }

        String set_name = items.get(position).getName();

        // We have to know which set we are editing.
        // We will pass it in the extras bundle.
        holder.editBtn.setOnClickListener(view -> {
            Intent EditSetIntent = new Intent(context, EditSet.class);

            Bundle editSetBundle = new Bundle();
            editSetBundle.putString("set_name", set_name);
            EditSetIntent.putExtras(editSetBundle);

            context.startActivity(EditSetIntent);
        });

        holder.deleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete set " + set_name + "?")
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        Toast.makeText(context, "Deleted set " + set_name, Toast.LENGTH_SHORT).show();
                        connectionHandlerUtils.deleteLearningSet(set_name);
                        Intent intent = new Intent(context, CreateOwnTermSets.class);
                        context.startActivity(intent);
                    })
                    .setNegativeButton("NO", (dialogInterface, i) -> {
                        Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Set> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView setName;
        private final Button editBtn;
        private final Button deleteBtn;
        private final TextView termsNumber;
        private final LinearLayout parent;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setName = itemView.findViewById(R.id.set_name);
            editBtn = itemView.findViewById(R.id.edit_btn);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            termsNumber = itemView.findViewById(R.id.set_items_number);
            parent = itemView.findViewById(R.id.set_parent);
            this.itemView = itemView;
        }
    }
}