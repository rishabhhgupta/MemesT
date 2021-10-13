package com.rigup.memest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.rigup.memest.Model.VideoModel;
import com.rigup.memest.Adapter.VideoAdapter;

import java.util.ArrayList;


public class home extends Fragment {



    static RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    static ArrayList<String> imagesurl=new ArrayList<String>();
    static ArrayList<String> videoUrl=new ArrayList<String>();
    int totalDownlaodImages = 0;
    static ProgressBar progressBar;
    ProgressBar bottomProgressBar;
    String recordedQuery;
    GridLayoutManager layoutManager;
    DownloadImagesInBackground downloadImagesInBackground;
    ArrayList<VideoModel> downloadedImagesArray;
    static VideoAdapter videoAdapter;

    SearchView searchView;
    static DataSnapshot filterUsedData;
    private boolean isLoading=true;
    BottomNavigationView bottomNavigationView;
    private int pastVisibleItems,visibleItemCount,totalItemCount,previosTotal = 0;
    private  int view_threshold = 10;
    static ArrayList<String> autoCompleteSearchList;
    
    public home(){
        // require a empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        bottomProgressBar = view.findViewById(R.id.bottomProgressBar);
        imagesurl= new ArrayList<String>();
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        autoCompleteSearchList = new ArrayList<>();
        downloadedImagesArray = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        videoUrl = new ArrayList<>();
        searchView = view.findViewById(R.id.searchBar);

        fetchVideoUrlFromDatabase();

    }


    public void fetchVideoUrlFromDatabase(){

        FirebaseDatabase.getInstance().getReference().child("Memes Video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filterUsedData = snapshot;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    autoCompleteSearchList.add(postSnapshot.getKey().toLowerCase());
                    Log.i("asdfasdf", postSnapshot.child("url").getValue().toString());
                    imagesurl.add(postSnapshot.child("thumbnail").getValue().toString());
                    videoUrl.add(postSnapshot.child("url").getValue().toString());
                }


                try {
                    downloadImagesInBackground = new DownloadImagesInBackground(home.this, videoUrl);
                    downloadImagesInBackground.execute(imagesurl);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });




    }



}