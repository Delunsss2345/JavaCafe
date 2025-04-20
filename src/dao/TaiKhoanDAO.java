package dao;
//Người làm phạm thanh huy
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ConnectDB.DatabaseConnection;
import entities.PhanQuyen;
import entities.TaiKhoan;

public class TaiKhoanDAO {
    private PhanQuyenDAO phanQuyen = new PhanQuyenDAO();

    public ArrayList<TaiKhoan> dsTaiKhoan() {
        String sql = "SELECT TenTaiKhoan, MatKhau, MaQuyen FROM TaiKhoan";
        ArrayList<TaiKhoan> dsTk = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            System.out.println("Đang truy vấn danh sách tài khoản...");
            int count = 0;
            
            while(rs.next()) {
                count++;
                String tenTK = rs.getString("TenTaiKhoan");
                String matKhau = rs.getString("MatKhau");
                int maQuyen = rs.getInt("MaQuyen");
                
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(tenTK);
                tk.setMatKhau(matKhau);
                
                PhanQuyen pq = phanQuyen.getPhanQuyen(maQuyen);
                tk.setQuyen(pq);
                
                dsTk.add(tk);
                System.out.println("Đã lấy tài khoản: " + tenTK + " với quyền: " + pq.getTenQuyen());
            }
            
            System.out.println("Tổng số tài khoản lấy được: " + count);
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách tài khoản: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dsTk;
    }
    
   
}