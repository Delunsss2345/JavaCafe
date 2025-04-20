package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import ConnectDB.DatabaseConnection;
import entities.KhachHang;

public class KhachHangDAO {
    // Xóa trường conn và constructor
    
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(MaKH, ho, ten, gioiTinh, soDienThoai, email, diemTichLuy, ngayDangKy) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, kh.getMaKhachHang());
            stmt.setString(2, kh.getHo());
            stmt.setString(3, kh.getTen());
            stmt.setString(4, kh.getGioiTinh());
            stmt.setString(5, kh.getSoDienThoai());
            stmt.setString(6, kh.getEmail());
            stmt.setInt(7, kh.getDiemTichLuy());
            stmt.setTimestamp(8, kh.getNgayDangKy());
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }
    
    public long generateUniqueMaKH() {
        Random rand = new Random();
        long maKH;
        do {
            maKH = 100000 + rand.nextInt(900000);
        } while (checkMaKHTonTai(maKH));
        return maKH;
    }

    public boolean checkMaKHTonTai(long maKH) {
        String sql = "SELECT MaKH FROM KhachHang WHERE MaKH = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setLong(1, maKH);
            ResultSet rs = stmt.executeQuery();
            boolean result = rs.next(); // Nếu có thì trùng
            
            rs.close();
            stmt.close();
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Mặc định tránh trùng nếu lỗi
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}
