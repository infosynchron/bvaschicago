package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.UserBean;
import com.bvas.formBeans.LoginForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;

public class LoginAction extends Action {

  private static final Logger logger = Logger.getLogger(LoginAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    // servlet.log("Error message text", exception);
    String username = ((LoginForm) form).getUsername();
    String password = ((LoginForm) form).getPassword();
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(true);

    if (username == null || username.trim().equals("")) {
      errorBean.setError("Please enter your User ID");
      forwardPage = "Login";
    }

    if (password == null || password.trim().equals("")) {
      errorBean.setError("Please enter your Password");
      forwardPage = "Login";
    }

    if (errorBean.getError() == null) {
      try {

        Connection con = DBInterfaceLocal.getSQLConnection();
        Statement stmt = con.createStatement();
        String loginSQL =
            "select * from Users where username = '" + username.trim() + "' and password = '"
                + password.trim() + "'";
        ResultSet rs = stmt.executeQuery(loginSQL);
        if (rs.next()) {
          UserBean user = new UserBean();
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setRole(rs.getString("role"));
          session.setAttribute("User", user);
          forwardPage = "MainMenu";
        } else {
          logger.error("Error: Please check your User ID and Password");
          errorBean.setError("Error: Please check your User ID and Password");
          forwardPage = "Login";
        }
        rs.close();
        stmt.close();
        con.close();

      } catch (SQLException e) {
        errorBean.setError("Error in processing: check after some time" + e.getMessage());
        logger.error("Unable to check Login*********");
        forwardPage = "Login";
      }
    }

    // logger.error("forwardPage======" + forwardPage);
    // logger.error("Username======" + username);

    if (errorBean.getError() != null) {
      session.setAttribute("LoginError", errorBean);
    } else {
      session.removeAttribute("LoginError");
    }
    return (mapping.findForward(forwardPage));

  }

}
