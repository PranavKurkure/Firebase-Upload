package com.example.nerdupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Button ch,up;
    CircleImageView img;
    StorageReference mStorageRef;
    DatabaseReference dbreff;
    public Uri imguri;
    ProgressDialog progress;
    private StorageTask uploadTask;

    EditText txtname,txtclg,txtphone;
    Datas datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        dbreff = FirebaseDatabase.getInstance().getReference().child("Datas");

        txtname = findViewById(R.id.txtname);
        txtclg = findViewById(R.id.txtclg);
        txtphone = findViewById(R.id.txtphone);
        datas = new Datas();
        ch = findViewById(R.id.btnchoose);
        up = findViewById(R.id.btnupload);
        img = findViewById(R.id.imgview);
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtname.length() == 0){
                    txtname.setError("Please enter any name for image!");
                }
                else{
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(MainActivity.this, "Upload in Progress!", Toast.LENGTH_SHORT).show();

                    }else {
                        progress = new ProgressDialog(MainActivity.this);
                        progress.setMessage("Loading...");
                        progress.setTitle("Upload in progress!");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.show(); // Display Progress Dialog
                        progress.setCancelable(false);
                        Fileuploader();
                    }
                }

            }
        });
    }

    private String getExtension(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }

    private void Fileuploader() {
        String imageid;
        imageid = System.currentTimeMillis()+ "."+getExtension(imguri);
        datas.setName(txtname.getText().toString().trim());
        datas.setCollegeName(txtclg.getText().toString().trim());
        datas.setPhone(txtphone.getText().toString().trim());
        datas.setImageid(imageid);
        dbreff.push().setValue(datas);

        StorageReference Ref = mStorageRef.child(imageid);

        uploadTask = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        progress.dismiss();
                        Toast.makeText(MainActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        txtname.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }



    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent ,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!= null)
        {
                imguri = data.getData();
                img.setImageURI(imguri);
        }
    }
}
