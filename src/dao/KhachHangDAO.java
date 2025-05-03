package dao;
//người làm nguyễn tấn phát
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import ConnectDB.DatabaseConnection;
import entities.KhachHang;

public class KhachHangDAO {
    private Connection getSafeConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            conn = DatabaseConnection.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Không thể thiết lập kết nối đến cơ sở dữ liệu");
            }
        }
        return conn;
    }
    
    public boolean insertKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(MaKH, ho, ten, gioiTinh, soDienThoai, email, diemTichLuy, ngayDangKy) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getSafeConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, kh.getMaKhachHang());
            stmt.setString(2, kh.getHo());
            stmt.setString(3, kh.getTen());
            stmt.setString(4, kh.getGioiTinh());
            stmt.setString(5, kh.getSoDienThoai());
            stmt.setString(6, kh.getEmail());
            stmt.setInt(7, kh.getDiemTichLuy());
            stmt.setTimestamp(8, kh.getNgayDangKy());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

        try (Connection conn = getSafeConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, maKH);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
    }
    public KhachHang getKhachHangById(int maKH) {
        String sql = "SELECT MaKH, ho, ten, gioiTinh, soDienThoai, email, diemTichLuy, ngayDangKy "
                   + "FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = getSafeConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, maKH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKhachHang(rs.getLong("MaKH"));
                    kh.setHo(rs.getString("ho"));
                    kh.setTen(rs.getString("ten"));
                    kh.setGioiTinh(rs.getString("gioiTinh"));
                    kh.setSoDienThoai(rs.getString("soDienThoai"));
                    kh.setEmail(rs.getString("email"));
                    kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
                    kh.setNgayDangKy(rs.getTimestamp("ngayDangKy"));
                    return kh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
