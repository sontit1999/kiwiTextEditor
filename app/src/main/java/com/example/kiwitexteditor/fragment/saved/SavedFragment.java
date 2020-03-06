package com.example.kiwitexteditor.fragment.saved;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.callback.PhotoCallback;
import com.example.kiwitexteditor.databinding.FragSavedBinding;
import com.example.kiwitexteditor.fragment.library.LibraryFragment;
import com.example.kiwitexteditor.model.PictureFacer;

import java.io.File;
import java.util.ArrayList;

public class SavedFragment extends BaseFragment<FragSavedBinding,SavedViewModel> {
    ArrayList<PictureFacer> arrImage;
    @Override
    public Class<SavedViewModel> getViewmodel() {
        return SavedViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_saved;
    }

    @Override
    public void setBindingViewmodel() {
           binding.setViewmodel(viewmodel);
           binding.ivLeft.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   NavHostFragment.findNavController(SavedFragment.this).navigateUp();
               }
           });
           binding.btnEditPhoto.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   NavHostFragment.findNavController(SavedFragment.this).navigate(R.id.action_navigationSaved_to_navigationLibrary);
               }
           });
           initRecyclerview();
    }

    private void initRecyclerview() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.rvPhotos.setHasFixedSize(true);
        binding.rvPhotos.setLayoutManager(layoutManager);
        binding.rvPhotos.setAdapter(viewmodel.PhotoAdapter);
    }

    @Override
    public void ViewCreated() {
         arrImage = getAllImagesByFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto");
         viewmodel.PhotoAdapter.setList(arrImage);
         viewmodel.PhotoAdapter.setCallback(new PhotoCallback() {
             @Override
             public void onPhotoClick(PictureFacer pictureFacer) {

             }
         });
         if(viewmodel.PhotoAdapter.getItemCount() > 0){
             binding.tvNoImgae.setVisibility(View.GONE);
             binding.IvnoImage.setVisibility(View.GONE);
         }
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
}
