package ntu.hung.HabitTracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Import Log để ghi lại log trong Logcat
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    // Đặt tag cho Logcat để dễ dàng nhận diện các log từ activity này
    private static final String TAG = "SignupActivity";

    // Khai báo các biến cần thiết cho giao diện người dùng và FirebaseAuth
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private TextView loginRedirectTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Ghi log khi Activity được tạo ra
        Log.d(TAG, "onCreate: Initializing UI components...");

        // Khởi tạo FirebaseAuth và các thành phần giao diện
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectTextView = findViewById(R.id.loginRedirectTextView);

        // Lắng nghe sự kiện khi người dùng nhấn nút Đăng ký
        signupButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Signup button clicked");
            signupUser();  // Gọi hàm đăng ký người dùng
        });

        // Lắng nghe sự kiện khi người dùng muốn chuyển đến màn hình đăng nhập
        loginRedirectTextView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Redirecting to LoginActivity");
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Kết nối với Firebase để kiểm tra kết nối
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase connection: Successful");
                    } else {
                        Log.e(TAG, "Firebase connection: Failed", task.getException());
                    }
                });
    }

    // Hàm xử lý đăng ký người dùng
    private void signupUser() {
        Log.d(TAG, "signupUser: Starting signup process...");

        // Lấy dữ liệu người dùng nhập vào
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra các trường nhập liệu có bị bỏ trống không
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.w(TAG, "signupUser: Empty fields detected");
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();  // Thông báo người dùng
            return;
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có trùng khớp không
        if (!password.equals(confirmPassword)) {
            Log.w(TAG, "signupUser: Passwords do not match");
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();  // Thông báo lỗi mật khẩu không trùng khớp
            return;
        }

        // Ghi log khi bắt đầu tạo người dùng mới
        Log.d(TAG, "signupUser: Attempting to create user with email: " + email);

        // Tạo người dùng mới bằng Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signupUser: User created successfully");
                        Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                        // Sau khi đăng ký thành công, chuyển đến màn hình đăng nhập
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    } else {
                        Log.e(TAG, "signupUser: Signup failed", task.getException());
                        Toast.makeText(this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();  // Thông báo lỗi khi đăng ký thất bại
                    }
                });
    }
}