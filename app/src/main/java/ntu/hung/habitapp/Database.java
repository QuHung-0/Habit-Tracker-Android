package ntu.hung.habitapp;

// Import các thư viện cần thiết để làm việc với Android và SQLite

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// Lớp Database kế thừa SQLiteOpenHelper để quản lý cơ sở dữ liệu
public class Database extends SQLiteOpenHelper
{

    //================PHẦN BẮT ĐẦU CƠ SỞ DỮ LIỆU=================//

    // Tên của cơ sở dữ liệu
    private static final String DATABASE_NAME = "BeAllEndAllApp.db";

    // Phiên bản của cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1;

    // Constructor: tạo đối tượng Database
    public Database(Context context)
    {
        // Gọi constructor của lớp SQLiteOpenHelper
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Phương thức onCreate: tạo bảng trong cơ sở dữ liệu
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Tạo bảng 'users' để lưu thông tin người dùng
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Cột 'id' là khóa chính tự động tăng
                "email TEXT UNIQUE, " + // Cột 'email' phải là duy nhất
                "password TEXT)"; // Cột 'password' để lưu mật khẩu
        db.execSQL(createUsersTable); // Thực thi lệnh SQL

        // Tạo bảng 'habits' để lưu thông tin thói quen
        String createHabitsTable = "CREATE TABLE habits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Khóa chính tự động tăng
                "user_id INTEGER, " + // Liên kết với bảng 'users'
                "habit_name TEXT NOT NULL, " + // Tên thói quen (không được để trống)
                "habit_description TEXT, " + // Mô tả thói quen
                "status INTEGER DEFAULT 0, " + // Trạng thái: 0 = chưa hoàn thành, 1 = đã hoàn thành
                "time_period TEXT NOT NULL, " + // Thời gian (vd: "1day", "1week")
                "begin_date TEXT NOT NULL, " + // Ngày bắt đầu (ISO 8601)
                "end_date TEXT NOT NULL, " +   // Ngày kết thúc (ISO 8601)
                "FOREIGN KEY (user_id) REFERENCES users(id))"; // Ràng buộc khóa ngoại
        db.execSQL(createHabitsTable); // Thực thi lệnh SQL

        // Tạo bảng 'mood' để lưu thông tin cảm xúc
        String createMoodTable = "CREATE TABLE mood (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Khóa chính tự động tăng
                "user_id INTEGER, " + // Liên kết với bảng 'users'
                "date TEXT UNIQUE, " + // Ngày cảm xúc (duy nhất)
                "emotion INTEGER, " + // Chỉ số cảm xúc
                "note TEXT, " + // Ghi chú
                "FOREIGN KEY (user_id) REFERENCES users(id))"; // Ràng buộc khóa ngoại
        db.execSQL(createMoodTable); // Thực thi lệnh SQL
    }

    // Phương thức onUpgrade: nâng cấp cơ sở dữ liệu (xóa và tạo lại bảng)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Xóa các bảng nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS habits");
        db.execSQL("DROP TABLE IF EXISTS mood");
        // Tạo lại các bảng
        onCreate(db);
    }

    //=================PHẦN KẾT THÚC CƠ SỞ DỮ LIỆU==================//

    //==============PHẦN BẮT ĐẦU TẠO NGƯỜI DÙNG==============//

    // Đăng ký người dùng mới
    public boolean registerUser(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues(); // Lưu trữ giá trị để chèn
        values.put("email", email); // Đặt email
        values.put("password", password); // Đặt mật khẩu

        try
        {
            db.insertOrThrow("users", null, values); // Chèn dữ liệu vào bảng 'users'
            return true; // Đăng ký thành công
        }
        catch (Exception e)
        {
            return false; // Người dùng đã tồn tại
        }
    }

    // Xác minh đăng nhập của người dùng
    public int validateUser(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu để đọc
        // Truy vấn bảng 'users' để tìm email và mật khẩu khớp
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        if (cursor.moveToFirst())
        { // Nếu có kết quả
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("id")); // Lấy id người dùng
            cursor.close(); // Đóng con trỏ
            return userId; // Trả về id người dùng
        }
        else
        {
            cursor.close(); // Đóng con trỏ
            return -1; // Đăng nhập thất bại
        }
    }

