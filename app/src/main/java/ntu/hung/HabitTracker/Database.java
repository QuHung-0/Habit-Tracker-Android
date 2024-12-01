package ntu.hung.HabitTracker; // Khai báo gói chứa các lớp trong dự án

import android.content.ContentValues; // Thư viện để thao tác với cặp key-value
import android.content.Context; // Thư viện để truy cập thông tin ngữ cảnh ứng dụng
import android.database.Cursor; // Thư viện để duyệt qua các bản ghi từ cơ sở dữ liệu
import android.database.sqlite.SQLiteDatabase; // Thư viện để làm việc với cơ sở dữ liệu SQLite
import android.database.sqlite.SQLiteOpenHelper; // Thư viện để tạo và quản lý cơ sở dữ liệu SQLite

// Lớp Database kế thừa từ SQLiteOpenHelper để quản lý cơ sở dữ liệu
public class Database extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "HabitApp.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1; // Phiên bản cơ sở dữ liệu

    // Khai báo tên bảng và các cột cho bảng người dùng
    private static final String TABLE_USERS = "users"; // Tên bảng người dùng
    private static final String COLUMN_USER_ID = "id"; // Cột ID người dùng
    private static final String COLUMN_USER_EMAIL = "email"; // Cột email người dùng
    private static final String COLUMN_USER_PASSWORD = "password"; // Cột mật khẩu người dùng

    // Khai báo tên bảng và các cột cho bảng thói quen (dự phòng)
    private static final String TABLE_HABITS = "habits"; // Tên bảng thói quen
    private static final String COLUMN_HABIT_ID = "habit_id"; // Cột ID thói quen
    private static final String COLUMN_HABIT_NAME = "name"; // Cột tên thói quen
    private static final String COLUMN_USER_REF_ID = "user_id"; // Cột tham chiếu đến ID người dùng

    // Constructor cho lớp Database
    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Gọi constructor của lớp cha
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Tạo bảng người dùng
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Tự động tăng ID
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " + // Email phải là duy nhất
                COLUMN_USER_PASSWORD + " TEXT)"); // Lưu mật khẩu

        // Tạo bảng thói quen
        db.execSQL("CREATE TABLE " + TABLE_HABITS + " (" +
                COLUMN_HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Tự động tăng ID thói quen
                COLUMN_HABIT_NAME + " TEXT, " + // Lưu tên thói quen
                COLUMN_USER_REF_ID + " INTEGER, " + // Tham chiếu đến ID người dùng
                "FOREIGN KEY(" + COLUMN_USER_REF_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))"); // Ràng buộc khóa ngoại
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Xóa bảng nếu đã tồn tại khi nâng cấp cơ sở dữ liệu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
        onCreate(db); // Tạo lại bảng
    }

    // Thêm người dùng mới
    public boolean insertUser(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng chứa dữ liệu
        values.put(COLUMN_USER_EMAIL, email); // Thêm email vào giá trị
        values.put(COLUMN_USER_PASSWORD, password); // Thêm mật khẩu vào giá trị
        long result = db.insert(TABLE_USERS, null, values); // Chèn dữ liệu vào bảng
        return result != -1; // Trả về true nếu chèn thành công
    }

    // Xác minh đăng nhập người dùng
    public boolean validateUser(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu ở chế độ đọc
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID}, // Truy vấn tìm ID người dùng
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?", // Điều kiện email và mật khẩu
                new String[]{email, password}, null, null, null); // Giá trị cần so khớp
        boolean isValid = cursor.moveToFirst(); // Kiểm tra có kết quả hay không
        cursor.close(); // Đóng con trỏ
        return isValid; // Trả về true nếu thông tin hợp lệ
    }
}


