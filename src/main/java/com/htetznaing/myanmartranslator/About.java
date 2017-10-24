package com.htetznaing.myanmartranslator;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class About extends AppCompatActivity {
    AdView banner;
    AdRequest adRequest;
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        adRequest = new AdRequest.Builder().build();
        banner = (AdView) findViewById(R.id.adView);
        banner.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-4173348573252986/3048681542");
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadAD();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                loadAD();
            }

            @Override
            public void onAdOpened() {
                loadAD();
            }
        });
    }

    public void dev(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dev,null);

        ImageView fb,git,msg;
        fb = view.findViewById(R.id.fb);
        git = view.findViewById(R.id.git);
        msg = view.findViewById(R.id.msg);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAD();
                try {
                    Intent fsendIntent = new Intent();
                    fsendIntent.setAction(Intent.ACTION_VIEW);
                    fsendIntent.setData(Uri.parse("fb://profile/100011339710114"));
                    startActivity(fsendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Intent fsendIntent = new Intent();
                    fsendIntent.setAction(Intent.ACTION_VIEW);
                    fsendIntent.setData(Uri.parse("https://m.facebook.com/KHtetzNaing"));
                    startActivity(fsendIntent);
                }
            }
        });

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAD();
                Intent git = new Intent(Intent.ACTION_VIEW);
                git.setData(Uri.parse("https://github.com/KhunHtetzNaing/MyanmarTranslator"));
                startActivity(git);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAD();
                Uri uri = Uri.parse("fb-messenger://user/");
                uri = ContentUris.withAppendedId(uri, Long.parseLong("100011339710114"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Developer!");
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void faq(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.faq,null);
        TextView textView = view.findViewById(R.id.text);
        textView.setTypeface(Typeface.createFromAsset(getAssets(),"mm.ttf"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("FAQ!");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void loadAD(){
        if (!interstitialAd.isLoaded()){
            interstitialAd.loadAd(adRequest);
        }
    }

    public void showAD(){
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }else{
            interstitialAd.loadAd(adRequest);
        }
    }

    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Myanmar Translator for Zawgyi Font Users!\nDownload free at Google Play Store : https://play.google.com/store/apps/details?id="+getPackageName());
        startActivity(Intent.createChooser(intent,"Share App Via..."));
    }
}
