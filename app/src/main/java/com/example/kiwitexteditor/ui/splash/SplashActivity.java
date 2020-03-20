package com.example.kiwitexteditor.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.kiwitexteditor.MainActivity;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseActivity;
import com.example.kiwitexteditor.databinding.ActivitySplashBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SplashActivity extends BaseActivity<ActivitySplashBinding,SplashViewModel> {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    public Class<SplashViewModel> getViewmodel() {
        return SplashViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                    // delay 3s and finish
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /* Create an Intent that will start the MainActivity. */
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            });

    }
}
