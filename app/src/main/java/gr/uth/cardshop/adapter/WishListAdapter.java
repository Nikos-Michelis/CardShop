package gr.uth.cardshop.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Items;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private List<Items> itemsList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ItemRemoved itemRemoved;

    public WishListAdapter(List<Items> itemsList, ItemRemoved itemRemoved) {
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
        this.itemRemoved = itemRemoved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_wishlist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.wishListName.setText(itemsList.get(position).getName());
        holder.wishListRarity.setText("Rarity: "+itemsList.get(position).getRarity());
        holder.wishListPrice.setText("Price: $"+itemsList.get(position).getPrice());
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.wishListImage);
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("Wishlist").document(itemsList.get(position).getDocId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    itemsList.remove(itemsList.get(position));
                                    notifyDataSetChanged();
                                    itemRemoved.onItemRemoved(itemsList);
                                    Toast.makeText(holder.itemView.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(holder.itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView wishListImage;
        private TextView wishListName;
        private TextView wishListRarity;
        private TextView wishListPrice;
        private TextView removeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wishListImage = itemView.findViewById(R.id.wishlist_image);
            wishListName = itemView.findViewById(R.id.wishlist_name);
            wishListPrice = itemView.findViewById(R.id.wishlist_price);
            wishListRarity = itemView.findViewById(R.id.wishlist_rarity);
            removeItem = itemView.findViewById(R.id.remove_item);
        }
    }
    public interface ItemRemoved{
        void onItemRemoved(List<Items> itemsList);
    }
}