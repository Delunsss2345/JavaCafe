package gui;
//Người làm Phạm Thanh Huy
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeThongQuanLyQuanCaPhe extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelMenu, panelNoiDung;
    private CardLayout cardLayout;

    // Các panel cho các màn hình khác nhau
    private ManHinhTrangChu manHinhTrangChu;
    private JPanel quanLySanPham;
    private JPanel quanLyHoaDon;
    private JPanel baoCaoDoanhThu;

    public HeThongQuanLyQuanCaPhe() {
        // Thiết lập frame chính
        setTitle("Hệ Thống Quản Lý Quán Cà Phê");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Khởi tạo CardLayout và panel nội dung
        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);

        // Khởi tạo các panel cho từng màn hình
        manHinhTrangChu = new ManHinhTrangChu();
        quanLySanPham = new QuanLySanPham();
////        quanLyHoaDon = new QuanLyDonHang();
//        baoCaoDoanhThu = new BaoCaoDoanhThu();

        // Thêm các panel vào panelNoiDung
        panelNoiDung.add(manHinhTrangChu, "TrangChu");
        panelNoiDung.add(quanLySanPham, "SanPham");
////        panelNoiDung.add(quanLyHoaDon, "HoaDon");
//        panelNoiDung.add(baoCaoDoanhThu, "DoanhThu");

        // Tạo panel menu ở bên trái
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(5, 1));
        panelMenu.setBackground(new Color(153, 76, 0)); // Màu nâu
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnTrangChu = taoNutMenu("Trang Chủ", "src\\images\\home.png");
        JButton btnSanPham = taoNutMenu("Sản Phẩm", "src\\images\\product.png");
        JButton btnHoaDon = taoNutMenu("Hóa Đơn", "src\\images\\rece.png");
        JButton btnThongKe = taoNutMenu("Thống Kê", "src\\images\\sta.png");
        JButton btnDangXuat = taoNutMenu("ĐĂNG XUẤT", "src\\images\\logout.png");

        // Thêm sự kiện cho các nút menu
        btnTrangChu.addActionListener(e -> {
        	hienThiTrangChu() ;
        	
        });
        btnSanPham.addActionListener(e -> hienThiQuanLySanPham());
        btnHoaDon.addActionListener(e -> hienThiQuanLyHoaDon());
        btnThongKe.addActionListener(e -> hienThiBaoCaoDoanhThu());
        btnDangXuat.addActionListener(e -> {
            dispose();
            ManHinhTrangChu dangNhap = new ManHinhTrangChu();
            dangNhap.setVisible(true);
        });

        // Thêm các nút vào panel menu
        panelMenu.add(btnTrangChu);
        panelMenu.add(btnSanPham);
        panelMenu.add(btnHoaDon);
        panelMenu.add(btnThongKe);
        panelMenu.add(btnDangXuat);

        // Thêm các panel vào frame
        add(panelMenu, BorderLayout.WEST);
        add(panelNoiDung, BorderLayout.CENTER);

        // Hiển thị màn hình trang chủ mặc định
        hienThiTrangChu();

        setVisible(true);
    }

    // Phương thức tạo nút menu với biểu tượng
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

    // Hiển thị màn hình trang chủ
    private void hienThiTrangChu() {
        cardLayout.show(panelNoiDung, "TrangChu");
    }

    // Hiển thị màn hình quản lý sản phẩm
    private void hienThiQuanLySanPham() {
        cardLayout.show(panelNoiDung, "SanPham");
    }

    // Hiển thị màn hình quản lý hóa đơn
    private void hienThiQuanLyHoaDon() {
        cardLayout.show(panelNoiDung, "HoaDon");
    }

    // Hiển thị màn hình báo cáo doanh thu
    private void hienThiBaoCaoDoanhThu() {
        cardLayout.show(panelNoiDung, "DoanhThu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeThongQuanLyQuanCaPhe::new);
    }
}