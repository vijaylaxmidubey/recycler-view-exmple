package com.assignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieItem extends RecyclerView.Adapter<MovieItem.ViewHolder> {
    private List<Movie> list;
    private RecycleItemListener itemListener;
   private Context context;


    public MovieItem(Context context, RecycleItemListener listener,List<Movie> list) {
        this.context = context;
        this.list = list;
        this.itemListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Movie movie = list.get(position);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvYear.setText(movie.getYear());
        // viewHolder.thumbnail.setImageResource(movie.getImageUrl());
        System.out.println("Image URL : "+movie.getImageUrl());
        Picasso.with(context).load(movie.getImageUrl()).into(viewHolder.thumbnail);
        viewHolder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClickItem(movie.getImdbID());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    List<Movie> filtered = new ArrayList<>();
                    for (Movie row : list) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filtered.add(row);
                        }
                    }

                    filteredList = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvYear;
        ImageView thumbnail;
        public LinearLayout main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.mvName);
            tvYear = (TextView) itemView.findViewById(R.id.mvYear);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            main = (LinearLayout) itemView.findViewById(R.id.rvItem);

        }
    }
}
