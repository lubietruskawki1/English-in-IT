package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;

import java.util.ArrayList;

/**
 * RecView of the Settings activity.
 */
public class SettingsListRecViewAdapter extends RecyclerView.Adapter<SettingsListRecViewAdapter.ViewHolder> {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<String> items = new ArrayList<>();
    private Context context;

    public SettingsListRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SettingsListRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsListRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.textName.setText(items.get(position));
        String uri = "@drawable/sun";

        switch (position) {
            case 0:
                uri = "@drawable/sun";
                break;
            case 1:
                uri = "@drawable/moon2";
                break;
        }

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.image.setImageDrawable(res);

        holder.parent.setOnClickListener(view -> {
            Toast.makeText(context, "selected " + items.get(position), Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                    editor = pref.edit();
                    editor.putString("theme", "LightTheme");
                    editor.commit();
                    context.setTheme(R.style.LightTheme);
                    break;
                case 1:
                    pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                    editor = pref.edit();
                    editor.putString("theme", "DarkTheme");
                    editor.commit();
                    context.setTheme(R.style.DarkTheme);
                    break;
            }
            Intent intent = new Intent(context, Settings.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final CardView parent;
        private final ImageView image;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            parent = itemView.findViewById(R.id.parent);
            this.itemView = itemView;
            image = itemView.findViewById(R.id.list_image);
        }
    }
}