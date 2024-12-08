package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder>
{

    private Context context; // Ngữ cảnh của ứng dụng (thường là Activity hoặc Fragment)
    private List<Habit> habitList; // Danh sách các thói quen cần hiển thị
    private Database db; // Đối tượng cơ sở dữ liệu để thực hiện các thao tác

    // Constructor để khởi tạo HabitAdapter
    public HabitAdapter(Context context, List<Habit> habitList, Database db)
    {
        this.context = context;
        this.habitList = habitList;
        this.db = db;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflate layout của mỗi mục (item) từ file XML
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position)
    {
        // Lấy đối tượng Habit tại vị trí hiện tại
        Habit habit = habitList.get(position);

        // Đặt tiêu đề cho thói quen
        holder.title.setText(habit.getTitle());

        // Kiểm tra và hiển thị mô tả (nếu có)
        if (!TextUtils.isEmpty(habit.getDescription()))
        {
            holder.description.setVisibility(View.VISIBLE); // Hiển thị TextView mô tả
            holder.description.setText(habit.getDescription()); // Gán nội dung mô tả
        }
        else
        {
            holder.description.setVisibility(View.GONE); // Ẩn TextView nếu không có mô tả
        }

        // Hiển thị khoảng thời gian của thói quen (vd: "Hằng ngày", "Hằng tuần")
        holder.timePeriod.setText(habit.getTimePeriod());

        // Đặt trạng thái checkbox dựa trên trạng thái của thói quen
        holder.status.setOnCheckedChangeListener(null); // Xóa listener cũ để tránh lỗi vòng lặp
        holder.status.setChecked(habit.getStatus() == 1); // Đánh dấu nếu trạng thái là "Đã hoàn thành"

        // Lắng nghe sự kiện khi người dùng thay đổi trạng thái checkbox
        holder.status.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            habit.setStatus(isChecked ? 1 : 0); // Cập nhật trạng thái thói quen trong bộ nhớ cục bộ

            // Cập nhật trạng thái thói quen trong cơ sở dữ liệu
            boolean updateSuccess = db.updateHabitStatus(habit.getId(), habit.getStatus());
            if (!updateSuccess)
            {
                // Nếu cập nhật thất bại, hoàn tác và thông báo lỗi
                habit.setStatus(isChecked ? 0 : 1);
                holder.status.setChecked(!isChecked);
                Toast.makeText(context, "Cập nhật trạng thái thất bại: " + habit.getTitle(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                // Làm mới giao diện nếu cập nhật thành công
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        // Trả về số lượng thói quen trong danh sách
        return habitList.size();
    }

    // Lớp ViewHolder cho từng mục trong RecyclerView
    public static class HabitViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, timePeriod, description; // Các TextView hiển thị thông tin
        CheckBox status; // Checkbox để thay đổi trạng thái thói quen

        public HabitViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // Liên kết các View với ID trong layout
            title = itemView.findViewById(R.id.tv_habit_title); // Tiêu đề
            timePeriod = itemView.findViewById(R.id.tv_habit_time); // Khoảng thời gian
            description = itemView.findViewById(R.id.tv_habit_description); // Mô tả
            status = itemView.findViewById(R.id.cb_habit_status); // Trạng thái (checkbox)
        }
    }
}
