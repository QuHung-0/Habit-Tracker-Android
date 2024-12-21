package ntu.hung.habitapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView; // Thanh điều hướng ở dưới cùng

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Kết nối với giao diện XML

        // Liên kết thanh điều hướng với mã Java
        bottomNavigationView = findViewById(R.id.bottom_nav);

        // Lấy đối tượng NavController để điều hướng giữa các Fragment
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragment_container);

        // Thiết lập thanh điều hướng để làm việc với NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}