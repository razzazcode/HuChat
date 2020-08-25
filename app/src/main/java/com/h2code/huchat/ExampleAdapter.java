package com.h2code.huchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private List<Contacts> exampleList;
    private List<Contacts> exampleListFull;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.users_profile_image);
            textView1 = itemView.findViewById(R.id.user_profile_name);
            textView2 = itemView.findViewById(R.id.user_status);
        }
    }

    ExampleAdapter(List<Contacts> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Contacts currentItem = exampleList.get(position);

        holder.imageView.setImageResource(R.drawable.profile_image);
        holder.textView1.setText(currentItem.getName());
        holder.textView2.setText(currentItem.getStatus());
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contacts> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

          for (Contacts item : exampleListFull) {
              if (item.getStatus().toLowerCase().contains(filterPattern)) {
                  filteredList.add(item);
              }
          }
      }

      FilterResults results = new FilterResults();
      results.values = filteredList;

      return results;
  }

  @Override
  protected void publishResults
          (CharSequence constraint, FilterResults results) {
      exampleList.clear();
      exampleList.addAll((List) results.values);
      notifyDataSetChanged();
        }
    };
}