package ntu.hung.habitapp;


public class Habit {
    private int id; // ID của thói quen
    private int userId; // ID của người dùng
    private String habitName; // Tên thói quen
    private String habitDescription; // Mô tả thói quen
    private int status; // 0 = chưa hoàn thành, 1 = đã hoàn thành
    private String timePeriod; // Thời gian thực hiện (hàng ngày, hàng tuần, v.v.)
    private String beginDate; // Ngày bắt đầu
    private String endDate; // Ngày kết thúc

    // Constructor mặc định
    public Habit() {
        // Khởi tạo với giá trị mặc định nếu cần
    }

    // Constructor có tham số
    public Habit(int id, int userId, String habitName, String habitDescription, int status, String timePeriod, String beginDate, String endDate) {
        this.id = id;
        this.userId = userId;
        this.habitName = habitName;
        this.habitDescription = habitDescription;
        this.status = status;
        this.timePeriod = timePeriod;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    // Các hàm getter và setter cho các thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitDescription() {
        return habitDescription;
    }

    public void setHabitDescription(String habitDescription) {
        this.habitDescription = habitDescription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
