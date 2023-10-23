package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quanlycuahang.R;

import org.json.JSONArray;
import org.json.JSONObject;

import until.Server;

public class RegisterActivity extends AppCompatActivity {
    private Button mBtnLogin;

    private EditText mEdtEmail;
    private EditText mEdtPhone;

    private EditText mEdtPassword;
    TextView mTxtReg;

    boolean isReging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        loadElement();
    }

    public void loadElement() {
        mBtnLogin = findViewById(R.id.btn_reg);
        mEdtEmail = findViewById(R.id.edt_email);
        mEdtPassword = findViewById(R.id.edt_password);
        mTxtReg = findViewById(R.id.txt_login);
        mEdtPhone = findViewById(R.id.edt_phone);
        mTxtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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

    @Override
    public void onBackPressed() {
        if (!isReging) {
            super.onBackPressed();
        }
    }

    public void valid() {

        String email = String.valueOf(mEdtEmail.getText());
        String password = String.valueOf(mEdtPassword.getText());
        String phone = String.valueOf(mEdtPhone.getText());
        if (TextUtils.equals(email, "")) {
            Toast.makeText(this, "Email không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.equals(password, "")) {
            Toast.makeText(this, "Password không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.equals(phone, "")) {
            Toast.makeText(this, "Phone không được trống", Toast.LENGTH_LONG).show();
            return;
        }
        reg(email, password, phone);
    }

    public void reg(String email, String password, String phone) {
        try {  // send request to server
            isReging = true;
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONArray data = new JSONArray();
            data.put(email);
            data.put(password);
            data.put(phone);
            JsonArrayRequest loginRequest = new JsonArrayRequest(Request.Method.POST, Server.regRequestUrl, data, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        if (Boolean.parseBoolean(String.valueOf(jsonObject.get("response")))) {
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.get("message")), Toast.LENGTH_LONG).show();
                        }
                        isReging = false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error :::", error.getMessage());

                }
            });
            requestQueue.add(loginRequest);

        } catch (Exception e) {

        }

        //end
    }
}
