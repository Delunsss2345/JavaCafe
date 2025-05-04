//Người làm: Hà Hoàng Anh 
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietNhapHangDAO;
import dao.NhaCungCapDAO;
import dao.NhanVienDAO;
import dao.NhapHangDAO;
import dao.SanPhamDAO;
import entities.ChiTietNhapHang;
import entities.NhaCungCap;
import entities.NhanVien;
import entities.NhapHang;
import entities.SanPham;

public class frmNhapHang extends JPanel implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private NhapHangDAO nhapHangDAO;
    private ChiTietNhapHangDAO daoChiTiet;
    private List<NhapHang> dsNH;
    private SanPhamDAO sanPhamDAO;
    private JButton btnThem,btnTimKiem,btnXemChiTiet;
    private JTextField txtTimKiem;
	private JButton btnLuu;
	private JButton btnHuy;
	private JDialog dialog;
	private JComboBox<Integer> cbNhanVien;
	private JComboBox<Integer> cbNCC;
	private JTextField txtNgayNhap;
	private JTextField txtTongTien;
	private JTextField txtTrangThai;
	private JTextField txtGhiChu;
	private JButton btnCapNhatNhapHang;
	private JButton btnTaiLai;
    
    public frmNhapHang() throws SQLException {
        setup();
        setLayout(new BorderLayout());

        // Panel tiêu đề
        JPanel panelTitle = new JPanel();
        JLabel titleLabel = new JLabel("Quản Lý Nhập Hàng", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitle.add(titleLabel);
        setBackground(new Color(255, 255, 245));
        add(panelTitle, BorderLayout.NORTH);

        // Bảng nhập hàng
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel tìm kiếm
        JPanel panelTimKiem = new JPanel();
        panelTimKiem.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelTimKiem.add(new JLabel("Tìm kiếm theo ID:"));
        txtTimKiem = new JTextField(15);
        panelTimKiem.add(txtTimKiem);
        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem, new Color(52, 152, 219));
        btnTimKiem.addActionListener(e -> timDonNhapHang());
        panelTimKiem.add(btnTimKiem);
         
        
        // Panel nút chức năng
        JPanel panelButtons = new JPanel();
        
        panelButtons.add(panelTimKiem, BorderLayout.SOUTH);
        panelButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnThem = new JButton("Thêm đơn nhập hàng");
        btnXemChiTiet = new JButton("Xem chi tiết đơn nhập hàng");
        btnCapNhatNhapHang = new JButton(" Cập nhật chi tiết nhập hàng ");
        btnTaiLai = new JButton(" Tải lại ");
        
        // Thiết lập style cho các nút
        styleButton(btnThem, new Color(255,240,255));
        styleButton(btnXemChiTiet, new Color(76, 175, 80));
        styleButton(btnCapNhatNhapHang, new Color(255,255,255));
        styleButton(btnTaiLai, new Color(255, 255, 0));
        // Thêm sự kiện
        panelButtons.add(btnThem);
        panelButtons.add(btnXemChiTiet);
        panelButtons.add(btnCapNhatNhapHang);
        panelButtons.add(btnTaiLai);
        add(panelButtons, BorderLayout.SOUTH);
        
        // Event
        btnThem.addActionListener(this);
        btnXemChiTiet.addActionListener(this);
        btnTimKiem.addActionListener(this);
        btnCapNhatNhapHang.addActionListener(this);
        btnTaiLai.addActionListener(this);
    }
    
    

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj.equals(btnThem)) {
			themDonNhapHang();
		} else if (obj.equals(btnXemChiTiet)) {
			xemChiTiet();
		} else if (obj.equals(btnTimKiem)) {
			timDonNhapHang();
		} else if (obj.equals(btnCapNhatNhapHang)) {
			themChiTietNhapHangChoDonNhapHang();
		} else if (obj.equals(btnTaiLai)) {
			lamMoiBangNhapHang();
		}
		
	}
	
	private void setup() throws SQLException {
    	nhapHangDAO = new NhapHangDAO();
    	daoChiTiet = new ChiTietNhapHangDAO();
    	sanPhamDAO = new SanPhamDAO();
        dsNH = nhapHangDAO.getDanhSachNhapHang();

        String[] columnNames = {"Mã nhập hàng", "Mã nhân viên", "Mã nhà cung cấp", "Ngày nhập", "Tổng tiền", "Trạng thái", "Ghi Chú"};
        model = new DefaultTableModel(columnNames,0);
        table = new JTable(model);
        for (NhapHang nh : dsNH) {
        	int maNH = nh.getMaNhapHang();
        	int manv = nh.getNhanVien().getMaNV();
        	int mancc = nh.getNhaCungCap().getMaNCC();
        	Date ngayNhap = nh.getNgayNhap();
        	double tongTien = nh.getTongTien();
        	String trangThai = nh.getTrangThai();
        	String ghiChu = nh.getGhiChu(); 
        	model.addRow(new Object[] {maNH,manv,mancc,ngayNhap,tongTien,trangThai,ghiChu});
        }
		
        
	}

    private void timDonNhapHang() {
        String text = txtTimKiem.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhập hàng để tìm kiếm.");
            return;
        }

        try {
            int maTim = Integer.parseInt(text);
            model.setRowCount(0); 
            for (NhapHang nh : dsNH) {
                if (nh.getMaNhapHang() == maTim) {
                    model.addRow(new Object[]{
                        nh.getMaNhapHang(),
                        nh.getNhanVien().getMaNV(),
                        nh.getNhaCungCap().getMaNCC(),
                        nh.getNgayNhap(),
                        nh.getTongTien(),
                        nh.getTrangThai(),
                        nh.getGhiChu()
                    });
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy đơn nhập hàng với mã: " + maTim);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã nhập hàng không hợp lệ. Vui lòng nhập lại.");
        }
    }


    private void xemChiTiet() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn nhập hàng để xem chi tiết.");
            return;
        }

        int maNhapHang = (int) model.getValueAt(selectedRow, 0);

        try {
            ChiTietNhapHangDAO chiTietDAO = new ChiTietNhapHangDAO();
            List<ChiTietNhapHang> chiTietList = chiTietDAO.getChiTietNhapHangTheoMaNhapHang(maNhapHang);
            
            // Tạo dialog để hiển thị chi tiết
            JDialog dialog = new JDialog();
            dialog.setTitle("Chi tiết đơn nhập hàng: " + maNhapHang);
            dialog.setSize(800, 400);
            dialog.setLocationRelativeTo(null);
            
            String[] column = {"Mã chi tiết nhập hàng","Mã nhập hàng", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"};
            DefaultTableModel detailModel = new DefaultTableModel(column, 0);
            JTable detailTable = new JTable(detailModel);

            for (ChiTietNhapHang ct : chiTietList) {
                detailModel.addRow(new Object[]{
						ct.getMaCTNH(), ct.getNhapHang().getMaNhapHang(), ct.getSanPham().getMaSanPham(),
						ct.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(),
						ct.getSoLuong() * ct.getDonGia()
                });
            }

            dialog.add(new JScrollPane(detailTable));
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void themDonNhapHang() {
        dialog = new JDialog();
        dialog.setTitle("Thêm đơn nhập hàng");
        dialog.setModal(true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        
        
        try {
            List<NhanVien> dsNV = new NhanVienDAO().getAllNhanVien();
            List<NhaCungCap> dsNCC = new NhaCungCapDAO().getAllNhaCungCap();

            cbNhanVien = new JComboBox<Integer>();
            for (NhanVien nv : dsNV) cbNhanVien.addItem(nv.getMaNV());

            cbNCC = new JComboBox<Integer>();
            for (NhaCungCap ncc : dsNCC) cbNCC.addItem(ncc.getMaNCC());
            
            JPanel pnNhapHang = new JPanel();
            pnNhapHang.setLayout(new BoxLayout(pnNhapHang, BoxLayout.Y_AXIS));
            
            JPanel pnMaNV = new JPanel();
            pnMaNV.setLayout(new BoxLayout(pnMaNV, BoxLayout.X_AXIS));
            JLabel lbMaNV = new JLabel(" Mã nhân viên: ");
            pnMaNV.add(lbMaNV);
            pnMaNV.add(cbNhanVien);
            
            JPanel pnNCC = new JPanel();
            pnNCC.setLayout(new BoxLayout(pnNCC, BoxLayout.X_AXIS));
            JLabel lbMaNCC = new JLabel(" Mã nhà cung cấp: ");
            pnNCC.add(lbMaNCC);
            pnNCC.add(cbNCC);
            
            JPanel pnNgayNhap = new JPanel();
            pnNgayNhap.setLayout(new BoxLayout(pnNgayNhap, BoxLayout.X_AXIS));
            JLabel lbNgayNhap = new JLabel(" Ngày nhập (yyyy-MM-dd): ");
			pnNgayNhap.add(lbNgayNhap );
            txtNgayNhap = new JTextField(LocalDate.now().toString());
            pnNgayNhap.add(txtNgayNhap);
            
            JPanel pnTongTien = new JPanel();
            pnTongTien.setLayout(new BoxLayout(pnTongTien, BoxLayout.X_AXIS));
            JLabel lbTongTien = new JLabel(" Tổng tiền: ");
            pnTongTien.add(lbTongTien);
            txtTongTien = new JTextField("0.0");
            txtTongTien.setEditable(false);
            pnTongTien.add(txtTongTien);
            
            JPanel pnTrangThai = new JPanel();
            pnTrangThai.setLayout(new BoxLayout(pnTrangThai, BoxLayout.X_AXIS));
            JLabel lbTrangThai = new JLabel(" Trạng thái: ");
            pnTrangThai.add(lbTrangThai);
            txtTrangThai = new JTextField(" Chờ cập nhật ");
            txtTrangThai.setEditable(false);
            pnTrangThai.add(txtTrangThai);
            
            JPanel pnGhiChu = new JPanel();
            pnGhiChu.setLayout(new BoxLayout(pnGhiChu, BoxLayout.X_AXIS));
            JLabel lbGhiChu = new JLabel(" Ghi chú: ");
            pnGhiChu.add(lbGhiChu);
            txtGhiChu = new JTextField("Đơn mới");
            txtGhiChu.setEditable(false);
            pnGhiChu.add(txtGhiChu);
            
            lbMaNV.setPreferredSize(lbNgayNhap.getPreferredSize());
            lbMaNCC.setPreferredSize(lbNgayNhap.getPreferredSize());
            lbTongTien.setPreferredSize(lbNgayNhap.getPreferredSize());
            lbGhiChu.setPreferredSize(lbNgayNhap.getPreferredSize());
            lbTrangThai.setPreferredSize(lbNgayNhap.getPreferredSize());
            
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnMaNV);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnNCC);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnNgayNhap);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnTongTien);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnTrangThai);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            pnNhapHang.add(pnGhiChu);
            pnNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
            
            
            // Nút chức năng
            JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnLuu = new JButton(" Lưu ");
            btnHuy = new JButton(" Huỷ ");
            pnButtons.add(btnLuu);
            pnButtons.add(btnHuy);
            
            // Add vào dialog
            dialog.add(pnNhapHang, BorderLayout.CENTER);
            dialog.add(pnButtons, BorderLayout.SOUTH);
            btnLuu.addActionListener(e -> luuDonNhapHang());
            btnHuy.addActionListener(e -> dialog.dispose());
            dialog.setVisible(true);
            
            
            
            
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Thêm không thành công");
        	e.printStackTrace();
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
    }
	
	private void themChiTietNhapHangChoDonNhapHang() {
		int row = table.getSelectedRow();
		if (row >= 0) {
			String ghiChu = table.getValueAt(row, 6).toString();
			if (ghiChu.equalsIgnoreCase("Đơn mới")) {
				int maNH = (int) table.getValueAt(row, 0);
				JDialog dialog = new JDialog();
				dialog.setTitle("THÊM CHI TIẾT NHẬP HÀNG");
				dialog.setModal(true);
				dialog.setSize(450,450);
				dialog.setLocationRelativeTo(null);
				
				JLabel lbTitle = new JLabel("THÊM CHI TIẾT NHẬP HÀNG", JLabel.CENTER);
				lbTitle.setFont(new Font("Arial", Font.BOLD, 20));
				lbTitle.setForeground(Color.BLUE);
				dialog.add(lbTitle, BorderLayout.NORTH);
				
				JPanel pnChiTietNhapHang = new JPanel();
				pnChiTietNhapHang.setLayout(new BoxLayout(pnChiTietNhapHang, BoxLayout.Y_AXIS));
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(30,30)));
				JPanel pnMaNH = new JPanel();
				pnMaNH.setLayout(new BoxLayout(pnMaNH, BoxLayout.X_AXIS));
				JLabel lbMaNH = new JLabel(" Mã nhập hàng: ");
				pnMaNH.add(lbMaNH);
				JTextField txtMaNH = new JTextField(maNH+"");
				txtMaNH.setEditable(false);
				pnMaNH.add(txtMaNH);
				
				JPanel pnMaSP = new JPanel();
				pnMaSP.setLayout(new BoxLayout(pnMaSP, BoxLayout.X_AXIS));
				JLabel lbMaSP = new JLabel(" Mã sản phẩm: ");
				pnMaSP.add(lbMaSP);
				JComboBox<Integer> cbMaSP = new JComboBox<Integer>();
				cbMaSP.setEditable(true);
				List<SanPham> dsSP = sanPhamDAO.getAllSanPham();
				for (SanPham sp : dsSP) {
					cbMaSP.addItem(sp.getMaSanPham());
				}
				pnMaSP.add(cbMaSP);
				
				JPanel pnTenSanPham = new JPanel();
				pnTenSanPham.setLayout(new BoxLayout(pnTenSanPham, BoxLayout.X_AXIS));
				JLabel lbTenSanPham = new JLabel(" Tên sản phẩm: ");
				pnTenSanPham.add(lbTenSanPham);
				JTextField txtTenSanPham = new JTextField();
				pnTenSanPham.add(txtTenSanPham);

				JPanel pnSoLuong = new JPanel();
				pnSoLuong.setLayout(new BoxLayout(pnSoLuong, BoxLayout.X_AXIS));
				JLabel lbSoLuong = new JLabel(" Số lượng: ");
				pnSoLuong.add(lbSoLuong);
				JTextField txtSoLuong = new JTextField();
				pnSoLuong.add(txtSoLuong);

				JPanel pnDonGia = new JPanel();
				pnDonGia.setLayout(new BoxLayout(pnDonGia, BoxLayout.X_AXIS));
				JLabel lbDonGia = new JLabel(" Đơn giá: ");
				pnDonGia.add(lbDonGia);
				JTextField txtDonGia = new JTextField();
				pnDonGia.add(txtDonGia);

				JPanel pnThanhTien = new JPanel();
				pnThanhTien.setLayout(new BoxLayout(pnThanhTien, BoxLayout.X_AXIS));
				JLabel lbThanhTien = new JLabel(" Thành tiền: ");
				pnThanhTien.add(lbThanhTien);
				JTextField txtThanhTien = new JTextField();
				txtThanhTien.setEditable(false);
				pnThanhTien.add(txtThanhTien);
				
				// Tính thành tiền khi nhập số lượng và đơn giá
				
				lbMaSP.setPreferredSize(lbMaNH.getPreferredSize());
				lbSoLuong.setPreferredSize(lbMaNH.getPreferredSize());
				lbDonGia.setPreferredSize(lbMaNH.getPreferredSize());
				lbThanhTien.setPreferredSize(lbMaNH.getPreferredSize());
				

				JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
				JButton btnLuu2 = new JButton(" Lưu ");
				JButton btnHuy2 = new JButton(" Huỷ ");
				pnButtons.add(btnLuu2);
				pnButtons.add(btnHuy2);
				
				pnChiTietNhapHang.add(pnMaNH);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnMaSP);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnTenSanPham);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnSoLuong);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnDonGia);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnThanhTien);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				pnChiTietNhapHang.add(pnButtons);
				pnChiTietNhapHang.add(Box.createRigidArea(new Dimension(10,10)));
				dialog.add(pnChiTietNhapHang, BorderLayout.CENTER);
				
				
				
				btnLuu2.addActionListener(e -> {
				    try {
				        int maSP = (int) cbMaSP.getSelectedItem();
						if (maSP <= 0) {
							JOptionPane.showMessageDialog(dialog, "Mã sản phẩm không hợp lệ!");
							return;
						}
				        String tenSP = txtTenSanPham.getText();
				        if (tenSP.isEmpty() || !tenSP.matches("[A-Za-zÀ-ỹ\\s]+")) {
				            JOptionPane.showMessageDialog(dialog, "Tên sản phẩm không hợp lệ! Chỉ chứa các ký tự chữ cái.");
				            return;
				        }
				        int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
						if (soLuong <= 0) {
							JOptionPane.showMessageDialog(dialog, "Số lượng phải lớn hơn 0!");
							txtSoLuong.requestFocus();
							return;
						}
				        double donGia = Double.parseDouble(txtDonGia.getText().trim());
						if (donGia <= 0) {
							JOptionPane.showMessageDialog(dialog, "Đơn giá phải lớn hơn 0!");
							txtDonGia.requestFocus();
							return;
						}
				        double thanhTien = soLuong * donGia;
				        txtThanhTien.setText(thanhTien + "");

				        ChiTietNhapHang ct = new ChiTietNhapHang(new NhapHang(maNH), new SanPham(maSP),tenSP, soLuong, donGia);
				        daoChiTiet.themChiTietNhapHang(ct);
				        JOptionPane.showMessageDialog(dialog, "Thêm chi tiết nhập hàng thành công!");
				        
				        // Cập nhật ghi chú cho đơn hàng
				        nhapHangDAO.updateGhiChu(maNH, "Không có");
				        // Cập nhật trạng thái cho đơn hàng
				        nhapHangDAO.updateTrangThai(maNH, "Đã nhập");
				        // Cập nhật lại bảng nhập hàng nếu người dùng vẫn tiếp tục nhập
				        txtTenSanPham.setText("");
				        txtSoLuong.setText("");
				        txtDonGia.setText("");
				        txtThanhTien.setText("");

				    } catch (Exception ex) {
				        JOptionPane.showMessageDialog(dialog, "Thêm không thành công!");
				        ex.printStackTrace();
				    }
				});

				btnHuy2.addActionListener(e -> {
					lamMoiBangNhapHang();
				    dialog.dispose(); 
				});

				dialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Chỉ được cập nhật đơn hàng mới");
			}
			
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn nhập hàng cần thêm chi tiết.");
		}
		
	}

	private void luuDonNhapHang() {
        try {
            int maNV = (int) cbNhanVien.getSelectedItem();
            int maNCC = (int) cbNCC.getSelectedItem();
            String ngayNhap = txtNgayNhap.getText().trim();
            String regex = "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])";
            if (ngayNhap.isEmpty() || !ngayNhap.matches(regex)) {
                JOptionPane.showMessageDialog(this, 
                    "Ngày nhập không hợp lệ, phải theo định dạng yyyy-MM-dd");
                txtNgayNhap.requestFocus();
                return;
            }
            LocalDate ngay = LocalDate.parse(ngayNhap);
            Date ngayNhap1 = java.sql.Date.valueOf(ngay);
            double tongTien = 0.0; 
            String trangThai = txtTrangThai.getText().trim();
            String ghiChu = txtGhiChu.getText().trim();

            NhanVien nv = new NhanVienDAO().getNhanVienByMa(maNV);
            NhaCungCap ncc = new NhaCungCapDAO().getNhaCungCapByMa(maNCC);

            NhapHang nh = new NhapHang(0, nv, ncc, ngayNhap1, tongTien, trangThai, ghiChu);
            boolean success = nhapHangDAO.themDonNhapHang(nh);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm đơn nhập hàng thành công!");
                dialog.dispose();
                lamMoiBangNhapHang();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm đơn nhập hàng thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu đơn nhập hàng!");
            e.printStackTrace();
        }
    }
    
	private void lamMoiBangNhapHang() {
        try {
            dsNH = nhapHangDAO.getDanhSachNhapHang();
            model.setRowCount(0);
            for (NhapHang nh : dsNH) {
                model.addRow(new Object[]{
                    nh.getMaNhapHang(),
                    nh.getNhanVien().getMaNV(),
                    nh.getNhaCungCap().getMaNCC(),
                    nh.getNgayNhap(),
                    nh.getTongTien(),
                    nh.getTrangThai(),
                    nh.getGhiChu()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi làm mới bảng");
            e.printStackTrace();
        }
    }

}