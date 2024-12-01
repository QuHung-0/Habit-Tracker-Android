package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án


import android.os.Bundle; // Thư viện để làm việc với dữ liệu chuyển giữa các thành phần
import android.view.LayoutInflater; // Thư viện để xử lý giao diện người dùng
import android.view.View; // Thư viện đại diện cho các thành phần giao diện
import android.view.ViewGroup; // Thư viện đại diện cho nhóm các thành phần giao diện

import androidx.fragment.app.Fragment; // Thư viện để sử dụng Fragment (giao diện con)

// Lớp HabitFragment kế thừa Fragment để tạo giao diện thói quen
public class HabitFragment extends Fragment
{

    // Các tham số truyền vào Fragment khi khởi tạo
    private static final String ARG_PARAM1 = "param1"; // Tham số 1
    private static final String ARG_PARAM2 = "param2"; // Tham số 2

    // Biến lưu trữ giá trị của tham số
    private String mParam1;
    private String mParam2;

    // Constructor mặc định (bắt buộc) của Fragment
    public HabitFragment()
    {
        // Constructor trống
    }

    /**
     * Phương thức tạo một đối tượng HabitFragment mới
     *
     * @param param1 Tham số 1
     * @param param2 Tham số 2
     * @return Một đối tượng HabitFragment
     */
    public static HabitFragment newInstance(String param1, String param2)
    {
        HabitFragment fragment = new HabitFragment(); // Tạo mới Fragment
        Bundle args = new Bundle(); // Tạo đối tượng Bundle để truyền tham số
        args.putString(ARG_PARAM1, param1); // Gán tham số 1 vào Bundle
        args.putString(ARG_PARAM2, param2); // Gán tham số 2 vào Bundle
        fragment.setArguments(args); // Truyền Bundle vào Fragment
        return fragment; // Trả về Fragment mới
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); // Gọi phương thức onCreate của Fragment cha
        if (getArguments() != null)
        { // Kiểm tra nếu có tham số được truyền vào
            mParam1 = getArguments().getString(ARG_PARAM1); // Lấy tham số 1
            mParam2 = getArguments().getString(ARG_PARAM2); // Lấy tham số 2
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Thực hiện khởi tạo giao diện cho Fragment
        return inflater.inflate(R.layout.fragment_habit, container, false); // Trả về bố cục của Fragment
    }
}
