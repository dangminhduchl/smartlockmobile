package com.example.smartlockjava.ui.userManagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.smartlockjava.R;
import com.example.smartlockjava.dto.Person;

public class UserEditDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the layout for this dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_edit_dialog, null);

        // Get the user data from the arguments
        Person user = (Person) getArguments().getSerializable("user");

        // Get a reference to the TextViews and EditTexts and set their text to the user data
        TextView dlgIdLabel = view.findViewById(R.id.dlg_id_label);
        dlgIdLabel.setText(String.format("ID: %s", user.getId()));

        TextView dlgEmail = view.findViewById(R.id.dlg_email);
        dlgEmail.setText(user.getEmail());

        TextView dlgUsername = view.findViewById(R.id.dlg_username);
        dlgUsername.setText(user.getUsername());

        builder.setView(view);

        return builder.create();
    }
}