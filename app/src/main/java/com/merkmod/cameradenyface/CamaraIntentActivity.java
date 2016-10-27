package com.merkmod.cameradenyface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CamaraIntentActivity extends Activity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_intent);

        mPhotoCapturedImageView = (ImageView) findViewById(R.id.capturePhotoImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camara_intent, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View view) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(CamaraIntentActivity.this,
                CamaraIntentActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile));
//        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            // Toast.makeText(this, "Picture taken successfully", Toast.LENGTH_SHORT).show();
            // Bundle extras = data.getExtras();
            // Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            // Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            // mPhotoCapturedImageView.setImageBitmap(photoCapturedBitmap);
            setReducedImageSize();

        }
    }

    File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }

    void setReducedImageSize() {
        int targetImageViewWidth = mPhotoCapturedImageView.getWidth();
        int targetImageViewHeight = mPhotoCapturedImageView.getHeight();


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        boolean facedeted = detectFaces(photoReducedSizeBitmp);
        if (facedeted) {
            Toast.makeText(CamaraIntentActivity.this, "Face detected", Toast.LENGTH_SHORT).show();
        } else {
            mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmp);
        }

    }

    public boolean detectFaces(Bitmap bitmap) {
        Bitmap convertedBitmap = convert(bitmap, Bitmap.Config.RGB_565);
        FaceDetector.Face face1;
        FaceDetector faceDetector = new FaceDetector(convertedBitmap.getWidth(), convertedBitmap.getHeight(), 10);
        FaceDetector.Face[] faces = new FaceDetector.Face[10];

        int face = faceDetector.findFaces(convertedBitmap, faces);


        for (int count = 0; count < face; count++) {
            if (count == 0) {
                face1 = faces[count];
                PointF midPoint1 = new PointF();
                face1.getMidPoint(midPoint1);

                float eyeDistance = face1.eyesDistance();

                float left1 = midPoint1.x - (float) (1.8 * eyeDistance);
                float right1 = midPoint1.x + (float) (1.4 * eyeDistance);
                float top1 = midPoint1.y - (float) (1.4 * eyeDistance);
                float bottom1 = midPoint1.y + (float) (1.8 * eyeDistance);

                Bitmap bmface = Bitmap.createBitmap(bitmap, (int) left1 + 5, (int) top1 + 5, (int) (2.8 * eyeDistance) + 5, (int) (3.6 * eyeDistance) + 5);

                if (bmface != null) {
                    return true;
                } else {
                    return false;
                }
            }

            if (count > 0) {

                face1 = faces[count];
                PointF midPoint1 = new PointF();
                face1.getMidPoint(midPoint1);

                float eyeDistance = face1.eyesDistance();

                float left1 = midPoint1.x - (float) (1.8 * eyeDistance);
                float right1 = midPoint1.x + (float) (1.4 * eyeDistance);
                float top1 = midPoint1.y - (float) (1.4 * eyeDistance);
                float bottom1 = midPoint1.y + (float) (1.8 * eyeDistance);

                Bitmap bmface = Bitmap.createBitmap(bitmap, (int) left1 + 5, (int) top1 + 5, (int) (2.8 * eyeDistance) + 5, (int) (3.6 * eyeDistance) + 5);

                if (bmface != null) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }
}
