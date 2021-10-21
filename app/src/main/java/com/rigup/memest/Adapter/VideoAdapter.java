package com.rigup.memest.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.rigup.memest.BuildConfig;
import com.rigup.memest.Model.VideoModel;
import com.rigup.memest.R;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static android.content.Context.DOWNLOAD_SERVICE;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    Context context;
    ArrayList<VideoModel> downlaodedImagesArray;
    Activity activity;
    VideoView videoView;
    ArrayList<String> videoUrl;
    ArrayList<String> videoName;

    public VideoAdapter(Context context,ArrayList<VideoModel>  downlaodedImagesArray, Activity activity,
                        ArrayList<String> videoUrl, ArrayList<String> name){
        this.context = context;
        this.downlaodedImagesArray = downlaodedImagesArray;
        this.activity = activity;
        this.videoUrl = videoUrl;
        this.videoName = name;


    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video, parent, false);
        return new VideoAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.isMemoryCacheable();
        Log.i("downloadImagesArray", downlaodedImagesArray.toString());
        if(downlaodedImagesArray.get(position).getDownloadImages()!=null){
            holder.imageView.setImageBitmap(downlaodedImagesArray.get(position).getDownloadImages());
        }




        //compress file name given

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentNamed = videoName.get(position);

                try {
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},1);
                    }


                    if(!checkMemesVideoExist(contentNamed)){
                        Log.i("checkMemes", checkMemesVideoExist(contentNamed).toString());
                        Uri downloadUri = Uri.parse(videoUrl.get(position));

                        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                        request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,contentNamed);


                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                        request.setVisibleInDownloadsUi(false);
                        request.setAllowedOverRoaming(false);
                        request.setNotificationVisibility(0);
                        request.setMimeType("video/mp4");
                        downloadManager.enqueue(request);
                        


                    }



                    //Alert box for video
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    final View videoContactView =  LayoutInflater.from(activity).inflate(R.layout.video_popup,null);
                    videoView = videoContactView.findViewById(R.id.videoView);
                    videoView.setVideoPath(Environment.getDataDirectory()+"/"+BuildConfig.APPLICATION_ID+"/files/Download/"+contentNamed);
                    videoView.requestFocus();
                    videoView.start();

                    alertDialogBuilder.setView(videoContactView);
                    Dialog dialog = alertDialogBuilder.create();
                    dialog.show();

                    ///end alert box


                } catch (Exception e) {
                    Log.d("Error....", e.toString());
                }

                /////////////////////////



            }
        });


        holder.shareButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShareVideo SV = new ShareVideo(videoUrl.get(position),context,activity,position, videoName);
//
//                SV.execute();

            }
        });
    }

    @Override
    public int getItemCount() {
        int position = 0;
        for(int i=0; i<downlaodedImagesArray.size();i++){
            if(downlaodedImagesArray.get(i)!=null){
                position++;
            }
        }
        return position;

    }


    public static  class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        Button shareButtonView;


        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            shareButtonView= itemView.findViewById(R.id.shareButtonView);



        }


    }

    public void addImagess(VideoModel images){

        downlaodedImagesArray.add(images);


        notifyDataSetChanged();
    }

    public void clearedImages(){
        downlaodedImagesArray.clear();
        notifyDataSetChanged();
    }


    public Boolean checkMemesVideoExist(String VideoName){
        File filePath = new File(Environment.getDataDirectory()+"/"+BuildConfig.APPLICATION_ID+"/files/Download/"+VideoName);
        Log.i("checkMemes", filePath.getAbsolutePath());

        if(filePath.exists()){
            return true;
        }else{
            return false;
        }
    }


}


//class ShareVideo extends AsyncTask<String ,String,String>{
//
//    String videourl;
//    Context context;
//    Activity activity;
//    int position;
//    ArrayList<String> compressVideoName;
//
//
//
//
//
//
//    public ShareVideo(String videourl, Context context,Activity activity, int pos, ArrayList<String> videoName) {
//        this.videourl = videourl;
//        this.context = context;
//        this.activity = activity;
//        this.compressVideoName = videoName;
//        this.position = pos;
//
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        sharevideFile(videourl, compressVideoName.get(position),activity);
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//
////
////        Uri imageUri = FileProvider.getUriForFile(
////                context,
////                BuildConfig.APPLICATION_ID+".provider", //(use your app signature + ".provider" )
////                new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName));
////
////
////        Intent sharintent=new Intent("android.intent.action.SEND");
////        sharintent.setType("video/mp4");
////        sharintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////
////        sharintent.putExtra("android.intent.extra.STREAM", imageUri) ;
////        activity.startActivity(Intent.createChooser(sharintent,"Send to"));
//
//        super.onPostExecute(s);
//    }
//
//
//
//
//
//    private void sharevideFile(String videourl, String name,Activity activity) {
//
//        try {
//            if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},1);
//            }
//
//
//
//            Uri downloadUri = Uri.parse(videourl);
//            Log.i("video URL", videourl);
//            DownloadManager downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
//            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
////            Environment.DIRECTORY_DOWNLOADS,name
//            request.setDestinationInExternalFilesDir(context,fiddle.getAbsolutePath(),name);
//
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
//            request.setVisibleInDownloadsUi(false);
//            request.setAllowedOverRoaming(false);
//            request.setNotificationVisibility(0);
//            request.setMimeType("video/mp4");
//
//
//            downloadManager.enqueue(request);
//        } catch (Exception e) {
//            Log.d("Error....", e.toString());
//        }
//
//
//
//    }
//
//}



