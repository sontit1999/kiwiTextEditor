package com.example.kiwitexteditor.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private String name;
//    private String firstImageContainedPath;

    public Bucket(String name) {
        this.name = name;
       // this.firstImageContainedPath = firstImageContainedPath;
    }

    public String getName() {
        return name;
    }

//    public String getFirstImageContainedPath() {
//        return firstImageContainedPath;
//    }

}
