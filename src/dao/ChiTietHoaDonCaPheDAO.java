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

    // Thêm một chi tiết hóa đơn
    public boolean insertChiTiet(ChiTietHoaDonCaPhe ct) {
        String sql = "INSERT INTO ChiTietHoaDonCaPhe (MaHoaDon, MaSanPham, TenSanPham, SoLuong, DonGia, ThanhTien) " +
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
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
    public List<ChiTietHoaDonCaPhe> getChiTietByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDonCaPhe> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDonCaPhe WHERE MaHoaDon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietHoaDonCaPhe ct = new ChiTietHoaDonCaPhe(
                        rs.getInt("MaHoaDon"),
                        rs.getInt("MaSanPham"),
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

    // Xóa tất cả chi tiết hóa đơn theo mã hóa đơn
    public boolean deleteChiTietByMaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDonCaPhe WHERE MaHoaDon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
