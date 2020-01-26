package com.example.marketapplication.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.marketapplication.R;
import com.example.marketapplication.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{

    private EditText editText;
    private ProgressBar progressBar;

    private SliderView recyclerView;
    private SliderAdapterExample recyclerAdapter;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ArrayList<Images> imagesArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        editText = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.recyclerViewForImages);
        swipeRefreshLayout = view.findViewById(R.id.refereshner);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        imagesArrayList = new ArrayList<>();

        recyclerView.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);


        init();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        init();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });

    }

    private void init()
    {
        clearAll();
        Query query = databaseReference.child("images");
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Images images = new Images();
                    images.setUrl(dataSnapshot1.child("url").getValue().toString());
                    images.setPrice(dataSnapshot1.child("price").getValue().toString());
                    imagesArrayList.add(images);
                }

                startRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private void startRecyclerView()
    {
        recyclerAdapter = new SliderAdapterExample(getActivity(), imagesArrayList);
        recyclerView.setSliderAdapter(recyclerAdapter);
        recyclerView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        recyclerView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        recyclerView.setIndicatorSelectedColor(Color.WHITE);
        recyclerView.setIndicatorUnselectedColor(Color.GRAY);
        recyclerView.setScrollTimeInSec(2);
        recyclerView.startAutoCycle();
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void clearAll()
    {
        if (imagesArrayList != null)
        {
            imagesArrayList.clear();
            if (recyclerAdapter != null)
            {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        imagesArrayList = new ArrayList<>();
    }

}