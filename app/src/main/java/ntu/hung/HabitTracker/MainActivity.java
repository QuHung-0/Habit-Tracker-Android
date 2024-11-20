package ntu.hung.HabitTracker;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // For debugging
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView habitRecyclerView;
    private HabitAdapter habitAdapter;
    public static List<Habit> habitList = new ArrayList<>(); // Static to share between activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        habitRecyclerView = findViewById(R.id.habitRecyclerView);
        findViewById(R.id.addHabitButton).setOnClickListener(v -> {
            Log.d(TAG, "Add Habit button clicked");
            Intent intent = new Intent(MainActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });

        // Set up RecyclerView
        Log.d(TAG, "Setting up RecyclerView and Adapter");
        habitAdapter = new HabitAdapter(habitList);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitRecyclerView.setAdapter(habitAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called - refreshing habit list");
        // Refresh the habit list after returning from AddHabitActivity
        habitAdapter.notifyDataSetChanged();
    }
}


