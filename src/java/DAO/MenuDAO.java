/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Menu;
import DTO.MenuDetail;
import MyLib.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class MenuDAO {

    public ArrayList<Menu> getMenuFood(String userID) {
        ArrayList<Menu> list = new ArrayList<>();
        Connection cn = null;
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                String sql = "SELECT m.MenuID,m.FoodID, m.MenuDate, m.UserID,m.MenuName, m.MenuStatus\n"
                        + "FROM dbo.Menu m\n"
                        + "INNER JOIN dbo.Food f ON m.FoodID = f.FoodID\n"
                        + "WHERE m.UserID=?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, userID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    int menuID = rs.getInt("MenuID");
                    int foodID = rs.getInt("FoodID");
                    String menuDate = rs.getNString("MenuDate");
                    int uID = rs.getInt("UserID");
                    String menuName = rs.getNString("MenuName");
                    boolean menuStatus = rs.getBoolean("MenuStatus");
                    Menu menu = new Menu(menuID, foodID, menuDate, uID, menuName, menuStatus);
                    list.add(menu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public ArrayList<Menu> insertNewMenu(String userID) {
        ArrayList<Menu> list = new ArrayList<>();
        Connection cn = null;
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                String sql = "SELECT m.MenuID,m.FoodID, m.MenuDate, m.UserID,m.MenuName, m.MenuStatus\n"
                        + "FROM dbo.Menu m\n"
                        + "INNER JOIN dbo.Food f ON m.FoodID = f.FoodID\n"
                        + "WHERE m.UserID=?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, userID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String menuID = rs.getString("MenuID");
                    String foodID = rs.getString("FoodID");
                    String menuDate = rs.getString("MenuDate");
                    String uID = rs.getString("UserID");
                    String menuName = rs.getNString("MenuName");
                    boolean menuStatus = rs.getBoolean("MenuStatus");
                    Menu menu = new Menu(menuID, foodID, menuDate, uID, menuName, menuStatus);
                    list.add(menu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public String checkWeekMenu(String userID, String menuWeek) {
        Connection cn = null;
        String check = null;
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                String sql = "SELECT UserID, MenuDate, MenuStatus FROM dbo.Menu\n"
                        + "WHERE UserID=? AND MenuDate LIKE N%?% AND MenuStatus ='1'";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, userID);
                pst.setString(2, menuWeek);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    if (rs.next()) {
                        check = null; // Menu exists
                    } else {
                        check = "1"; // Menu does not exist
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    public String insertWeekMenu(String userID, String menuWeek, String newMenuName, String menuTag) {
        Connection cn = null;
        String result = null;
        try {
            cn = DBUtil.makeConnection();
            if (cn != null) {
                // Disable auto-commit mode
                cn.setAutoCommit(false);

                String sql = "INSERT INTO Menu (UserID, MenuDate, MenuName, MenuTag, MenuStatus) VALUES (?, ?, ?, ?, '1')";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, userID);
                pst.setString(2, menuWeek);
                pst.setString(3, newMenuName);
                pst.setString(4, menuTag);

                // Execute update and get affected rows
                int affectedRows = pst.executeUpdate();

                if (affectedRows > 0) {
                    // Commit the transaction if insert is successful
                    cn.commit();
                    result = "Insert successful";
                } else {
                    // Rollback the transaction if insert failed
                    cn.rollback();
                    result = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            result = "An error occurred: " + e.getMessage();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
