package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sportsmedia.models.Usuario;

public class HomeActivity extends AppCompatActivity {
    public static Usuario getUserglobal() {
        return userglobal;
    }

    public static void setUserglobal(Usuario userglobal) {
        HomeActivity.userglobal = userglobal;
    }

    private static Usuario userglobal;
    private TextView tv_editname,tv_editlastname,tv_editusername,tv_editfechanac,tv_editpassword,tv_editid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }


}