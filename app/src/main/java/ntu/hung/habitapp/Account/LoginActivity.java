package ntu.hung.habitapp.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ntu.hung.habitapp.Utils.MainActivity;
import ntu.hung.habitapp.R;
import ntu.hung.habitapp.Utils.Database;

public class LoginActivity extends AppCompatActivity
{

    // Khai báo các thành phần giao diện và biến cần thiết
    private EditText etEmail, etPassword; // Ô nhập email và mật khẩu
    private Button btnLogin; // Nút đăng nhập
    private TextView tvRegister; // Liên kết để chuyển sang màn hình đăng ký
    private Database dbHelper; // Đối tượng để làm việc với cơ sở dữ liệu
    private SharedPreferences sharedPreferences; // Để lưu thông tin người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Kết nối với giao diện XML

        // Liên kết các thành phần giao diện với mã Java
        etEmail = findViewById(R.id.et_email); // Ô nhập email
        etPassword = findViewById(R.id.et_password); // Ô nhập mật khẩu
        btnLogin = findViewById(R.id.btn_login); // Nút đăng nhập
        tvRegister = findViewById(R.id.tv_register); // Văn bản đăng ký
        dbHelper = new Database(this); // Khởi tạo đối tượng cơ sở dữ liệu
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE); // Lấy đối tượng SharedPreferences

        // Sự kiện khi người dùng bấm nút đăng nhập
        btnLogin.setOnClickListener(v ->
        {
            // Lấy email và mật khẩu từ các ô nhập
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Kiểm tra thông tin người dùng trong cơ sở dữ liệu
            int userId = dbHelper.validateUser(email, password);

            if (userId != -1) // Nếu thông tin hợp lệ
            {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo

                // Lưu ID người dùng vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", userId);
                editor.apply(); // Áp dụng thay đổi

                // Chuyển đến MainActivity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            else // Nếu thông tin không hợp lệ
            {
                Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi
            }
        });

        // Sự kiện khi người dùng bấm vào liên kết đăng ký
        tvRegister.setOnClickListener(v ->
        {
            // Chuyển đến RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}