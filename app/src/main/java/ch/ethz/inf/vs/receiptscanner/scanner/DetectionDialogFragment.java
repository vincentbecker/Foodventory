package ch.ethz.inf.vs.receiptscanner.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ch.ethz.inf.vs.receiptscanner.InteractionCallback;
import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

public class DetectionDialogFragment extends DialogFragment {

    private CheckedRecyclerViewAdapter adapter;
    private InteractionCallback callback;


    public static DetectionDialogFragment newInstance(DetectionResult result) {
        DetectionDialogFragment f = new DetectionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putStringArrayList("items", result.getDetectedItems());
        args.putDouble("sum", result.getSum());
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        try {
            callback = (InteractionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement InteractionCallback");
        }

        final ArrayList<String> detectedItems = getArguments().getStringArrayList("items");
        final double sum = getArguments().getDouble("sum", -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.detection_dialog, null);

        // Set the list
        RecyclerView recyclerView = view.findViewById(R.id.detected_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CheckedRecyclerViewAdapter(detectedItems);
        recyclerView.setAdapter(adapter);

        // Set the sum and date views
        EditText sumEditText = view.findViewById(R.id.sumEditText);
        sumEditText.setText(String.format("%.2f", sum), TextView.BufferType.EDITABLE);
        EditText dateEditText = view.findViewById(R.id.dateEditText);
        final String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        dateEditText.setText(timeStamp, TextView.BufferType.EDITABLE);

        // Set the dialog title
        builder.setTitle(R.string.dialog_title)
                .setView(view)
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Write to database
                        SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                        //Check if item is selected or not via size
                        int s = selectedRows.size();
                        if (s > 0) {
                            String[] dates = new String[s];
                            String[] names = new String[s];
                            for (int i = 0; i < s; i++) {
                                dates[i] = timeStamp;
                                names[i] = detectedItems.get(selectedRows.keyAt(i));
                            }
                            CommonDatabase.addInventoryItems(dates, names);
                            callback.onInteraction(InteractionCallback.InteractionType.INVENTORY_UPDATE);
                        }
                        if (sum > -1) {
                            CommonDatabase.addExpense(timeStamp, sum);
                            callback.onInteraction(InteractionCallback.InteractionType.EXPENSE_UPDATE);
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DetectionDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();

    }
}
