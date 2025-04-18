package dao;
//Người làm phạm thanh huy
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ConnectDB.DatabaseConnection;
import entities.LoaiSanPham;
import entities.SanPham;

public class SanPhamDAO {
    private static final Logger LOGGER = Logger.getLogger(SanPhamDAO.class.getName());

    
    // Lấy tất cả sản phẩm với thông tin loại sản phẩm (sử dụng JOIN)
    public List<SanPham> getAllSanPham() {
        String sql = "SELECT sp.*, lsp.TenLoai, lsp.MoTa as MoTaLoai, lsp.Icon " +
                    "FROM SanPham sp " +
                    "LEFT JOIN LoaiSanPham lsp ON sp.MaLoai = lsp.MaLoai " +
                    "ORDER BY sp.NgayCapNhat DESC";
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
    
    // Lấy sản phẩm theo ID với thông tin đầy đủ
    public SanPham getSanPhamById(int maSanPham) {
    	System.out.println(maSanPham) ;
        String sql = " SELECT sp.*, lsp.TenLoai, lsp.MoTa as MoTaLoai, lsp.Icon " +
                    " FROM SanPham sp " +
                    " LEFT JOIN LoaiSanPham lsp ON sp.MaLoai = lsp.MaLoai " +
                    " WHERE sp.MaSanPham = ? ";
        
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
    
    // Thêm sản phẩm mới
    public boolean addSanPham(SanPham sanPham) {
        String sql = "INSERT INTO SanPham (TenSanPham, MaLoai, Gia, MoTa, TrangThai, HinhAnh, NgayCapNhat) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?)"; 

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sanPham.getTenSanPham());  
            stmt.setInt(2, sanPham.getLoaiSanPham().getMaLoai());
            stmt.setBigDecimal(3, sanPham.getGia());
            stmt.setString(4, sanPham.getMoTa()); 

            // Validate trạng thái
            String trangThai = sanPham.getTrangThai();
            if (!trangThai.equalsIgnoreCase("Đang bán") &&  !trangThai.equals("Hết hàng") && !trangThai.equals("Ngưng Bán")) {
                throw new SQLException("Giá trị TrangThai không hợp lệ");
            }
            stmt.setString(5, trangThai);
            stmt.setString(6, sanPham.getHinhAnh());  
            stmt.setTimestamp(7, Timestamp.valueOf(sanPham.getNgayCapNhat()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm sản phẩm: " + sanPham.getTenSanPham(), e);
            return false;
        }
    }

    // Cập nhật sản phẩm
    public boolean updateSanPham(SanPham sanPham) {
        String sql = "UPDATE SanPham SET TenSanPham = ?, MaLoai = ?, Gia = ?, " +
                   "MoTa = ?, TrangThai = ?, HinhAnh = ?, NgayCapNhat = ? " +
                   "WHERE MaSanPham = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Validate trạng thái
            String trangThai = sanPham.getTrangThai();
            if (!trangThai.equals("Đang Bán") && !trangThai.equals("Hết Hàng") && !trangThai.equals("Ngưng Bán")) {
                throw new SQLException("Trạng thái không hợp lệ");
            }

            stmt.setString(1, sanPham.getTenSanPham());
            stmt.setInt(2, sanPham.getLoaiSanPham().getMaLoai());
            stmt.setBigDecimal(3, sanPham.getGia());
            stmt.setString(4, sanPham.getMoTa());
            stmt.setString(5, trangThai);
            stmt.setString(6, sanPham.getHinhAnh());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(8, sanPham.getMaSanPham());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm: " + sanPham.getMaSanPham(), e);
            return false;
        }
    }

    // Xóa mềm sản phẩm (cập nhật trạng thái)
    public boolean deleteSanPham(int maSanPham) {
        String sql = "UPDATE SanPham SET TrangThai = N'Ngừng bán', NgayCapNhat = GETDATE() WHERE MaSanPham = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa sản phẩm: " + maSanPham, e);
            return false;
        }
    }

    // Lấy sản phẩm theo loại
    public List<SanPham> getSanPhamByLoai(int maLoai) {
        String sql = " SELECT sp.*, lsp.TenLoai, lsp.MoTa as MoTaLoai, lsp.Icon " +
                    " FROM SanPham sp " +
                    " LEFT JOIN LoaiSanPham lsp ON sp.MaLoai = lsp.MaLoai " +
                    " WHERE sp.MaLoai = ? AND sp.TrangThai = N'Đang Bán' " +
                    " ORDER BY sp.NgayCapNhat DESC ";
        
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
    
    // Ánh xạ ResultSet thành đối tượng SanPham
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(rs.getInt("MaSanPham"));
        sanPham.setTenSanPham(rs.getString("TenSanPham"));
        
       
        LoaiSanPham loaiSP = new LoaiSanPham();
        loaiSP.setMaLoai(rs.getInt("MaLoai"));
        loaiSP.setTenLoai(rs.getString("TenLoai"));
        loaiSP.setMoTa(rs.getString("MoTaLoai"));
        loaiSP.setIcon(rs.getString("Icon"));
        
        sanPham.setLoaiSanPham(loaiSP);
        sanPham.setGia(rs.getBigDecimal("Gia"));
        sanPham.setMoTa(rs.getString("MoTa"));
        sanPham.setTrangThai(rs.getString("TrangThai"));
        sanPham.setHinhAnh(rs.getString("HinhAnh"));
        
        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayTao != null) {
            sanPham.setNgayTao(ngayTao.toLocalDateTime());
        }
        if (ngayCapNhat != null) {
            sanPham.setNgayCapNhat(ngayCapNhat.toLocalDateTime());
        }
        
        return sanPham;
    }
}