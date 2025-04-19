//Người làm Hà Hoàng Anh

package gui;

import dao.BaoCaoDoanhThuDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

public class BaoCaoDoanhThu extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panelLoc, panelNoiDung;
    private JTable tableBaoCao, tableChiTiet;
    private DefaultTableModel modelBaoCao, modelChiTiet;
    private JComboBox<String> cboNgayBatDau, cboThangBatDau, cboNamBatDau;
    private JComboBox<String> cboNgayKetThuc, cboThangKetThuc, cboNamKetThuc;
    private JComboBox<String> cboLoaiBaoCao;
    private JLabel lblTongDoanhThu;
    private DecimalFormat dinhDangTien;
    private BaoCaoDoanhThuDAO baoCaoDAO;
    private JButton btnTaoBaoCao;
    private JButton btnXuatExcel;
    private JButton btnTaoBieuDO;

    public BaoCaoDoanhThu() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dinhDangTien = new DecimalFormat("#,### VNĐ");
        try {
            baoCaoDAO = new BaoCaoDoanhThuDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        taoPanelLoc();
        taoPanelNoiDung();
    }

    private Date layNgayTuComboBox(JComboBox<String> cboNgay, JComboBox<String> cboThang, JComboBox<String> cboNam)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ngay = cboNam.getSelectedItem() + "-" + cboThang.getSelectedItem() + "-" + cboNgay.getSelectedItem();
        return sdf.parse(ngay);
    }

    private void taoPanelLoc() {
        panelLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelLoc.setBackground(new Color(240, 240, 240));
        panelLoc.setBorder(BorderFactory.createTitledBorder("Lọc dữ liệu"));

        JLabel lblNgayBatDau = new JLabel("Từ ngày:");
        String[] cacNgay = new String[31];
        for (int i = 1; i <= 31; i++)
            cacNgay[i - 1] = String.format("%02d", i);
        String[] cacThang = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
        String[] cacNam = new String[5];
        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++)
            cacNam[i] = String.valueOf(namHienTai - i);

        cboNgayBatDau = new JComboBox<>(cacNgay);
        cboThangBatDau = new JComboBox<>(cacThang);
        cboNamBatDau = new JComboBox<>(cacNam);

        JLabel lblNgayKetThuc = new JLabel("Đến ngày:");
        cboNgayKetThuc = new JComboBox<>(cacNgay);
        cboThangKetThuc = new JComboBox<>(cacThang);
        cboNamKetThuc = new JComboBox<>(cacNam);

        JLabel lblLoaiBaoCao = new JLabel("Loại báo cáo:");
        String[] cacLoaiBaoCao = { "Tổng số hóa đơn", "Tổng doanh thu", "Doanh thu theo ngày", "Doanh thu theo tháng",
                "Doanh thu theo nhân viên", "Sản phẩm bán chạy nhất", "Tên sản phẩm và tổng doanh thu" };
        cboLoaiBaoCao = new JComboBox<>(cacLoaiBaoCao);

        btnTaoBaoCao = new JButton("Tạo báo cáo");
        btnTaoBaoCao.setBackground(new Color(70, 130, 180));
        btnTaoBaoCao.setForeground(Color.WHITE);
        btnTaoBaoCao.addActionListener(this);
        
        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(70, 130, 180));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.addActionListener(this);
        
        btnTaoBieuDO = new JButton("Tạo biểu đồ");
        btnTaoBieuDO.setBackground(new Color(70, 130, 180));
        btnTaoBieuDO.setForeground(Color.WHITE);
		btnTaoBieuDO.addActionListener(this);
        
        panelLoc.add(lblNgayBatDau);
        panelLoc.add(cboNgayBatDau);
        panelLoc.add(new JLabel("/"));
        panelLoc.add(cboThangBatDau);
        panelLoc.add(new JLabel("/"));
        panelLoc.add(cboNamBatDau);

        panelLoc.add(Box.createHorizontalStrut(15));
        panelLoc.add(lblNgayKetThuc);
        panelLoc.add(cboNgayKetThuc);
        panelLoc.add(new JLabel("/"));
        panelLoc.add(cboThangKetThuc);
        panelLoc.add(new JLabel("/"));
        panelLoc.add(cboNamKetThuc);

        panelLoc.add(Box.createHorizontalStrut(15));
        panelLoc.add(lblLoaiBaoCao);
        panelLoc.add(cboLoaiBaoCao);
        panelLoc.add(btnTaoBaoCao);
        
        panelLoc.add(Box.createHorizontalStrut(15));
        panelLoc.add(btnXuatExcel);
        
        panelLoc.add(Box.createHorizontalStrut(15));
        panelLoc.add(btnTaoBieuDO);

        add(panelLoc, BorderLayout.NORTH);
    }

    private void taoPanelNoiDung() {
        panelNoiDung = new JPanel(new GridLayout(2, 1, 10, 10));
        panelNoiDung.setBorder(BorderFactory.createTitledBorder("Kết quả báo cáo"));

        modelBaoCao = new DefaultTableModel(new String[] { "Thông tin", "Giá trị" }, 0);
        tableBaoCao = new JTable(modelBaoCao);
        JScrollPane scrollBaoCao = new JScrollPane(tableBaoCao);
        scrollBaoCao.setBorder(BorderFactory.createTitledBorder("Báo cáo tổng hợp"));
        panelNoiDung.add(scrollBaoCao);

        modelChiTiet = new DefaultTableModel();
        tableChiTiet = new JTable(modelChiTiet);
        JScrollPane scrollChiTiet = new JScrollPane(tableChiTiet);
        scrollChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết"));
        panelNoiDung.add(scrollChiTiet);
        lblTongDoanhThu = new JLabel("Tổng doanh thu: " + dinhDangTien.format(0));
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTongDoanhThu, BorderLayout.SOUTH);

        add(panelNoiDung, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(btnTaoBaoCao)) {
            taoBaoCao();
		} else if (obj.equals(btnXuatExcel)) {
			xuatExcel();
		} else if (obj.equals(btnTaoBieuDO)) {
			try {
				taoBieuDo();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    }




    private void xuatExcel() {
        String directoryPath = "src/BaoCaoLuuTru";
        String fileName = "BaoCaoDoanhThu.xlsx";
        String filePath = directoryPath + File.separator + fileName;

        // Tạo thư mục nếu chưa tồn tại
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            // --- TẠO CELL STYLES ---
            // style cho header (in đậm)
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);  
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // style cho tiền tệ
            CellStyle moneyStyle = workbook.createCellStyle();
            DataFormat fmt = workbook.createDataFormat();
            moneyStyle.setDataFormat(fmt.getFormat("#,##0 VNĐ"));

            // --- TẠO SHEET "BÁO CÁO TỔNG HỢP" ---
            Sheet sheetSummary = workbook.createSheet("Báo cáo tổng hợp");
            writeTableWithStyles(sheetSummary, tableBaoCao, headerStyle, moneyStyle);

            // --- TẠO SHEET "CHI TIẾT" ---
            Sheet sheetDetail = workbook.createSheet("Chi tiết");
            writeTableWithStyles(sheetDetail, tableChiTiet, headerStyle, moneyStyle);

            // --- AUTO-SIZE COLUMNS CHO CẢ HAI SHEET ---
            autoSizeAllColumns(sheetSummary);
            autoSizeAllColumns(sheetDetail);

            // Ghi ra file
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(this,
                "Xuất file Excel thành công:\n" + filePath,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất file Excel:\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    /** Ghi dữ liệu từ JTable ra Sheet, áp dụng style cho header và tất cả ô có giá trị số */
    private void writeTableWithStyles(Sheet sheet, JTable table,
                                      CellStyle headerStyle, CellStyle moneyStyle) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        // 1. Header
        Row headerRow = sheet.createRow(0);
        for (int c = 0; c < model.getColumnCount(); c++) {
            Cell cell = headerRow.createCell(c);
            cell.setCellValue(model.getColumnName(c));
            cell.setCellStyle(headerStyle);
        }
        // 2. Data rows
        for (int r = 0; r < model.getRowCount(); r++) {
            Row row = sheet.createRow(r + 1);
            for (int c = 0; c < model.getColumnCount(); c++) {
                Cell cell = row.createCell(c);
                Object value = model.getValueAt(r, c);
                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                    cell.setCellStyle(moneyStyle);
                } else {
                    cell.setCellValue(value == null ? "" : value.toString());
                }
            }
        }
    }

    /** Để tự động căn rộng tất cả các cột hiện có trong sheet */
    private void autoSizeAllColumns(Sheet sheet) {
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(0);
            if (row != null) {
                int lastCol = row.getLastCellNum();
                for (int c = 0; c < lastCol; c++) {
                    sheet.autoSizeColumn(c);
                }
            }
        }
    }




