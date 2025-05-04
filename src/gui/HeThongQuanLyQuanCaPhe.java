package gui;
// Người làm Phạm Thanh Huy
import javax.swing.*;

import dao.NhanVienDAO;
import entities.TaiKhoan;

import java.awt.*;

public class HeThongQuanLyQuanCaPhe extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel panelMenu, panelNoiDung;
    private CardLayout cardLayout;
    private TaiKhoan taiKhoanLogin;  

    // Các panel cho các màn hình khác nhau
    private ManHinhTrangChu manHinhTrangChu;
    private JPanel quanLySanPham;
    private JPanel quanLyHoaDon;
    private JPanel baoCaoDoanhThu;
    private JPanel quanLyNhanVien;
    private JPanel ThongTinCaNhan;
    private JPanel NhaCungCap;
    
    public HeThongQuanLyQuanCaPhe(TaiKhoan tk) {
        this.taiKhoanLogin = tk; 
        // Thiết lập frame chính
        setTitle("Hệ Thống Quản Lý Quán Cà Phê");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Khởi tạo CardLayout và panel nội dung
        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);

        // Khởi tạo các panel cho từng màn hình
        manHinhTrangChu = new ManHinhTrangChu(taiKhoanLogin); // Truyền tài khoản vào
        quanLySanPham = new QuanLySanPham(taiKhoanLogin);
        quanLyHoaDon = new frmQuanLyHoaDon();
        baoCaoDoanhThu = new BaoCaoDoanhThu();
        quanLyNhanVien = new frmNhanVien();
        ThongTinCaNhan = new ThongTinCaNhan(new NhanVienDAO().getNhanVienByTaiKhoan(taiKhoanLogin.getTenDangNhap()));
        NhaCungCap = new frmNhaCungCap();

        // Thêm các panel vào panelNoiDung
        panelNoiDung.add(manHinhTrangChu, "TrangChu");
        panelNoiDung.add(quanLySanPham, "SanPham");
        panelNoiDung.add(quanLyHoaDon, "HoaDon");
        panelNoiDung.add(baoCaoDoanhThu, "DoanhThu");
        panelNoiDung.add(quanLyNhanVien, "NhanVien");
        panelNoiDung.add(ThongTinCaNhan, "ThongTinCaNhan");
        panelNoiDung.add(NhaCungCap, "NhaCungCap");

        // Tạo panel menu ở bên trái
        panelMenu = new JPanel(new GridLayout(8, 1));
        panelMenu.setBackground(new Color(153, 76, 0));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnTrangChu = taoNutMenu("Trang Chủ", "src/images/home.png");
        JButton btnSanPham = taoNutMenu("Sản Phẩm", "src/images/product.png");
        JButton btnHoaDon = taoNutMenu("Hóa Đơn", "src/images/rece.png");
        JButton btnThongKe = taoNutMenu("Thống Kê", "src/images/sta.png");
        JButton btnDangXuat = taoNutMenu("Đăng xuất", "src/images/logout.png");
        JButton btnNhanVien = taoNutMenu("Nhân Viên", "src/images/nhanvien.png");
        JButton btnThongTinCaNhan = taoNutMenu("Thông tin cá nhân", "src/images/user.png");
        JButton btnNhaCungCap = taoNutMenu("Nhà Cung Cấp", "src/images/supplier.png");

        // Sự kiện cho các nút menu
        btnTrangChu.addActionListener(e -> hienThiTrangChu());
        btnSanPham.addActionListener(e -> hienThiQuanLySanPham());
        btnHoaDon.addActionListener(e -> hienThiQuanLyHoaDon());
        btnThongKe.addActionListener(e -> hienThiBaoCaoDoanhThu());
        btnDangXuat.addActionListener(e -> {
            dispose();
            // Trở về màn đăng nhập
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
        btnNhanVien.addActionListener(e -> hienThiQuanLyNhanVien());
        btnThongTinCaNhan.addActionListener(e -> hienThiThongTinCaNhan());
        btnNhaCungCap.addActionListener(e -> hienThiNhaCungCap());

        // Thêm nút vào panel menu
        panelMenu.add(btnTrangChu);
        panelMenu.add(btnSanPham);
        panelMenu.add(btnHoaDon);
        panelMenu.add(btnThongKe);
        panelMenu.add(btnNhanVien);
        panelMenu.add(btnThongTinCaNhan);
        panelMenu.add(btnNhaCungCap);
        panelMenu.add(btnDangXuat);

        add(panelMenu, BorderLayout.WEST);
        add(panelNoiDung, BorderLayout.CENTER);

        hienThiTrangChu();
        setVisible(true);
    }

    private JButton taoNutMenu(String text, String duongDanIcon) {
        JButton nut = new JButton(text);
        nut.setIcon(new ImageIcon(new ImageIcon(duongDanIcon).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        nut.setHorizontalTextPosition(SwingConstants.CENTER);
        nut.setVerticalTextPosition(SwingConstants.BOTTOM);
        nut.setForeground(Color.WHITE);
        nut.setFont(new Font("Arial", Font.BOLD, 16));
        nut.setFocusPainted(false);
        nut.setBorderPainted(false);
        nut.setContentAreaFilled(false);
        return nut;
    }

    // Các phương thức chuyển Card
    private void hienThiTrangChu() {
        cardLayout.show(panelNoiDung, "TrangChu");
        manHinhTrangChu.taiDanhSachSanPham();
    }
    private void hienThiQuanLySanPham() { cardLayout.show(panelNoiDung, "SanPham"); }
    private void hienThiQuanLyHoaDon() { cardLayout.show(panelNoiDung, "HoaDon"); }
    private void hienThiBaoCaoDoanhThu() {
       cardLayout.show(panelNoiDung, "DoanhThu");
    }
    private void hienThiQuanLyNhanVien() {
        if (taiKhoanLogin.getQuyen().getMaQuyen() == 1) cardLayout.show(panelNoiDung, "NhanVien");
        else JOptionPane.showMessageDialog(this, "Chỉ có quản lý mới có quyền quản lý nhân viên");
    }
    private void hienThiThongTinCaNhan() { cardLayout.show(panelNoiDung, "ThongTinCaNhan"); }
    private void hienThiNhaCungCap() {
        if (taiKhoanLogin.getQuyen().getMaQuyen() == 1) cardLayout.show(panelNoiDung, "NhaCungCap");
        else JOptionPane.showMessageDialog(this, "Chỉ có quản lý mới có quyền quản lý nhà cung cấp");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HeThongQuanLyQuanCaPhe(null));
    }
}
