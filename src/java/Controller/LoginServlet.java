/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import DAO.UserDAO;
import DTO.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
        
        String email = request.getParameter("txtemail");
        String password = request.getParameter("txtpassword");
        String checkBox = request.getParameter("checkbox");
        String passwordDecoded = (String) request.getAttribute("PasswordDecoded");
        if (email != null && password != null) {
            UserDAO userDAO = new UserDAO();
            String Email = userDAO.getUserLogin(email, passwordDecoded);
            
            if (Email != null) {
                HttpSession session = request.getSession();
                session.setAttribute("LoginedEmail", Email);
                int userID = userDAO.getUserID(email);               
                session.setAttribute("LoginedUID", userID);
                String fullName = userDAO.getFullName(Email);
                session.setAttribute("LoginedUser", fullName);  
                int userRole = userDAO.isStaff(userID);
                session.setAttribute("Role", userRole);
                request.removeAttribute("PasswordDecoded");
                request.getRequestDispatcher("Index.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                String msg = "Sai Email hoặc Mật Khẩu hoặc Tài Khoản đã bị khóa";
                request.setAttribute("Error", msg);
                request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
            }
        }
    }
}
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
