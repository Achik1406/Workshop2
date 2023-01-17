package com.example.goldendreamsbowling.LoggedInUser;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.goldendreamsbowling.AboutUsFragment;
import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityAccountBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class Account extends Drawer_base{

    static String Pref_Name = "MyFile";

    ActivityAccountBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth ID;
    String UID;
    StorageReference storageReference;
    Uri imageUri;
    StorageTask uploadTask;
    String murl;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference().child("profile");
        SharedPreferences sharedPreferences = getSharedPreferences(Account.Pref_Name, 0);
        boolean hasChangeProfile = sharedPreferences.getBoolean("hasChangeProfile", false);

        binding.chgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, ForgotPassword.class));
                finish();
            }
        });

        if (hasChangeProfile) {
            databaseReference.child("profileUser").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        final String dataImg = snapshot.child("image").getValue().toString();
                        Glide.with(getApplicationContext()).load(dataImg).into(binding.img);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String dataName = snapshot.child("fullname").getValue().toString();
                final String dataEmail = snapshot.child("email").getValue().toString();
                final String dataPhone = snapshot.child("phone").getValue().toString();
                binding.fullName.setText(dataName);
                binding.Email.setText(dataEmail);
                binding.noPhone.setText(dataPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().goOnline();


        binding.fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.linEdtName.setVisibility(View.VISIBLE);
                binding.linEdtPhone.setVisibility(View.GONE);
                binding.linEdtUsername.setVisibility(View.GONE);
                binding.linEdtEmail.setVisibility(View.GONE);
                binding.txtSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String NewName = binding.updateName.getText().toString();
                        if(NewName.isEmpty())
                        {
                            Toast.makeText(Account.this,"Please Enter Name",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            databaseReference.child("users").child(UID).child("fullname").setValue(NewName);
                            binding.fullName.setText(NewName);
                            binding.linEdtName.setVisibility(View.GONE);
                        }

                    }
                });
                binding.txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.linEdtName.setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.linEdtEmail.setVisibility(View.VISIBLE);
                binding.linEdtPhone.setVisibility(View.GONE);
                binding.linEdtName.setVisibility(View.GONE);
                binding.linEdtUsername.setVisibility(View.GONE);
                binding.txtSave2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String NewEmail = binding.updateEmail.getText().toString();
                        if(!Patterns.EMAIL_ADDRESS.matcher(NewEmail).matches())
                        {
                            binding.updateEmail.setError("Please provide correct Email");
                            binding.updateEmail.requestFocus();
                            return;
                        }
                        else
                        {
                            databaseReference.child("users").child(UID).child("email").setValue(NewEmail);
                            binding.Email.setText(NewEmail);
                            binding.linEdtEmail.setVisibility(View.GONE);
                        }

                    }
                });
                binding.txtCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.linEdtEmail.setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.noPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.linEdtPhone.setVisibility(View.VISIBLE);
                binding.linEdtUsername.setVisibility(View.GONE);
                binding.linEdtName.setVisibility(View.GONE);
                binding.linEdtEmail.setVisibility(View.GONE);
                binding.txtSave3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String NewPhone = binding.updatePhone.getText().toString();
                       if(!Patterns.PHONE.matcher(NewPhone).matches())
                        {
                            binding.updatePhone.setError("invalid phone number");
                            binding.updatePhone.requestFocus();
                        }
                       else
                       {
                           databaseReference.child("users").child(UID).child("phone").setValue(NewPhone);
                           binding.noPhone.setText(NewPhone);
                           binding.linEdtPhone.setVisibility(View.GONE);
                       }

                    }
                });
                binding.txtCancel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.linEdtPhone.setVisibility(View.GONE);
                    }
                });
            }
        });

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        PickFromGallery();
                    }
                } else {
                    PickFromGallery();
                }

            }
        });



    }

    private void UploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Profile ");
        progressDialog.setMessage("Please wait,while we setting up your profile");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference reference = storageReference.child(UID + ".jpg");
            uploadTask = reference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Uri uri = (Uri) task.getResult();
                    murl = uri.toString();
                    databaseReference.child("profileUser").child(UID).child("image").setValue(murl);
                    binding.img.setImageURI(imageUri);
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel("GoldenDream","GoldenDream",NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Account.this,"GoldenDream");
                    builder.setSmallIcon(R.drawable.logo);
                    builder.setContentTitle("Profile");
                    builder.setContentText("Profile Change Success");
                    builder.setAutoCancel(true);
                    NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(Account.this);
                    notificationManagerCompat.notify(1,builder.build());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload Successful ", Toast.LENGTH_LONG);

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Upload failed. Please try again! ", Toast.LENGTH_LONG);
            progressDialog.dismiss();
        }
    }

    private void PickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickFromGallery();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();

        SharedPreferences sharedPreferences = getSharedPreferences(Pref_Name, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasChangeProfile", true);
        editor.commit();
        UploadImage();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Account.this, HomePage.class));
        finish();
    }

}