package com.technetapps.english_in_it;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;

import learning_sets.EditSet;

import java.util.ArrayList;
import java.util.List;

public class VocabularyRecViewAdapter extends RecyclerView.Adapter<VocabularyRecViewAdapter.ViewHolder> implements Filterable {
    private List<TermAndDef> vocabulary = new ArrayList<>();
    private List<TermAndDef> fullVocabulary;

    private Context context;
    private boolean allowDelete = false;
    private ConnectionHandlerUtils connectionHandlerUtils;
    private String setName;

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

        if (allowDelete) {
            holder.parent.setOnClickListener(view -> {
                TermAndDef chosen = vocabulary.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String term = chosen.getTerm();
                builder.setMessage("Are you sure you want to delete word " + term + " from set?")
                        .setPositiveButton("YES", (dialogInterface, i) -> {
                            Toast.makeText(context, "Deleted word " + term, Toast.LENGTH_SHORT).show();
                            connectionHandlerUtils.deleteWordFromLearningSet(term, setName);
                            Intent EditSetIntent = new Intent(context, EditSet.class);
                            Bundle editSetBundle = new Bundle();
                            editSetBundle.putString("set_name", setName);
                            EditSetIntent.putExtras(editSetBundle);
                            context.startActivity(EditSetIntent);
                        })
                        .setNegativeButton("NO", (dialogInterface, i) -> {
                            Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return vocabulary.size();
    }
    public void setVocabulary(ArrayList<TermAndDef> voc) {
        this.vocabulary = voc;
        fullVocabulary = new ArrayList<>(voc);
    }
    public Context getContext() { return context; }

    @Override
    public Filter getFilter() {
        return wordsFilter;
    }

    private Filter wordsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TermAndDef> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullVocabulary);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TermAndDef item : fullVocabulary) {
                    if (item.getTerm().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            vocabulary.clear();
            vocabulary.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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

    public ArrayList<TermAndDef> getVocabulary() {
        return new ArrayList<TermAndDef>(vocabulary);
    }

    public void allowDelete(ConnectionHandlerUtils connectionHandlerUtils, String setName) {
        this.allowDelete = true;
        this.connectionHandlerUtils = connectionHandlerUtils;
        this.setName = setName;
    }
}
