package academy.learnprogramming.newtest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import academy.learnprogramming.newtest.ApiUrl.ApiInterface;
import academy.learnprogramming.newtest.ApiUrl.ApiUrl;
import academy.learnprogramming.newtest.response.ImageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button,btn;
    String userChoosenTask,encodedImage ;
    ProgressBar progressBar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.img);
        button = findViewById(R.id.btn);
        btn= findViewById(R.id.upload);
        progressBar = findViewById(R.id.progress);
        editText = findViewById(R.id.text);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       selectImage();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1;
                s1=editText.getText().toString();
                 progressBar.setVisibility(View.VISIBLE);
                Retrofit retrofit=new Retrofit.Builder()
                                .baseUrl(ApiUrl.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiInterface apiService=retrofit.create(ApiInterface.class);

                        Call<ImageResponse> call=apiService.image(encodedImage,s1);
                        call.enqueue(new Callback<ImageResponse>() {
                            @Override
                            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                 progressBar.setVisibility(View.GONE);
                                if (response.body().getSuccess().equalsIgnoreCase("yes")){
                                    Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                                } else{
                                       Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ImageResponse> call, Throwable t) {
                                     Toast.makeText(MainActivity.this, "shit", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });










    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }


            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
        imageView.setImageBitmap(bm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
         encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);






    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            imageView.setImageBitmap(thumbnail);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
             encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }


    





}