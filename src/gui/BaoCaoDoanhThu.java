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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

public class BaoCaoDoanhThu extends JPanel implements ActionListener, MouseListener {
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
	private JLabel lblTongLoiNhuan;
	private DefaultTableModel modelChiTietHoaDon;
	private JTable tableChiTietHoaDon;
	private JPanel panelBang;
	private JPanel panelChiTietHoaDon;

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
        String[] cacLoaiBaoCao = { "Số hoá đơn bán ra","Doanh thu theo ngày trong tháng","Doanh thu theo tháng","Sản phẩm bán chạy nhất và chậm nhất", "Tên sản phẩm và tổng doanh thu" };
        cboLoaiBaoCao = new JComboBox<>(cacLoaiBaoCao);

        btnTaoBaoCao = new JButton("Tạo báo cáo");
        btnTaoBaoCao.setBackground(new Color(70, 130, 180));
        btnTaoBaoCao.setForeground(Color.WHITE);
        
        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(70, 130, 180));
        btnXuatExcel.setForeground(Color.WHITE);
        
        btnTaoBieuDO = new JButton("Tạo biểu đồ");
        btnTaoBieuDO.setBackground(new Color(70, 130, 180));
        btnTaoBieuDO.setForeground(Color.WHITE);
        
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
        
        // Event
        btnTaoBaoCao.addActionListener(this);
        btnXuatExcel.addActionListener(this);
        btnTaoBieuDO.addActionListener(this);
    }

    private void taoPanelNoiDung() {
        panelNoiDung = new JPanel();
        panelNoiDung.setLayout(new BoxLayout(panelNoiDung, BoxLayout.Y_AXIS));
        panelNoiDung.setBorder(BorderFactory.createTitledBorder("Kết quả báo cáo"));
        panelNoiDung.setBackground(new Color(240, 240, 240));
        
        panelBang = new JPanel();
        panelBang.setLayout(new GridLayout(1, 2, 10, 10));
        
        modelBaoCao = new DefaultTableModel(new String[] { "Thông tin", "Giá trị" }, 0);
        tableBaoCao = new JTable(modelBaoCao);
        JScrollPane scrollBaoCao = new JScrollPane(tableBaoCao);
        scrollBaoCao.setBorder(BorderFactory.createTitledBorder("Báo cáo tổng hợp"));
        panelBang.add(scrollBaoCao);

        modelChiTiet = new DefaultTableModel();
        tableChiTiet = new JTable(modelChiTiet);
        JScrollPane scrollChiTiet = new JScrollPane(tableChiTiet);
        scrollChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết"));
        panelBang.add(scrollChiTiet);
        lblTongDoanhThu = new JLabel("Tổng doanh thu: " + dinhDangTien.format(0));
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTongLoiNhuan = new JLabel("Tổng lợi nhuận: " + dinhDangTien.format(0));
        lblTongLoiNhuan.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongLoiNhuan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTongLoiNhuan.setForeground(Color.RED);
        JPanel panelTongDoanhThuLoiNhuan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTongDoanhThuLoiNhuan.setBackground(new Color(240, 240, 240));
        panelTongDoanhThuLoiNhuan.add(lblTongDoanhThu);
        panelTongDoanhThuLoiNhuan.add(lblTongLoiNhuan);
        
        panelChiTietHoaDon = new JPanel(new BorderLayout());
        
        
        panelNoiDung.add(panelBang);
        panelNoiDung.add(panelChiTietHoaDon);
        add(panelNoiDung, BorderLayout.CENTER);
        add(panelTongDoanhThuLoiNhuan, BorderLayout.SOUTH);
        // Event
        tableBaoCao.addMouseListener(this);
        tableChiTiet.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(btnTaoBaoCao)) {
			if (modelChiTietHoaDon != null) {
				modelChiTietHoaDon.setRowCount(0);
			}
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
            ghiTableVaoSheet(sheetSummary, tableBaoCao, headerStyle, moneyStyle);

            // --- TẠO SHEET "CHI TIẾT" ---
            Sheet sheetDetail = workbook.createSheet("Chi tiết");
            ghiTableVaoSheet(sheetDetail, tableChiTiet, headerStyle, moneyStyle);

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
    private void ghiTableVaoSheet(Sheet sheet, JTable table,
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
                case "Số hoá đơn bán ra":
                	baoCaoCacHoaDonBanRa(ngayBatDau, ngayKetThuc);
                    break;
                case "Doanh thu theo ngày trong tháng":
                    baoCaoDoanhThuTheoNgay(ngayBatDau, ngayKetThuc);
                    break;
                case "Doanh thu theo tháng":
                    baoCaoDoanhThuTheoThang(ngayBatDau, ngayKetThuc);
                    break;
                case "Sản phẩm bán chạy nhất và chậm nhất":
                    baoCaoSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
                    hienThiChiTietSanPham(ngayBatDau, ngayKetThuc);
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
    
    private void baoCaoCacHoaDonBanRa(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
    	modelBaoCao.setColumnIdentifiers(new Object[] {"Mã hoá đơn", "Doanh Thu", "Lợi nhuận"});
    	List<Object[]> danhSachHoaDon = baoCaoDAO.getCacHoaDonBanRa(ngayBatDau, ngayKetThuc);
    	modelBaoCao.setRowCount(0);
    	double tongDoanhThu = 0;
    	double tongLoiNhuan = 0;
		for (Object[] hoaDon : danhSachHoaDon) {
			modelBaoCao.addRow(hoaDon);
			tongDoanhThu += (double) hoaDon[1];
			tongLoiNhuan += (double) hoaDon[2];
		}
		lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
		lblTongLoiNhuan.setText("Tổng lợi nhuận: " + dinhDangTien.format(tongLoiNhuan));
		
    }



    private void baoCaoDoanhThuTheoNgay(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Tháng", "Doanh thu", "Lợi nhuận"});
        modelBaoCao.setRowCount(0);

        List<Object[]> ketQua = baoCaoDAO.getDoanhThuVaLoiNhuanTheoThang(ngayBatDau, ngayKetThuc);

        double tongDoanhThu = 0;
        double tongLoiNhuan = 0;

        for (Object[] row : ketQua) {
            int thang = (int) row[0];
            double doanhThu = ((Number) row[1]).doubleValue();
            double loiNhuan = ((Number) row[2]).doubleValue();

            modelBaoCao.addRow(new Object[]{
                thang,
                dinhDangTien.format(doanhThu),
                dinhDangTien.format(loiNhuan)
            });

            tongDoanhThu += doanhThu;
            tongLoiNhuan += loiNhuan;
        }

        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
        lblTongLoiNhuan.setText("Tổng lợi nhuận: " + dinhDangTien.format(tongLoiNhuan));
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

    private void baoCaoSanPhamBanChayNhat(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelBaoCao.setColumnIdentifiers(new Object[]{"Tên sản phẩm", "Số lượng bán"});
        List<Map.Entry<String, Integer>> sanPhamBanChay = baoCaoDAO.getSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
        List<Map.Entry<String, Integer>> sanPhamBanChamNhat = baoCaoDAO.getSanPhamBanChamNhat(ngayBatDau, ngayKetThuc);
        modelBaoCao.setRowCount(0);
        if (!sanPhamBanChay.isEmpty()) {
            Map.Entry<String, Integer> sanPhamBanChayNhat = sanPhamBanChay.get(0);
            Map.Entry<String, Integer> sanPhamBanCham = sanPhamBanChamNhat.get(0);
            modelBaoCao.addRow(new Object[]{sanPhamBanChayNhat.getKey() , sanPhamBanChayNhat.getValue()});
            modelBaoCao.addRow(new Object[] {sanPhamBanCham.getKey() , sanPhamBanCham.getValue()});
        } else {
            modelBaoCao.addRow(new Object[]{"Không có sản phẩm", "Không có dữ liệu"});
        }
    }

    private void hienThiChiTietSanPham(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        modelChiTiet.setColumnIdentifiers(new Object[]{"Tên sản phẩm", "Số lượng bán"});
        List<Map.Entry<String, Integer>> sanPhamBanChay = baoCaoDAO.getSanPhamBanChayNhat(ngayBatDau, ngayKetThuc);
        modelChiTiet.setRowCount(0);
        for (Map.Entry<String, Integer> entry : sanPhamBanChay) {
            modelChiTiet.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void baoCaoSanPhamVaTongDoanhThu(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        List<Object[]> list = baoCaoDAO.getDoanhThuVaLoiNhuanTheoSanPham(ngayBatDau, ngayKetThuc);

        double tongDT = 0, tongLN = 0;
        for (Object[] row : list) {
            tongDT += (double) row[5];  // TongDoanhThu
            tongLN += (double) row[6];  // TongLoiNhuan
        }

        modelBaoCao.setColumnIdentifiers(new Object[]{"Chỉ số", "Giá trị"});
        modelBaoCao.setRowCount(0);
        modelBaoCao.addRow(new Object[]{
            "Tổng doanh thu tất cả sản phẩm", dinhDangTien.format(tongDT)
        });
        modelBaoCao.addRow(new Object[]{
            "Tổng lợi nhuận tất cả sản phẩm", dinhDangTien.format(tongLN)
        });

        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDT));
        lblTongLoiNhuan.setText("Tổng lợi nhuận: " + dinhDangTien.format(tongLN));
    }


    private void hienThiChiTietDoanhThuSanPham(Date ngayBatDau, Date ngayKetThuc) throws SQLException {
        List<Object[]> list = baoCaoDAO.getDoanhThuVaLoiNhuanTheoSanPham(ngayBatDau, ngayKetThuc);

        modelChiTiet.setColumnIdentifiers(new Object[]{
            "Mã Sản Phẩm", "Tên Sản Phẩm", "Số lượng bán ra", "Giá nhập", "Giá bán",
            "Doanh thu", "Lợi nhuận"
        });
        modelChiTiet.setRowCount(0);

        for (Object[] row : list) {
            modelChiTiet.addRow(new Object[]{
                row[0],                             // MaSP
                row[1],                             // TenSanPham
                row[2],                             // TongSoLuongBanRa
                dinhDangTien.format((double)row[3]),// GiaNhap
                dinhDangTien.format((double)row[4]),// GiaBanRa
                dinhDangTien.format((double)row[5]),// TongDoanhThu
                dinhDangTien.format((double)row[6]) // TongLoiNhuan
            });
        }
    }

    
    private void taoBieuDo() throws SQLException {
        new BieuDoBaoCao().setVisible(true);;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String loaiBaoCao = (String) cboLoaiBaoCao.getSelectedItem();
        if (loaiBaoCao.equalsIgnoreCase("Số hoá đơn bán ra")) {
            int row = tableBaoCao.getSelectedRow();
            if (row >= 0) {
                int maHoaDon = (int) modelBaoCao.getValueAt(row, 0);
                try {
                    Object[] thongTinHoaDon = baoCaoDAO.getThongTinTheoMaHoaDon(maHoaDon);
                    if (thongTinHoaDon != null) {
                        modelChiTiet.setRowCount(0);
                        modelChiTiet.setColumnIdentifiers(new Object[] {
                            "Mã hoá đơn", "Ngày tạo", "Tổng tiền",
                            "Tiền khách trả", "Tiền thừa", "Mã nhân viên", "Mã khách hàng"
                        });
                        modelChiTiet.addRow(thongTinHoaDon);
                        hienThiChiTietHoaDon(maHoaDon);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Không tìm thấy thông tin hoá đơn mã: " + maHoaDon,
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
            
        } else if (loaiBaoCao.equalsIgnoreCase("Doanh thu theo tháng")) {
        	
            int row = tableBaoCao.getSelectedRow();
            if (row >= 0) {
                String thangNam = (String) modelBaoCao.getValueAt(row, 0);
                String[] thangNamParts = thangNam.split("/");
                int thang = Integer.parseInt(thangNamParts[0]);
                double tongLoiNhuan = 0;
                try {
                    List<Object[]> danhSachChiTiet = baoCaoDAO.getChiTietDoanhThuVaLoiNhuanTheoThang(thang);
                    
                    modelChiTiet.setRowCount(0);
                    modelChiTiet.setColumnIdentifiers(new Object[] {
                        "Năm", "Tháng", "Tên sản phẩm", "Giá nhập", "Giá bán", "Số lượng", "Doanh thu", "Lợi nhuận"
                    });
                    
                    for (Object[] dong : danhSachChiTiet) {
                        modelChiTiet.addRow(dong);
                        tongLoiNhuan += (double) dong[7];
                    }
                   
                 lblTongLoiNhuan.setText("Tổng lợi nhuận: " + dinhDangTien.format(tongLoiNhuan));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else if (loaiBaoCao.equalsIgnoreCase("Doanh thu theo ngày trong tháng")) {
            int row = tableBaoCao.getSelectedRow();
            if (row >= 0) {
                int thang = (Integer) modelBaoCao.getValueAt(row, 0);
                try {
                	hienThiChiTietHoaDonTheoNgayTrongThang(thang);  
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
      
        
            }
        }

        }


private void hienThiChiTietHoaDon(int maHoaDon) throws SQLException {
    
	System.out.println("Đang truy vấn thông tin chi tiết hoá đơn mã: " + maHoaDon);
    List<Object[]> chiTietHoaDon = baoCaoDAO.getThongTinChiTietHoaDonTheoMaHoaDon(maHoaDon);
	for (Object[] row1 : chiTietHoaDon) {
		System.out.println(Arrays.toString(row1));
	}
    
    if (chiTietHoaDon != null && !chiTietHoaDon.isEmpty()) {
    	panelChiTietHoaDon.removeAll();
        String[] columnNames = { "Mã chi tiết hoá đơn", "Mã hoá đơn", "Mã sản phẩm", "Tên sản phẩm",
                                 "Số lượng", "Đơn giá", "Thành tiền" ,"Lợi nhuận"};

        modelChiTietHoaDon = new DefaultTableModel(columnNames, 0);
        tableChiTietHoaDon = new JTable(modelChiTietHoaDon);

        
        for (Object[] row1 : chiTietHoaDon) {
            modelChiTietHoaDon.addRow(row1);
        }

        JScrollPane scrollPane = new JScrollPane(tableChiTietHoaDon);
        panelChiTietHoaDon.setBorder(BorderFactory.createTitledBorder("Chi tiết hoá đơn"));
        panelChiTietHoaDon.add(scrollPane);
        panelNoiDung.add(panelChiTietHoaDon);
        
        panelChiTietHoaDon.revalidate();
        panelChiTietHoaDon.repaint();
    }
}

private void hienThiChiTietHoaDonTheoNgayTrongThang(int thang) throws SQLException {
	modelChiTiet.setColumnIdentifiers(new Object[] {"Ngày", "Tên sản phẩm", "Số lượng" , "Đơn giá", "Doanh Thu", "Lợi Nhuận"});
	modelChiTiet.setRowCount(0);
	tableChiTiet = new JTable(modelChiTiet);
	List<Object[]> chiTietHoaDon = baoCaoDAO.getDoanhThuVaLoiNhuanTrongNgay(thang);
	for (Object[] row1 : chiTietHoaDon) {
        System.out.println(Arrays.toString(row1));
    }
	for (Object[] row1 : chiTietHoaDon) {
		modelChiTiet.addRow(row1);
	}
	
}

	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

    
}
