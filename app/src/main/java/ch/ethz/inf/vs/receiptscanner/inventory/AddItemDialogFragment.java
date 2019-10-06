package ch.ethz.inf.vs.receiptscanner.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import ch.ethz.inf.vs.receiptscanner.InteractionCallback;
import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

public class AddItemDialogFragment extends DialogFragment {

    private InteractionCallback callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        try {
            callback = (InteractionCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement InteractionCallback");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText sumEditText = view.findViewById(R.id.sumEditText);
        final EditText dateEditText = view.findViewById(R.id.dateEditText);

        // Set the dialog title
        builder.setTitle(R.string.add_new_item)
                .setView(view)
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameEditText.getText().toString();
                        String sumText = sumEditText.getText().toString();
                        String date = dateEditText.getText().toString();
                        boolean nameCorrect = checkName(name);
                        double sum = checkSum(sumText);
                        boolean dateCorrect = checkDate(date);

                        if (nameCorrect && dateCorrect) {
                            CommonDatabase.addInventoryItem(date, name);
                            callback.onInteraction(InteractionCallback.InteractionType.INVENTORY_UPDATE);
                        } else {
                            Toast.makeText(getContext(), R.string.input_incorrect, Toast.LENGTH_LONG).show();
                            AddItemDialogFragment.this.getDialog().cancel();
                        }
                        if (sum > 0 && dateCorrect) {
                            CommonDatabase.addExpense(date, sum);
                            callback.onInteraction(InteractionCallback.InteractionType.EXPENSE_UPDATE);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddItemDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();

    }

    private boolean checkName(String name) {
        if (name.length() == 0) {
            Toast.makeText(getContext(), R.string.name_incorrect, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkDate(String date) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            df.parse(date);
        } catch (ParseException p) {
            // exception
            Toast.makeText(getContext(), R.string.date_incorrect, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private double checkSum(String sumText) {
        double sum;
        try {
            sum = Double.valueOf(sumText);
        } catch (NumberFormatException n) {
            Toast.makeText(getContext(), R.string.sum_incorrect, Toast.LENGTH_LONG).show();
            return -1;
        }
        return sum;
    }
}