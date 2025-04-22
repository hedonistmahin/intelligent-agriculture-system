package com.example.agri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private int[] posterList;

    // Constructor to receive the list of posters (images)
    public PosterAdapter(int[] posterList) {
        this.posterList = posterList;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each poster item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        // Set the image resource for the poster at the current position
        holder.posterImageView.setImageResource(posterList[position]);
    }

    @Override
    public int getItemCount() {
        // Return the number of posters in the list
        return posterList.length;
    }

    // ViewHolder class to hold each individual poster item
    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            // Bind the ImageView from the layout file
            posterImageView = itemView.findViewById(R.id.posterImageView);
        }
    }
}
