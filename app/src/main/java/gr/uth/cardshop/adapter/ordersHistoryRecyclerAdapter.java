package gr.uth.cardshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Orders;


public class ordersHistoryRecyclerAdapter extends RecyclerView.Adapter<ordersHistoryRecyclerAdapter.ViewHolder> {
    private List<Orders> itemsList;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    public ordersHistoryRecyclerAdapter(List<Orders> itemsList) {
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.signle_order_history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {

        holder.orderName.setText(itemsList.get(position).getName());
        holder.orderRarity.setText("Rarity: "+itemsList.get(position).getRarity());
        holder.orderPrice.setText("Price: $"+itemsList.get(position).getAmount());
        holder.orderQuantity.setText("Quantity: "+itemsList.get(position).getQuantity());
        holder.orderAddress.setText(itemsList.get(position).getAddress());
        holder.orderEmail.setText(itemsList.get(position).getEmail());
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.orderImage);
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView orderImage;
        private TextView orderAddress;
        private TextView orderEmail;
        private TextView orderName;
        private TextView orderRarity;
        private TextView orderPrice;
        private TextView orderQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.cart_image);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderEmail = itemView.findViewById(R.id.order_email);
            orderName = itemView.findViewById(R.id.order_name);
            orderRarity = itemView.findViewById(R.id.order_rarity);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
        }
    }
}