package Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quanlycuahang.R;
//import com.google.android.gms.common.api.Response;
import com.android.volley.Response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.User;
import until.Server;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    TextView mTxtReg;
    CheckBox mCkRemember;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loadElement();

        try {
            loadAutoLogin();
        } catch (JSONException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAutoLogin() throws JSONException, InterruptedException {
        if (!TextUtils.equals(AuthManager.gI().getToken(), "")) {
            // cos data
            JSONObject jsonObject = new JSONObject(AuthManager.gI().getToken());
            login(String.valueOf(jsonObject.get("email")), String.valueOf(jsonObject.get("password")));
        }
    }

    public void loadElement() {
        mBtnLogin = findViewById(R.id.btn_login);
        mEdtEmail = findViewById(R.id.edt_email);
        mEdtPassword = findViewById(R.id.edt_password);
        mTxtReg = findViewById(R.id.txt_reg);
        mCkRemember = findViewById(R.id.cb_remember);

        mTxtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void valid() {

        String email = String.valueOf(mEdtEmail.getText());
        String password = String.valueOf(mEdtPassword.getText());

        if (TextUtils.equals(email, "")) {
            Toast.makeText(this, "Email không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.equals(password, "")) {
            Toast.makeText(this, "Password không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        login(email, password);
    }

    public void login(String email, String password) {
        try {  // send request to server
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONArray data = new JSONArray();
            data.put(email);
            data.put(password);
            JsonArrayRequest loginRequest = new JsonArrayRequest(Request.Method.POST, Server.loginRequestUrl, data, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        if (response.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_LONG).show();
                        } else {
                            JSONObject jsonObject = (JSONObject) response.getJSONObject(0);
                            User user = new User(
                                    Integer.parseInt(String.valueOf(jsonObject.get("id"))),
                                    String.valueOf(jsonObject.get("name")),
                                    String.valueOf(jsonObject.get("email")),
                                    String.valueOf(jsonObject.get("password")),
                                    String.valueOf(jsonObject.get("phone"))
                            );
                            MainActivity.authViewModel.getUser().setValue(user);
                            if (mCkRemember.isChecked()) {
                                JSONObject jsonObject_ = new JSONObject();
                                jsonObject_.put("email", email);
                                jsonObject_.put("password", password);
                                AuthManager.gI().saveToken(jsonObject_.toString());
                            }
                            onBackPressed();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d("error  :::  ", error.getMessage());
                }
            });
            requestQueue.add(loginRequest);

        } catch (Exception e) {

        }

        //end
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.authViewModel.getUser().getValue() != null) {
            super.onBackPressed();
        }
    }
}
