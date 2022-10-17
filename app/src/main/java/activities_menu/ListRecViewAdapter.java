package activities_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.BrowseVocabulary;
import com.technetapps.english_in_it.DailyRepetitions;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.TypingWordsExerciseOptions;

import java.util.ArrayList;

import comet.CometTimerActivity;
import learning_sets.CreateOwnTermSets;
import flashcards.FlashcardsOptions;
import memory.Memory;

/**
 * RecView of the StartListActivity activity.
 */
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
                uri = "@drawable/dark_blue_clock_img";
                break;
            case 3:
                uri = "@drawable/flashcard_img";
                break;
            case 4:
                uri = "@drawable/memory_img";
                break;
            case 5:
                uri = "@drawable/comet_img";
                break;
            case 6:
                uri = "@drawable/pencil_no_background_img";
                break;
            case 7:
                uri = "@drawable/settings_img";
                break;
        }

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.image.setImageDrawable(res);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        Intent browse_intent = new Intent(context, BrowseVocabulary.class);
                        context.startActivity(browse_intent);
                        break;
                    case 1:
                        Intent choose_intent = new Intent(context, CreateOwnTermSets.class);
                        context.startActivity(choose_intent);
                        break;
                    case 2:
                        Intent repetitions_intent = new Intent(context, DailyRepetitions.class);
                        context.startActivity(repetitions_intent);
                        break;
                    case 3:
                        Intent flashcards_intent = new Intent(context, FlashcardsOptions.class);
                        context.startActivity(flashcards_intent);
                        break;
                    case 4:
                        Intent memory_intent = new Intent(context, Memory.class);
                        context.startActivity(memory_intent);
                        break;
                    case 5:
                        Intent comet_intent = new Intent(context, CometTimerActivity.class);
                        context.startActivity(comet_intent);
                        break;
                    case 6:
                        Intent typing_exercise_intent = new Intent(context, TypingWordsExerciseOptions.class);
                        context.startActivity(typing_exercise_intent);
                        break;
                    case 7:
                        Intent settings_intent = new Intent(context, Settings.class);
                        context.startActivity(settings_intent);
                        break;
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
        }
    }
}