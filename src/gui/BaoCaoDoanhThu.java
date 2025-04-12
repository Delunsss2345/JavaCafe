package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class BaoCaoDoanhThu extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelLoc, panelNoiDung;
    private JTable tableBaoCao;
    private DefaultTableModel modelBaoCao;
    private JComboBox<String> cboNgayBatDau, cboThangBatDau, cboNamBatDau;
    private JComboBox<String> cboNgayKetThuc, cboThangKetThuc, cboNamKetThuc;
    private JComboBox<String> cboLoaiBaoCao;
    private JLabel lblTongDoanhThu;
    private DecimalFormat dinhDangTien;
    
    public BaoCaoDoanhThu() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        dinhDangTien = new DecimalFormat("#,### VNĐ");
        
        taoPanelLoc();
        taoPanelNoiDung();
        khoiTaoTable();
    }
    
    private void taoPanelLoc() {
        panelLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelLoc.setBackground(new Color(240, 240, 240));
        panelLoc.setBorder(BorderFactory.createTitledBorder("Lọc dữ liệu"));
        
        // Ngày bắt đầu
        JLabel lblNgayBatDau = new JLabel("Từ ngày:");
        
        // Danh sách ngày
        String[] cacNgay = new String[31];
        for(int i = 1; i <= 31; i++) {
            cacNgay[i-1] = String.format("%02d", i);
        }
        cboNgayBatDau = new JComboBox<>(cacNgay);
        
        // Danh sách tháng
        String[] cacThang = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        cboThangBatDau = new JComboBox<>(cacThang);
        
        // Danh sách năm (5 năm gần nhất)
        String[] cacNam = new String[5];
        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = 0; i < 5; i++) {
            cacNam[i] = String.valueOf(namHienTai - i);
        }
        cboNamBatDau = new JComboBox<>(cacNam);
        
        // Ngày kết thúc
        JLabel lblNgayKetThuc = new JLabel("Đến ngày:");
        cboNgayKetThuc = new JComboBox<>(cacNgay);
        cboThangKetThuc = new JComboBox<>(cacThang);
        cboNamKetThuc = new JComboBox<>(cacNam);
        
        // Loại báo cáo
        JLabel lblLoaiBaoCao = new JLabel("Loại báo cáo:");
        String[] cacLoaiBaoCao = {"Doanh thu theo ngày", "Doanh thu theo danh mục", "Sản phẩm bán chạy"};
        cboLoaiBaoCao = new JComboBox<>(cacLoaiBaoCao);
        
        // Nút tạo báo cáo
        JButton btnTaoBaoCao = new JButton("Tạo báo cáo");
        btnTaoBaoCao.setBackground(new Color(70, 130, 180));
        btnTaoBaoCao.setForeground(Color.WHITE);
        btnTaoBaoCao.addActionListener(e -> taoBaoCao());
        
        // Nút xuất Excel
        JButton btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(34, 139, 34));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.addActionListener(e -> xuatBaoCaoExcel());
        
        // Thêm các thành phần vào panel
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
        panelLoc.add(btnXuatExcel);
        
        add(panelLoc, BorderLayout.NORTH);
    }
    
    private Date layNgayBatDau() {
        String ngayStr = String.format("%s/%s/%s", 
            cboNgayBatDau.getSelectedItem(),
            cboThangBatDau.getSelectedItem(),
            cboNamBatDau.getSelectedItem());
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(ngayStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Date layNgayKetThuc() {
        String ngayStr = String.format("%s/%s/%s", 
            cboNgayKetThuc.getSelectedItem(),
            cboThangKetThuc.getSelectedItem(),
            cboNamKetThuc.getSelectedItem());
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(ngayStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void taoBaoCao() {
        Date ngayBatDau = layNgayBatDau();
        Date ngayKetThuc = layNgayKetThuc();
        
        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (ngayBatDau.after(ngayKetThuc)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String loaiBaoCao = (String) cboLoaiBaoCao.getSelectedItem();
        
        switch (loaiBaoCao) {
            case "Doanh thu theo ngày":
                taoBaoCaoDoanhThuNgay(ngayBatDau, ngayKetThuc);
                break;
            case "Doanh thu theo danh mục":
                taoBaoCaoDanhMuc(ngayBatDau, ngayKetThuc);
                break;
            case "Sản phẩm bán chạy":
                taoBaoCaoSanPhamBanChay(ngayBatDau, ngayKetThuc);
                break;
        }
    }
    
    private void taoPanelNoiDung() {
        panelNoiDung = new JPanel(new BorderLayout());
        panelNoiDung.setBorder(BorderFactory.createTitledBorder("Kết quả báo cáo"));
        
        tableBaoCao = new JTable();
        tableBaoCao.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tableBaoCao);
        panelNoiDung.add(scrollPane, BorderLayout.CENTER);
        
        lblTongDoanhThu = new JLabel("Tổng doanh thu: " + dinhDangTien.format(0));
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelNoiDung.add(lblTongDoanhThu, BorderLayout.SOUTH);
        
        add(panelNoiDung, BorderLayout.CENTER);
    }
    
    private void khoiTaoTable() {
        String[] cot = {"STT", "Thông tin", "Số lượng", "Doanh thu"};
        modelBaoCao = new DefaultTableModel(cot, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 2 || columnIndex == 3) return Double.class;
                return String.class;
            }
        };
        tableBaoCao.setModel(modelBaoCao);
    }
    
    private void taoBaoCaoDoanhThuNgay(Date ngayBatDau, Date ngayKetThuc) {
        // TODO: Thêm logic lấy dữ liệu từ database
        modelBaoCao.setRowCount(0);
        
        // Dữ liệu mẫu
        Object[] row1 = {1, "01/01/2023", 15, 1250000};
        Object[] row2 = {2, "02/01/2023", 20, 1800000};
        Object[] row3 = {3, "03/01/2023", 12, 950000};
        
        modelBaoCao.addRow(row1);
        modelBaoCao.addRow(row2);
        modelBaoCao.addRow(row3);
        
        double tongDoanhThu = 1250000 + 1800000 + 950000;
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }
    
    private void taoBaoCaoDanhMuc(Date ngayBatDau, Date ngayKetThuc) {
        // TODO: Thêm logic lấy dữ liệu từ database
        modelBaoCao.setRowCount(0);
        
        // Dữ liệu mẫu
        Object[] row1 = {1, "Cà phê", 45, 3200000};
        Object[] row2 = {2, "Trà", 30, 1500000};
        Object[] row3 = {3, "Bánh ngọt", 25, 1250000};
        
        modelBaoCao.addRow(row1);
        modelBaoCao.addRow(row2);
        modelBaoCao.addRow(row3);
        
        double tongDoanhThu = 3200000 + 1500000 + 1250000;
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }
    
    private void taoBaoCaoSanPhamBanChay(Date ngayBatDau, Date ngayKetThuc) {
        // TODO: Thêm logic lấy dữ liệu từ database
        modelBaoCao.setRowCount(0);
        
        // Dữ liệu mẫu
        Object[] row1 = {1, "Cà phê đen", 35, 1750000};
        Object[] row2 = {2, "Trà sữa trân châu", 25, 1250000};
        Object[] row3 = {3, "Bánh mì sandwich", 20, 800000};
        
        modelBaoCao.addRow(row1);
        modelBaoCao.addRow(row2);
        modelBaoCao.addRow(row3);
        
        double tongDoanhThu = 1750000 + 1250000 + 800000;
        lblTongDoanhThu.setText("Tổng doanh thu: " + dinhDangTien.format(tongDoanhThu));
    }
    
    private void xuatBaoCaoExcel() {
        // TODO: Thêm logic xuất Excel
        JOptionPane.showMessageDialog(this, "Đã xuất báo cáo sang Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}