package com.example.marketapplication.ui.home;

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

import com.example.marketapplication.DetailedView;
import com.example.marketapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Images> imagesArrayListForRecycler;

    public RecyclerAdapter(Context context, ArrayList<Images> imagesArrayListForRecycler)
    {
        this.context = context;
        this.imagesArrayListForRecycler = imagesArrayListForRecycler;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position)
    {
        final Images model = imagesArrayListForRecycler.get(position);
        holder.textView.setText(imagesArrayListForRecycler.get(position).getPrice());
        Picasso.get().load(imagesArrayListForRecycler.get(position).getUrl()).into(holder.imageView);
        holder.recyclerRelativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailedView.class);
                intent.putExtra("delatiledImage",model.getUrl());
                intent.putExtra("priceOfProduct",model.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return imagesArrayListForRecycler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout recyclerRelativeLayout;
        ImageView imageView;
        TextView textView;
        public ViewHolder(View view)
        {
            super(view);
            recyclerRelativeLayout = view.findViewById(R.id.recyclerRelativeLayout);
            imageView = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.price);

        }
    }

}
