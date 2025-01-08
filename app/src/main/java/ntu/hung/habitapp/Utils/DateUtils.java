package ntu.hung.habitapp.Utils;

// Tiện ích liên quan đến xử lý ngày tháng
public class DateUtils
{

    // Chuyển đổi ngày theo định dạng ISO (YYYY/MM/DD) sang định dạng hiển thị (DD/MM/YYYY)
    public static String formatDisplayDate(String isoDate)
    {
        String[] parts = isoDate.split("/"); // Tách chuỗi ngày theo ký tự "/"
        if (parts.length == 3)
        { // Kiểm tra nếu đủ ba thành phần (năm, tháng, ngày)
            return parts[2] + "/" + parts[1] + "/" + parts[0]; // Sắp xếp lại theo DD/MM/YYYY
        }
        return isoDate; // Nếu không đúng định dạng, trả về chuỗi gốc
    }

    // Chuyển đổi năm, tháng, ngày riêng lẻ thành định dạng ISO8601 (YYYY/MM/DD)
    public static String formatISODate(int year, int month, int day)
    {
        // Sử dụng String.format để định dạng và đảm bảo đúng định dạng với số 0 đứng trước
        return String.format("%04d/%02d/%02d", year, month + 1, day);
    }

}

