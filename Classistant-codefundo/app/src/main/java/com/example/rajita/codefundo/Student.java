package com.example.rajita.codefundo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.AddPersistedFaceResult;
import com.microsoft.projectoxford.face.contract.CreatePersonResult;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceList;
import com.microsoft.projectoxford.face.contract.FaceListMetadata;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.GroupResult;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.contract.PersonFace;
import com.microsoft.projectoxford.face.contract.PersonGroup;
import com.microsoft.projectoxford.face.contract.SimilarFace;
import com.microsoft.projectoxford.face.contract.SimilarPersistedFace;
import com.microsoft.projectoxford.face.contract.TrainingStatus;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static java.lang.String.format;

public class Student extends Activity {

    Button button, btnIdentify;
    ImageView imageView;
    TextView text;
    Bitmap bitmap;
    Face[] facesDetected;
    private FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://southeastasia.api.cognitive.microsoft.com/face/v1.0","668fa7e318f941be857dbf0cfeaaa8c8");
    private final String personGroupId = "hollywoodstar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        button = (Button) findViewById(R.id.button);
        btnIdentify = (Button) findViewById(R.id.button2);
        imageView = (ImageView) findViewById(R.id.image_view);
        text = (TextView) findViewById(R.id.tt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, 0);
            }
        });
        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UUID[] faceIds = new UUID[facesDetected.length];
                for(int i=0;i<facesDetected.length;i++){
                    faceIds[i] = facesDetected[i].faceId;
                }
            }
        });

    }

    private void detectAndFrame(final Bitmap myBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        @SuppressLint("StaticFieldLeak") AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {

            private ProgressDialog progress = new ProgressDialog(Student.this);

            @Override
            protected void onPostExecute(Face[] faces) {
                progress.dismiss();
                if(faces == null) {
                    return;
                }
                text.setText(String.format("Detection finished. %d face(s) detected", faces.length));
                imageView.setImageBitmap(drawFaceRectangleOnBitmap(myBitmap, faces, "not found in database"));
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                progress.setMessage(values[0]);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected Face[] doInBackground(InputStream... params) {
                publishProgress("Detecting...");
                try {
                    Log.d("tag","Hi");
                    Face[] result = faceServiceClient.detect(params[0], true, false, null);
                    if(result == null) {
                        publishProgress("Detection finished. Nothing Deteted");
                        return null;
                    }
                    publishProgress(String.format("Detection finished. %d face(s) detected", result.length));
                    Log.d("tag", "Hello");
                    return result;
                } catch (Exception e) {
                    publishProgress("Detection failed!!");
                    e.printStackTrace();
                    return null;
                }
            }
        };
        detectTask.execute(inputStream);
    }

    private Bitmap drawFaceRectangleOnBitmap(Bitmap mBitmap, Face[] facesDetected, String name) {

        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(12);

        if(facesDetected != null)
        {
            for(Face face:facesDetected)
            {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left+faceRectangle.width,
                        faceRectangle.top+faceRectangle.height,
                        paint);
                text.append(String.format("\nName : %s", name));
            }
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        detectAndFrame(bitmap);
    }
}
