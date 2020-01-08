package com.example.marketapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketapplication.ui.home.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterForSearch extends RecyclerView.Adapter<RecyclerAdapterForSearch.ViewHolder>
{
    private Context context;
    private ArrayList<Images> imagesForSearchArrayListForRecycler;

    public RecyclerAdapterForSearch(Context context, ArrayList<Images> imagesForSearchArrayListForRecycler)
    {
        this.context = context;
        this.imagesForSearchArrayListForRecycler = imagesForSearchArrayListForRecycler;
    }

    @NonNull
    @Override
    public RecyclerAdapterForSearch.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_recycler, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterForSearch.ViewHolder holder, int position)
    {
        final Images model = imagesForSearchArrayListForRecycler.get(position);
        holder.textView.setText(model.getPrice());
        Picasso.get().load(model.getUrl()).into(holder.imageView);
        holder.recyclerRelativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailedView.class);
                intent.putExtra("delatiledImage", model.getUrl());
                intent.putExtra("priceOfProduct", model.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return imagesForSearchArrayListForRecycler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout recyclerRelativeLayout;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view)
        {
            super(view);
            recyclerRelativeLayout = view.findViewById(R.id.recyclerRelativeLayout1);
            imageView = view.findViewById(R.id.imageView1);
            textView = view.findViewById(R.id.price1);

        }
    }

}
