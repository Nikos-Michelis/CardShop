package gr.uth.cardshop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stripe.android.PaymentIntentResult;

import java.util.ArrayList;
import java.util.List;

import gr.uth.cardshop.adapter.ordersHistoryRecyclerAdapter;
import gr.uth.cardshop.domain.Orders;

public class OrderHistoryActivity extends AppCompatActivity{
    private MaterialToolbar mToolbar;
    private TextView emptyMsg;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private List<Orders> ordersList;
    private RecyclerView orderRecyclerView;
    private ordersHistoryRecyclerAdapter ordersHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ordersList = new ArrayList<>();
        orderRecyclerView = findViewById(R.id.order_history_container);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setHasFixedSize(true);
        emptyMsg = findViewById(R.id.empty_msg);
        mToolbar = findViewById(R.id.order_history_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ordersHistoryAdapter = new ordersHistoryRecyclerAdapter(ordersList);
        orderRecyclerView.setAdapter(ordersHistoryAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    Orders order = doc.toObject(Orders.class);
                                    ordersList.add(order);
                                }
                                ordersHistoryAdapter.notifyDataSetChanged();
                                if(ordersList.isEmpty()){
                                    emptyMsg.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Toast.makeText(OrderHistoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}