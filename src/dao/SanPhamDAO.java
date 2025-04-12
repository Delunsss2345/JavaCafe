package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ConnectDB.DatabaseConnection;
import entities.SanPham;

public class SanPhamDAO {
    private static final Logger LOGGER = Logger.getLogger(SanPhamDAO.class.getName());
    
    // Lấy tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        String sql = "SELECT * FROM SanPham ORDER BY NgayCapNhat DESC";
        List<SanPham> list = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách sản phẩm", e);
        }
        return list;
    }
    
    // Lấy sản phẩm theo ID
    public SanPham getSanPhamById(int maSanPham){
        String sql = "SELECT * FROM SanPham WHERE MaSanPham = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, maSanPham);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSanPham(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy sản phẩm theo ID: " + maSanPham, e);
        }
        return null;
    }
    public boolean addSanPham(SanPham sanPham) {
        String sql = "INSERT INTO SanPham (TenSanPham, MaLoai, Gia, MoTa, TrangThai, HinhAnh, NgayCapNhat) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)"; 

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

           
            stmt.setString(1, sanPham.getTenSanPham());  
            stmt.setInt(2, sanPham.getMaLoai());
            stmt.setBigDecimal(3, sanPham.getGia());
            stmt.setString(4, sanPham.getMoTa()); 

            // Kiểm tra giá trị TrangThai có hợp lệ không
            String trangThai = sanPham.getTrangThai();
            if (!trangThai.equals("Đang Bán") && !trangThai.equals("Hết Hàng")) {
                throw new SQLException("Giá trị TrangThai không hợp lệ. Chỉ được phép 'Đang Bán' hoặc 'Hết Hàng'.");
            }
            stmt.setString(5, trangThai);
            stmt.setString(6, sanPham.getHinhAnh());  

           
            LocalDateTime ngayCapNhatLocalDateTime = sanPham.getNgayCapNhat(); 
            java.sql.Timestamp ngayCapNhat = java.sql.Timestamp.valueOf(ngayCapNhatLocalDateTime);  
            stmt.setTimestamp(7, ngayCapNhat);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm sản phẩm: " + sanPham.getTenSanPham(), e);
            return false;
        }
    }

    
    // Cập nhật sản phẩm
    public boolean updateSanPham(SanPham sanPham) {
        String sql = "UPDATE SanPham SET TenSanPham = ?, MaLoai = ?, Gia = ?, "
                   + "MoTa = ?, TrangThai = ?, HinhAnh = ?, NgayCapNhat = GETDATE() "
                   + "WHERE MaSanPham = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sanPham.getTenSanPham());
            stmt.setInt(2, sanPham.getMaLoai());
            stmt.setBigDecimal(3, sanPham.getGia());
            stmt.setString(4, sanPham.getMoTa());
            stmt.setString(5, sanPham.getTrangThai());
            stmt.setString(6, sanPham.getHinhAnh());
            stmt.setInt(7, sanPham.getMaSanPham());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm: " + sanPham.getMaSanPham(), e);
        }
        return false;
    }
    
    // Xóa sản phẩm (soft delete)
    public boolean deleteSanPham(int maSanPham) {
        String sql = "UPDATE SanPham SET TrangThai = 'Ngừng bán', NgayCapNhat = GETDATE() "
                   + "WHERE MaSanPham = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa sản phẩm: " + maSanPham, e);
        }
        return false;
    }
    
    // Tìm kiếm sản phẩm
    public List<SanPham> searchSanPham(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE TenSanPham LIKE ? OR MoTa LIKE ? ORDER BY NgayCapNhat DESC";
        List<SanPham> list = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm sản phẩm với từ khóa: " + keyword, e);
        }
        return list;
    }
    
    // Lấy sản phẩm theo loại
    public List<SanPham> getSanPhamByLoai(int maLoai) {
        String sql = "SELECT * FROM SanPham WHERE MaLoai = ? AND TrangThai = N'Đang bán' ORDER BY NgayCapNhat DESC";
        List<SanPham> list = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, maLoai);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy sản phẩm theo loại: " + maLoai, e);
        }
        return list;
    }
    
    // Helper method để ánh xạ ResultSet sang đối tượng SanPham
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(rs.getInt("MaSanPham"));
        sanPham.setTenSanPham(rs.getString("TenSanPham"));
        sanPham.setMaLoai(rs.getInt("MaLoai")); 
        sanPham.setGia(rs.getBigDecimal("Gia"));
        sanPham.setMoTa(rs.getString("MoTa"));
        sanPham.setTrangThai(rs.getString("TrangThai"));
        sanPham.setHinhAnh(rs.getString("HinhAnh"));
        sanPham.setNgayTao(rs.getTimestamp("NgayTao").toLocalDateTime());
        sanPham.setNgayCapNhat(rs.getTimestamp("NgayCapNhat").toLocalDateTime());
        return sanPham;
    }
}
