package com.shatha;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Config.PostHttpHandler;
import moldel.infoPackeg;
import moldel.textpln;

import static android.content.ContentValues.TAG;

public class MainActivity<text> extends AppCompatActivity {
    private TextView txtresult;

    ArrayList<String> result = null;
    public static final String CALCULATOR_PACKAGE ="com.android.calculator2";
    public static final String CALCULATOR_CLASS ="com.android.calculator2.Calculat";
    ImageView mic;

    List<infoPackeg> infoPackegs;


    private Dialog dialog;
    private SpinKitView fb;
    private Button imageView3, imageView4;
    private static final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fill();
        setContentView(R.layout.activity_main);
        txtresult = (TextView) findViewById(R.id.txtview);

        mic = (ImageView) findViewById(R.id.imageView);
        //SpeakText.init(getApplicationContext());



//speech recognition
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }


    private void populateList() {


    }
    void doPermAudio()
    {
        int MY_PERMISSIONS_RECORD_AUDIO = 1;


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_RECORD_AUDIO);
            startVoiceInput();
        }
        else
        {
            startVoiceInput();
        }
    }


    @Override
    //result from the speech show it in textview
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == -1 && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // editText.setText(result.get(0));
                    Toast.makeText(getApplication(),result.get(0),Toast.LENGTH_LONG).show();
                    new GetText(result.get(0)).execute();
                }
                break;
            }
        }
    }

    public class GetText extends AsyncTask<Void,Void,String> {
        List<textpln> info_students=new ArrayList<>();
        String text;

        public GetText(String text) {
            this.text = text;
            waite();
        }

        @Override
        protected String doInBackground(Void... voids) {
           // String besUrl="http://192.168.1.111:8080/farasaApi/ApiNlp";
            String besUrl="http://192.168.1.103:8080/farasaApi/ApiNlp";
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("text",text);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            PostHttpHandler pst=new PostHttpHandler();
            String jsonStr2=pst.makeServiceCall(besUrl,jsonParam);


            try {
                if (jsonStr2 != null) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<textpln>>() {
                        }.getType();
                        info_students = gson.fromJson(jsonStr2, type);


                    } catch (final Exception e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());


                    }

                } else {
                    Log.e(TAG, "Couldn't get json from server.");



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(info_students.size()>0)
            {
                Toast.makeText(getApplication(),info_students.get(0).getWord(),Toast.LENGTH_LONG).show();
                for(textpln info:info_students)
                {
                    for(infoPackeg inf:infoPackegs)
                    {
                        if(inf.getWord().contains(info.getWord()))
                        {

                            try{

                                open(inf.getPakeg());

                            }catch (Exception e)
                            {}
                        }
                    }


                }





            }
            dialog.dismiss();
        }

    }
    public void fill()
    {
        infoPackegs=new ArrayList<>();
        infoPackegs.add(new infoPackeg("الكاميرا","0"));
        infoPackegs.add(new infoPackeg("الكاميرة","0"));
        infoPackegs.add(new infoPackeg("كيمرة","0"));
        infoPackegs.add(new infoPackeg("اتصال","1"));
        infoPackegs.add(new infoPackeg("سجل الهاتف","1"));
        infoPackegs.add(new infoPackeg("الرسائل","2"));
        infoPackegs.add(new infoPackeg("رساله","2"));
        infoPackegs.add(new infoPackeg("استديو","3"));
        infoPackegs.add(new infoPackeg("الصور","3"));
        infoPackegs.add(new infoPackeg("حاسبه","5"));

    }
    public void waite() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.prog_waite);
        fb = dialog.findViewById(R.id.progress_circular);
        imageView3 = dialog.findViewById(R.id.succeeded);
        imageView4 = dialog.findViewById(R.id.failed);
        dialog.show();


    }

    public void open(String no)
    {
        try {
            if(no.equals("0"))
            {
                Intent launchFacebookApplication = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.camera");

                startActivity(launchFacebookApplication);
            }else if(no.equals("1"))
            {

                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivity(i);

            }
            else if(no.equals("2"))
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
            else if(no.equals("3"))
            {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(no.equals("4"))
            {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(no.equals("5"))
            {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(
                        CALCULATOR_PACKAGE,
                        CALCULATOR_CLASS));
startActivity(intent);
            }

            else if(no.equals("5"))
            {
                String url = CompanyWeb.getText().toString().trim();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }






        }catch (Exception e)
        {

        }

    }}
