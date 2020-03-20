package com.example.kiwitexteditor.fragment.save;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.databinding.FragSaveBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaveFragment extends BaseFragment<FragSaveBinding,SaveViewModel> {
    String urlImage = "";
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    @Override
    public Class<SaveViewModel> getViewmodel() {
        return SaveViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_save;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle bundle =  getArguments();
        if(bundle != null){
            urlImage = bundle.getString("uri");
        }

    }

    @Override
    public void setBindingViewmodel() {
        // load ad
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        StrictMode.VmPolicy.Builder  builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        checkpermisionns();
        binding.setViewmodel(viewmodel);
        binding.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteImage(urlImage)){
                    Snackbar.make(view,"Image not save to gallery",Snackbar.LENGTH_LONG).show();
                };
                NavHostFragment.findNavController(SaveFragment.this).navigateUp();
            }
        });
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Image save succesfully",Snackbar.LENGTH_LONG).show();
                NavHostFragment.findNavController(SaveFragment.this).navigate(R.id.action_navigationSave_to_navigationHome);
            }
        });
        binding.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
    }

    @Override
    public void ViewCreated() {
       // Glide.with(getContext()).load(urlImage).into(binding.ivSaveImage);
        Glide.with(getContext()).load(urlImage).into(binding.ivSaveImage.getSource());
    }
    private void shareImage() {
        bitmapDrawable = (BitmapDrawable) binding.ivSaveImage.getSource().getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }
    private void checkpermisionns() {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
        }
    }
    public boolean deleteImage(String pathImage){
        File fdelete = new File(pathImage);

        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.d("test","Đã xóa");
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(new File(pathImage)));
                getActivity().sendBroadcast(intent);
                return true;
            } else {
                Log.d("test","Chưa xóa");
                return false;
            }
        }
        return false;
    }
    public void saveImageFromImageView(ImageView imageView){
        // get bitmap from imageview
        BitmapDrawable Drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = Drawable.getBitmap();
        // create array contain image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        // create file empty
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto");
        directory.mkdir();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        String pathImageSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto/" + fileName;
        File f = new File(pathImageSaved);
        try {
            f.createNewFile();
            // write image to file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
