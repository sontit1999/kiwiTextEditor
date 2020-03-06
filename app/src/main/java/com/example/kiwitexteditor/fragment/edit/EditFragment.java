package com.example.kiwitexteditor.fragment.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.databinding.FragEditBinding;
import com.example.kiwitexteditor.fragment.library.LibraryFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFragment extends BaseFragment<FragEditBinding,EditViewModel> {
    String urlImage ;
    @Override
    public Class<EditViewModel> getViewmodel() {
        return EditViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_edit;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle bundle =  getArguments();
        if(bundle != null && bundle.get("uri")!= null){
            urlImage = bundle.getString("uri");
        }

    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
         binding.ivHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationHome);
             }
         });
         binding.tvSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("uri",urlImage);
                 NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationSave,bundle);
             }
         });
    }

    @Override
    public void ViewCreated() {
        Glide.with(getContext()).load(urlImage).into(binding.ivPhoto);
    }
}
