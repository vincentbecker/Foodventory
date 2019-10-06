package ch.ethz.inf.vs.receiptscanner.inventory;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.ethz.inf.vs.receiptscanner.FoodInformation;
import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;
import ch.ethz.inf.vs.receiptscanner.inventory.InventoryFragment.OnListFragmentInteractionListener;

import java.util.Comparator;
import java.util.List;

public class InventoryItemRecyclerViewAdapter extends RecyclerView.Adapter<InventoryItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private RecyclerView recyclerView;
    private List<InventoryItem> items;
    private OnListFragmentInteractionListener mListener;

    public InventoryItemRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.context = context;
        items = CommonDatabase.getInventory();
        mListener = listener;
    }

    public void onDatabaseUpdate() {
        this.items = CommonDatabase.getInventory();
        items.sort(new Comparator<InventoryItem>() {
            @Override
            public int compare(InventoryItem item1, InventoryItem item2) {
                int daysLeftItem1 = FoodInformation.calculateDaysUntilExpiry(item1.expiryDate);
                int daysLeftItem2 = FoodInformation.calculateDaysUntilExpiry(item2.expiryDate);
                return daysLeftItem1 - daysLeftItem2;
            }
        });
        for (int i = 0; i < getItemCount(); i++) {
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.cardView.setCardBackgroundColor(getColor(items.get(i).expiryDate));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventory_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        InventoryItem item = items.get(position);
        holder.item = item;
        String name = item.name;
        holder.nameView.setText(name);
        holder.expiryInfoView.setText("Läuft am " + item.expiryDate + " ab.");
        holder.storageInfoView.setText(FoodInformation.storageInfo.get(name));
        holder.cardView.setCardBackgroundColor(getColor(item.expiryDate));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int getColor(String expiryDate) {
        int daysLeft = FoodInformation.calculateDaysUntilExpiry(expiryDate);
        int color;
        if (daysLeft <= 0) {
            color = R.color.colorRed;
        } else if (daysLeft <= 2) {
            color = R.color.colorOrange;
        } else if (daysLeft <= 5) {
            color = R.color.colorYellow;
        } else if (daysLeft <= 7) {
            color = R.color.colorGreenYellow;
        } else {
            color = R.color.colorGreen;
        }
        return ContextCompat.getColor(context, color);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InventoryItem item;
        final View view;
        final CardView cardView;
        final TextView nameView;
        final TextView expiryInfoView;
        final TextView storageInfoView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            cardView = view.findViewById(R.id.card_view);
            nameView = view.findViewById(R.id.inventory_item_name);
            expiryInfoView = view.findViewById(R.id.expiry_info);
            storageInfoView = view.findViewById(R.id.storage_info);
        }
    }
}
