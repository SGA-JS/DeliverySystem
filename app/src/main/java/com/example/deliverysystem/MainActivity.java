package com.example.deliverysystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.deliverysystem.Database.DBHelper;
import com.example.deliverysystem.ui.addtask.AddtaskFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deliverysystem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public AppBarConfiguration mAppBarConfiguration;
    public ActivityMainBinding binding;
    public TextView sideBarUsername;
    public MenuItem listItem, taskItem;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public View headerView;
    public Menu navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeUI();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_list, R.id.nav_addtask)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void initializeUI() {
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        headerView = navigationView.getHeaderView(0);
        navMenu = navigationView.getMenu();
        sideBarUsername = headerView.findViewById(R.id.sideBarUsername);
        listItem = navMenu.findItem(R.id.nav_list);
        taskItem = navMenu.findItem(R.id.nav_addtask);

        SharedPreferences sharedPreferences = getSharedPreferences("Credential", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (validateUsername(username)) {
            sideBarUsername.setText(username);
        } else {
            Log.e("MainActivity", "sideBarUsername is null");
        }

        DBHelper myDB = new DBHelper(MainActivity.this);
        int roleId = myDB.getRoleIdByUsername(username);
        if(roleId >= 0)
        {
            if(roleId == ConstantValue.ROLE_ADMIN)
            {
                listItem.setVisible(false);
                taskItem.setVisible(true);
            }
            else if(roleId == ConstantValue.ROLE_DRIVER)
            {
                listItem.setVisible(true);
                taskItem.setVisible(false);
            }
            else
            {
                listItem.setVisible(false);
                taskItem.setVisible(false);
            }
        }
        else
        {
            listItem.setVisible(false);
            taskItem.setVisible(false);
        }
    }

    public boolean validateUsername(String username)
    {
        if (username != null || !username.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable the back key
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void buttonLogoutClicked(View view) {
        // Call the logout method in LoginActivity
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.logout(this);
    }
}