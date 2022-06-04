package learning_sets;

import com.example.english_in_it.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import flashcards.FlashcardsOptions;

public class SetListRecViewAdapter extends RecyclerView.Adapter<SetListRecViewAdapter.ViewHolder> {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ArrayList<Set> items = new ArrayList<>();
    private Context context;

    public SetListRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SetListRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetListRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setName.setText(items.get(position).getName());
        holder.termsNumber.setText(String.valueOf(items.get(position).getTerms_number()) + " terms");

        // We have to know which set we are editing.
        // We will pass it in the extras bundle.
        holder.editBtn.setOnClickListener(view -> {
            Intent EditSetIntent = new Intent(context, EditText.class);

            Bundle editSetBundle = new Bundle();
            editSetBundle.putString("set_name", items.get(position).getName());
            EditSetIntent.putExtras(editSetBundle);

            context.startActivity(EditSetIntent);
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
        private final TextView termsNumber;
        private final LinearLayout parent;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setName = itemView.findViewById(R.id.set_name);
            editBtn = itemView.findViewById(R.id.edit_btn);
            termsNumber = itemView.findViewById(R.id.set_items_number);
            parent = itemView.findViewById(R.id.set_parent);
            this.itemView = itemView;
        }
    }
}