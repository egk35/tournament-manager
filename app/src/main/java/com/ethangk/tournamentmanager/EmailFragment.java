package com.ethangk.tournamentmanager;

import android.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Warning Fragment to prompt the user whether to continue returning to the home page
 */
public class EmailFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks. */
    public interface EmailDialogListener {
        public void onEmailPositiveClick(DialogFragment dialog, String email);
    }

    EmailDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EmailDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EmailDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Sets the content dialog to be dialog_post
        // Also sets a Post and Cancel button

        View view = inflater.inflate(R.layout.email_layout, null);

        builder.setView(view)
                .setPositiveButton(R.string.send_mail, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onEmailPositiveClick(EmailFragment.this, getEmail());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * Retrieve and return the message entered by the user
     *
     * @return the message entered by the user
     */
    private String getEmail() {
        EditText e = (EditText) getDialog().findViewById(R.id.email_text);
        return e.getText().toString();
    }
}
