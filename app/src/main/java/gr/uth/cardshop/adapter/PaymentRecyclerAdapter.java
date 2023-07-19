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
import gr.uth.cardshop.domain.Items;


public class PaymentRecyclerAdapter extends RecyclerView.Adapter<PaymentRecyclerAdapter.ViewHolder> {
    private List<Items> itemsList;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    public PaymentRecyclerAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_payment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        holder.paymentName.setText(itemsList.get(position).getName());
        holder.paymentRarity.setText("Rarity: "+itemsList.get(position).getRarity());
        holder.paymentPrice.setText("Price: $"+itemsList.get(position).getPrice());
        holder.paymentQuantity.setText("Quantity: "+itemsList.get(position).getQuantity());
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.paymentImage);
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView paymentImage;
        private TextView paymentName;
        private TextView paymentRarity;
        private TextView paymentPrice;
        private TextView paymentQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentImage = itemView.findViewById(R.id.payment_image);
            paymentName = itemView.findViewById(R.id.payment_name);
            paymentRarity = itemView.findViewById(R.id.payment_rarity);
            paymentPrice = itemView.findViewById(R.id.payment_price);
            paymentQuantity = itemView.findViewById(R.id.payment_quantity);
        }
    }
}