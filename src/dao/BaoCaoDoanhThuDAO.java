// Người làm: Hà Hoàng Anh 
package dao;

import java.sql.*;
import java.text.DecimalFormat;
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
    

    private String taoMenhDeWhere(String cotNgay, Date ngayBatDau, Date ngayKetThuc) {
        if (ngayBatDau != null && ngayKetThuc != null) {
            return " WHERE " + cotNgay + " BETWEEN ? AND ?";
        } else if (ngayBatDau != null) {
            return " WHERE " + cotNgay + " >= ?";
        } else if (ngayKetThuc != null) {
            return " WHERE " + cotNgay + " <= ?";
        }
        return "";
    }

    private void setNgay(PreparedStatement ps, Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        int parameterIndex = 1;
        if (ngayBatDau != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(ngayBatDau.getTime()));
        }
        if (ngayKetThuc != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(ngayKetThuc.getTime()));
        }
    }

    public List<Object[]> getCacHoaDonBanRa(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        List<Object[]> cacHoaDon = new ArrayList<>();
        String sql = "SELECT HD.MaHD, SUM(CTHD.ThanhTien) AS TongDoanhThu, " +
                     "SUM(CTHD.SoLuong * (CTHD.DonGia - ISNULL(CTNH.GiaNhap, 0))) AS TongLoiNhuan " +
                     "FROM HoaDon HD " +
                     "JOIN ChiTietHoaDon CTHD ON HD.MaHD = CTHD.MaHD " +
                     "LEFT JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP ";
        String whereClause = taoMenhDeWhere("HD.NgayTao", ngayBatDau, ngayKetThuc);
        sql += whereClause + " GROUP BY HD.MaHD";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setNgay(ps, ngayBatDau, ngayKetThuc);
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
    	DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");
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
                        maHoaDon, ngayTao, dinhDangTien.format(tongTien), dinhDangTien.format(tienKhachTra),  dinhDangTien.format(tienThua), maNV, maKhachHang
                    };
                }
            }
        }
        return thongTin;
    }

    public List<Object[]> getThongTinChiTietHoaDonTheoMaHoaDon(int maHoaDon) throws SQLException {
        List<Object[]> danhSachChiTiet = new ArrayList<>();

        String sql = "SELECT MaHD,MaCTHD, CTHD.MaSP, CTHD.TenSanPham, CTHD.SoLuong, CTHD.DonGia,CTHD.ThanhTien, LoiNhuan = CTHD.SoLuong * (CTHD.DonGia - CTNH.GiaNhap) "
        		+ " FROM ChiTietHoaDon CTHD JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP"
        		+ " WHERE MaHD = ? "
        		+ " ORDER BY MaHD ";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
        	
            ps.setInt(1, maHoaDon);
            DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int maCTHD = rs.getInt("MaCTHD");
                    int maHD = rs.getInt("MaHD");
                    int maSanPham = rs.getInt("MaSP");
                    String tenSanPham = rs.getString("TenSanPham");
                    int soLuong = rs.getInt("SoLuong");
                    double donGia = rs.getDouble("DonGia");
                    double thanhTien = rs.getDouble("ThanhTien");
                    double loiNhuan = rs.getDouble("LoiNhuan");

                    Object[] thongTin = { maCTHD, maHD, maSanPham, tenSanPham, soLuong, dinhDangTien.format(donGia)
                    		, dinhDangTien.format(thanhTien), dinhDangTien.format(loiNhuan) };
                    danhSachChiTiet.add(thongTin);
                }
            }
        }

        return danhSachChiTiet;
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

        String sql = "SELECT YEAR(HD.NgayTao) AS Nam, MONTH(HD.NgayTao) AS Thang,CTHD.TenSanPham,GiaNhap,DonGia,CTHD.SoLuong,"
        		+ " SUM(CTHD.ThanhTien) AS DoanhThu,"
        		+ " SUM(CTHD.SoLuong * (CTHD.DonGia - ISNULL(CTNH.GiaNhap, 0))) AS LoiNhuan"
        		+ " FROM HoaDon HD"
        		+ " JOIN ChiTietHoaDon CTHD ON HD.MaHD = CTHD.MaHD"
        		+ " LEFT JOIN ChiTietNhapHang CTNH ON CTHD.MaSP = CTNH.MaSP "
        		+ " WHERE MONTH(HD.NgayTao) = ?";

        sql += "  GROUP BY NgayTao,CTHD.TenSanPham,GiaNhap,DonGia,CTHD.SoLuong";

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


	public List<Object[]> getDoanhThuVaLoiNhuanTheoThang(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
		DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");
	    String sql = """
	        SELECT MONTH(HD.NgayTao) AS Thang,
	               SUM(CTHD.ThanhTien) AS DoanhThu,
	               SUM(CTHD.SoLuong * (CTHD.DonGia - CTNH.GiaNhap)) AS LoiNhuan
	        FROM ChiTietHoaDon CTHD
	        JOIN ChiTietNhapHang CTNH ON CTNH.MaSP = CTHD.MaSP
	        JOIN HoaDon HD ON HD.MaHD = CTHD.MaHD
	        WHERE HD.NgayTao BETWEEN ? AND ?
	        GROUP BY MONTH(HD.NgayTao)
	        ORDER BY Thang
	    """;

	    List<Object[]> result = new ArrayList<>();
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setDate(1, new java.sql.Date(ngayBatDau.getTime()));
	        ps.setDate(2, new java.sql.Date(ngayKetThuc.getTime()));

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                result.add(new Object[]{
	                    rs.getInt("Thang"),
	                    rs.getDouble("DoanhThu"),
	                    rs.getDouble("LoiNhuan")
	                });
	            }
	        }
	    }
	    return result;
	}

	
	public List<Object[]> getDoanhThuVaLoiNhuanTheoNgayTrong1Thang(int thang) throws SQLException {
		String sql = "SELECT DAY(HD.NgayTao) AS NGAY,"
				+ " DoanhThu = SUM(CTHD.ThanhTien), "
				+ "SUM(CTHD.SoLuong * (CTHD.DonGia - CTNH.GiaNhap))  AS LoiNhuan "
				+ "FROM  ChiTietHoaDon CTHD JOIN ChiTietNhapHang CTNH ON CTNH.MaSP = CTHD.MaSP "
				+ "JOIN HoaDon HD  ON HD.MaHD  = "
				+ "CTHD.MaHD WHERE MONTH(HD.NGAYTAO) = ? "
				+ "GROUP BY DAY(HD.NgayTao) "
				+ "ORDER BY DAY(HD.NgayTao)";

		List<Object[]> result = new ArrayList<>();
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, thang);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Object[] row = new Object[] { rs.getInt("Ngay"), rs.getDouble("DoanhThu"),
							rs.getDouble("LoiNhuan")};
					result.add(row);
				}
			}
		}
		return result;
	}
	
	public List<Object[]> getDoanhThuVaLoiNhuanTrongNgay(int thang) throws SQLException {
		DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");
	    String sql =
	        "SELECT CONVERT(DATE, HD.NgayTao) AS Ngay, " +
	        "       CTHD.TenSanPham, " +
	        "       SUM(CTHD.SoLuong) AS TongSoLuongBanRa, " +
	        "       AVG(CTNH.GiaNhap) AS GiaNhap, " +
	        "       AVG(CTHD.DonGia) AS GiaBan, " +
	        "       SUM(CTHD.ThanhTien) AS DoanhThu, " +
	        "       SUM(CTHD.SoLuong * (CTHD.DonGia - CTNH.GiaNhap)) AS LoiNhuan " +
	        " FROM ChiTietHoaDon CTHD " +
	        " JOIN ChiTietNhapHang CTNH ON CTNH.MaSP = CTHD.MaSP " +
	        " JOIN HoaDon HD ON HD.MaHD = CTHD.MaHD " +
	        " WHERE MONTH(HD.NgayTao) = ? " +
	        " GROUP BY CONVERT(DATE, HD.NgayTao), CTHD.TenSanPham " +
	        " ORDER BY Ngay";

	    List<Object[]> result = new ArrayList<>();
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {
	        ps.setInt(1, thang);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Object[] row = new Object[] {
	                    rs.getDate("Ngay"),
	                    rs.getString("TenSanPham"),
	                    rs.getInt("TongSoLuongBanRa"),
	                    dinhDangTien.format(rs.getDouble("GiaNhap")),
	                    dinhDangTien.format(rs.getDouble("GiaBan")),
	                    dinhDangTien.format(rs.getDouble("DoanhThu")),
	                    dinhDangTien.format(rs.getDouble("LoiNhuan")),
	                };
	                result.add(row);
	            }
	        }
	    }
	    return result;
	}
}
