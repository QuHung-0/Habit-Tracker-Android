package ntu.hung.HabitTracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder>
{

    private static final String TAG = "HabitAdapter";
    private final List<Habit> habitList;

    public HabitAdapter(List<Habit> habitList)
    {
        Log.d(TAG, "HabitAdapter initialized");
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position)
    {
        Habit habit = habitList.get(position);
        Log.d(TAG, "onBindViewHolder called - position: " + position + ", habit: " + habit.getName());
        holder.habitName.setText(habit.getName());
        holder.habitStreak.setText(habit.getStreak() + " Days");
    }

    @Override
    public int getItemCount()
    {
        int size = habitList.size();
        Log.d(TAG, "getItemCount called - size: " + size);
        return size;
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder
    {
        TextView habitName, habitStreak;

        public HabitViewHolder(@NonNull View itemView)
        {
            super(itemView);
            Log.d(TAG, "HabitViewHolder initialized");
            habitName = itemView.findViewById(R.id.habitName);
            habitStreak = itemView.findViewById(R.id.habitStreak);
        }
    }
}



