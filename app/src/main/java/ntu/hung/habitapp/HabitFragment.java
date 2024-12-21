package ntu.hung.habitapp;

// Nhập các thư viện cần thiết
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Định nghĩa lớp HabitFragment kế thừa Fragment
public class HabitFragment extends Fragment {

    // Các tham số để truyền dữ liệu khi tạo Fragment
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1; // Tham số 1
    private String mParam2; // Tham số 2

    // Hàm khởi tạo mặc định (bắt buộc có trong Fragment)
    public HabitFragment() {}

    // Tạo một phiên bản mới của HabitFragment với các tham số
    public static HabitFragment newInstance(String param1, String param2) {
        HabitFragment fragment = new HabitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Gọi khi Fragment được tạo
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Các biến thành viên
    private RecyclerView recyclerView; // RecyclerView hiển thị danh sách thói quen
    private HabitAdapter habitAdapter; // Adapter để kết nối dữ liệu với RecyclerView
    private List<Habit> habitList; // Danh sách thói quen
    private FloatingActionButton fabAdd; // Nút thêm thói quen
    private Database dbHelper; // Đối tượng làm việc với cơ sở dữ liệu

    // Tạo giao diện cho Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout từ file XML
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        // Khởi tạo các thành phần trong giao diện
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAdd = view.findViewById(R.id.fab_add);

        // Tạo danh sách rỗng để lưu thói quen
        habitList = new ArrayList<>();
        // Khởi tạo đối tượng làm việc với cơ sở dữ liệu
        dbHelper = new Database(requireContext());

        // Khởi tạo adapter để hiển thị danh sách thói quen
        habitAdapter = new HabitAdapter(habitList, new HabitAdapter.OnHabitClickListener() {
            @Override
            public void onEditHabit(Habit habit) {
                openEditHabitDialog(habit); // Mở hộp thoại chỉnh sửa
            }

            @Override
            public void onDeleteHabit(Habit habit) {
                showDeleteConfirmation(habit); // Hiển thị xác nhận xóa
            }

            @Override
            public void onStatusChanged(Habit habit, boolean isChecked) {
                // Cập nhật trạng thái của thói quen
                habit.setStatus(isChecked ? 1 : 0);
                dbHelper.updateHabitStatus(habit.getId(), habit.getStatus());
            }
        });

        // Đặt layout dạng danh sách dọc cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(habitAdapter);

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        // Tải danh sách thói quen của người dùng
        loadHabits(userId);

        // Gắn sự kiện cho nút thêm thói quen
        fabAdd.setOnClickListener(v -> openAddHabitDialog(userId));

        return view; // Trả về giao diện đã được tạo
    }

    // Tải danh sách thói quen từ cơ sở dữ liệu
    private void loadHabits(int userId) {
        habitList.clear(); // Xóa danh sách hiện tại
        habitList.addAll(dbHelper.getHabitsByUserId(userId)); // Thêm danh sách từ cơ sở dữ liệu
        checkHabitStatus(); // Kiểm tra và cập nhật trạng thái thói quen
        habitAdapter.notifyDataSetChanged(); // Cập nhật giao diện
    }

    // Kiểm tra trạng thái của từng thói quen
    private void checkHabitStatus() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateStr = sdf.format(new Date()); // Lấy ngày hiện tại

        for (Habit habit : habitList) {
            String endDateStr = habit.getEndDate();
            try {
                Date currentDate = sdf.parse(currentDateStr);
                Date endDate = sdf.parse(endDateStr);
                if (currentDate != null && endDate != null && !currentDate.before(endDate)) {
                    habit.setStatus(0); // Đặt trạng thái là chưa hoàn thành
                    habit.setBeginDate(currentDateStr); // Đặt lại ngày bắt đầu
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    addPeriodToDate(cal, habit.getTimePeriod()); // Thêm khoảng thời gian
                    habit.setEndDate(sdf.format(cal.getTime())); // Cập nhật ngày kết thúc
                    dbHelper.updateHabit(habit); // Cập nhật trong cơ sở dữ liệu
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Thêm khoảng thời gian vào ngày hiện tại
    private void addPeriodToDate(Calendar cal, String timePeriod) {
        if ("1 day".equalsIgnoreCase(timePeriod)) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else if ("1 week".equalsIgnoreCase(timePeriod)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        } else if ("1 month".equalsIgnoreCase(timePeriod)) {
            cal.add(Calendar.MONTH, 1);
        } else if ("1 year".equalsIgnoreCase(timePeriod)) {
            cal.add(Calendar.YEAR, 1);
        }
    }

    // Mở hộp thoại thêm thói quen
    private void openAddHabitDialog(int userId) {
        HabitDialog dialog = new HabitDialog(getContext(), new Habit(), false, (habit, isEditMode) -> {
            habit.setUserId(userId);
            dbHelper.addHabit(habit);
            loadHabits(userId);
        });
        dialog.show();
    }

    // Mở hộp thoại chỉnh sửa thói quen
    private void openEditHabitDialog(Habit habit) {
        HabitDialog dialog = new HabitDialog(getContext(), habit, true, (updatedHabit, isEditMode) -> {
            dbHelper.updateHabit(updatedHabit);
            loadHabits(updatedHabit.getUserId());
        });
        dialog.show();
    }

    // Hiển thị hộp thoại xác nhận xóa thói quen
    private void showDeleteConfirmation(Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Habit");
        builder.setMessage("Are you sure you want to delete this habit?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            dbHelper.deleteHabit(habit.getId());
            loadHabits(habit.getUserId());
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
