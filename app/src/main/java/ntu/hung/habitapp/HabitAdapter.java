package ntu.hung.habitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter để quản lý danh sách thói quen hiển thị trong RecyclerView
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList; // Danh sách thói quen
    private OnHabitClickListener listener; // Listener để xử lý sự kiện người dùng

    // Giao diện định nghĩa các hành động: chỉnh sửa, xóa, thay đổi trạng thái
    public interface OnHabitClickListener {
        void onEditHabit(Habit habit); // Khi người dùng nhấn để chỉnh sửa
        void onDeleteHabit(Habit habit); // Khi người dùng nhấn giữ để xóa
        void onStatusChanged(Habit habit, boolean isChecked); // Khi người dùng thay đổi trạng thái
    }

    // Constructor nhận danh sách thói quen và listener
    public HabitAdapter(List<Habit> habitList, OnHabitClickListener listener) {
        this.habitList = habitList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder từ layout XML của một mục thói quen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position); // Lấy dữ liệu thói quen tại vị trí hiện tại

        // Gán dữ liệu từ thói quen vào các view
        holder.habitTitle.setText(habit.getHabitName());
        holder.habitDescription.setText(habit.getHabitDescription());
        holder.habitDescription.setVisibility(habit.getHabitDescription().isEmpty() ? View.GONE : View.VISIBLE);
        holder.habitTime.setText(habit.getTimePeriod());
        holder.habitStatus.setChecked(habit.getStatus() == 1);

        // Lắng nghe sự thay đổi trạng thái checkbox
        holder.habitStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStatusChanged(habit, isChecked);
            }
        });

        // Nhấn để chỉnh sửa
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditHabit(habit);
            }
        });

        // Nhấn giữ để xóa
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDeleteHabit(habit);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habitList.size(); // Trả về số lượng mục trong danh sách
    }

    // ViewHolder chứa các view trong layout item_habit.xml
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView habitTitle, habitDescription, habitTime;
        CheckBox habitStatus;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitTitle = itemView.findViewById(R.id.tv_habit_title);
            habitDescription = itemView.findViewById(R.id.tv_habit_description);
            habitTime = itemView.findViewById(R.id.tv_habit_time);
            habitStatus = itemView.findViewById(R.id.cb_habit_status);
        }
    }
}

