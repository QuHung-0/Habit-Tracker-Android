package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

public class HabitAddDialog extends Dialog {

    private EditText etHabitTitle, etDescription; // Các trường nhập liệu
    private RadioGroup rgTimePeriod; // Nhóm Radio để chọn khoảng thời gian
    private Button btnAddHabit; // Nút để thêm thói quen
    private final OnHabitAddedListener listener; // Lắng nghe sự kiện khi thói quen mới được thêm
    private final int userId; // ID của người dùng

    // Constructor để khởi tạo Dialog
    public HabitAddDialog(@NonNull Context context, OnHabitAddedListener listener, int userId) {
        super(context);
        this.listener = listener; // Gán listener để giao tiếp khi thói quen được thêm
        this.userId = userId; // Lưu ID người dùng
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_habit_dialog_add_habit); // Gán layout cho dialog

        // Liên kết các thành phần giao diện từ XML
        etHabitTitle = findViewById(R.id.et_habit_title); // Trường nhập tiêu đề thói quen
        rgTimePeriod = findViewById(R.id.rg_time_period); // Nhóm Radio cho khoảng thời gian
        etDescription = findViewById(R.id.et_description); // Trường nhập mô tả
        btnAddHabit = findViewById(R.id.btn_add_habit); // Nút để thêm thói quen

        // Mặc định chọn "Hằng ngày" nếu chưa có lựa chọn
        if (rgTimePeriod.getCheckedRadioButtonId() == -1) {
            rgTimePeriod.check(R.id.rb_every_day); // Chọn "Hằng ngày" làm mặc định
        }

        // Xử lý sự kiện khi người dùng nhấn nút thêm thói quen
        btnAddHabit.setOnClickListener(v -> {
            // Lấy và kiểm tra tiêu đề thói quen
            String title = etHabitTitle.getText().toString().trim();
            if (TextUtils.isEmpty(title)) { // Nếu tiêu đề trống, báo lỗi
                etHabitTitle.setError("Title cannot be empty");
                return;
            }

            // Lấy khoảng thời gian được chọn từ nhóm Radio
            String timePeriod = getTimePeriod();

            // Lấy mô tả (không bắt buộc)
            String description = etDescription.getText().toString().trim();

            // Tạo đối tượng Habit mới với trạng thái mặc định (chưa hoàn thành)
            // Gán `id` và `lastReset` bằng 0 vì chưa được lưu vào cơ sở dữ liệu
            Habit newHabit = new Habit(0, title, timePeriod, 0, 0, description, userId);

            // Gọi listener để gửi thói quen mới ra ngoài
            listener.onHabitAdded(newHabit);

            // Đóng dialog sau khi thói quen được thêm
            dismiss();
        });
    }

    // Phương thức lấy khoảng thời gian được chọn từ nhóm Radio
    private String getTimePeriod() {
        int selectedId = rgTimePeriod.getCheckedRadioButtonId(); // ID của RadioButton được chọn
        if (selectedId == R.id.rb_every_day) {
            return "Every Day";
        } else if (selectedId == R.id.rb_every_week) {
            return "Every Week";
        } else if (selectedId == R.id.rb_every_month) {
            return "Every Month";
        } else if (selectedId == R.id.rb_every_year) {
            return "Every Year";
        } else {
            return "Every Day"; // Mặc định nếu không chọn
        }
    }

    // Giao diện lắng nghe sự kiện khi một thói quen mới được thêm
    public interface OnHabitAddedListener {
        void onHabitAdded(Habit habit); // Phương thức được gọi khi thói quen được thêm
    }
}
