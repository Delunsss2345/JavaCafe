package dao;
import entities.ChiTietHoaDonCaPhe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ChiTietHoaDonCaPheDAO {
    private Connection conn;
    public ChiTietHoaDonCaPheDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Thêm phương thức kiểm tra sản phẩm tồn tại
    private boolean isSanPhamExists(int maSP) {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE MaSanPham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Thêm một chi tiết hóa đơn với kiểm tra
    public boolean insertChiTiet(ChiTietHoaDonCaPhe ct) {
        // Kiểm tra sản phẩm có trong CSDL không
        if (!isSanPhamExists(ct.getMaSanPham())) {
            System.err.println("Lỗi: Sản phẩm với mã " + ct.getMaSanPham() + " không tồn tại trong bảng SanPham");
            return false;
        }
        
        // Kiểm tra hóa đơn có tồn tại không
        if (!isHoaDonExists(ct.getMaHoaDon())) {
            System.err.println("Lỗi: Hóa đơn với mã " + ct.getMaHoaDon() + " không tồn tại");
            return false;
        }
        
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, TenSanPham, SoLuong, DonGia, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaHoaDon());
            ps.setInt(2, ct.getMaSanPham());
            ps.setString(3, ct.getTenSanPham());
            ps.setInt(4, ct.getSoLuong());
            ps.setDouble(5, ct.getDonGia());
            ps.setDouble(6, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức kiểm tra hóa đơn tồn tại
    private boolean isHoaDonExists(int maHD) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Các phương thức khác giữ nguyên
    public List<ChiTietHoaDonCaPhe> getChiTietByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDonCaPhe> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public boolean deleteChiTietByMaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
