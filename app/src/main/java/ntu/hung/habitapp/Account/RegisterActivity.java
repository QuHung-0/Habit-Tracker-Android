package ntu.hung.habitapp.Account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ntu.hung.habitapp.R;
import ntu.hung.habitapp.Utils.Database;

// Lớp RegisterActivity xử lý giao diện và logic đăng ký người dùng
public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword; // Các trường nhập liệu cho email và mật khẩu
    private Button btnRegister; // Nút để thực hiện đăng ký
    private Database dbHelper; // Đối tượng hỗ trợ làm việc với cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Liên kết với giao diện XML

        // Liên kết các thành phần giao diện với mã
        etEmail = findViewById(R.id.et_email); // Trường nhập email
        etPassword = findViewById(R.id.et_password); // Trường nhập mật khẩu
        btnRegister = findViewById(R.id.btn_register); // Nút đăng ký
        dbHelper = new Database(this); // Khởi tạo đối tượng cơ sở dữ liệu

        // Xử lý sự kiện bấm nút đăng ký
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim(); // Lấy dữ liệu email và loại bỏ khoảng trắng
            String password = etPassword.getText().toString().trim(); // Lấy dữ liệu mật khẩu và loại bỏ khoảng trắng

            // Thực hiện đăng ký người dùng qua dbHelper
            if (dbHelper.registerUser(email, password)) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show(); // Thông báo thành công
                finish(); // Đóng hoạt động này và quay về màn hình trước đó (màn hình đăng nhập)
            } else {
                Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show(); // Thông báo lỗi nếu người dùng đã tồn tại
            }
        });
    }
}

