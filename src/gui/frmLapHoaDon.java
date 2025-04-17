//Nguyễn Tuấn Phát
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entities.ChiTietHoaDonCaPhe;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class frmLapHoaDon extends JFrame {
    private JTextField txtMaNV, txtTenNV, txtMaKH, txtTenKH;
    private JTable tableGioHang;
    private JLabel lblTongTien;
    private JButton btnXuatHoaDon;
    private List<ChiTietHoaDonCaPhe> gioHang;
    private double tongTien;

    public frmLapHoaDon(List<ChiTietHoaDonCaPhe> gioHang, double tongTien) {
        this.gioHang = gioHang;
        this.tongTien = tongTien;

        setTitle("Lập Hóa Đơn");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thông tin nhân viên và khách hàng
        JPanel panelThongTin = new JPanel(new GridLayout(2, 4, 10, 10));
        panelThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));

        panelThongTin.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        panelThongTin.add(txtMaNV);

        panelThongTin.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField();
        panelThongTin.add(txtTenNV);

        panelThongTin.add(new JLabel("Mã KH:"));
        txtMaKH = new JTextField(taoMaKhachHangNgauNhien());
        txtMaKH.setEditable(false);
        panelThongTin.add(txtMaKH);

        panelThongTin.add(new JLabel("Tên KH:"));
        txtTenKH = new JTextField();
        panelThongTin.add(txtTenKH);

        add(panelThongTin, BorderLayout.NORTH);

        // Bảng giỏ hàng
        String[] columnNames = {"STT", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableGioHang = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableGioHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết giỏ hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel tổng tiền + nút
        JPanel panelBottom = new JPanel(new BorderLayout());

        // Panel chứa các nút
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnXuatHoaDon = new JButton("Xuất hóa đơn");

        // Sự kiện nút (bạn có thể thêm logic xử lý riêng ở đây)
        btnXuatHoaDon.addActionListener(e -> xuatHoaDon());

        panelButtons.add(btnXuatHoaDon);

        panelBottom.add(panelButtons, BorderLayout.WEST);

        // Hiển thị tổng tiền
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        panelBottom.add(lblTongTien, BorderLayout.EAST);

        add(panelBottom, BorderLayout.SOUTH);

        hienThiGioHang();
        hienThiTongTien();
    }

    private void hienThiGioHang() {
        DefaultTableModel model = (DefaultTableModel) tableGioHang.getModel();
        model.setRowCount(0);
        int stt = 1;
        if (gioHang != null) {
            for (ChiTietHoaDonCaPhe ct : gioHang) {
                model.addRow(new Object[]{
                    stt++,
                    ct.getTenSanPham(),
                    String.format("%,.0f VNĐ", ct.getDonGia()),
                    ct.getSoLuong(),
                    String.format("%,.0f VNĐ", ct.getThanhTien())
                });
            }
        }
    }

    private void hienThiTongTien() {
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tongTien));
    }

    private String taoMaKhachHangNgauNhien() {
        int soNgauNhien = (int) (Math.random() * 90000) + 10000;
        return "KH" + soNgauNhien;
    }

    private void xuatHoaDon() {
        String maNV = getMaNhanVien();
        String tenNV = getTenNhanVien();
        String maKH = getMaKhachHang();
        String tenKH = getTenKhachHang();

        LocalDateTime gioVao = LocalDateTime.now();
        LocalDateTime gioRa = LocalDateTime.now();

        frmChiTietHoaDon chiTietForm = new frmChiTietHoaDon(maNV, tenNV, maKH, tenKH, tongTien, gioRa, gioRa, gioHang, getDefaultCloseOperation(), tongTien);
        chiTietForm.setVisible(true);
    }


    public String getMaNhanVien() {
        return txtMaNV.getText().trim();
    }

    public String getTenNhanVien() {
        return txtTenNV.getText().trim();
    }
    
    public String getMaKhachHang() {
        return txtMaKH.getText().trim();
    }

    public String getTenKhachHang() {
        return txtTenKH.getText().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmLapHoaDon frm = new frmLapHoaDon(null, 0);
            frm.setVisible(true);
        });
    }
}
