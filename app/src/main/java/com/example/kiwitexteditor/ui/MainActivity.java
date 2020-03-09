package com.example.kiwitexteditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kiwitexteditor.base.BaseActivity;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.callback.ActionbarListener;
import com.example.kiwitexteditor.databinding.ActivityMainBinding;
import com.example.kiwitexteditor.fragment.Fragment;
import com.example.kiwitexteditor.fragment.edit.EditFragment;
import com.example.kiwitexteditor.fragment.home.HomeFragment;
import com.example.kiwitexteditor.fragment.library.LibraryFragment;
import com.example.kiwitexteditor.ui.splash.main.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel>{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 121;
    @Override
    public Class<MainViewModel> getViewmodel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void setBindingViewmodel() {
        if(Build.VERSION.SDK_INT>23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        if (!checkPermissionForReadExtertalStorage()){
            requestPermissionForReadExtertalStorage();
        }
        event();
    }

    private void event() {

    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExtertalStorage(){
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
