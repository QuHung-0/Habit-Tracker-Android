package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.content.Intent; // Thư viện để làm việc với Intent
import android.os.Bundle; // Thư viện để làm việc với dữ liệu truyền giữa các thành phần
import android.widget.Button; // Thư viện cho giao diện nút bấm
import android.widget.EditText; // Thư viện cho giao diện nhập văn bản
import android.widget.TextView; // Thư viện cho giao diện hiển thị văn bản
import android.widget.Toast; // Thư viện để hiển thị thông báo nhanh

import androidx.appcompat.app.AppCompatActivity; // Lớp cha cho Activity

// Lớp SignupActivity kế thừa AppCompatActivity để tạo giao diện đăng ký
public class SignupActivity extends AppCompatActivity
{

    private EditText emailEditText, passwordEditText, confirmPasswordEditText; // Ô nhập email, mật khẩu, xác nhận mật khẩu
    private Button signupButton; // Nút đăng ký
    private TextView loginRedirectTextView; // Văn bản dẫn đến trang đăng nhập
    private Database dbHelper; // Đối tượng quản lý cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // Gọi phương thức cha
        setContentView(R.layout.activity_signup); // Gắn bố cục giao diện

        dbHelper = new Database(this); // Khởi tạo đối tượng cơ sở dữ liệu

        // Gán các thành phần giao diện vào biến
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectTextView = findViewById(R.id.loginRedirectTextView);

        // Xử lý khi nhấn nút đăng ký
        signupButton.setOnClickListener(v -> registerUser());
        // Xử lý khi nhấn vào liên kết đăng nhập
        loginRedirectTextView.setOnClickListener(v ->
        {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class)); // Chuyển sang LoginActivity
        });
    }

    // Phương thức xử lý đăng ký
    private void registerUser()
    {
        String email = emailEditText.getText().toString().trim(); // Lấy email từ ô nhập
        String password = passwordEditText.getText().toString().trim(); // Lấy mật khẩu từ ô nhập
        String confirmPassword = confirmPasswordEditText.getText().toString().trim(); // Lấy mật khẩu xác nhận

        // Kiểm tra nếu các trường trống
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra nếu mật khẩu và xác nhận mật khẩu không khớp
        if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm người dùng vào cơ sở dữ liệu
        boolean isInserted = dbHelper.insertUser(email, password); // Gọi hàm thêm người dùng trong cơ sở dữ liệu
        if (isInserted)
        { // Nếu thêm thành công
            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class)); // Chuyển sang LoginActivity
        }
        else
        { // Nếu thêm thất bại
            Toast.makeText(this, "Sign Up Failed. Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
