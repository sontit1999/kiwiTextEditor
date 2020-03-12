package com.example.kiwitexteditor.fragment.addtext;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.adapter.ColorPickerAdapter;

public class AddTextDialogFragment extends DialogFragment {
    AddTextListener listener;
    TextView tvDone;
    RecyclerView rvColor;
    EditText edtAdd;
    int colorcodeCurent = R.color.colorWhile;

    public AddTextDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_addtext_dialog,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // set fullcreen diaglog
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDone = (TextView) view.findViewById(R.id.tvDone);
        edtAdd = ( EditText) view.findViewById(R.id.edtAdd);
        rvColor = (RecyclerView) view.findViewById(R.id.rvPickcolor);
        initRecyclerview();
        initEvent();
    }

    private void initRecyclerview() {

        rvColor.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rvColor.setLayoutManager(manager);
        ColorPickerAdapter colorAdapter = new ColorPickerAdapter(getContext());
        rvColor.setAdapter(colorAdapter);
        colorAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                colorcodeCurent = colorCode;
                edtAdd.setTextColor(colorCode);
            }
        });

    }

    private void initEvent() {
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtAdd.getText().toString().trim();
                if(listener != null){
                    listener.addTextListener(text,colorcodeCurent);
                }
                dismiss();
            }
        });
    }
    public interface AddTextListener{
        void addTextListener(String text,int colorcode);
    }

    public void setListener(AddTextListener listener) {
        this.listener = listener;
    }
}
