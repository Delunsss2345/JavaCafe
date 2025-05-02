// Người làm: Hà Hoàng Anh 
package dao;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import ConnectDB.DatabaseConnection;

public class BaoCaoDoanhThuDAO {
    private Connection connection;

    public BaoCaoDoanhThuDAO() throws SQLException {
    	this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Failed to establish a database connection.");
        }
    }
    

    private String taoMenhDeWhere(String dateColumn, Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            return " WHERE " + dateColumn + " BETWEEN ? AND ?";
        } else if (startDate != null) {
            return " WHERE " + dateColumn + " >= ?";
        } else if (endDate != null) {
            return " WHERE " + dateColumn + " <= ?";
        }
        return "";
    }

    private void setNgay(PreparedStatement ps, Date startDate, Date endDate) throws SQLException {
        int parameterIndex = 1;
        if (startDate != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(startDate.getTime()));
        }
        if (endDate != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(endDate.getTime()));
        }
    }

    public List<Object[]> getCacHoaDonBanRa(Date startDate, Date endDate) throws SQLException {
        List<Object[]> cacHoaDon = new ArrayList<>();
        String sql = "SELECT HD.MaHD, SUM(CTHD.ThanhTien) AS TongDoanhThu, " +
                     "SUM(CTHD.SoLuong * (CTHD.DonGia - ISNULL(CTNH.GiaNhap, 0))) AS TongLoiNhuan " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHoaDon CTHD ON HD.MaHD = CTHD.MaHD " +
                     "LEFT JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP ";
        String whereClause = taoMenhDeWhere("HD.NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY HD.MaHD";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setNgay(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int maHD = rs.getInt("MaHD");
                    double tongDoanhThu = rs.getDouble("TongDoanhThu");
                    double tongLoiNhuan = rs.getDouble("TongLoiNhuan");
                    cacHoaDon.add(new Object[] { maHD, tongDoanhThu, tongLoiNhuan });
                }
            }
        }
        return cacHoaDon;
    }
    
    public Object[] getThongTinTheoMaHoaDon(int maHD) throws SQLException {
        Object[] thongTin = null;
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maHoaDon = rs.getInt("MaHD");
                    Date ngayTao = rs.getDate("NgayTao");
                    double tongTien = rs.getDouble("TongTien");
                    double tienKhachTra = rs.getDouble("TienKhachTra");
                    double tienThua = rs.getDouble("TienThua");
                    int maNV = rs.getInt("MaNV");
                    int maKhachHang = rs.getInt("maKH");

                    thongTin = new Object[] {
                        maHoaDon, ngayTao, tongTien, tienKhachTra, tienThua, maNV, maKhachHang
                    };
                }
            }
        }
        return thongTin;
    }

    
    public List<Map<String, Object>> getDoanhThuVaLoiNhuanTheoNgay(Date startDate, Date endDate) throws SQLException {
        List<Map<String, Object>> danhSach = new ArrayList<>();
        String sql = "SELECT CAST(HD.NgayTao AS DATE) AS Ngay, " +
                     "SUM(CTHD.ThanhTien) AS DoanhThu, " +
                     "SUM(CTHD.SoLuong * (CTHD.DonGia - ISNULL(CTNH.GiaNhap, 0))) AS LoiNhuan " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHoaDon CTHD ON HD.MaHD = CTHD.MaHD " +
                     "LEFT JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP ";

        String whereClause = taoMenhDeWhere("HD.NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY CAST(HD.NgayTao AS DATE) ORDER BY Ngay";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            setNgay(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("Ngay", rs.getDate("Ngay"));
                    row.put("DoanhThu", rs.getDouble("DoanhThu"));
                    row.put("LoiNhuan", rs.getDouble("LoiNhuan"));
                    danhSach.add(row);
                }
            }
        }

        return danhSach;
    }


    public List<Entry<String, Double>> getDoanhThuTheoThang(Date startDate, Date endDate) throws SQLException {
        List<Entry<String, Double>> doanhThuTheoThang = new ArrayList<>();
        String sql = "SELECT DATEPART(year, NgayTao) AS Nam, DATEPART(month, NgayTao) AS Thang, SUM(TongTien) AS DoanhThu FROM HoaDon";
        String whereClause = taoMenhDeWhere("NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY DATEPART(year, NgayTao), DATEPART(month, NgayTao) ORDER BY Nam, Thang";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
        	setNgay(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String thangNam = String.format("%02d/%d", rs.getInt("Thang"), rs.getInt("Nam"));
                    doanhThuTheoThang.add(new AbstractMap.SimpleEntry<>(thangNam, rs.getDouble("DoanhThu")));
                }
            }
        }
        return doanhThuTheoThang;
    }

    public List<Object[]> getChiTietDoanhThuVaLoiNhuanTheoThang(int thang) throws SQLException {
        List<Object[]> danhSach = new ArrayList<>();

        String sql = "SELECT YEAR(HD.NgayTao) AS Nam, MONTH(HD.NgayTao) AS Thang,TenSanPham,GiaNhap,DonGia,CTHD.SoLuong,"
        		+ " SUM(CTHD.ThanhTien) AS DoanhThu,"
        		+ " SUM(CTHD.SoLuong * (CTHD.DonGia - ISNULL(CTNH.GiaNhap, 0))) AS LoiNhuan"
        		+ " FROM HoaDon HD"
        		+ " JOIN ChiTietHoaDon CTHD ON HD.MaHD = CTHD.MaHD"
        		+ " LEFT JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP "
        		+ " WHERE MONTH(HD.NgayTao) = ?";

        sql += "  GROUP BY NgayTao,TenSanPham,GiaNhap,DonGia,CTHD.SoLuong";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, thang);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int nam = rs.getInt("Nam");
                    int thangDB = rs.getInt("Thang");
                    String tenSanPham = rs.getString("TenSanPham");
                    double giaNhap = rs.getDouble("GiaNhap");
                    double donGia = rs.getDouble("DonGia");
                    int soLuong = rs.getInt("SoLuong");
                    double doanhThu = rs.getDouble("DoanhThu");
                    double loiNhuan = rs.getDouble("LoiNhuan");

                    Object[] thongTin = { nam, thangDB, tenSanPham, giaNhap, donGia, soLuong, doanhThu, loiNhuan };
                    danhSach.add(thongTin);
                }
            }
        }

        return danhSach;
    }



