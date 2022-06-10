package com.example.english_in_it;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class VocabularyRecViewAdapter extends RecyclerView.Adapter<VocabularyRecViewAdapter.ViewHolder> {
    private ArrayList<TermAndDef> vocabulary = new ArrayList<>();
    private Context context;

    public VocabularyRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VocabularyRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocabulary_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.term.setText(vocabulary.get(position).getTerm());
        holder.def.setText(String.valueOf(vocabulary.get(position).getDef()));
    }

    @Override
    public int getItemCount() {
        return vocabulary.size();
    }
    public void setVocabulary(ArrayList<TermAndDef> voc) {
        this.vocabulary = voc;
    }
    public Context getContext() { return context; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView term;
        private final TextView def;
        private final LinearLayout parent;
        View itemView;

        @SuppressLint("CutPasteId")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            term = itemView.findViewById(R.id.rec_view_term);
            def = itemView.findViewById(R.id.rec_view_def);
            parent = itemView.findViewById(R.id.vocabulary_parent);
            this.itemView = itemView;
        }
    }
}
