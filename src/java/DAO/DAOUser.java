/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAL.DBContext;
import Entity.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

/**
 *
 * @author ADMIN
 */
public class DAOUser extends DBContext {
    private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // Sử dụng SHA-256
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException ex) {
        return null;
    }
}
      public String login(String email, String password) {
    String sql = "SELECT role FROM Users WHERE email=? AND password=?";
    String hashedPassword = hashPassword(password); // Hash the password

    try {
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, email);
        pre.setString(2, hashedPassword); // Use the hashed password
        ResultSet rs = pre.executeQuery();
        
        if (rs.next()) {
            return rs.getString("role"); // Return the user's role
        }
    } catch (SQLException ex) {
        Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null; // Return null if login fails
}
//    public boolean login(String email, String password) {
//    boolean flag = false;
//    String hashedPassword = hashPassword(password); // Băm mật khẩu trước
//    String sql = "SELECT * FROM Users WHERE email=? AND password=?";
//    try {
//        PreparedStatement pre = conn.prepareStatement(sql);
//        pre.setString(1, email);
//        pre.setString(2, hashedPassword); // Sử dụng mật khẩu đã băm
//        ResultSet rs = pre.executeQuery();
//        if (rs.next()) {
//            flag = true;
//        }
//    } catch (SQLException ex) {
//        Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    return flag;
//}   

    public int insertUser(User user) {
        int n = 0;
        String sql = "INSERT INTO Users (userID, username, password, email, role, shopID) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, user.getUserID());
            pre.setString(2, user.getUsername());
            pre.setString(3, user.getPassword());
            pre.setString(4, user.getEmail());
            pre.setString(5, user.getRole());
            pre.setInt(6, user.getShopID());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public int updateUser(User user) {
        int n = 0;
        String sql = "UPDATE Users SET username=?, password=?, email=?, role=?, shopID=? WHERE userID=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, user.getUsername());
            pre.setString(2, user.getPassword());
            pre.setString(3, user.getEmail());
            pre.setString(4, user.getRole());
            pre.setInt(5, user.getShopID());
            pre.setInt(6, user.getUserID());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public int removeUser(int userID) {
        int n = 0;
        String sql = "DELETE FROM Users WHERE userID=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, userID);
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public Vector<User> getUsers(String sql) {
        Vector<User> vector = new Vector<User>();
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String role = rs.getString("role");
                int shopID = rs.getInt("shopID");
                User user = new User(userID, username, password, email, role, shopID);
                vector.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vector;
    }
   public void updatePassword(String email, String password) {
    String sql = "UPDATE [dbo].[users] SET [password] = ? WHERE [email] = ?";

    try (PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setString(1, password);
        pre.setString(2, email);
        pre.executeUpdate(); // Không cần lưu kết quả
    } catch (SQLException ex) {
        Logger.getLogger(DAOUser.class.getName()).log(Level.SEVERE, null, ex);
        // Có thể ném ra ngoại lệ hoặc xử lý lỗi ở đây
    }
}
    public void listAll() {
        String sql = "SELECT * FROM Users";
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String role = rs.getString("role");
                int shopID = rs.getInt("shopID");
                User user = new User(userID, username, password, email, role, shopID);
                System.out.println(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
      
}

    public static void main(String[] args) {
      DAOUser dao = new DAOUser();

    dao.listAll();

}

}
