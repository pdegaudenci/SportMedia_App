package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportsmedia.models.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText edt_user,edt_password;
    Button btn_login, btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_user = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register=(Button) findViewById(R.id.btn_registro);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
               startActivity(intent);
            }
        });
    }


public void login(){
    HashMap postData = new HashMap();
    postData.put("username",edt_user.getText().toString());
    postData.put("password",edt_password.getText().toString());
    Toast.makeText(this,"Credenciales Incorrecta. Ingréselas nuevamente", Toast.LENGTH_SHORT).show();
    edt_user.setText("");
    edt_password.setText("");
}

    public void processFinish(String s) {
        Usuario user = checkUser(s);

        if(user == null){
            Toast.makeText(this,"Verifique usuario y contraseña", Toast.LENGTH_LONG).show();
        }
        else{
            HomeActivity.setUserglobal(user);
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
    private Usuario checkUser(String s){
        Usuario user = null;
        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.length() >= 0){
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                String nombre = jsonObject.getString("nombre");
                String apellido = jsonObject.getString("apellido");
                String fechanac = jsonObject.getString("fechanac");
                int id = jsonObject.getInt("id");
                //user = new Usuario(id,nombre,apellido,username,password,fechanac);
            }
        } catch (JSONException e) {

            return user;
        }

        return user;
    }
}