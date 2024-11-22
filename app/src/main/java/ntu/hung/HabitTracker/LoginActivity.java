package ntu.hung.HabitTracker;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;  // Khai báo FirebaseAuth để xác thực người dùng
    private EditText emailEditText, passwordEditText; // Các trường nhập liệu
    private Button loginButton;  // Nút đăng nhập
    private TextView signupRedirectTextView;  // Văn bản điều hướng đến màn hình đăng ký

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);  // Khởi tạo FirebaseApp trước khi sử dụng FirebaseAuth
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth và các thành phần giao diện
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectTextView = findViewById(R.id.signupRedirectTextView);

        // Lắng nghe sự kiện khi người dùng nhấn nút Đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Lắng nghe sự kiện khi người dùng muốn chuyển đến màn hình đăng ký
        signupRedirectTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);  // Chuyển sang màn hình đăng ký
        });
    }

    // Hàm xử lý đăng nhập người dùng
    private void loginUser() {
        // Lấy dữ liệu người dùng nhập vào
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra các trường nhập liệu có bị bỏ trống không
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();  // Thông báo lỗi khi thiếu thông tin
            return;
        }

        // Ghi log khi bắt đầu quá trình đăng nhập
        Log.d("LoginActivity", "loginUser: Attempting to log in with email: " + email);

        // Đăng nhập với Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Nếu đăng nhập thành công, chuyển đến màn hình chính (MainActivity)
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // Nếu đăng nhập thất bại, thông báo lỗi cho người dùng
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

