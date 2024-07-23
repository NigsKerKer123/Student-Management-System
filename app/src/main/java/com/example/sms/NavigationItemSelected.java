package com.example.sms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sms.All.DisplayAllMainActivity;
import com.example.sms.college.CollegeMainActivity;
import com.google.android.material.navigation.NavigationView;

public class NavigationItemSelected {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    AlertDialog logoutDialog;
    Context context;

    public NavigationItemSelected(Context context, AlertDialog logoutDialog, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.context = context;
        this.logoutDialog = logoutDialog;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
    }

    public void itemSelected(){
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.college){
                Intent intent = new Intent(context, CollegeMainActivity.class);
                context.startActivity(intent);
            } else if (id == R.id.course){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                context.startActivity(intent);
            } else if (id == R.id.section){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                context.startActivity(intent);
            } else if (id == R.id.student){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                context.startActivity(intent);
            } else if (id == R.id.logout){
                logoutDialog.show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
