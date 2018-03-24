package com.example.rajita.codefundo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Notes extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    TextView mVoiceInputTv;
    ImageButton mSpeakBtn;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        t2 = (TextView) findViewById(R.id.t2);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, Please start recording your notes!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0 ));
                    StringBuilder sb = new StringBuilder();
                    for(String str : result){
                        sb.append(str).append("");
                        break;

                        //separating contents using semi colon
                    }

                    String mystring = sb.toString();
                    writeToSDFile(mystring);
                }
                break;
            }

        }
    }

    private void writeToSDFile(String speechToTextData) {

       String content = speechToTextData;
        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "MyNotes.txt");
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        /*File root = android.os.Environment.getDownloadCacheDirectory();

        File dir = new File(root.getAbsolutePath(), "/folder");;
        dir.mkdirs();
        File file = new File(dir, "Notes.txt");
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(speechToTextData);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "plain/text");
            startActivity(intent);
        } catch(Exception ex) {
            Log.e("tag", "No file browser installed. " + ex.getMessage());
        }
    }*/
}
