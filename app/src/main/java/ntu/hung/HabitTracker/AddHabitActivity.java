package ntu.hung.HabitTracker;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddHabitActivity extends AppCompatActivity {

    private static final String TAG = "AddHabitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_add_habit);

        EditText habitNameInput = findViewById(R.id.habitNameInput);
        Button saveHabitButton = findViewById(R.id.saveHabitButton);

        saveHabitButton.setOnClickListener(v -> {
            String habitName = habitNameInput.getText().toString();
            if (!habitName.isEmpty()) {
                Log.d(TAG, "Save button clicked - habit name: " + habitName);
                // Add new habit to the shared habit list
                MainActivity.habitList.add(new Habit(habitName, 0));
                finish(); // Go back to the main activity
            } else {
                Log.e(TAG, "Habit name is empty, not saving");
            }
        });
    }
}

