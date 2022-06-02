package learning_sets;

import com.example.english_in_it.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetListRecViewAdapter extends RecyclerView.Adapter<SetListRecViewAdapter.ViewHolder> {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ArrayList<String> items = new ArrayList<>();
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
        holder.textName.setText(items.get(position));

        switch (position) {
        }

        holder.parent.setOnClickListener(view -> {
            Toast.makeText(context, "selected " + items.get(position), Toast.LENGTH_SHORT).show();
            switch (position) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        View itemView;
        Button parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            this.itemView = itemView;
        }
    }
}