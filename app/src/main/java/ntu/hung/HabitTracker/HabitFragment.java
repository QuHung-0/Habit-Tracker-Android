package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Một {@link Fragment} đơn giản.
 * Sử dụng {@link HabitFragment#newInstance} để tạo một instance của fragment này.
 */
public class HabitFragment extends Fragment {

    private RecyclerView recyclerView; // Hiển thị danh sách thói quen
    private FloatingActionButton fabAdd; // Nút để thêm thói quen mới

    // Helper cơ sở dữ liệu và adapter cho RecyclerView
    private Database db;
    private HabitAdapter habitAdapter;

    // Danh sách thói quen được tải từ cơ sở dữ liệu
    private List<Habit> habitList;
    private int userId; // ID của người dùng (liên kết với các thói quen)

    public HabitFragment() {
        // Constructor trống cần thiết
    }

    // Phương thức để tạo instance của fragment với các tham số nếu cần
    public static HabitFragment newInstance(String param1, String param2) {
        HabitFragment fragment = new HabitFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Lấy các tham số nếu cần thiết
            String param1 = getArguments().getString("ARG_PARAM1");
            String param2 = getArguments().getString("ARG_PARAM2");
        }

        // Giả sử bạn có cách lấy userId (ví dụ: thông qua bundle, shared preferences hoặc session manager)
        userId = getActivity().getIntent().getIntExtra("USER_ID", -1); // Ví dụ minh họa
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Nạp layout cho fragment
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        // Khởi tạo các thành phần giao diện
        recyclerView = view.findViewById(R.id.recyclerView); // RecyclerView cho danh sách thói quen
        fabAdd = view.findViewById(R.id.fab_add); // FloatingActionButton để thêm thói quen mới

        // Khởi tạo database helper
        db = new Database(getActivity());

        // Tải danh sách thói quen từ cơ sở dữ liệu
        loadHabits();

        // Xử lý sự kiện khi nhấn FAB để thêm thói quen
        fabAdd.setOnClickListener(v -> showAddHabitDialog());

        return view;
    }

    // Phương thức tải danh sách thói quen từ cơ sở dữ liệu
    private void loadHabits() {
        // Lấy danh sách thói quen từ cơ sở dữ liệu theo userId
        habitList = db.getAllHabits(userId);

        if (habitList.isEmpty()) {
            // Hiển thị thông báo nếu chưa có thói quen nào
            Toast.makeText(getActivity(), "No habits found. Start by adding one!", Toast.LENGTH_SHORT).show();
            return;
        }

        long currentTime = System.currentTimeMillis(); // Thời gian hiện tại
        List<Habit> habitsToReset = new ArrayList<>();

        // Kiểm tra các thói quen cần đặt lại trạng thái
        for (Habit habit : habitList) {
            long lastReset = habit.getLastReset();
            boolean needsReset = false;

            if (habit.getTimePeriod().equals("Every Day")) {
                needsReset = currentTime - lastReset >= 24 * 60 * 60 * 1000; // 24 giờ
            } else if (habit.getTimePeriod().equals("Every Week")) {
                needsReset = currentTime - lastReset >= 7 * 24 * 60 * 60 * 1000; // 7 ngày
            } else if (habit.getTimePeriod().equals("Every Month")) {
                needsReset = currentTime - lastReset >= 30 * 24 * 60 * 60 * 1000; // Khoảng 1 tháng
            } else if (habit.getTimePeriod().equals("Every Year")) {
                needsReset = currentTime - lastReset >= 365 * 24 * 60 * 60 * 1000; // Khoảng 1 năm
            }

            if (needsReset) {
                habitsToReset.add(habit);
            }
        }

        // Cập nhật trạng thái các thói quen cần đặt lại
        for (Habit habit : habitsToReset) {
            boolean resetSuccess = db.updateHabitReset(habit.getId(), currentTime);
            if (resetSuccess) {
                habit.setStatus(0); // Đặt trạng thái chưa hoàn thành
                habit.setLastReset(currentTime); // Cập nhật thời gian reset
            } else {
                Toast.makeText(getActivity(), "Failed to reset habit: " + habit.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }

        // Thiết lập RecyclerView với danh sách thói quen và adapter
        habitAdapter = new HabitAdapter(getActivity(), habitList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(habitAdapter);
    }

    // Phương thức hiển thị dialog để thêm thói quen mới
    private void showAddHabitDialog() {
        HabitAddDialog dialog = new HabitAddDialog(getActivity(), new HabitAddDialog.OnHabitAddedListener() {
            @Override
            public void onHabitAdded(Habit newHabit) {
                newHabit.setUserId(userId); // Gán userId cho thói quen mới
                int id = db.addHabit(userId, newHabit.getTitle(), newHabit.getTimePeriod(), newHabit.getStatus(), newHabit.getDescription());

                if (id == -1) {
                    // Thông báo lỗi nếu không thể thêm thói quen
                    Toast.makeText(getActivity(), "Failed to add habit. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                newHabit.setId(id); // Gán ID trả về từ cơ sở dữ liệu
                habitList.add(newHabit); // Thêm thói quen vào danh sách
                habitAdapter.notifyItemInserted(habitList.size() - 1); // Cập nhật RecyclerView
            }
        }, userId);

        dialog.show(); // Hiển thị dialog
    }
}
