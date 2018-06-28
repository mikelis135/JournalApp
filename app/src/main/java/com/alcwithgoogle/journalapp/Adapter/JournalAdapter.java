package com.alcwithgoogle.journalapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alcwithgoogle.journalapp.Domain.CategoryJournal;
import com.alcwithgoogle.journalapp.R;
import java.util.ArrayList;
import java.util.List;

/*
    author Taiwo Adebayo
 */

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private List<CategoryJournal> journals = new ArrayList<>();
    private ActionCallBack actionCallBack;

    public JournalAdapter(@NonNull ActionCallBack actionCallBack){
        this.actionCallBack = actionCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionCallBack.onEdit(journals.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryJournal categoryJournal = journals.get(position);
        holder.title.setText(categoryJournal.getTitle());
        holder.description.setText(categoryJournal.getDescription());
        holder.category.setText(categoryJournal.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public void setNotes(@NonNull List<CategoryJournal> journals) {
        this.journals = journals;
        notifyDataSetChanged();
    }

    public CategoryJournal getJournal(int position) {
        return journals.get(position);
    }

    public interface ActionCallBack {
        void onEdit(CategoryJournal categoryJournal);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, category, description;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            description = itemView.findViewById(R.id.description);

        }
    }
}


