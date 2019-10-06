package ch.ethz.inf.vs.receiptscanner.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ch.ethz.inf.vs.receiptscanner.FoodInformation;
import ch.ethz.inf.vs.receiptscanner.R;

public class InformationDialogFragment extends DialogFragment {

    public static InformationDialogFragment newInstance(InventoryItem item) {
        InformationDialogFragment f = new InformationDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("uid", item.uid);
        args.putString("name", item.name);
        args.putString("purchase_date", item.purchaseDate);
        args.putString("expiry_date", item.expiryDate);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        String name = args.getString("name");
        final String expiryDate = args.getString("expiry_date");

        String storage = FoodInformation.storageInfo.get(name);
        if (storage == null) {
            storage = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(name)
                .setMessage("Expires on " + expiryDate + ". " + storage)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        InformationDialogFragment.this.getDialog().cancel();
                    }
                }).setNegativeButton(R.string.change_date, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogFragment newFragment = DatePickerFragment.newInstance(args);
                newFragment.show(getFragmentManager(), "datePickerFragment");
            }
        });

        return builder.create();

    }
}