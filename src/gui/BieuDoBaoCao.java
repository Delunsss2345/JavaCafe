
// Người làm: Hà Hoàng Anh

package gui;

import dao.BaoCaoDoanhThuDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BieuDoBaoCao extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private BaoCaoDoanhThuDAO baoCaoDoanhThuDAO;
    private JButton btnBieuDoPie;
    private JButton btnBieuDoCot;
    private JPanel buttonPanel;

    public BieuDoBaoCao() throws SQLException {
        setTitle("Biểu Đồ Báo Cáo Doanh Thu");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        baoCaoDoanhThuDAO = new BaoCaoDoanhThuDAO();

        // Tạo các nút
        btnBieuDoPie = new JButton("Tỉ lệ sản phẩm bán ra");
        btnBieuDoCot = new JButton("Doanh thu theo tháng");

        btnBieuDoPie.addActionListener(this);
        btnBieuDoCot.addActionListener(this);

        // Panel chứa nút
        buttonPanel = new JPanel();
        buttonPanel.add(btnBieuDoPie);
        buttonPanel.add(btnBieuDoCot);

        // Layout chính
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        getContentPane().removeAll(); // Xoá biểu đồ cũ
        getContentPane().add(buttonPanel, BorderLayout.NORTH); // Thêm lại panel nút

        if (source == btnBieuDoPie) {
            taoBieuDoTiLeSanPhamBanRa();
        } else if (source == btnBieuDoCot) {
            String input = JOptionPane.showInputDialog(this,
                    "Nhập năm để xem biểu đồ doanh thu:", "Chọn năm", JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int nam = Integer.parseInt(input.trim());
                    hienThiBieuDoDoanhThuTheoThang(nam);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Năm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        revalidate();
        repaint();
    }

    public void taoBieuDoTiLeSanPhamBanRa() {
        try {
            List<Entry<String, Integer>> danhSach = baoCaoDoanhThuDAO.getSanPhamBanChayNhat(null, null);

            if (danhSach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để vẽ biểu đồ.");
                return;
            }

            DefaultPieDataset dataset = new DefaultPieDataset();
            for (Entry<String, Integer> e : danhSach) {
                dataset.setValue(e.getKey(), e.getValue());
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Tỉ lệ sản phẩm bán ra",
                    dataset,
                    true,
                    true,
                    false
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            getContentPane().add(chartPanel, BorderLayout.CENTER);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi khởi tạo biểu đồ:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void hienThiBieuDoDoanhThuTheoThang(int nam) {
        try {
            Map<Integer, Double> doanhThuTheoThang = baoCaoDoanhThuDAO.getDoanhThuTheoThangTrongNam(nam);

            if (doanhThuTheoThang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu doanh thu cho năm " + nam);
                return;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<Integer, Double> entry : doanhThuTheoThang.entrySet()) {
                dataset.addValue(entry.getValue(), "Doanh thu", "Tháng " + entry.getKey());
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Doanh thu từng tháng trong năm " + nam,
                    "Tháng",
                    "Doanh thu (VND)",
                    dataset
            );

            barChart.setBackgroundPaint(Color.white);

            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(800, 500));
            getContentPane().add(chartPanel, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo biểu đồ: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}