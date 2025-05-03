package gui;
// Người làm Nguyễn Tuấn Phát

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import ConnectDB.DatabaseConnection;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import entities.ChiTietHoaDonCaPhe;
import entities.KhachHang;
import entities.NhanVien;
import entities.TaiKhoan;

public class frmLapHoaDon extends JFrame {
    private JTextField txtMaNV, txtTenNV, txtMaKH, txtTenKH, txtTienKhachDua, txtTienThua;
    private JTable tableGioHang;
    private JButton btnXuatHoaDon, btnLuuKhachHang;
    private List<ChiTietHoaDonCaPhe> gioHang;
    private double tongTien = 0;
    private Connection conn;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoan taiKhoan;

    public frmLapHoaDon(List<ChiTietHoaDonCaPhe> gioHang, TaiKhoan taiKhoan) {
        this.gioHang = gioHang;
        this.taiKhoan = taiKhoan;
        this.nhanVienDAO = new NhanVienDAO();

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            khachHangDAO = new KhachHangDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Lập Hóa Đơn");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        if (gioHang != null) {
            for (ChiTietHoaDonCaPhe item : gioHang) {
                item.setThanhTien(item.getDonGia() * item.getSoLuong());
                tongTien += item.getThanhTien();
            }
        }

        // Panel thông tin giao dịch
        JPanel panelThongTin = new JPanel(new GridLayout(4, 4, 10, 10));
        panelThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));

        panelThongTin.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        panelThongTin.add(txtMaNV);

        panelThongTin.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField();
        txtTenNV.setEditable(false);
        panelThongTin.add(txtTenNV);

        panelThongTin.add(new JLabel("Mã KH:"));
        txtMaKH = new JTextField(taoMaKhachHangNgauNhien());
        txtMaKH.setEditable(false);
        panelThongTin.add(txtMaKH);

        panelThongTin.add(new JLabel("Tên KH:"));
        txtTenKH = new JTextField();
        panelThongTin.add(txtTenKH);

        panelThongTin.add(new JLabel("Tổng tiền:"));
        JTextField txtTongTien = new JTextField(String.format("%,.0f VNĐ", tongTien));
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        panelThongTin.add(txtTongTien);

        panelThongTin.add(new JLabel("Tiền khách đưa:"));
        txtTienKhachDua = new JTextField();
        txtTienKhachDua.setFont(new Font("Arial", Font.BOLD, 14));
        panelThongTin.add(txtTienKhachDua);
        
        panelThongTin.add(new JLabel("Tiền thừa:"));
        txtTienThua = new JTextField();
        txtTienThua.setEditable(false);
        txtTienThua.setFont(new Font("Arial", Font.BOLD, 14));
        panelThongTin.add(txtTienThua);

        txtTienKhachDua.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTienThua();
            }
        });

        add(panelThongTin, BorderLayout.NORTH);

        NhanVien nvLogin = nhanVienDAO.getNhanVienByTaiKhoan(taiKhoan.getTenDangNhap());
        if (nvLogin != null) {
            txtMaNV.setText(String.valueOf(nvLogin.getMaNV()));
            txtTenNV.setText(nvLogin.getHo() + " " + nvLogin.getTen());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Bảng giỏ hàng
        String[] columnNames = {"STT", "Tên SP", "Mã SP", "Đơn giá", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableGioHang = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableGioHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết giỏ hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // Nút chức năng
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnLuuKhachHang = new JButton("Lưu thông tin KH");
        btnLuuKhachHang.addActionListener(e -> luuKhachHang());
        btnXuatHoaDon = new JButton("Xuất hóa đơn");
        btnXuatHoaDon.addActionListener(e -> xuatHoaDon());
        panelBottom.add(btnLuuKhachHang);
        panelBottom.add(btnXuatHoaDon);
        add(panelBottom, BorderLayout.SOUTH);

        hienThiGioHang();
    }

    private void tinhTienThua() {
        try {
            String tienKhachDuaText = txtTienKhachDua.getText().trim().replace(",", "").replace("VNĐ", "").trim();
            if (!tienKhachDuaText.isEmpty()) {
                double tienKhachDua = Double.parseDouble(tienKhachDuaText);
                double tienThua = tienKhachDua - tongTien;
                if (tienThua >= 0) {
                    txtTienThua.setText(String.format("%,.0f VNĐ", tienThua));
                } else {
                    txtTienThua.setText("Thiếu " + String.format("%,.0f VNĐ", Math.abs(tienThua)));
                }
            } else {
                txtTienThua.setText("");
            }
        } catch (NumberFormatException e) {
            txtTienThua.setText("Lỗi định dạng");
        }
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
                        ct.getMaSanPham(),
                        String.format("%,.0f VNĐ", ct.getDonGia()),
                        ct.getSoLuong(),
                        String.format("%,.0f VNĐ", ct.getThanhTien())
                });
            }
        }
    }

    private String taoMaKhachHangNgauNhien() {
        int soNgauNhien = (int) (Math.random() * 90000) + 10000;
        return String.valueOf(soNgauNhien);
    }

    private void luuKhachHang() {
        String tenKH = getTenKhachHang();
        if (tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String maKH = getMaKhachHang();
            KhachHang kh = new KhachHang(tenKH);
            kh.setMaKhachHang(Long.parseLong(maKH));
            boolean ketQua = khachHangDAO.insertKhachHang(kh);
            if (ketQua) {
                JOptionPane.showMessageDialog(this, "Đã lưu thông tin khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lưu thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatHoaDon() {
        String maNV = getMaNhanVien();
        String tenNV = getTenNhanVien();
        String maKH = getMaKhachHang();
        String tenKH = getTenKhachHang();
        if (maNV.isEmpty() || tenNV.isEmpty() || tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin nhân viên và khách hàng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double tienKhachTra = 0;
        try {
            String tienKhachDuaText = txtTienKhachDua.getText().trim().replace(",", "").replace("VNĐ", "").trim();
            if (tienKhachDuaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tienKhachTra = Double.parseDouble(tienKhachDuaText);
            if (tienKhachTra < tongTien) {
                JOptionPane.showMessageDialog(this, "Số tiền khách đưa không đủ thanh toán!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa không hợp lệ!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LocalDateTime gioVao = LocalDateTime.now();
        LocalDateTime gioRa = LocalDateTime.now();
        try {
            frmChiTietHoaDon chiTietForm = new frmChiTietHoaDon(
                    maNV, tenNV, maKH, tenKH, tongTien, gioVao, gioRa, gioHang, 0, tienKhachTra
            );
            chiTietForm.setVisible(true);
            if (chiTietForm.inResult) {
                this.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
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

    public double getTongTien() {
        return tongTien;
    }
    
    public double getTienKhachDua() {
        try {
            String tienText = txtTienKhachDua.getText().trim().replace(",", "").replace("VNĐ", "").trim();
            return tienText.isEmpty() ? 0 : Double.parseDouble(tienText);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public List<ChiTietHoaDonCaPhe> getGioHang() {
        return gioHang;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaiKhoan demoTk = new TaiKhoan();
            frmLapHoaDon frm = new frmLapHoaDon(null, demoTk);
            frm.setVisible(true);
        });
    }
}
