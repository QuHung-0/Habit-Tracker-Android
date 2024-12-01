package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.os.Bundle; // Thư viện để làm việc với dữ liệu chuyển giữa các thành phần

import androidx.appcompat.app.AppCompatActivity; // Lớp cha cho Activity
import androidx.navigation.NavController; // Thư viện điều hướng Fragment
import androidx.navigation.Navigation; // Thư viện hỗ trợ điều hướng
import androidx.navigation.ui.NavigationUI; // Thư viện UI cho điều hướng

import com.google.android.material.bottomnavigation.BottomNavigationView; // Giao diện thanh điều hướng dưới cùng

// Lớp MainActivity kế thừa AppCompatActivity để quản lý màn hình chính
public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView; // Thanh điều hướng dưới cùng

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // Gọi phương thức cha
        setContentView(R.layout.activity_main); // Gắn bố cục giao diện

        bottomNavigationView = findViewById(R.id.bottom_nav); // Kết nối với thành phần giao diện

        // Thiết lập điều hướng với thanh điều hướng dưới cùng
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController); // Kết nối thanh điều hướng với điều hướng
    }
}
