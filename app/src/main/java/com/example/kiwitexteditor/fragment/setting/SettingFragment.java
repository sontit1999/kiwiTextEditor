package com.example.kiwitexteditor.fragment.setting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseFragment;
import com.example.kiwitexteditor.databinding.FragSettingBinding;

public class SettingFragment extends BaseFragment<FragSettingBinding,SettingViewModel> {
    @Override
    public Class<SettingViewModel> getViewmodel() {
        return SettingViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_setting;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
    }

    @Override
    public void ViewCreated() {
         event();
    }

    private void event() {
        binding.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingFragment.this).navigateUp();
            }
        });
        binding.ContainerUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Bạn đang sử dụng phiên bản mới nhất rồi ^^", Toast.LENGTH_SHORT).show();
            }
        });
        binding.ContainerShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp("https://play.google.com/store/apps/details?id=com.gomin.photo.cut.paste.background.editor","Sơn Tít");
            }
        });
        binding.ContainerFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchMarket();
            }
        });
        binding.ContainerRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchMarket();
            }
        });
        binding.ContainerPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kiwi-text-editor.flycricket.io/privacy.html"));
                startActivity(browserIntent);
            }
        });
    }
    private void launchMarket() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.gomin.photo.cut.paste.background.editor");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
        }

    }
    private void shareApp(final String linkapp,final String subject){
        Intent intentInvite = new Intent(Intent.ACTION_SEND);
        intentInvite.setType("text/plain");
        String body = linkapp;
        intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentInvite.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intentInvite, "Share using"));
    }
}
