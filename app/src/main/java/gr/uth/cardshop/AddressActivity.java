package gr.uth.cardshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gr.uth.cardshop.adapter.AddressAdapter;
import gr.uth.cardshop.domain.Address;
import gr.uth.cardshop.domain.Items;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress, AddressAdapter.AddressRemoved {
    private RecyclerView mAddressRecyclerView;
    private AddressAdapter mAddressAdapter;
    private Button paymentBtn;
    private Button mAddAddress;
    private List<Address> mAddressList;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private MaterialToolbar mToolbar;
    private TextView emptyMsg;
    private String address = "";
    private String fullName = "";
    private String Email = "";
    private String Phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        List<Items> itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemList");
        mAddressRecyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        mAddAddress = findViewById(R.id.add_address_btn);
        emptyMsg = findViewById(R.id.empty_msg_address);
        mToolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mAddressList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(getApplicationContext(), mAddressList,this, this);
        mAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAddressRecyclerView.setAdapter(mAddressAdapter);

        mStore.collection("UserProfile").document(mAuth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //if fetching data from firebase and is not empty string then
                            if (!task.getResult().isEmpty()) {
                                if (!address.isEmpty()) {
                                    for (DocumentSnapshot doc : task.getResult()) {
                                        Address address = doc.toObject(Address.class);
                                        mAddressList.add(address);
                                        mAddressAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                                        String documentId = doc.getDocument().getId();
                                        Address address = doc.getDocument().toObject(Address.class);
                                        address.setDocId(documentId);
                                        mAddressList.add(address);
                                        mAddressAdapter.notifyDataSetChanged();
                                    }
                                }
                            }else{
                                emptyMsg.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        //fetching data from firebase//
        DocumentReference documentReference = mStore.collection("UserProfile").document(mAuth.getCurrentUser().getUid())
                .collection("Information").document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    fullName = documentSnapshot.getString("fName");
                    Email = documentSnapshot.getString("email");
                    Phone = documentSnapshot.getString("phone");
                    //Toast.makeText(AddressActivity.this, "fName "+fullName+"email "+Email+"Phone "+Phone, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddressActivity.this, "not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
                startActivity(intent);
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!address.isEmpty()) {
                    //Toast.makeText(AddressActivity.this, "Your address: "+address, Toast.LENGTH_SHORT).show();
                    if (itemsList != null && itemsList.size() > 0) {
                        Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                        intent.putExtra("itemsList", (Serializable) itemsList);
                        intent.putExtra("fName", fullName);
                        intent.putExtra("email", Email);
                        intent.putExtra("phone", Phone);
                        intent.putExtra("address", address);
                        //Toast.makeText(AddressActivity.this, ""+itemsList.get(1).getProductId(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(AddressActivity.this, "fName "+fullName+"email "+Email+"Phone "+Phone, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(AddressActivity.this, "Please add an address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setAddress(String s) {
        address = s;
    }

    @Override
    public void onAddressRemoved(List<Address> addressList) {

    }
}