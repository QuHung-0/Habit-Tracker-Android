package ntu.hung.habitapp;

public class Mood {
    private int id; // ID của cảm xúc
    private int userId; // ID của người dùng
    private String date; // Ngày ghi nhận cảm xúc
    private int emotion; // Chỉ số cảm xúc (1-4, đại diện cho các loại cảm xúc)
    private String note; // Ghi chú thêm về cảm xúc

    // Constructor đầy đủ
    public Mood(int id, int userId, String date, int emotion, String note) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.emotion = emotion;
        this.note = note;
    }

    // Constructor không bao gồm ID (thường dùng khi thêm mới)
    public Mood(int userId, String date, int emotion, String note) {
        this.userId = userId;
        this.date = date;
        this.emotion = emotion;
        this.note = note;
    }

    // Các hàm getter cho các thuộc tính
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public int getEmotion() {
        return emotion;
    }

    public String getNote() {
        return note;
    }
}
