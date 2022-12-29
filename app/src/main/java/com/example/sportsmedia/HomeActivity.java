package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sportsmedia.models.Usuario;

public class HomeActivity extends AppCompatActivity {
    private static Usuario userglobal;
    private TextView user;
    public static Usuario getUserglobal() {
        return userglobal;
    }

    public static void setUserglobal(Usuario userglobal) {
        HomeActivity.userglobal = userglobal;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String text = getString(R.string.welcome, getUserglobal().getNombre());
        System.out.println("ENTROOOOOO"+text);
        user=(TextView) findViewById(R.id.user_home);
        user.setText(text);

    }


}