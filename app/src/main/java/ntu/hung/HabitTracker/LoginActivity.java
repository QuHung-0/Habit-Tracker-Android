package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.content.Intent; // Thư viện để làm việc với Intent (chuyển Activity)
import android.os.Bundle; // Thư viện để làm việc với dữ liệu truyền giữa các thành phần
import android.widget.Button; // Thư viện cho giao diện nút bấm
import android.widget.EditText; // Thư viện cho giao diện nhập văn bản
import android.widget.TextView; // Thư viện cho giao diện hiển thị văn bản
import android.widget.Toast; // Thư viện để hiển thị thông báo nhanh

import androidx.appcompat.app.AppCompatActivity; // Lớp cha cho Activity

// Lớp LoginActivity kế thừa AppCompatActivity để tạo giao diện đăng nhập
public class LoginActivity extends AppCompatActivity
{

    private EditText emailEditText, passwordEditText; // Ô nhập email và mật khẩu
    private Button loginButton; // Nút đăng nhập
    private TextView signupRedirectTextView; // Văn bản dẫn đến trang đăng ký
    private Database dbHelper; // Đối tượng quản lý cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // Gọi phương thức cha
        setContentView(R.layout.activity_login); // Gắn bố cục giao diện

        dbHelper = new Database(this); // Khởi tạo đối tượng cơ sở dữ liệu

        // Gán các thành phần giao diện vào biến
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectTextView = findViewById(R.id.signupRedirectTextView);

        // Xử lý khi nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> loginUser());
        // Xử lý khi nhấn vào liên kết đăng ký
        signupRedirectTextView.setOnClickListener(v ->
        {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class)); // Chuyển sang SignupActivity
        });
    }

    // Phương thức xử lý đăng nhập
    private void loginUser()
    {
        String email = emailEditText.getText().toString().trim(); // Lấy email từ ô nhập
        String password = passwordEditText.getText().toString().trim(); // Lấy mật khẩu từ ô nhập

        // Kiểm tra nếu các trường trống
        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác thực người dùng
        boolean isValid = dbHelper.validateUser(email, password); // Gọi hàm kiểm tra trong cơ sở dữ liệu
        if (isValid)
        { // Nếu thông tin đúng
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class)); // Chuyển sang MainActivity
        }
        else
        { // Nếu thông tin sai
            Toast.makeText(this, "Invalid Email or Password!", Toast.LENGTH_LONG).show();
        }
    }
}
