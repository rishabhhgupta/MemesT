package com.beetleInk.memest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import com.beetleInk.memest.Adapter.VideoAdapter;
import com.beetleInk.memest.Model.VideoModel;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DownloadImagesInBackground extends AsyncTask<ArrayList<String>, ArrayList<VideoModel>,ArrayList<VideoModel>> {
    private WeakReference<MainActivity> activityWeakReference;
    private int totalDownlaodedImages = 10;
    ArrayList<String> videoUrl = new ArrayList<>();

    public DownloadImagesInBackground(MainActivity activity, ArrayList<String> VideoUrl) {
        activityWeakReference = new WeakReference<MainActivity>(activity);
        videoUrl = VideoUrl;


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }





    }

    @Override
    protected ArrayList<VideoModel> doInBackground(ArrayList<String>... arrayLists) {
        ArrayList<VideoModel> downlaodedImagesArray = new ArrayList<>();
        MainActivity activity = activityWeakReference.get();
        totalDownlaodedImages += activity.totalDownlaodImages;
        for (int i = activity.totalDownlaodImages; i < totalDownlaodedImages; i++) {
            activity.totalDownlaodImages++;

                try {
                    URL url = new URL(arrayLists[0].get(i));
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    Bitmap myBitMap = BitmapFactory.decodeStream(in);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setDownloadImages(myBitMap);
                    downlaodedImagesArray.add(videoModel);

                } catch (Exception e) {
                    e.printStackTrace();
                    downlaodedImagesArray.add(null);
                }


        }



        return downlaodedImagesArray;
    }





    @Override
    protected void onPostExecute(ArrayList<VideoModel> arrayList) {
        super.onPostExecute(arrayList);
        MainActivity activity =activityWeakReference.get();

        if(activity == null || activity.isFinishing()){
            return;
        }

        activity.videoAdapter = new VideoAdapter(activity, arrayList, activity,videoUrl, activity.videoNamee );
        activity.recyclerView.setAdapter(activity.videoAdapter);
        activity.progressBar.setVisibility(View.GONE);

    }












}
