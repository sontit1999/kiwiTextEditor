package com.example.kiwitexteditor.fragment.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.adapter.EditingToolsAdapter;
import com.example.kiwitexteditor.adapter.filter.FilterListener;
import com.example.kiwitexteditor.adapter.filter.FilterViewAdapter;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.databinding.FragEditBinding;
import com.example.kiwitexteditor.fragment.addtext.AddTextDialogFragment;
import com.example.kiwitexteditor.fragment.bottomsheet.BottomSheetAddImage;
import com.example.kiwitexteditor.fragment.bottomsheet.BottomSheetEmoji;
import com.example.kiwitexteditor.fragment.bottomsheet.PropertiesBSFragment;
import com.example.kiwitexteditor.fragment.home.HomeFragment;
import com.example.kiwitexteditor.fragment.library.LibraryFragment;
import com.example.kiwitexteditor.model.ToolType;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class EditFragment extends BaseFragment<FragEditBinding,EditViewModel> implements EditingToolsAdapter.OnItemSelected, FilterListener {
    NavController navController;
    PhotoEditor mPhotoEditor;
    PropertiesBSFragment mPropertiesBSFragment;
    BottomSheetEmoji bottomSheetEmoji;
    AddTextDialogFragment addTextDialogFragment;
    BottomSheetAddImage bottomSheetAddImage;
    String urlImage ;
    boolean statusClickSave = false;
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
         navController = NavHostFragment.findNavController(EditFragment.this);
    }
    @Override
    public void ViewCreated() {
        initEditor();
        initview();
        initEvent();
       // binding.ivPhoto.getSource().setImageURI(Uri.parse(urlImage));
        Glide.with(getContext()).load(urlImage).into(binding.ivPhoto.getSource());
    }

    private void initview() {
        // init bottom sheet properties
        mPropertiesBSFragment = new PropertiesBSFragment(mPhotoEditor);
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
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),6,RecyclerView.VERTICAL,false);
        binding.rvTool.setLayoutManager(manager);
        binding.rvTool.setAdapter(new EditingToolsAdapter(this));
        // init recyclerview Filter
        binding.rvFilter.setHasFixedSize(true);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        binding.rvFilter.setLayoutManager(manager1);
        binding.rvFilter.setAdapter(new FilterViewAdapter(this));
        // init bottomsheet Emoji
        bottomSheetEmoji = new BottomSheetEmoji();
        bottomSheetEmoji.setEmojiBottomSheetListener(new BottomSheetEmoji.EmojiBottomSheetListener() {
            @Override
            public void onEmojiClick(String emojiCode) {
                mPhotoEditor.addEmoji(emojiCode);
            }
        });


        // init addtextDialog
        addTextDialogFragment = new AddTextDialogFragment();
        addTextDialogFragment.setListener(new AddTextDialogFragment.AddTextListener() {
            @Override
            public void addTextListener(String text, int colorcode) {
                mPhotoEditor.addText(text,colorcode);
            }
        });

        bottomSheetAddImage = new BottomSheetAddImage();
        bottomSheetAddImage.setStickerListener(new BottomSheetAddImage.StickerListener() {
            @Override
            public void onClickImage(Bitmap imageResource) {
                mPhotoEditor.addImage(imageResource);
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
                if (navController.getCurrentDestination().getId() == R.id.navigationEdit){
                    navController.navigate(R.id.action_navigationEdit_to_navigationHome);
                }
            }
        });
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                   if(!statusClickSave){
                       binding.spinKit.setVisibility(View.VISIBLE);
                       File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto");
                       directory.mkdir();
                       String fileName = String.format("%d.jpg", System.currentTimeMillis());
                       String pathImageSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextOnPhoto/" + fileName;
                       mPhotoEditor.saveAsFile(pathImageSaved, new PhotoEditor.OnSaveListener() {
                           @Override
                           public void onSuccess(@NonNull String imagePath) {
                               binding.spinKit.setVisibility(View.GONE);
                               // update gallery
                               Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                               intent.setData(Uri.fromFile(new File(imagePath)));
                               getActivity().sendBroadcast(intent);
                               // tạo bundle r gửi path image
                               Bundle bundle = new Bundle();
                               bundle.putString("uri",imagePath);
                               NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_navigationEdit_to_navigationSave,bundle);
                           }

                           @Override
                           public void onFailure(@NonNull Exception exception) {
                               binding.spinKit.setVisibility(View.GONE);
                               Snackbar.make(view,"Failed to save Image",Snackbar.LENGTH_LONG).show();
                           }
                       });
                       statusClickSave = true;
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                                statusClickSave = false;
                           }
                       },3000);
                   }
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
                mPropertiesBSFragment.show(getFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                addTextDialogFragment.show(getFragmentManager(),addTextDialogFragment.getTag());
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                break;
            case FILTER:
//                hiddenUndo();
                hiddenRvTool();
                showFilter();
                break;
            case EMOJI:
                bottomSheetEmoji.show(getFragmentManager(),bottomSheetEmoji.getTag());
                break;
            case STICKER:
                bottomSheetAddImage.show(getFragmentManager(),bottomSheetAddImage.getTag());
                break;
        }

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
        hiddenFilter();
        showRvTool();
        showwUndo();
    }
    public void showFilter(){
        binding.rvFilter.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.backfragment);
        binding.rvFilter.startAnimation(animation);
    }
    public void hiddenFilter(){
        binding.rvFilter.setVisibility(View.GONE);
    }
    public void showRvTool(){
        binding.rvTool.setVisibility(View.VISIBLE);
    }
    public void hiddenRvTool(){
        binding.rvTool.setVisibility(View.INVISIBLE);
    }
    public void hiddenUndo(){
        binding.ivRedo.setVisibility(View.INVISIBLE);
        binding.ivUndo.setVisibility(View.INVISIBLE);
    }
    public void showwUndo(){
        binding.ivRedo.setVisibility(View.VISIBLE);
        binding.ivUndo.setVisibility(View.VISIBLE);
    }
    public static ArrayList<Bitmap> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        ArrayList<Bitmap> arrbitmap = new ArrayList<>();
        for(String i : listOfAllImages){
            Bitmap bitmap = getBitmap(i);
            arrbitmap.add(bitmap);
        }
        return arrbitmap;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }

}
