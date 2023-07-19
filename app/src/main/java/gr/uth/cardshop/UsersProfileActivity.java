package gr.uth.cardshop;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UsersProfileActivity extends AppCompatActivity {
    private TextView fullName,email,phone,verifyMsg,address;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private Button resendCode;
    private Button resetPassLocal, changeProfileImage;
    private FirebaseUser user;
    private ImageView profileImage;
    private StorageReference storageReference;
    private MaterialToolbar mToolbar;
    ValidationForm validationForm = new ValidationForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        phone =  findViewById(R.id.profilePhone);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);
        mToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userId = fAuth.getCurrentUser().getUid();//get current user id
        user = fAuth.getCurrentUser();
        if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"onFailure: Email not sent" + e.getMessage());
                        }
                    });
                }
            });
        }

        DocumentReference documentReference = fStore.collection("UserProfile").document(userId).collection("Information").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                phone.setText(documentSnapshot != null ? documentSnapshot.getString("phone") : null);
                fullName.setText(documentSnapshot != null ? documentSnapshot.getString("fName") : null);
                email.setText(documentSnapshot != null ? documentSnapshot.getString("email") : null);
            }
        });
        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPassword = new EditText(v.getContext());
                resetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password ");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        if (validationForm.isValidPassword(newPassword)) {
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UsersProfileActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UsersProfileActivity.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(UsersProfileActivity.this, "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one digit, one special character, and no whitespace characters", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                passwordResetDialog.create().show();
            }
        });
        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                intent.putExtra("fullName",fullName.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("phone",phone.getText().toString());
                startActivity(intent);
            }
        });

    }
}