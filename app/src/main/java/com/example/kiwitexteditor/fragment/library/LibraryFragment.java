package com.example.kiwitexteditor.fragment.library;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.callback.ActionbarListener;
import com.example.kiwitexteditor.callback.PhotoCallback;
import com.example.kiwitexteditor.databinding.FragLibraryBinding;
import com.example.kiwitexteditor.model.Bucket;
import com.example.kiwitexteditor.model.ImageFolder;
import com.example.kiwitexteditor.model.Photo;
import com.example.kiwitexteditor.model.PictureFacer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LibraryFragment extends BaseFragment<FragLibraryBinding,LibraryViewModel> {
    PopupMenu menu;
    ArrayList<ImageFolder> folds;
    ArrayList<PictureFacer> arrImage;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 121;
    @Override
    public Class<LibraryViewModel> getViewmodel() {
        return LibraryViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_library;
    }

    @Override
    public void setBindingViewmodel() {
        if(!checkPermissionForReadExtertalStorage()){
            requestPermissionForReadExtertalStorage();
        }
         binding.setViewmodel(viewmodel);
         menu = new PopupMenu(getContext(), binding.llFoder);
         folds = getPicturePaths();
         initRecyclerview();
    }

    private void initRecyclerview() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        binding.rvPhoto.setHasFixedSize(true);
        binding.rvPhoto.setLayoutManager(layoutManager);
        binding.rvPhoto.setAdapter(viewmodel.PhotoAdapter);
    }


    @Override
    public void ViewCreated() {
        binding.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LibraryFragment.this).navigateUp();
            }
        });

         getForderImage();
         // get default image
         arrImage = getAllImagesByFolder(folds.get(0).getPath());
         viewmodel.PhotoAdapter.setList(arrImage);
         binding.tvFoder.setText(folds.get(0).getFolderName());
         event();


    }
    private void event() {
        binding.llFoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ArrayList<PictureFacer> arr = getAllImagesByFolder(getPathForderByName(menuItem.getTitle().toString()));
                        viewmodel.PhotoAdapter.setList(arr);
                        binding.tvFoder.setText(menuItem.getTitle().toString());
                        return true;
                    }
                });
            }
        });
        viewmodel.PhotoAdapter.setCallback(new PhotoCallback() {
            @Override
            public void onPhotoClick(PictureFacer pictureFacer) {
                Bundle bundle = new Bundle();
                bundle.putString("uri",pictureFacer.getPicturePath());
                NavHostFragment.findNavController(LibraryFragment.this).navigate(R.id.action_navigationLibrary_to_navigationEdit,bundle);
            }
        });
    }
    private void getForderImage() {
        menu.getMenu().clear();
        for(ImageFolder i : folds){
           menu.getMenu().add(i.getFolderName());
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExtertalStorage(){
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    private ArrayList<ImageFolder> getPicturePaths(){
        ArrayList<ImageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = getContext().getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do{
                ImageFolder folds = new ImageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
                folderpaths = folderpaths+folder+"/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                }else{
                    for(int i = 0;i<picFolders.size();i++){
                        if(picFolders.get(i).getPath().equals(folderpaths)){
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i < picFolders.size();i++){
            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "+picFolders.get(i).getNumberOfPics());
        }
        return picFolders;
    }
    public ArrayList<PictureFacer> getAllImagesByFolder(String path){
        ArrayList<PictureFacer> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = getContext().getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                PictureFacer pic = new PictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }
    public String getPathForderByName(String name){
        for(ImageFolder i : folds){
            if(i.getFolderName().equals(name)){
                return i.getPath();
            }
        }
        return "null";
    }
}
