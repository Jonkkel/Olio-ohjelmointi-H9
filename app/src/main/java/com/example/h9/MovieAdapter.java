package com.example.h9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // ... constructor and member variables
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ViewHolder(View itemView) {

            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
        }
    }
    private ArrayList<String> movieNames;
    public MovieAdapter(ArrayList<String> movies) {
        movieNames = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movies, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        String movie = movieNames.get(position);
        TextView textView = holder.txtName;

        textView.setText(movie);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movieNames.size();
    }

}