//===============PHẦN KẾT THÚC TẠO NGƯỜI DÙNG===============//


    //==============PHẦN BẮT ĐẦU TẠO THÓI QUEN=============//

    // Lấy danh sách các thói quen theo id người dùng
    public List<Habit> getHabitsByUserId(int userId)
    {
        List<Habit> habits = new ArrayList<>(); // Tạo danh sách để lưu thói quen
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu để đọc
        // Truy vấn bảng 'habits' theo user_id
        Cursor cursor = db.rawQuery("SELECT * FROM habits WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst())
        { // Nếu có dữ liệu
            do
            {
                // Tạo đối tượng Habit từ dữ liệu trong con trỏ
                Habit habit = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Lấy id thói quen
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")), // Lấy id người dùng
                        cursor.getString(cursor.getColumnIndexOrThrow("habit_name")), // Lấy tên thói quen
                        cursor.getString(cursor.getColumnIndexOrThrow("habit_description")), // Lấy mô tả
                        cursor.getInt(cursor.getColumnIndexOrThrow("status")), // Lấy trạng thái
                        cursor.getString(cursor.getColumnIndexOrThrow("time_period")), // Lấy thời gian
                        cursor.getString(cursor.getColumnIndexOrThrow("begin_date")), // Lấy ngày bắt đầu
                        cursor.getString(cursor.getColumnIndexOrThrow("end_date")) // Lấy ngày kết thúc
                );
                habits.add(habit); // Thêm thói quen vào danh sách
            }
            while (cursor.moveToNext()); // Di chuyển đến dòng tiếp theo
        }

        cursor.close(); // Đóng con trỏ
        db.close(); // Đóng cơ sở dữ liệu
        return habits; // Trả về danh sách thói quen
    }

    // Thêm một thói quen mới vào cơ sở dữ liệu
    public void addHabit(Habit habit)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để lưu dữ liệu
        values.put("user_id", habit.getUserId()); // Lưu id người dùng
        values.put("habit_name", habit.getHabitName()); // Lưu tên thói quen
        values.put("habit_description", habit.getHabitDescription()); // Lưu mô tả thói quen
        values.put("status", habit.getStatus()); // Lưu trạng thái
        values.put("time_period", habit.getTimePeriod()); // Lưu thời gian
        values.put("begin_date", habit.getBeginDate()); // Lưu ngày bắt đầu
        values.put("end_date", habit.getEndDate()); // Lưu ngày kết thúc
        db.insert("habits", null, values); // Chèn dữ liệu vào bảng 'habits'
        db.close(); // Đóng cơ sở dữ liệu
    }

    // Cập nhật trạng thái của thói quen (ví dụ: đã hoàn thành hay chưa)
    public void updateHabitStatus(int habitId, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để lưu dữ liệu
        values.put("status", status); // Lưu trạng thái mới
        db.update("habits", values, "id = ?", new String[]{String.valueOf(habitId)}); // Cập nhật trạng thái theo id thói quen
        db.close(); // Đóng cơ sở dữ liệu
    }

    // Cập nhật thông tin chi tiết của thói quen
    public void updateHabit(Habit habit)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để lưu dữ liệu
        values.put("habit_name", habit.getHabitName()); // Lưu tên thói quen
        values.put("habit_description", habit.getHabitDescription()); // Lưu mô tả
        values.put("status", habit.getStatus()); // Lưu trạng thái
        values.put("time_period", habit.getTimePeriod()); // Lưu thời gian
        values.put("begin_date", habit.getBeginDate()); // Lưu ngày bắt đầu
        values.put("end_date", habit.getEndDate()); // Lưu ngày kết thúc
        db.update("habits", values, "id = ?", new String[]{String.valueOf(habit.getId())}); // Cập nhật dữ liệu theo id thói quen
        db.close(); // Đóng cơ sở dữ liệu
    }

    // Xóa một thói quen khỏi cơ sở dữ liệu
    public void deleteHabit(int habitId)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        db.delete("habits", "id = ?", new String[]{String.valueOf(habitId)}); // Xóa thói quen theo id
        db.close(); // Đóng cơ sở dữ liệu
    }

    //===============PHẦN KẾT THÚC TẠO THÓI QUEN==============//

    //===============PHẦN BẮT ĐẦU TẠO CẢM XÚC=============//

    // Lấy thông tin cảm xúc theo ngày
    public Mood getMoodByDate(int userId, String date)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu để đọc
        // Truy vấn bảng 'mood' theo user_id và ngày
        Cursor cursor = db.query("mood", null, "user_id = ? AND date = ?",
                new String[]{String.valueOf(userId), date}, null, null, null);

        if (cursor.moveToFirst())
        { // Nếu có dữ liệu
            // Tạo đối tượng Mood từ dữ liệu trong con trỏ
            Mood mood = new Mood(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Lấy id cảm xúc
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")), // Lấy id người dùng
                    cursor.getString(cursor.getColumnIndexOrThrow("date")), // Lấy ngày
                    cursor.getInt(cursor.getColumnIndexOrThrow("emotion")), // Lấy chỉ số cảm xúc
                    cursor.getString(cursor.getColumnIndexOrThrow("note")) // Lấy ghi chú
            );
            cursor.close(); // Đóng con trỏ
            return mood; // Trả về đối tượng Mood
        }
        cursor.close(); // Đóng con trỏ
        return null; // Không có dữ liệu cho ngày đã cho
    }

    // Lưu thông tin cảm xúc (chèn hoặc cập nhật nếu đã tồn tại)
    public void saveMood(Mood mood)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để lưu dữ liệu
        values.put("user_id", mood.getUserId()); // Lưu id người dùng
        values.put("date", mood.getDate()); // Lưu ngày
        values.put("emotion", mood.getEmotion()); // Lưu chỉ số cảm xúc
        values.put("note", mood.getNote()); // Lưu ghi chú

        db.replace("mood", null, values); // Chèn hoặc thay thế dữ liệu nếu đã tồn tại
        db.close(); // Đóng cơ sở dữ liệu
    }

    //===============PHẦN KẾT THÚC TẠO CẢM XÚC===============//
}
