package com.example.smartlockjava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlockjava.database.DatabaseHelper;
import com.example.smartlockjava.database.PersonTableManager;
import com.example.smartlockjava.dto.ChangePasswordRequest;
import com.example.smartlockjava.dto.Person;
import com.example.smartlockjava.dto.PersonSync;
import com.example.smartlockjava.preferences.Preferences;
import com.example.smartlockjava.service.UserService;
import com.example.smartlockjava.ui.userManagement.UserManagementViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlockjava.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static MainActivity instance;

    private DatabaseHelper databaseHelper;

    private PersonTableManager personTableManager;


    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        databaseHelper = new DatabaseHelper(this);
        personTableManager = new PersonTableManager(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (preferences.getUserId() != null && !preferences.getUserId().isEmpty()) {
            // If Preferences do not have data, make login and face_login visible
            binding.appBarMain.login.setVisibility(View.VISIBLE);
            binding.appBarMain.faceLogin.setVisibility(View.VISIBLE);

            // Make logout invisible
            binding.appBarMain.logout.setVisibility(View.GONE);

        } else {
            // If Preferences have data, make login and face_login invisible
            binding.appBarMain.login.setVisibility(View.GONE);
            binding.appBarMain.faceLogin.setVisibility(View.GONE);

            // Make logout visible and move it to the login position
            binding.appBarMain.logout.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.appBarMain.faceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FaceLoginActivity.class);
                startActivityForResult(intent, 1); // Use any request code you like
            }
        });
        binding.appBarMain.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.clear();
                TextView userNameTextView = findViewById(R.id.userName);
                TextView userIdTextView = findViewById(R.id.userId);
                userNameTextView.setText("Anonymous");
                userIdTextView.setText("Anonymous");
                binding.appBarMain.login.setVisibility(View.VISIBLE);
                binding.appBarMain.faceLogin.setVisibility(View.VISIBLE);
                binding.appBarMain.logout.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.user_management, R.id.history)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (preferences.getUserId() != null && !preferences.getUserId().isEmpty()) {
            // If Preferences have data, make login and face_login invisible
            binding.appBarMain.login.setVisibility(View.GONE);
            binding.appBarMain.faceLogin.setVisibility(View.GONE);

            // Make logout visible
            binding.appBarMain.logout.setVisibility(View.VISIBLE);
        } else {
            // If Preferences do not have data, make login and face_login visible
            binding.appBarMain.login.setVisibility(View.VISIBLE);
            binding.appBarMain.faceLogin.setVisibility(View.VISIBLE);

            // Make logout invisible
            binding.appBarMain.logout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userIdTextView = findViewById(R.id.userId);
        String userId = preferences.getUserId();
        String userName = preferences.getUserName();
        userIdTextView.setText(userId);
        userNameTextView.setText(userName);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("SetTextI18n")
    public void onClickLogout(MenuItem item) {
        preferences.clear();
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userIdTextView = findViewById(R.id.userId);
        userNameTextView.setText("Anonymous");
        userIdTextView.setText("Anonymous");
        // Update visibility of buttons
        binding.appBarMain.login.setVisibility(View.VISIBLE);
        binding.appBarMain.faceLogin.setVisibility(View.VISIBLE);
        binding.appBarMain.logout.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void onClickSyncData(MenuItem item) {
        UserManagementViewModel userManagementViewModel = new ViewModelProvider(this).get(UserManagementViewModel.class);
        // Step 1: Call API to get data
        userManagementViewModel.syncUser("Bearer " + preferences.getAccessToken(), new Callback<List<PersonSync>>() {
            ;

            @Override
            public void onResponse(Call<List<PersonSync>> call, Response<List<PersonSync>> response) {
                if (response.isSuccessful()) {
                    List<PersonSync> users = response.body();

                    // Step 2: Clear old data from SQLite database
                    databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 1, 1);

                    // Step 3: Save new data to SQLite database
                    for (PersonSync user : users) {
                        if (user.getFaceVector() == null) {
                            continue;
                        }
                        personTableManager.savePerson(com.example.smartlockjava.database.Person.from(user));
                    }

                    Toast.makeText(MainActivity.this, "Data synced successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PersonSync>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get data from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void onClickChangePassword(MenuItem item) {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        // Inflate the change_password_dialog.xml layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);

        // Find the EditText views
        final EditText oldPasswordInput = dialogView.findViewById(R.id.dlg_old_password);
        final EditText newPasswordInput = dialogView.findViewById(R.id.dlg_new_password);

        // Set the view of the builder to our custom view
        builder.setView(dialogView);

        // Set up the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();

                // Call the API to change the password
                UserManagementViewModel userManagementViewModel = new ViewModelProvider(MainActivity.this).get(UserManagementViewModel.class);
                ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, newPassword);
                userManagementViewModel.changePassword("Bearer " + preferences.getAccessToken(), Integer.parseInt(preferences.getUserId()), changePasswordRequest, new Callback<Person>() {
                    @Override
                    public void onResponse(Call<Person> call, Response<Person> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Person> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // The request code used when starting the FaceLoginActivity
            if (resultCode == Activity.RESULT_OK) {
                // Update the user ID and username
                TextView userNameTextView = findViewById(R.id.userName);
                TextView userIdTextView = findViewById(R.id.userId);
                String userId = preferences.getUserId();
                String userName = preferences.getUserName();
                userIdTextView.setText(userId);
                userNameTextView.setText(userName);
            }
        }
    }
}