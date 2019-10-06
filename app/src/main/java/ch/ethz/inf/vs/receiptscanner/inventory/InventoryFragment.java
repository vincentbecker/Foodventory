package ch.ethz.inf.vs.receiptscanner.inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.receiptscanner.DatabaseListener;
import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class InventoryFragment extends Fragment implements DatabaseListener {

    private static final String TAG = "INVENTORY_FRAGMENT";

    private InventoryItemRecyclerViewAdapter adapter;

    public InventoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.inventory_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new InventoryItemRecyclerViewAdapter(getContext(), new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(InventoryItem item) {
                Log.d(TAG, "Inventory item interaction");
                DialogFragment dialog = InformationDialogFragment.newInstance(item);
                dialog.show(getFragmentManager(), "InformationDialogFragment");
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.add_item_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new AddItemDialogFragment();
                dialog.show(getFragmentManager(), "AddItemDialogFragment");
            }
        });

        return view;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(InventoryItem item);
    }

    public void onDatabaseUpdate() {
        adapter.onDatabaseUpdate();
    }
}
