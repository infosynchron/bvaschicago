package com.bvas.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.FaxBean;
import com.bvas.formBeans.FaxCoverForm;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.PrintUtils;

public class FaxCoverAction extends Action {
  private static final Logger logger = Logger.getLogger(FaxCoverAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    FaxCoverForm faxForm = (FaxCoverForm) form;
    String buttonClicked = faxForm.getButtonClicked();
    faxForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in MainMenuAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    int faxNo = 0;
    try {
      faxNo = Integer.parseInt(faxForm.getFaxNo());
    } catch (Exception e) {
      logger.error(e);
      faxNo = 0;
      errorBean.setError("In FaxCoverAction - Fax No Not Available");
    }

    String prty = faxForm.getFaxPriority();
    session.setAttribute("faxSelected", prty);

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetFax")) {

      if (faxNo != 0) {
        FaxBean fax = FaxBean.getFax(faxNo);
        if (fax != null) {
          fillForm(faxForm, fax, request);
          PrintUtils.createFax(fax);
          session.setAttribute("faxNum", faxNo + "");
        } else {
          errorBean.setError("This fax doesn't exist - Please check the number");
        }
      } else {
        errorBean.setError("Fax No. not valid - Enter a correct number");
      }

      forwardPage = "FaxCover";
    } else if (buttonClicked.equals("CreateFax")) {
      logger.error("Going to create the Fax");
      if (faxNo != 0) {
        FaxBean fax = FaxBean.getFax(faxNo);
        if (fax != null) {
          fax.removeFax();
          fax = null;
        }
        fax = createBean(faxForm);
        fax.writeFax();
        PrintUtils.createFax(fax);

        session.setAttribute("faxNum", faxNo + "");
      } else {
        errorBean.setError("Fax No. not valid - Enter a correct number");
      }

      forwardPage = "FaxCover";
    } else if (buttonClicked.equals("PrintFax")) {
      if (faxNo != 0) {
        FaxBean fax = FaxBean.getFax(faxNo);
        if (fax == null) {
          fax = createBean(faxForm);
          fax.writeFax();
        }

        PrintUtils.createFax(fax);

        session.setAttribute("CheckFaxPrint", "PrintFax");
        session.setAttribute("FaxNoToPrint", faxNo + "");
      }

      forwardPage = "FaxCover";
    } else if (buttonClicked.equals("ReturnToMain")) {
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("FaxCoverError", errorBean);
    } else {
      session.removeAttribute("FaxCoverError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillForm(FaxCoverForm faxForm, FaxBean fax, HttpServletRequest request) {
    faxForm.setFaxNo(fax.getFaxNumber() + "");
    faxForm.setFaxDate(fax.getFaxDate());
    faxForm.setToWhom(fax.getToWhom());
    faxForm.setFromWhom(fax.getFromWhom());
    faxForm.setFaxTo(fax.getFaxTo());
    faxForm.setPhoneTo(fax.getPhoneTo());
    faxForm.setPages(fax.getPages() + "");
    faxForm.setFaxPriority(fax.getPriority() + "");
    request.getSession().setAttribute("faxSelected", faxForm.getFaxPriority());
    faxForm.setAttention(fax.getAttention());
    faxForm.setComments(fax.getComments());
    faxForm.setCommentsSize(fax.getCommentsSize() + "");
  }

  public FaxBean createBean(FaxCoverForm faxForm) {
    FaxBean fax = new FaxBean();
    fax.setFaxNumber(Integer.parseInt(faxForm.getFaxNo()));
    fax.setFaxDate(faxForm.getFaxDate());
    fax.setToWhom(faxForm.getToWhom());
    fax.setFromWhom(faxForm.getFromWhom());
    fax.setFaxTo(faxForm.getFaxTo());
    fax.setPhoneTo(faxForm.getPhoneTo());
    try {
      fax.setPriority(Integer.parseInt(faxForm.getFaxPriority()));
    } catch (Exception e) {
      logger.error(e);
      fax.setPriority(2);
    }
    fax.setAttention(faxForm.getAttention());
    fax.setComments(faxForm.getComments());
    try {
      fax.setCommentsSize(Integer.parseInt(faxForm.getCommentsSize()));
    } catch (Exception e) {
      logger.error(e);
      fax.setCommentsSize(12);
    }
    try {
      fax.setPages(Integer.parseInt(faxForm.getPages()));
    } catch (Exception e) {
      fax.setPages(1);
    }

    return fax;
  }

}
