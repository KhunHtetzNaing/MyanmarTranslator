package com.htetznaing.myanmartranslator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    WebView webView;
    EditText input;
    Typeface mm;
    Spinner spinnerFrom,spinnerTo;
    String from,to;
    String fromcountries = "auto af sq am ar hy az eu be bn bs bg ca ceb zh-CN zh-TW co hr cs da nl en eo et fi fr fy gl ka de el gu ht ha haw iw hi hmn hu is ig id ga it ja jw kn kk km ko ku ky lo la lv lt lb mk mg ms ml mt mi mr mn my ne no ny ps fa pl pt pa ro ru sm gd sr st sn sd si sk sl so es su sw sv tl tg ta te th tr uk ur uz vi cy xh yi yo zu";
    String fromcountryName[] = {"Auto detect","Afrikaans","Albanian","Amharic","Arabic","Armenian","Azeerbaijani","Basque","Belarusian","Bengali","Bosnian","Bulgarian","Catalan","Cebuano","Chinese (Simplified)","Chinese (Traditional)","Corsican","Croatian","Czech","Danish","Dutch","English","Esperanto","Estonian","Finnish","French","Frisian","Galician","Georgian","German","Greek","Gujarati","Haitian Creole","Hausa","Hawaiian","Hebrew","Hindi","Hmong","Hungarian","Icelandic","Igbo","Indonesian","Irish","Italian","Japanese","Javanese","Kannada","Kazakh","Khmer","Korean","Kurdish","Kyrgyz","Lao","Latin","Latvian","Lithuanian","Luxembourgish","Macedonian","Malagasy","Malay","Malayalam","Maltese","Maori","Marathi","Mongolian","Myanmar","Nepali","Norwegian","Nyanja (Chichewa)","Pashto","Persian","Polish","Portuguese","Punjabi","Romanian","Russian","Samoan","Scots Gaelic","Serbian","Sesotho","Shona","Sindhi","Sinhala (Sinhalese)","Slovak","Slovenian","Somali","Spanish","Sundanese","Swahili","Swedish","Tagalog (Filipino)","Tajik","Tamil","Telugu","Thai","Turkish","Ukrainian","Urdu","Uzbek","Vietnamese","Welsh","Xhosa","Yiddish","Yoruba","Zulu"};
    String fromcontryCode [];

    String tocountries = "my af sq am ar hy az eu be bn bs bg ca ceb zh-CN zh-TW co hr cs da nl en eo et fi fr fy gl ka de el gu ht ha haw iw hi hmn hu is ig id ga it ja jw kn kk km ko ku ky lo la lv lt lb mk mg ms ml mt mi mr mn ne no ny ps fa pl pt pa ro ru sm gd sr st sn sd si sk sl so es su sw sv tl tg ta te th tr uk ur uz vi cy xh yi yo zu";
    String tocountryName[] = {"Myanmar","Afrikaans","Albanian","Amharic","Arabic","Armenian","Azeerbaijani","Basque","Belarusian","Bengali","Bosnian","Bulgarian","Catalan","Cebuano","Chinese (Simplified)","Chinese (Traditional)","Corsican","Croatian","Czech","Danish","Dutch","English","Esperanto","Estonian","Finnish","French","Frisian","Galician","Georgian","German","Greek","Gujarati","Haitian Creole","Hausa","Hawaiian","Hebrew","Hindi","Hmong","Hungarian","Icelandic","Igbo","Indonesian","Irish","Italian","Japanese","Javanese","Kannada","Kazakh","Khmer","Korean","Kurdish","Kyrgyz","Lao","Latin","Latvian","Lithuanian","Luxembourgish","Macedonian","Malagasy","Malay","Malayalam","Maltese","Maori","Marathi","Mongolian","Nepali","Norwegian","Nyanja (Chichewa)","Pashto","Persian","Polish","Portuguese","Punjabi","Romanian","Russian","Samoan","Scots Gaelic","Serbian","Sesotho","Shona","Sindhi","Sinhala (Sinhalese)","Slovak","Slovenian","Somali","Spanish","Sundanese","Swahili","Swedish","Tagalog (Filipino)","Tajik","Tamil","Telugu","Thai","Turkish","Ukrainian","Urdu","Uzbek","Vietnamese","Welsh","Xhosa","Yiddish","Yoruba","Zulu"};
    String tocontryCode [];

    AdView banner;
    InterstitialAd interstitialAd;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mm = Typeface.createFromAsset(getAssets(),"mm.ttf");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Translating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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

        fromcontryCode = fromcountries.split(" ");
        tocontryCode = tocountries.split(" ");

        webView = (WebView) findViewById(R.id.webView);
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        input = (EditText) findViewById(R.id.Input);
        input.setTypeface(mm);

        spinFrom();
        spinTo();

        from = "auto";
        to = "my";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = input.getText().toString();
                if (!check.equals(null) && !check.isEmpty()){
                    if (isInternetOn()==true) {
                        progressDialog.show();
                        webView.loadUrl("https://translate.google.com/#" + from + "/" + to + "/" + new Yone().zg2uni(input.getText().toString()));
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("No Internet connection :(\nYou need to connect Internet!");
                        builder.setPositiveButton("OK",null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please input your text for translate :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(
                            "(function() { return ('<html>'+document.getElementsByTagName('body')[0].innerHTML+'</html>'); })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String html) {
                                    next(html);
                                }
                            });
                }
            }
        });
    }

    public void next(String html){
        if (html.contains("gt-tgt-txt")) {
            html = html.substring(html.lastIndexOf("gt-tgt-txt"),html.indexOf("class=\\\"list-placeholder"));
            html = html.substring(html.indexOf(">")+1);
            html = html.replace("\\u003C/div>\\u003C/div>\\u003C/div>\\u003C/div>\\u003Cdiv","");
            Log.d("Result", html);

            progressDialog.hide();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            builder.setTitle("Translated!");
            final View view = inflater.inflate(R.layout.box,null);
            EditText edt = view.findViewById(R.id.edt);
            final TextView textView = view.findViewById(R.id.tv);
            textView.setVisibility(View.VISIBLE);
            textView.setEnabled(true);
            edt.setVisibility(View.GONE);
            edt.setEnabled(false);

            textView.setText(new Yone().uni2zg(html));
            textView.setTypeface(mm);
            builder.setView(view);
            builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showAD();
                    ClipboardManager copy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    copy.setText(textView.getText());
                    if (copy.hasText()){
                        Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,textView.getText());
                    startActivity(Intent.createChooser(intent,"Send Text Via..."));
                }
            });
            builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    edit(textView.getText().toString());
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void edit(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        builder.setTitle("Edit Mode!");
        final View view = inflater.inflate(R.layout.box,null);
        final EditText edt = view.findViewById(R.id.edt);
        TextView textV = view.findViewById(R.id.tv);
        textV.setVisibility(View.GONE);
        textV.setEnabled(false);
        edt.setVisibility(View.VISIBLE);
        edt.setEnabled(true);
        edt.setText(text);
        edt.setTypeface(mm);
        builder.setView(view);
        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
                ClipboardManager copy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                copy.setText(edt.getText());
                if (copy.hasText()){
                    Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,About.class));
        }

        if (id == R.id.clear){
            input.setText("");
            Toast.makeText(this, "Clear!", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.paste){
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (clipboardManager.hasText()){
                input.setText(clipboardManager.getText());
                Toast.makeText(this, "Paste!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void spinFrom(){
        ArrayList<ItemData> list=new ArrayList<>();

        for (int i=0;i<fromcountryName.length;i++){
            list.add(new ItemData(fromcountryName[i]));
        }

        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinner_item,R.id.tv,list);
        spinnerFrom.setAdapter(adapter);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = fromcontryCode[i];
                try {
                    TextView textView = view.findViewById(R.id.tv);
                    textView.setTextColor(Color.WHITE);
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void spinTo(){
        ArrayList<ItemData> list=new ArrayList<>();
        for (int i=0;i<tocountryName.length;i++){
            list.add(new ItemData(tocountryName[i]));
        }

        SpinnerAdapter adapter=new SpinnerAdapter(this,
                R.layout.spinner_item2,R.id.tv,list);
        spinnerTo.setAdapter(adapter);
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = tocontryCode[i];
                try {
                    TextView textView = view.findViewById(R.id.tv);
                    textView.setTextColor(Color.WHITE);
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage("Do you want to Exit ?");
        builder.setIcon(R.drawable.ic);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showAD();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
