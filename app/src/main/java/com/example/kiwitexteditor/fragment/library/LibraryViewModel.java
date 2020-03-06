package com.example.kiwitexteditor.fragment.library;

import androidx.lifecycle.MutableLiveData;

import com.example.kiwitexteditor.adapter.PhotoAdapter;
import com.example.kiwitexteditor.base.BaseViewmodel;
import com.example.kiwitexteditor.model.PictureFacer;

import java.util.ArrayList;

public class LibraryViewModel extends BaseViewmodel {
    private MutableLiveData<String> nameForderNow = new MutableLiveData<>();
    public PhotoAdapter PhotoAdapter = new PhotoAdapter();
    MutableLiveData<ArrayList<PictureFacer>> arrPhoto = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PictureFacer>> getArrPhoto(){
        return arrPhoto;
    }
    public void setPhoto(ArrayList<PictureFacer> arrayList){
        arrPhoto.postValue(arrayList);
    }
    public void setNameForder(String name){
        nameForderNow.postValue(name);
    }
    public MutableLiveData<String> getNameForderNow(){
        return nameForderNow;
    }
}
