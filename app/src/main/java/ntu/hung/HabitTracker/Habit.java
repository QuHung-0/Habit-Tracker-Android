package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

public class Habit
{
    private int id; // ID duy nhất cho thói quen
    private String title; // Tiêu đề của thói quen
    private String timePeriod; // Khoảng thời gian (ví dụ: hàng ngày, hàng tuần, v.v.)
    private int status; // 0: Chưa hoàn thành, 1: Đã hoàn thành
    private long lastReset; // Dấu thời gian lần đặt lại cuối cùng
    private String description; // Mô tả (tùy chọn)
    private int userId; // ID của người dùng liên kết với thói quen (dùng trong cơ sở dữ liệu)


    // Constructor cho thói quen đã tồn tại (bao gồm cả ID và userId)
    public Habit(int id, String title, String timePeriod, int status, long lastReset, String description, int userId)
    {
        this.id = id;
        this.title = title;
        this.timePeriod = timePeriod;
        this.status = status;
        this.lastReset = lastReset;
        this.description = description;
        this.userId = userId;
    }

    // Các phương thức Getter và Setter

    // Lấy ID của thói quen
    public int getId()
    {
        return id;
    }

    // Đặt ID cho thói quen
    public void setId(int id)
    {
        this.id = id;
    }

    // Lấy tiêu đề của thói quen
    public String getTitle()
    {
        return title;
    }

    // Đặt tiêu đề cho thói quen
    public void setTitle(String title)
    {
        this.title = title;
    }

    // Lấy khoảng thời gian của thói quen
    public String getTimePeriod()
    {
        return timePeriod;
    }

    // Đặt khoảng thời gian cho thói quen
    public void setTimePeriod(String timePeriod)
    {
        this.timePeriod = timePeriod;
    }

    // Lấy trạng thái của thói quen
    public int getStatus()
    {
        return status;
    }

    // Đặt trạng thái cho thói quen
    public void setStatus(int status)
    {
        this.status = status;
    }

    // Lấy thời gian đặt lại cuối cùng
    public long getLastReset()
    {
        return lastReset;
    }

    // Đặt thời gian đặt lại cuối cùng
    public void setLastReset(long lastReset)
    {
        this.lastReset = lastReset;
    }

    // Lấy mô tả của thói quen
    public String getDescription()
    {
        return description;
    }

    // Đặt mô tả cho thói quen
    public void setDescription(String description)
    {
        this.description = description;
    }

    // Lấy ID của người dùng liên kết
    public int getUserId()
    {
        return userId;
    }

    // Đặt ID của người dùng liên kết
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
}
