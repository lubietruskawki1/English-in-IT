package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListRecViewAdapter extends RecyclerView.Adapter<ListRecViewAdapter.ViewHolder> {
    private ArrayList<String> items = new ArrayList<>();
    private Context context;

    public ListRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.textName.setText(items.get(position));
        String uri = "@drawable/flashcard_img";

        switch (position) {
            case 0:
                uri = "@drawable/vocab_img";
                break;
            case 1:
                uri = "@drawable/choosing_img";
                break;
            case 2:
                uri = "@drawable/flashcard_img";
                break;
            case 3:
                uri = "@drawable/memory_img";
                break;
            case 4:
                uri = "@drawable/comet_img";
                break;
            case 5:
                uri = "@drawable/settings_img";
                break;
        }

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.image.setImageDrawable(res);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "selected " + items.get(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent browse_intent = new Intent(context, BrowseVocabulary.class);
                        context.startActivity(browse_intent);
                        break;
                    case 1:
                        break;
                    case 2:
                    case 3:
                        Intent memory_intent = new Intent(context, Memory.class);
                        context.startActivity(memory_intent);
                        break;
                    case 4:
                    case 5:
                }
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
        private final CardView parent;
        private final ImageView image;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            parent = itemView.findViewById(R.id.parent);
            this.itemView = itemView;
            image = itemView.findViewById(R.id.list_image);

            //if (image == null) System.out.println("jest nullem");
            //else System.out.println("nie jest nullem");
        }
    }
}