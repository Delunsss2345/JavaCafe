package dao;

import entities.LoaiSanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.DatabaseConnection;

public class LoaiSanPhamDAO {
    private Connection connection;

    public LoaiSanPhamDAO() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
    }

   
    public List<LoaiSanPham> getAllLoaiSanPham() throws SQLException {
        List<LoaiSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSanPham";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiSanPham loaiSanPham = new LoaiSanPham(
                        rs.getInt("MaLoai"),
                        rs.getString("TenLoai"),
                        rs.getString("MoTa"),
                        rs.getString("Icon")
                );
                list.add(loaiSanPham);
            }
        }
        return list;
    }

    // Add a new product category
    public boolean addLoaiSanPham(LoaiSanPham loaiSanPham) throws SQLException {
        String sql = "INSERT INTO LoaiSanPham (TenLoai, MoTa, Icon) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loaiSanPham.getTenLoai());
            pstmt.setString(2, loaiSanPham.getMoTa());
            pstmt.setString(3, loaiSanPham.getIcon());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update an existing product category
    public boolean updateLoaiSanPham(LoaiSanPham loaiSanPham) throws SQLException {
        String sql = "UPDATE LoaiSanPham SET TenLoai = ?, MoTa = ?, Icon = ? WHERE MaLoai = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loaiSanPham.getTenLoai());
            pstmt.setString(2, loaiSanPham.getMoTa());
            pstmt.setString(3, loaiSanPham.getIcon());
            pstmt.setInt(4, loaiSanPham.getMaLoai());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a product category by ID
    public boolean deleteLoaiSanPham(int maLoai) throws SQLException {
        String sql = "DELETE FROM LoaiSanPham WHERE MaLoai = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, maLoai);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Find a product category by ID
    public LoaiSanPham getLoaiSanPhamById(int maLoai) throws SQLException {
        String sql = "SELECT * FROM LoaiSanPham WHERE MaLoai = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, maLoai);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new LoaiSanPham(
                            rs.getInt("MaLoai"),
                            rs.getString("TenLoai"),
                            rs.getString("MoTa"),
                            rs.getString("Icon")
                    );
                }
            }
        }
        return null;
    }
}