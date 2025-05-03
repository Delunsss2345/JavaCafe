package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import dao.NhanVienDAO;
import entities.NhanVien;

public class ThongTinCaNhan extends JPanel {
    private JTextField txtMaNV, txtHo, txtTen, txtNgaySinh, txtCMND, txtSDT, txtLoaiNV, txtNgayVaoLam, txtLuong;
    private JComboBox<String> cbGioiTinh;
    private JButton btnLuu;
    private NhanVien nhanVien;

    public ThongTinCaNhan(NhanVien nv) {
        this.nhanVien = nv;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(39, 174, 96));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        txtMaNV = createTextField(nv.getMaNV() + "", font);
        txtMaNV.setEditable(false);

        txtHo = createTextField(nv.getHo(), font);
        txtTen = createTextField(nv.getTen(), font);
        txtNgaySinh = createTextField(nv.getNgaySinh().toString(), font);
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setFont(font);
        cbGioiTinh.setSelectedItem(nv.getGioiTinh());

        txtCMND = createTextField(nv.getCmnd(), font);
        txtSDT = createTextField(nv.getSoDienThoai(), font);
        txtLoaiNV = createTextField(nv.getLoaiNV(), font);
        txtNgayVaoLam = createTextField(nv.getNgayVaoLam().toString(), font);
        txtLuong = createTextField(nv.getLuongCoBan().toString(), font);

        int y = 0;
        addField(formPanel, gbc, "Mã nhân viên:", txtMaNV, y++);
        addField(formPanel, gbc, "Họ:", txtHo, y++);
        addField(formPanel, gbc, "Tên:", txtTen, y++);
        addField(formPanel, gbc, "Ngày sinh (yyyy-MM-dd):", txtNgaySinh, y++);
        addField(formPanel, gbc, "Giới tính:", cbGioiTinh, y++);
        addField(formPanel, gbc, "CMND:", txtCMND, y++);
        addField(formPanel, gbc, "Số điện thoại:", txtSDT, y++);
        addField(formPanel, gbc, "Loại nhân viên:", txtLoaiNV, y++);
        addField(formPanel, gbc, "Ngày vào làm (yyyy-MM-dd):", txtNgayVaoLam, y++);
        addField(formPanel, gbc, "Lương cơ bản:", txtLuong, y++);

        btnLuu = new JButton("💾 Lưu thông tin");
        btnLuu.setPreferredSize(new Dimension(180, 45));
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> luuThongTin());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnLuu);

        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JTextField createTextField(String text, Font font) {
        JTextField tf = new JTextField(text);
        tf.setFont(font);
        return tf;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void luuThongTin() {
        try {
            if (txtHo.getText().isEmpty() || txtTen.getText().isEmpty() || txtNgaySinh.getText().isEmpty()
                || txtCMND.getText().isEmpty() || txtSDT.getText().isEmpty() || txtLoaiNV.getText().isEmpty()
                || txtNgayVaoLam.getText().isEmpty() || txtLuong.getText().isEmpty()) {
                showMessage("Vui lòng không để trống trường nào!");
                return;
            }

            if (!txtCMND.getText().matches("\\d{9}")) {
                showMessage("CMND phải đúng 9 số.");
                return;
            }

            if (!txtSDT.getText().matches("0\\d{9}")) {
                showMessage("Số điện thoại phải bắt đầu bằng 0 và đủ 10 số.");
                return;
            }

            BigDecimal luong = new BigDecimal(txtLuong.getText());
            if (luong.compareTo(BigDecimal.ZERO) <= 0) {
                showMessage("Lương cơ bản phải > 0.");
                return;
            }

            LocalDate ngaySinh = LocalDate.parse(txtNgaySinh.getText());
            if (!ngaySinh.isBefore(LocalDate.now())) {
                showMessage("Ngày sinh phải trước hôm nay.");
                return;
            }

            nhanVien.setHo(txtHo.getText());
            nhanVien.setTen(txtTen.getText());
            nhanVien.setNgaySinh(ngaySinh);
            nhanVien.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
            nhanVien.setCmnd(txtCMND.getText());
            nhanVien.setSoDienThoai(txtSDT.getText());
            nhanVien.setLoaiNV(txtLoaiNV.getText());
            nhanVien.setNgayVaoLam(LocalDate.parse(txtNgayVaoLam.getText()));
            nhanVien.setLuongCoBan(luong);

            boolean success = new NhanVienDAO().updateNhanVien(nhanVien);
            showMessage(success ? "Cập nhật thành công!" : "Cập nhật thất bại.");
        } catch (DateTimeParseException ex) {
            showMessage("Ngày không hợp lệ. Định dạng yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            showMessage("Lương không hợp lệ.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Lỗi: " + ex.getMessage());
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
