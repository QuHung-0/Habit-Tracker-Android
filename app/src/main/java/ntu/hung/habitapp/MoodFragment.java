package ntu.hung.habitapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

// MoodFragment quản lý giao diện theo dõi tâm trạng và các tương tác của người dùng
public class MoodFragment extends Fragment
{

    private Button btnOpenCalendar; // Nút để mở trình chọn ngày
    private EditText etNote; // Trường để ghi chú liên quan đến tâm trạng
    private CardView[] cards; // Mảng các CardView đại diện cho các trạng thái cảm xúc
    private int selectedEmotion = 0; // Biến theo dõi cảm xúc được chọn
    private String currentDate; // Ngày hiện tại được chọn

    private Database dbHelper; // Đối tượng hỗ trợ cơ sở dữ liệu
    private int userId; // ID người dùng đã đăng nhập

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Tải giao diện từ tệp XML cho fragment
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        // Khởi tạo các thành phần giao diện
        btnOpenCalendar = view.findViewById(R.id.btn_open_calendar); // Nút chọn ngày
        etNote = view.findViewById(R.id.et_note); // Ô nhập ghi chú
        cards = new CardView[]{
                view.findViewById(R.id.card_happy), // CardView biểu thị cảm xúc vui
                view.findViewById(R.id.card_sad),   // CardView biểu thị cảm xúc buồn
                view.findViewById(R.id.card_tired), // CardView biểu thị cảm xúc mệt mỏi
                view.findViewById(R.id.card_angry)  // CardView biểu thị cảm xúc tức giận
        };

        // Khởi tạo đối tượng cơ sở dữ liệu
        dbHelper = new Database(requireContext());

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        // Nếu ID người dùng không hợp lệ, trả về view nhưng không xử lý tiếp
        if (userId == -1)
        {
            return view;
        }

        // Mặc định sử dụng ngày hôm nay
        Calendar calendar = Calendar.getInstance();
        currentDate = DateUtils.formatISODate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        btnOpenCalendar.setText(DateUtils.formatDisplayDate(currentDate)); // Hiển thị ngày dưới dạng văn bản

        // Tải dữ liệu cho ngày hiện tại
        loadDataForDate(currentDate);

        // Xử lý sự kiện bấm nút chọn ngày
        btnOpenCalendar.setOnClickListener(v -> showDatePicker());

        // Lưu tự động khi người dùng nhập ghi chú
        etNote.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                saveData(); // Lưu dữ liệu tâm trạng khi ghi chú thay đổi
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        // Xử lý sự kiện bấm vào các CardView cảm xúc
        for (int i = 0; i < cards.length; i++)
        {
            final int emotionId = i + 1; // Ánh xạ chỉ số thành ID cảm xúc (1-4)
            cards[i].setOnClickListener(v -> selectEmotion(emotionId));
        }

        return view;
    }

    private void showDatePicker()
    {
        // Khởi tạo lịch
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị hộp thoại chọn ngày
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) ->
                {
                    // Cập nhật ngày hiện tại khi người dùng chọn
                    currentDate = DateUtils.formatISODate(year1, month1, dayOfMonth);
                    btnOpenCalendar.setText(DateUtils.formatDisplayDate(currentDate));
                    loadDataForDate(currentDate); // Tải dữ liệu cho ngày được chọn
                },
                year, month, day
        );
        datePickerDialog.show(); // Hiển thị hộp thoại
    }

    private void loadDataForDate(String date)
    {
        // Lấy dữ liệu tâm trạng cho người dùng và ngày được chỉ định
        Mood mood = dbHelper.getMoodByDate(userId, date);
        if (mood != null)
        {
            selectedEmotion = mood.getEmotion(); // Đặt cảm xúc
            String note = mood.getNote(); // Đặt ghi chú

            highlightSelectedEmotion(selectedEmotion); // Làm nổi bật cảm xúc
            etNote.setText(note); // Hiển thị ghi chú
        }
        else
        {
            // Xóa dữ liệu nếu không có mục tâm trạng nào cho ngày đó
            selectedEmotion = 0;
            highlightSelectedEmotion(0);
            etNote.setText("");
        }
    }

    private void saveData()
    {
        // Lưu dữ liệu tâm trạng vào cơ sở dữ liệu
        String note = etNote.getText().toString();
        Mood mood = new Mood(userId, currentDate, selectedEmotion, note); // Tạo đối tượng Mood
        dbHelper.saveMood(mood); // Lưu vào cơ sở dữ liệu
    }

    private void selectEmotion(int emotionId)
    {
        // Đặt và làm nổi bật cảm xúc được chọn
        selectedEmotion = emotionId;
        highlightSelectedEmotion(emotionId);
        saveData(); // Lưu cảm xúc đã cập nhật
    }

    private void highlightSelectedEmotion(int emotionId)
    {
        for (int i = 0; i < cards.length; i++)
        {
            cards[i].setCardBackgroundColor(
                    getResources().getColor(i + 1 == emotionId ? R.color.colorPrimary : R.color.white)
            );
        }
    }
}

