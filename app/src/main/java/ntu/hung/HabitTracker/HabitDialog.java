package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án
import android.app.Dialog; // Import lớp Dialog để tạo hộp thoại tùy chỉnh
import android.content.Context; // Import Context để truyền ngữ cảnh
import android.os.Bundle; // Import Bundle để truyền dữ liệu khi tạo Activity/Fragment
import android.view.View; // Import View để xử lý giao diện người dùng
import android.widget.Button; // Import Button để xử lý các nút bấm
import android.widget.EditText; // Import EditText để nhập dữ liệu
import android.widget.RadioButton; // Import RadioButton để tạo lựa chọn
import android.widget.RadioGroup; // Import RadioGroup để nhóm các RadioButton

import androidx.annotation.NonNull; // Import annotation để đảm bảo các tham số không null

import java.text.SimpleDateFormat; // Import để định dạng ngày
import java.util.Calendar; // Import để làm việc với thời gian
import java.util.Locale; // Import để thiết lập định dạng theo ngôn ngữ

// Lớp HabitDialog để hiển thị và xử lý hộp thoại thêm/sửa thói quen
public class HabitDialog extends Dialog {

    private final Habit habit; // Đối tượng Habit chứa thông tin thói quen
    private final boolean isEditMode; // Biến xác định chế độ thêm mới hay chỉnh sửa
    private final HabitDialogListener listener; // Lắng nghe sự kiện lưu thói quen

    // Constructor để khởi tạo hộp thoại với các tham số cần thiết
    public HabitDialog(@NonNull Context context, Habit habit, boolean isEditMode, HabitDialogListener listener) {
        super(context); // Gọi constructor của lớp cha
        this.habit = habit; // Lưu đối tượng Habit
        this.isEditMode = isEditMode; // Lưu trạng thái chế độ
        this.listener = listener; // Lưu listener để gọi lại
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Gọi phương thức của lớp cha
        setContentView(R.layout.fragment_habit_add); // Thiết lập giao diện hộp thoại

        // Liên kết các thành phần giao diện
        EditText etTitle = findViewById(R.id.et_habit_title); // Trường nhập tên thói quen
        EditText etDescription = findViewById(R.id.et_description); // Trường nhập mô tả
        RadioGroup rgTimePeriod = findViewById(R.id.rg_time_period); // Nhóm các lựa chọn thời gian
        Button btnAction = findViewById(R.id.btn_add_habit); // Nút thêm hoặc lưu thói quen

        // Nếu là chế độ chỉnh sửa, hiển thị dữ liệu hiện có
        if (isEditMode) {
            etTitle.setText(habit.getHabitName()); // Hiển thị tên thói quen
            etDescription.setText(habit.getHabitDescription()); // Hiển thị mô tả thói quen

            // Đặt lựa chọn thời gian dựa trên thói quen hiện tại
            switch (habit.getTimePeriod()) {
                case "1day":
                    rgTimePeriod.check(R.id.rb_every_day);
                    break;
                case "1week":
                    rgTimePeriod.check(R.id.rb_every_week);
                    break;
                case "1month":
                    rgTimePeriod.check(R.id.rb_every_month);
                    break;
                case "1year":
                    rgTimePeriod.check(R.id.rb_every_year);
                    break;
            }
            btnAction.setText("Save Habit"); // Thay đổi nhãn nút thành "Save Habit"
        }

        // Xử lý sự kiện khi bấm nút hành động (thêm hoặc lưu)
        btnAction.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim(); // Lấy tiêu đề thói quen
            String description = etDescription.getText().toString().trim(); // Lấy mô tả
            String timePeriod = "1 day"; // Thời gian mặc định

            // Lấy lựa chọn thời gian từ RadioGroup
            int selectedId = rgTimePeriod.getCheckedRadioButtonId();
            if (selectedId == R.id.rb_every_day) timePeriod = "1 day";
            else if (selectedId == R.id.rb_every_week) timePeriod = "1 week";
            else if (selectedId == R.id.rb_every_month) timePeriod = "1 month";
            else if (selectedId == R.id.rb_every_year) timePeriod = "1 year";

            // Kiểm tra nếu tiêu đề thói quen rỗng, hiển thị lỗi
            if (title.isEmpty()) {
                etTitle.setError("Habit title is required"); // Hiển thị lỗi
                return; // Dừng xử lý
            }

            // Lấy ngày hiện tại
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Calendar.getInstance().getTime());
            // Tính toán ngày kết thúc dựa trên thời gian được chọn
            String endDate = calculateEndDate(currentDate, timePeriod);

            // Cập nhật dữ liệu thói quen
            habit.setHabitName(title);
            habit.setHabitDescription(description);
            habit.setTimePeriod(timePeriod);
            habit.setBeginDate(currentDate);
            habit.setEndDate(endDate);

            // Gửi dữ liệu thói quen qua listener và đóng hộp thoại
            listener.onHabitSaved(habit, isEditMode);
            dismiss(); // Đóng hộp thoại
        });
    }

    // Phương thức tính toán ngày kết thúc dựa trên thời gian bắt đầu và thời gian lặp lại
    private String calculateEndDate(String beginDate, String timePeriod) {
        Calendar calendar = Calendar.getInstance(); // Tạo đối tượng Calendar
        try {
            // Chuyển đổi chuỗi ngày bắt đầu thành đối tượng Date
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(beginDate));
        } catch (Exception e) {
            e.printStackTrace(); // Hiển thị lỗi nếu có
        }

        // Thêm thời gian lặp lại vào ngày bắt đầu
        switch (timePeriod) {
            case "1 day":
                calendar.add(Calendar.DAY_OF_YEAR, 1); // Thêm 1 ngày
                break;
            case "1 week":
                calendar.add(Calendar.WEEK_OF_YEAR, 1); // Thêm 1 tuần
                break;
            case "1 month":
                calendar.add(Calendar.MONTH, 1); // Thêm 1 tháng
                break;
            case "1 year":
                calendar.add(Calendar.YEAR, 1); // Thêm 1 năm
                break;
        }

        // Trả về ngày kết thúc dưới dạng chuỗi
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    // Giao diện lắng nghe sự kiện khi lưu thói quen
    public interface HabitDialogListener {
        void onHabitSaved(Habit habit, boolean isEditMode); // Phương thức gọi lại khi thói quen được lưu
    }
}