public List<Entry<String, Integer>> getSanPhamBanChayNhat(Date startDate, Date endDate) throws SQLException {
    List<Entry<String, Integer>> sanPhamBanChay = new ArrayList<>();
    String sql = "SELECT SP.TenSanPham, SUM(CTHD.SoLuong) AS TongSoLuong " +
                 "FROM ChiTietHoaDon CTHD " +
                 "JOIN SanPham SP ON CTHD.MaSP = SP.MaSanPham " +
                 "JOIN HoaDon HD ON CTHD.MaHD = HD.MaHD";
    String whereClause = taoMenhDeWhere("HD.NgayTao", startDate, endDate);
    sql += whereClause + " GROUP BY SP.TenSanPham ORDER BY TongSoLuong DESC";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
    	setNgay(ps, startDate, endDate);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sanPhamBanChay.add(new AbstractMap.SimpleEntry<>(
                    rs.getString("TenSanPham"), rs.getInt("TongSoLuong")
                ));
            }
        }
    }
    return sanPhamBanChay;
}

public List<Entry<String, Integer>> getSanPhamBanChamNhat(Date startDate, Date endDate) throws SQLException {
    List<Entry<String, Integer>> sanPhamBanChamNhat = new ArrayList<>();
    String sql = "SELECT SP.TenSanPham, SUM(CTHD.SoLuong) AS TongSoLuong " +
                 "FROM ChiTietHoaDon CTHD " +
                 "JOIN SanPham SP ON CTHD.MaSP = SP.MaSanPham " +
                 "JOIN HoaDon HD ON CTHD.MaHD = HD.MaHD";
    String whereClause = taoMenhDeWhere("HD.NgayTao", startDate, endDate);
    sql += whereClause + " GROUP BY SP.TenSanPham ORDER BY TongSoLuong ASC";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
    	setNgay(ps, startDate, endDate);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
            	sanPhamBanChamNhat.add(new AbstractMap.SimpleEntry<>(
                    rs.getString("TenSanPham"), rs.getInt("TongSoLuong")
                ));
            }
        }
    }
    return sanPhamBanChamNhat;
}


public List<Object[]> getDoanhThuVaLoiNhuanTheoSanPham(Date startDate, Date endDate) throws SQLException {
    String sql =
        "SELECT CTHD.MaSP, CTHD.TenSanPham, " +
        "       SUM(CTHD.SoLuong)                                 AS TongSoLuongBanRa, " +
        "       AVG(CTNH.GiaNhap)                                 AS GiaNhap,     " +
        "       AVG(CTHD.DonGia)                                 AS GiaBanRa,    " +
        "       SUM(CTHD.ThanhTien)                              AS TongDoanhThu," +
        "       SUM(CTHD.SoLuong * (CTHD.DonGia - CTNH.GiaNhap)) AS TongLoiNhuan " +
        "FROM ChiTietHoaDon CTHD " +
        "JOIN ChiTietNhapHang CTNH ON CTNH.MaSP = CTHD.MaSP " +
        "JOIN HoaDon HD           ON HD.MaHD  = CTHD.MaHD " +
        taoMenhDeWhere("HD.NgayTao", startDate, endDate) +
        " GROUP BY CTHD.MaSP, CTHD.TenSanPham " +
        " ORDER BY CTHD.MaSP";

    List<Object[]> result = new ArrayList<>();
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
    	setNgay(ps, startDate, endDate);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("MaSP"),
                    rs.getString("TenSanPham"),
                    rs.getInt("TongSoLuongBanRa"),
                    rs.getDouble("GiaNhap"),
                    rs.getDouble("GiaBanRa"),
                    rs.getDouble("TongDoanhThu"),
                    rs.getDouble("TongLoiNhuan")
                };
                result.add(row);
            }
        }
    }
    return result;
}

	public Map<Integer, Double> getDoanhThuTheoThangTrongNam(int nam) {
		Map<Integer, Double> doanhThuTheoThang = new HashMap<>();
		String sql = "SELECT MONTH(NgayTao) AS Thang, SUM(TongTien) AS DoanhThu "
				+ "FROM HoaDon WHERE YEAR(NgayTao) = ? " + "GROUP BY MONTH(NgayTao) ORDER BY Thang";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, nam);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					doanhThuTheoThang.put(rs.getInt("Thang"), rs.getDouble("DoanhThu"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return doanhThuTheoThang;
	}

}
