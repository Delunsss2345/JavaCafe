package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import ConnectDB.DatabaseConnection;
import dao.KhachHangDAO;
import entities.ChiTietHoaDonCaPhe;
import entities.KhachHang;

public class frmLapHoaDon extends JFrame {
    private JTextField txtMaNV, txtTenNV, txtMaKH, txtTenKH;
    private JTable tableGioHang;
    private JButton btnXuatHoaDon, btnLuuKhachHang;
    private List<ChiTietHoaDonCaPhe> gioHang;
    private double tongTien = 0;
    private Connection conn;
    private KhachHangDAO khachHangDAO;

    public frmLapHoaDon(List<ChiTietHoaDonCaPhe> gioHang) {
        this.gioHang = gioHang;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            khachHangDAO = new KhachHangDAO(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Lập Hóa Đơn");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tính tổng tiền từ giỏ hàng
        if (gioHang != null) {
            for (ChiTietHoaDonCaPhe item : gioHang) {
                item.setThanhTien(item.getDonGia() * item.getSoLuong());
                tongTien += item.getThanhTien();
            }
        }

        // Panel thông tin nhân viên và khách hàng
        JPanel panelThongTin = new JPanel(new GridLayout(3, 4, 10, 10));
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

        // Thêm label và hiển thị tổng tiền
        panelThongTin.add(new JLabel("Tổng tiền:"));
        JTextField txtTongTien = new JTextField(String.format("%,.0f VNĐ", tongTien));
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        panelThongTin.add(txtTongTien);

        add(panelThongTin, BorderLayout.NORTH);

        // Bảng giỏ hàng
        String[] columnNames = {"STT", "Tên SP", "Mã SP", "Đơn giá", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tableGioHang = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableGioHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết giỏ hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
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
            // Sử dụng mã khách hàng ngẫu nhiên từ trường mã KH đã tạo
            String maKH = getMaKhachHang();
            
            // Tạo đối tượng khách hàng mới với thông tin cơ bản
            KhachHang kh = new KhachHang(tenKH);
            
            // Đặt mã khách hàng từ trường mã KH đã tạo ngẫu nhiên
            kh.setMaKhachHang(Long.parseLong(maKH));
            
            // Lưu vào cơ sở dữ liệu
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
        
        // Kiểm tra thông tin cần thiết
        if (maNV.isEmpty() || tenNV.isEmpty() || tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin nhân viên và khách hàng!", 
                                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tính tiền mặc định (có thể mở rộng chức năng nhập tiền khách trả)
        double tienKhachTra = tongTien;
        
        // Thời gian hiện tại
        LocalDateTime gioVao = LocalDateTime.now();
        LocalDateTime gioRa = LocalDateTime.now();
        
        try {
            // Hiển thị chi tiết hóa đơn
            frmChiTietHoaDon chiTietForm = new frmChiTietHoaDon(
                maNV, tenNV, maKH, tenKH, tongTien, gioVao, gioRa, gioHang, 0, tienKhachTra
            );
            chiTietForm.setVisible(true);
            
            // Nếu hóa đơn đã in thành công, đóng form hiện tại
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

    public List<ChiTietHoaDonCaPhe> getGioHang() {
        return gioHang;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmLapHoaDon frm = new frmLapHoaDon(null);
            frm.setVisible(true);
        });
    }
}
