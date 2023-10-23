package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quanlycuahang.R;
import com.example.quanlycuahang.StorageActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.Loaispadapter;
import adapter.Sanphamadapter;
import model.GioHang;
import model.Loaisp;
import model.SanPham;
import model.User;
import until.Checkconnection;
import until.Server;
import viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    //    ViewFlipper viewFlipper;
    RecyclerView recyclerViewmanhinhchinh;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisp;
    Loaispadapter loaispadapter;
    ArrayList<SanPham> mangsanpham;
    Sanphamadapter sanphamadapter;
    View headerView;
    int id;
    String tenloaisp = "";
    String hinhanhsp = "";
    public static ArrayList<GioHang> manggiohang;
    private AuthManager.AuthMiddleware authMiddleware;

    private TextView mTxtEmailI;
    private TextView mTxtPhoneI;
    public static AuthViewModel authViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthManager.init(getApplicationContext());
        // check login
        Anhxa();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.authUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    mTxtEmailI.setText(user.getEmail());
                    mTxtPhoneI.setText(user.getPhone());
                }
            }
        });
        // check login
        authMiddleware = new AuthManager.AuthMiddleware() {
            @Override
            public void onAuth() {
                if (authViewModel.getUser().getValue() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        if (Checkconnection.HaveNetworkConnection(getApplicationContext())) {
            authMiddleware.onAuth();
            Actionbar();
            ActionViewFlipper();
            Getdulieuloaisp();
            Getdulieusanphammoinhat();
            chonItemlistview();
        } else {
            Checkconnection.Showtoast_short(getApplicationContext(), "Kiểm tra lại mạng");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_giohang, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void chonItemlistview() {
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

    }

    private void Getdulieusanphammoinhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdannphammoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String Tensp = "";
                    Integer Giasp = 0;
                    String Hinhanhsp = "";
                    String Motasp = "";
                    int IDsanpham = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensp = jsonObject.getString("tensp");
                            Giasp = jsonObject.getInt("giasp");
                            Hinhanhsp = jsonObject.getString("hinhanhsp");
                            Motasp = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new SanPham(ID, Tensp, Giasp, Hinhanhsp, Motasp, IDsanpham));
                            sanphamadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Checkconnection.Showtoast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void Getdulieuloaisp() {
        //hàm của thư viện Volley đọc nội dung đường dẫn Url
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duonganloaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhsp = jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new Loaisp(id, tenloaisp, hinhanhsp));
                            loaispadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(3, new Loaisp(3, "Liên hệ", "https://i.pinimg.com/originals/57/cf/21/57cf2127a1b9c8fdb334e5860fc22f61.png"));
                    mangloaisp.add(4, new Loaisp(4, "Thông tin", "https://png.pngtree.com/png-vector/20190916/ourlarge/pngtree-info-icon-for-your-project-png-image_1731084.jpg"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Checkconnection.Showtoast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {

    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {

        toolbar = findViewById(R.id.toolbarmanhinhchinh);
//        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewmanhinhchinh = findViewById(R.id.recyclerview);
        navigationView = findViewById(R.id.navigationview);
        headerView = navigationView.inflateHeaderView(R.layout.main_nav_header);
        mTxtEmailI = headerView.findViewById(R.id.txtEmailI);
        mTxtPhoneI = headerView.findViewById(R.id.txtPhoneI);
        drawerLayout = findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisp(0, "Trang chính", "https://noithattinnghia.com/wp-content/uploads/2019/03/cropped-icon-home-cam.png"));
        loaispadapter = new Loaispadapter(mangloaisp, getApplicationContext());
        mangsanpham = new ArrayList<>();
        sanphamadapter = new Sanphamadapter(getApplicationContext(), mangsanpham, new Sanphamadapter.ItemClickListener() {
            @Override
            public void onItemCick(SanPham details) {
                onClickrecyclerview(details);
            }
        });
        recyclerViewmanhinhchinh.setHasFixedSize(true);
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewmanhinhchinh.setAdapter(sanphamadapter);
        if (manggiohang != null) {

        } else {
            manggiohang = new ArrayList<>();
        }
    }

    private void onClickrecyclerview(SanPham details) {
        Intent intent = new Intent(MainActivity.this, Chitietsanpham.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("thongtinsanpham", details);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int checked = getCheckedItem(navigationView);
        if (checked == -1) {
            return true;
        }

        if (item.getTitle().equals("Trang chủ")) {
            if (checked == item.getOrder()) {
                return true;
            }
            if (Checkconnection.HaveNetworkConnection(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Checkconnection.Showtoast_short(getApplicationContext(), "Hay kiem tra mang");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getTitle().equals("Laptop")) {
            if (Checkconnection.HaveNetworkConnection(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, LaptopActivity.class);
                intent.putExtra("idloaisp", mangloaisp.get(2).getId());
                startActivity(intent);
            } else {
                Checkconnection.Showtoast_short(getApplicationContext(), "Hay kiem tra mang");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getTitle().equals("Địa chỉ")) {
            if (Checkconnection.HaveNetworkConnection(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, ThongtinActivity.class);
                startActivity(intent);
            } else {
                Checkconnection.Showtoast_short(getApplicationContext(), "Hay kiem tra mang");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getTitle().equals("Điện thoại")) {
            if (Checkconnection.HaveNetworkConnection(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, DienthoaiActivity.class);
                intent.putExtra("idloaisp", mangloaisp.get(1).getId());
                startActivity(intent);
            } else {
                Checkconnection.Showtoast_short(getApplicationContext(), "Hay kiem tra mang");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getTitle().equals("Đăng xuất")) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            AuthManager.gI().saveToken("");
            MainActivity.authViewModel.getUser().setValue(null);
            startActivity(intent);
        }
        if (item.getTitle().equals("Kho hàng")) {
            Intent intent = new Intent(MainActivity.this, StorageActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
    }
}