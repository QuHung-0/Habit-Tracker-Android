package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper
{

    // Hằng số cơ sở dữ liệu
    private static final String DATABASE_NAME = "HabitApp.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 3; // Phiên bản cơ sở dữ liệu

    // Hằng số bảng người dùng
    private static final String TABLE_USERS = "users"; // Tên bảng người dùng
    private static final String COLUMN_USER_ID = "id"; // Cột ID người dùng
    private static final String COLUMN_USER_EMAIL = "email"; // Cột email người dùng
    private static final String COLUMN_USER_PASSWORD = "password"; // Cột mật khẩu người dùng

    // Hằng số bảng thói quen
    private static final String TABLE_HABITS = "habits"; // Tên bảng thói quen
    private static final String COLUMN_HABIT_ID = "id"; // Cột ID thói quen
    private static final String COLUMN_HABIT_TITLE = "title"; // Cột tiêu đề thói quen
    private static final String COLUMN_HABIT_TIME_PERIOD = "time_period"; // Cột khoảng thời gian
    private static final String COLUMN_HABIT_STATUS = "status"; // Cột trạng thái
    private static final String COLUMN_HABIT_LAST_RESET = "last_reset"; // Cột lần đặt lại cuối cùng
    private static final String COLUMN_HABIT_DESCRIPTION = "description"; // Cột mô tả
    private static final String COLUMN_USER_REF_ID = "user_id"; // Cột tham chiếu ID người dùng

    // Constructor
    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Gọi constructor của lớp cha
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Tạo bảng người dùng
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Tự động tăng ID
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " + // Email là duy nhất
                COLUMN_USER_PASSWORD + " TEXT)"; // Mật khẩu
        db.execSQL(createUsersTable);

        // Tạo bảng thói quen
        String createHabitsTable = "CREATE TABLE " + TABLE_HABITS + " (" +
                COLUMN_HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Tự động tăng ID thói quen
                COLUMN_HABIT_TITLE + " TEXT, " + // Tiêu đề thói quen
                COLUMN_HABIT_TIME_PERIOD + " TEXT, " + // Khoảng thời gian thói quen
                COLUMN_HABIT_STATUS + " INTEGER, " + // Trạng thái (VD: hoàn thành/chưa hoàn thành)
                COLUMN_HABIT_LAST_RESET + " INTEGER DEFAULT 0, " + // Thời gian đặt lại cuối
                COLUMN_HABIT_DESCRIPTION + " TEXT, " + // Mô tả thói quen
                COLUMN_USER_REF_ID + " INTEGER, " + // Tham chiếu đến ID người dùng
                "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createHabitsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < 3)
        {
            // Xóa bảng cũ nếu tồn tại và tạo lại
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
            onCreate(db);
        }
    }

    // PHƯƠNG THỨC NGƯỜI DÙNG

    // Thêm người dùng mới
    public boolean insertUser(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email); // Thêm email vào ContentValues
        values.put(COLUMN_USER_PASSWORD, password); // Thêm mật khẩu vào ContentValues
        long result = db.insert(TABLE_USERS, null, values); // Thực hiện lệnh chèn
        return result != -1; // Trả về true nếu chèn thành công
    }

    // Xác thực đăng nhập người dùng
    public boolean validateUser(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu ở chế độ đọc
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null); // Truy vấn với điều kiện email và mật khẩu
        boolean isValid = cursor.moveToFirst(); // Kiểm tra nếu có bản ghi khớp
        cursor.close(); // Đóng con trỏ
        return isValid;
    }

    // PHƯƠNG THỨC THÓI QUEN

    // Thêm thói quen mới
    public int addHabit(int userId, String title, String timePeriod, int status, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_TITLE, title);
        values.put(COLUMN_HABIT_TIME_PERIOD, timePeriod);
        values.put(COLUMN_HABIT_STATUS, status);
        values.put(COLUMN_HABIT_DESCRIPTION, description);
        values.put(COLUMN_USER_REF_ID, userId); // Tham chiếu đến ID người dùng

        long id = db.insert(TABLE_HABITS, null, values); // Thực hiện chèn dữ liệu
        return (int) id; // Trả về ID của thói quen vừa được thêm
    }

    // Cập nhật trạng thái thói quen
    public boolean updateHabitStatus(int habitId, int newStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_STATUS, newStatus); // Cập nhật trạng thái mới
        int rowsAffected = db.update(TABLE_HABITS, values, COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    // Cập nhật thời gian đặt lại của thói quen
    public boolean updateHabitReset(int habitId, long newLastReset)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_LAST_RESET, newLastReset); // Cập nhật thời gian đặt lại mới
        values.put(COLUMN_HABIT_STATUS, 0); // Đặt trạng thái về "Chưa hoàn thành"
        int rowsAffected = db.update(TABLE_HABITS, values, COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    // Lấy tất cả các thói quen của một người dùng
    public List<Habit> getAllHabits(int userId)
    {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABITS, null, COLUMN_USER_REF_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HABIT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TITLE));
                String timePeriod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TIME_PERIOD));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HABIT_STATUS));
                long lastReset = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HABIT_LAST_RESET));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_DESCRIPTION));

                // Tạo đối tượng Habit và thêm vào danh sách
                Habit habit = new Habit(id, title, timePeriod, status, lastReset, description, userId);
                habitList.add(habit);
            }
            while (cursor.moveToNext());
        }

        cursor.close(); // Đóng con trỏ
        return habitList; // Trả về danh sách thói quen
    }

}



