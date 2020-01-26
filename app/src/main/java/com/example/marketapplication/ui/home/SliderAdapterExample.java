package com.example.marketapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.marketapplication.DetailedView;
import com.example.marketapplication.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>
{

    private Context context;
    private ArrayList<Images> imagesArrayListForRecycler;

    public SliderAdapterExample(Context context, ArrayList<Images> imagesArrayListForRecycler)
    {
        this.context = context;
        this.imagesArrayListForRecycler = imagesArrayListForRecycler;
    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent, false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position)
    {
//        viewHolder.textViewDescription.setText("This is slider item " + position);


        final Images model = imagesArrayListForRecycler.get(position);
        viewHolder.textViewDescription.setText(model.getPrice());
        Glide.with(viewHolder.itemView).load(model.getUrl()).into(viewHolder.imageViewBackground);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
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
    public int getCount()
    {
        //slider view count could be dynamic size
        return imagesArrayListForRecycler.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder
    {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView)
        {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imageView);
            textViewDescription = itemView.findViewById(R.id.price);
            this.itemView = itemView;
        }
    }
}