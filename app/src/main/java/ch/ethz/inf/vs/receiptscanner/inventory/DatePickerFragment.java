package ch.ethz.inf.vs.receiptscanner.inventory;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import ch.ethz.inf.vs.receiptscanner.InteractionCallback;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

import static ch.ethz.inf.vs.receiptscanner.InteractionCallback.InteractionType.INVENTORY_UPDATE;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private InventoryItem item;

    public static DatePickerFragment newInstance(Bundle args) {
        DatePickerFragment f = new DatePickerFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        item = new InventoryItem();
        item.uid = args.getInt("uid");
        item.name = args.getString("name");
        item.purchaseDate = args.getString("purchase_date");
        item.expiryDate = args.getString("expiry_date");

        String[] datePieces = item.expiryDate.split("\\.");
        int day = Integer.valueOf(datePieces[0]);
        int month = (Integer.valueOf(datePieces[1]) - 1) % 12;
        int year = Integer.valueOf(datePieces[2]);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String newExpiryDate = day + "." + (month + 1) + "." + year;
        item.expiryDate = newExpiryDate;
        CommonDatabase.updateInventoryItem(item);
        ((InteractionCallback) getActivity()).onInteraction(INVENTORY_UPDATE);
    }
}