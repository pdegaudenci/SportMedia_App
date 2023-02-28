package com.example.sportsmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportsmedia.controller.FirebaseController;
import com.example.sportsmedia.dto.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_user,edt_password;
    private Button btn_login, btn_register;

    private static ArrayList<Usuario> users = new ArrayList<>();
    private FirebaseController firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding();
        listeners();    }
    private void binding() {
        edt_user = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register=(Button) findViewById(R.id.btn_registro);
        firebase = new FirebaseController("Usuarios",getApplicationContext());    }
    private void listeners(){
    btn_register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);startActivity(intent);
        }
    });
    btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            autenticarUsuario();
        }
    });
}


private void setUsers(ArrayList<Usuario> lista){
        users = lista;
}
private boolean autenticarUsuario() {
    HashMap dataUser = new HashMap();
    dataUser.put("username",edt_user.getText().toString());
    dataUser.put("password",edt_password.getText().toString());
        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoginActivity.getUsers().clear();
                for (DataSnapshot element: snapshot.getChildren()){
                    Usuario userAux = element.getValue(Usuario.class);
                    LoginActivity.users.add(userAux);
                }
                for (Usuario user_aux:LoginActivity.users){
                    if ((user_aux.getUsername().equals(dataUser.get("username").toString()) ||
                            user_aux.getEmail().equals(dataUser.get("username").toString())&&
                                    user_aux.getPassword().equals(dataUser.get("password"))))
                    {
                        HomeActivity.setUserglobal(user_aux);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Credenciales Incorrecta. Ingréselas nuevamente", Toast.LENGTH_SHORT).show();
                        edt_user.setText("");
                        edt_password.setText("");                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return false;
    }

    public static ArrayList<Usuario> getUsers() {
        return users;
    }




}