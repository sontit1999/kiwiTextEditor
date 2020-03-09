package com.example.kiwitexteditor.fragment.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.adapter.EditingToolsAdapter;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.databinding.FragEditBinding;
import com.example.kiwitexteditor.fragment.bottomsheet.BottomSheetEmoji;
import com.example.kiwitexteditor.fragment.bottomsheet.PropertiesBSFragment;
import com.example.kiwitexteditor.fragment.library.LibraryFragment;
import com.example.kiwitexteditor.model.ToolType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class EditFragment extends BaseFragment<FragEditBinding,EditViewModel> implements EditingToolsAdapter.OnItemSelected {
    PhotoEditor mPhotoEditor;
    PropertiesBSFragment mPropertiesBSFragment;
    BottomSheetEmoji bottomSheetEmoji;
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

    }

    @Override
    public void ViewCreated() {
        initview();
        initEditor();
        initEvent();
       // binding.ivPhoto.getSource().setImageURI(Uri.parse(urlImage));
        Glide.with(getContext()).load(urlImage).into(binding.ivPhoto.getSource());
    }

    private void initview() {
        // init bottom sheet properties
        mPropertiesBSFragment = new PropertiesBSFragment();
        mPropertiesBSFragment.setPropertiesChangeListener(new PropertiesBSFragment.Properties() {
            @Override
            public void onColorChanged(int colorCode) {
                mPhotoEditor.setBrushColor(colorCode);
            }

            @Override
            public void onOpacityChanged(int opacity) {
               mPhotoEditor.setOpacity(opacity);
            }

            @Override
            public void onBrushSizeChanged(int brushSize) {
                mPhotoEditor.setBrushSize(brushSize);
            }
        });
        // init recyclerview Tool
        binding.rvTool.setHasFixedSize(true);
        binding.rvTool.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        binding.rvTool.setAdapter(new EditingToolsAdapter(this));
        // init bottomsheet Emoji
        bottomSheetEmoji = new BottomSheetEmoji();
        bottomSheetEmoji.setEmojiBottomSheetListener(new BottomSheetEmoji.EmojiBottomSheetListener() {
            @Override
            public void onEmojiClick(String emojiCode) {
                mPhotoEditor.addEmoji(emojiCode);
            }
        });
    }


    private void initEvent() {
        binding.ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.undo();
            }
        });
        binding.ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.redo();
            }
        });
        binding.ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationHome);
            }
        });
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                String pathImageSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto/" + fileName;
                mPhotoEditor.saveAsFile(pathImageSaved, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                       // Snackbar.make(view,"Image Saved Successfully at :" + imagePath,Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(new File(imagePath)));
                        getActivity().sendBroadcast(intent);
                        Bundle bundle = new Bundle();
                        bundle.putString("uri",imagePath);
                        NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationSave,bundle);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Snackbar.make(view,"Failed to save Image",Snackbar.LENGTH_LONG).show();
                    }
                });

//                 Bundle bundle = new Bundle();
//                 bundle.putString("uri",urlImage);
//                 NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationSave,bundle);
            }
        });
    }

    private void initEditor() {
        mPhotoEditor = new PhotoEditor.Builder(getContext(), binding.ivPhoto)
                .setPinchTextScalable(true)
//                .setDefaultTextTypeface(mTextRobotoTf)
//                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();

    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType){
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mPhotoEditor.setBrushSize(20);
                mPhotoEditor.setBrushColor(getResources().getColor(R.color.yellow_color_picker));
                mPropertiesBSFragment.show(getFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                Toast.makeText(getActivity(), " text Active", Toast.LENGTH_SHORT).show();
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                break;
            case FILTER:
                mPhotoEditor.setFilterEffect(PhotoFilter.CONTRAST);
                Toast.makeText(getActivity(), "Filter Active", Toast.LENGTH_SHORT).show();
                break;
            case EMOJI:
                bottomSheetEmoji.show(getFragmentManager(),bottomSheetEmoji.getTag());
                Toast.makeText(getActivity(), "Emoji Active", Toast.LENGTH_SHORT).show();
                break;
            case STICKER:
                Toast.makeText(getActivity(), "Sticker Active", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
