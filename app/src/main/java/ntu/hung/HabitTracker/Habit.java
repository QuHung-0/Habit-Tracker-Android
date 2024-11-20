package ntu.hung.HabitTracker;

import android.util.Log;

public class Habit {
    private static final String TAG = "Habit";
    private String name;
    private int streak;

    public Habit(String name, int streak) {
        Log.d(TAG, "Habit created - name: " + name + ", streak: " + streak);
        this.name = name;
        this.streak = streak;
    }

    public String getName() {
        Log.d(TAG, "getName called - returning: " + name);
        return name;
    }

    public int getStreak() {
        Log.d(TAG, "getStreak called - returning: " + streak);
        return streak;
    }

    public void incrementStreak() {
        streak++;
        Log.d(TAG, "incrementStreak called - new streak: " + streak);
    }
}