private void writeTableToSheet(Sheet sheet, JTable table) {
   
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    Row headerRow = sheet.createRow(0);
    for (int col = 0; col < model.getColumnCount(); col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(model.getColumnName(col));
    }

    // Write data rows
    for (int row = 0; row < model.getRowCount(); row++) {
        Row dataRow = sheet.createRow(row + 1);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell cell = dataRow.createCell(col);
            Object value = model.getValueAt(row, col);
            cell.setCellValue(value == null ? "" : value.toString());
        }
    }
}


	private void taoBaoCao() {
        khoiTaoTable();
        try {
            Date ngayBatDau = layNgayTuComboBox(cboNgayBatDau, cboThangBatDau, cboNamBatDau);
            Date ngayKetThuc = layNgayTuComboBox(cboNgayKetThuc, cboThangKetThuc, cboNamKetThuc);
            String loaiBaoCao = (String) cboLoaiBaoCao.getSelectedItem();

            if (ngayBatDau != null && ngayKetThuc != null && ngayBatDau.after(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc.", "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;}

            switch (loaiBaoCao) {
                case "Tổng số hóa đơn":
                    baoCaoTongSoHoaDon(ngayBatDau, ngayKetThuc);
                    hienThiChiTietHoaDon(ngayBatDau, ngayKetThuc);
                    break;
                case "Tổng doanh thu":
                    baoCaoTongDoanhThu(ngayBatDau, ngayKetThuc);
                    hienThiChiTietHoaDon(ngayBatDau, ngayKetThuc);
                    break;
                case "Doanh thu theo ngày":
                    baoCaoDoanhThuTheoNgay(ngayBatDau, ngayKetThuc);
                    hienThiChiTietTheoNgay(ngayBatDau, ngayKetThuc);
                    break;
                case "Doanh thu theo tháng":
                    baoCaoDoanhThuTheoThang(ngayBatDau, ngayKetThuc);
                    hienThiChiTietTheoThang(ngayBatDau, ngayKetThuc);
                    break;
                case "Doanh thu theo nhân viên":
                    baoCaoDoanhThuTheoNhanVien(ngayBatDau, ngayKetThuc);
                    hienThiChiTietTheoNhanVien(ngayBatDau, ngayKetThuc);
                    break;
                case "Sản phẩm bán chạy nhất":
                    baoCaoSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
                    hienThiChiTietSanPhamBanChay(ngayBatDau, ngayKetThuc);
                    break;
                case "Tên sản phẩm và tổng doanh thu":
                    baoCaoSanPhamVaTongDoanhThu(ngayBatDau, ngayKetThuc);
                    hienThiChiTietDoanhThuSanPham(ngayBatDau, ngayKetThuc);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Loại báo cáo không hợp lệ.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void khoiTaoTable() {
        modelBaoCao.setRowCount(0);
        modelChiTiet.setRowCount(0);
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(0));
        modelBaoCao.setColumnIdentifiers(new Object[]{"Thông tin", "Giá trị"});
        modelChiTiet.setColumnIdentifiers(new Object[]{}); // Reset columns for detail table
    }

    private void baoCaoTongSoHoaDon(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        int tongSoHoaDon = baoCaoDAO.getTongSoHoaDon(ngayBatDau, ngayKetThuc);
        modelBaoCao.addRow(new Object[]{"Tổng số hóa đơn", tongSoHoaDon});
    }

    private void hienThiChiTietHoaDon(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Mã hoá đơn", "Tên nhân viên", "Ngày Tạo", "Tổng Tiền"});
        modelChiTiet.setRowCount(0);
        String thongTinHoaDon = baoCaoDAO.getThongTinTungHoaDon(ngayBatDau, ngayKetThuc);
        String[] cacHoaDon = thongTinHoaDon.split("\n");
        for (String hoaDon : cacHoaDon) {
            String[] chiTiet = hoaDon.split(", ");
            if (chiTiet.length == 4) {
                String maHD = chiTiet[0].substring(chiTiet[0].indexOf(": ") + 2);
                String tenNV = chiTiet[1].substring(chiTiet[1].indexOf(": ") + 2);
                String ngayTao = chiTiet[2].substring(chiTiet[2].indexOf(": ") + 2);
                String tongTienStr = chiTiet[3].substring(chiTiet[3].indexOf(": ") + 2);
                try {
                    modelChiTiet.addRow(new Object[]{maHD, tenNV, ngayTao, dinhDangTien.parse(tongTienStr)});
                } catch (ParseException e) {
                    modelChiTiet.addRow(new Object[]{maHD, tenNV, ngayTao, tongTienStr});
                    e.printStackTrace();
                }
            }
        }
    }

    private void baoCaoTongDoanhThu(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        double tongDoanhThu = baoCaoDAO.getTongDoanhThu(ngayBatDau, ngayKetThuc);
        modelBaoCao.addRow(new Object[]{"Tổng doanh thu", dinhDangTien.format(tongDoanhThu)});
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }

    private void baoCaoDoanhThuTheoNgay(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Ngày", "Doanh thu"});
        List<Map.Entry<Date, Double>> doanhThuTheoNgay = baoCaoDAO.getDoanhThuTheoNgay(ngayBatDau, ngayKetThuc);
        modelBaoCao.setRowCount(0);
        double tongDoanhThu = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<Date, Double> entry : doanhThuTheoNgay) {
            modelBaoCao.addRow(new Object[]{sdf.format(entry.getKey()), dinhDangTien.format(entry.getValue())});
            tongDoanhThu += entry.getValue();
        }
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }

    private void hienThiChiTietTheoNgay(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Ngày", "Doanh thu"});
        List<Map.Entry<Date, Double>> doanhThuTheoNgay = baoCaoDAO.getDoanhThuTheoNgay(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<Date, Double> entry : doanhThuTheoNgay) {
            modelChiTiet.addRow(new Object[]{sdf.format(entry.getKey()), dinhDangTien.format(entry.getValue())});
        }
    }

    private void baoCaoDoanhThuTheoThang(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Tháng/Năm", "Doanh thu"});
        List<Map.Entry<String, Double>> doanhThuTheoThang = baoCaoDAO.getDoanhThuTheoThang(ngayBatDau, ngayKetThuc);
        modelBaoCao.setRowCount(0);
        double tongDoanhThu = 0;
        for (Map.Entry<String, Double> entry : doanhThuTheoThang) {
            modelBaoCao.addRow(new Object[]{entry.getKey(), dinhDangTien.format(entry.getValue())});
            tongDoanhThu += entry.getValue();
        }
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }

    private void hienThiChiTietTheoThang(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Tháng/Năm", "Doanh thu"});
        List<Map.Entry<String, Double>> doanhThuTheoThang = baoCaoDAO.getDoanhThuTheoThang(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        for (Map.Entry<String, Double> entry : doanhThuTheoThang) {
            modelChiTiet.addRow(new Object[]{entry.getKey(), dinhDangTien.format(entry.getValue())});
        }
    }

    private void baoCaoDoanhThuTheoNhanVien(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Nhân viên", "Doanh thu"});
        List<Map.Entry<String, Double>> doanhThuTheoNhanVien = baoCaoDAO.getDoanhThuTheoNhanVien(ngayBatDau, ngayKetThuc);
        modelBaoCao.setRowCount(0);
        double tongDoanhThu = 0;
        for (Map.Entry<String, Double> entry : doanhThuTheoNhanVien) {
            modelBaoCao.addRow(new Object[]{entry.getKey(), dinhDangTien.format(entry.getValue())});
            tongDoanhThu += entry.getValue();
        }
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }

    private void hienThiChiTietTheoNhanVien(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Nhân viên", "Doanh thu"});
        List<Map.Entry<String, Double>> doanhThuTheoNhanVien = baoCaoDAO.getDoanhThuTheoNhanVien(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        for (Map.Entry<String, Double> entry : doanhThuTheoNhanVien) {
            modelChiTiet.addRow(new Object[]{entry.getKey(), dinhDangTien.format(entry.getValue())});
        }
    }

    private void baoCaoSanPhamBanChayNhat(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Thông tin", "Giá trị"});
        List<Map.Entry<String, Integer>> sanPhamBanChay = baoCaoDAO.getSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
        modelBaoCao.setRowCount(0);
        if (!sanPhamBanChay.isEmpty()) {
            Map.Entry<String, Integer> topSanPham = sanPhamBanChay.get(0);
            modelBaoCao.addRow(new Object[]{"Sản phẩm bán chạy nhất", topSanPham.getKey()});
            modelBaoCao.addRow(new Object[]{"Số lượng bán", topSanPham.getValue()});
        } else {
            modelBaoCao.addRow(new Object[]{"Sản phẩm bán chạy nhất", "Không có dữ liệu"});
        }
    }

    private void hienThiChiTietSanPhamBanChay(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Tên sản phẩm", "Số lượng bán"});
        List<Map.Entry<String, Integer>> sanPhamBanChay = baoCaoDAO.getSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        for (Map.Entry<String, Integer> entry : sanPhamBanChay) {
            modelChiTiet.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void baoCaoSanPhamVaTongDoanhThu(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Thông tin", "Giá trị"});
        double tongDoanhThuTatCaSP = 0;
        List<Map.Entry<String, Double>> doanhThuTheoSanPham = baoCaoDAO.getDoanhThuTheoSanPham(ngayBatDau, ngayKetThuc);
        for (Map.Entry<String, Double> entry : doanhThuTheoSanPham) {
            tongDoanhThuTatCaSP += entry.getValue();
        }
        modelBaoCao.addRow(new Object[]{"Tổng doanh thu tất cả sản phẩm", dinhDangTien.format(tongDoanhThuTatCaSP)});
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThuTatCaSP));
    }

    private void hienThiChiTietDoanhThuSanPham(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Tên sản phẩm", "Tổng doanh thu"});
        List<Map.Entry<String, Double>> doanhThuTheoSanPham = baoCaoDAO.getDoanhThuTheoSanPham(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        for (Map.Entry<String, Double> entry : doanhThuTheoSanPham) {
            modelChiTiet.addRow(new Object[]{entry.getKey(), dinhDangTien.format(entry.getValue())});
        }
    }
    
    private void taoBieuDo() throws SQLException {
        new BieuDoBaoCao().setVisible(true);;
    }

    
}
