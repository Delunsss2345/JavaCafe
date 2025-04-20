package dao;
import entities.ChiTietHoaDonCaPhe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ConnectDB.DatabaseConnection;

public class ChiTietHoaDonCaPheDAO {
    // Phương thức bảo vệ để kiểm tra kết nối
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

    // Kiểm tra sản phẩm tồn tại
    private boolean isSanPhamExists(int maSP) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE MaSanPham = ?";
        try (Connection conn = getSafeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra hóa đơn tồn tại
    private boolean isHoaDonExists(int maHD) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE MaHD = ?";
        try (Connection conn = getSafeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm một chi tiết hóa đơn với kiểm tra
    public boolean insertChiTiet(ChiTietHoaDonCaPhe ct) {
        if (!isSanPhamExists(ct.getMaSanPham())) {
            System.err.println("Lỗi: Sản phẩm với mã " + ct.getMaSanPham() + " không tồn tại trong bảng SanPham");
            return false;
        }

        if (!isHoaDonExists(ct.getMaHoaDon())) {
            System.err.println("Lỗi: Hóa đơn với mã " + ct.getMaHoaDon() + " không tồn tại");
            return false;
        }

        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, TenSanPham, SoLuong, DonGia, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getSafeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ct.getMaHoaDon());
            ps.setInt(2, ct.getMaSanPham());
            ps.setString(3, ct.getTenSanPham());
            ps.setInt(4, ct.getSoLuong());
            ps.setDouble(5, ct.getDonGia());
            ps.setDouble(6, ct.getThanhTien());

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDonCaPhe> getChiTietByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDonCaPhe> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";

        try (Connection conn = getSafeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDonCaPhe ct = new ChiTietHoaDonCaPhe(
                        rs.getInt("MaHD"),
                        rs.getInt("MaSP"),
                        rs.getString("TenSanPham"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("DonGia")
                    );
                    ds.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean deleteChiTietByMaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";

        try (Connection conn = getSafeConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maHoaDon);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
