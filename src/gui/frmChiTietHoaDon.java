//Nguyễn Tuấn Phát
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import ConnectDB.DatabaseConnection;
import dao.ChiTietHoaDonCaPheDAO;
import dao.HoaDonCaPheDAO;
import entities.ChiTietHoaDonCaPhe;
import entities.HoaDon;

public class frmChiTietHoaDon extends JFrame {
    private JTextArea txtChiTiet;

    public frmChiTietHoaDon(String maNV, String	 tenNV, String maKH, String tenKH, double tongTien,
                            LocalDateTime gioVao, LocalDateTime gioRa,
                            List<ChiTietHoaDonCaPhe> danhSachMon,
                            int maDH, double tienKhachTra) {

        setTitle("Chi Tiết Hóa Đơn");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String gioVaoStr = dtf.format(gioVao);
        String gioRaStr = dtf.format(gioRa);

        txtChiTiet = new JTextArea();
        txtChiTiet.setEditable(false);
        txtChiTiet.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder sb = new StringBuilder();
        sb.append("----- Chi Tiết Hóa Đơn -----\n")
          .append("Ngày bán: ").append(gioVao.toLocalDate()).append("\n")
          .append("Giờ vào: ").append(gioVaoStr).append("\n")
          .append("Giờ ra: ").append(gioRaStr).append("\n")
          .append("-----------------------------\n")
          .append("Mã NV: ").append(maNV).append("\n")
          .append("Tên NV: ").append(tenNV).append("\n")
          .append("Mã KH: ").append(maKH).append("\n")
          .append("Tên KH: ").append(tenKH).append("\n")
          .append("-----------------------------\n")
          .append("Danh sách món:\n");

        for (ChiTietHoaDonCaPhe ct : danhSachMon) {
            sb.append(String.format("- %s x%d : %,.0f VNĐ\n",
                    ct.getTenSanPham(),
                    ct.getSoLuong(),
                    ct.getThanhTien()));
        }

        sb.append("-----------------------------\n")
          .append(String.format("Tổng tiền: %,.0f VNĐ\n", tongTien));

        txtChiTiet.setText(sb.toString());
        add(new JScrollPane(txtChiTiet), BorderLayout.CENTER);

        String thongTinChuyenKhoan = "Ngân hàng: MB Bank\nSố tài khoản: 1234567890\nTên: NGUYEN VAN A\nNội dung: " + maKH;
        BufferedImage qrImage = QRGenerator.generateQR(thongTinChuyenKhoan, 200, 200);
        JLabel lblQR = new JLabel(new ImageIcon(qrImage));
        lblQR.setBorder(BorderFactory.createTitledBorder("QR Chuyển khoản (MB Bank - 1234567890)"));
        lblQR.setHorizontalAlignment(JLabel.CENTER);
        add(lblQR, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel();
        JButton btnIn = new JButton("In hóa đơn");
        topPanel.add(btnIn);
        add(topPanel, BorderLayout.NORTH);

        btnIn.addActionListener((ActionEvent e) -> {
            try {
                boolean done = txtChiTiet.print();
                if (done) {
                    // Sau khi in, lưu hóa đơn vào CSDL
                    Connection conn = DatabaseConnection.getInstance().getConnection();
                    HoaDonCaPheDAO hoaDonDAO = new HoaDonCaPheDAO(conn);
                    ChiTietHoaDonCaPheDAO chiTietDAO = new ChiTietHoaDonCaPheDAO(conn);

                    int maNVInt = Integer.parseInt(maNV);
                    HoaDon hoaDon = new HoaDon(
                        0, maDH, gioRa, tongTien, tienKhachTra,
                        tienKhachTra - tongTien, maNVInt
                    );

                    int maHD = hoaDonDAO.insertHoaDon(hoaDon);
                    if (maHD > 0) {
                        for (ChiTietHoaDonCaPhe ct : danhSachMon) {
                            ct.setMaHoaDon(String.valueOf(maHD));
                            chiTietDAO.insertChiTiet(ct);
                        }
                        JOptionPane.showMessageDialog(this, "In hóa đơn & lưu vào hệ thống thành công!");
                        this.dispose(); // Đóng form
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "In hóa đơn bị hủy.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi in hoặc lưu hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    static class QRGenerator {
        public static BufferedImage generateQR(String data, int width, int height) {
            try {
                BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
                return MatrixToImageWriter.toBufferedImage(matrix);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
