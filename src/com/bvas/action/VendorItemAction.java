package com.bvas.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bvas.beans.MakeModelBean;
import com.bvas.beans.PartsBean;
import com.bvas.beans.VendorItemBean;
import com.bvas.formBeans.VendorItemForm;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.ErrorBean;
import com.bvas.utils.UserException;

public class VendorItemAction extends Action {
  private static final Logger logger = Logger.getLogger(VendorItemAction.class);

  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {

    VendorItemForm viForm = (VendorItemForm) form;
    String buttonClicked = viForm.getButtonClicked();
    viForm.setButtonClicked("");
    String forwardPage = "";

    ErrorBean errorBean = new ErrorBean();
    HttpSession session = request.getSession(false);

    int supplierId = 0;
    try {
      supplierId = Integer.parseInt((String) session.getAttribute("SupplierIdForItem"));
      viForm.setSupplierId(supplierId + "");
    } catch (Exception e) {
      logger.error(e);
      errorBean.setError("Vendor Id Not available");
    }
    String partNo = viForm.getPartNo();
    String vendPartNo = viForm.getVendorPartNo();
    Object oo = session.getAttribute("VIBean");
    VendorItemBean sessionVIBean = null;
    if (oo != null) {
      sessionVIBean = (VendorItemBean) oo;
    }

    if (session == null || session.getAttribute("User") == null) {
      logger.error("No session or no User in VendorItemAction");
      buttonClicked = null;
      forwardPage = "Login";
    }

    if (buttonClicked == null || buttonClicked.equals("")) {
      forwardPage = "Login";
    } else if (buttonClicked.equals("GetItem")) {
      session.removeAttribute("VIBean");

      if (supplierId == 0) {
        errorBean.setError("<BR>Please check the errors...");
      } else {
        if ((partNo == null || partNo.trim().equals(""))
            && (vendPartNo == null || vendPartNo.trim().equals(""))) {
          viForm.resetForm();
          errorBean.setError("Please enter either Part No or Vendor Item No for getting the Item");
        } else {
          viForm.resetForm();
          if (partNo == null)
            partNo = "";
          if (vendPartNo == null)
            vendPartNo = "";
          try {
            VendorItemBean viBean = VendorItemBean.getThePart(supplierId, partNo, vendPartNo, null);
            if (viBean == null) {
              viForm.resetForm();
              throw new UserException("");
            } else {
              if (viBean.getPartNo() == null || viBean.getPartNo().trim().equals("")) {
                Vector<String[]> v = checkSuggestedParts(viBean);
                if (v != null && v.size() > 0) {
                  session.setAttribute("VendorSuggestedParts", v);
                } else {
                  session.removeAttribute("VendorSuggestedParts");
                }
              }
              fillForm(viForm, viBean);
              session.setAttribute("VIBean", viBean);
            }
          } catch (UserException e) {
            logger.error(e);
            errorBean.setError("This Item Not Available - Please check your values");
          }
        }
      }
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("AddNewItem")) {
      try {
        VendorItemBean viBean = new VendorItemBean();
        fillBean(viForm, viBean);
        if (sessionVIBean == null
            || !sessionVIBean.getVendorPartNo().trim()
                .equalsIgnoreCase(viBean.getVendorPartNo().trim())) {
          viBean.addNewItem();
          if (viBean.getPartNo() == null || viBean.getPartNo().trim().equals("")) {
            Vector<String[]> v = checkSuggestedParts(viBean);
            if (v != null && v.size() > 0) {
              session.setAttribute("VendorSuggestedParts", v);
            }
          } else {
            session.removeAttribute("VendorSuggestedParts");
          }
          session.setAttribute("VIBean", viBean);
          errorBean.setError("ITEM ADDED SUCCESSFULLY!!!");
        } else {
          errorBean.setError("THIS ITEM CANNOT BE ADDED......Press Change");
        }
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("ChangeItem")) {
      try {
        VendorItemBean viBean = new VendorItemBean();
        fillBean(viForm, viBean);
        viBean.changeItem();
        if (viBean.getPartNo() == null || viBean.getPartNo().trim().equals("")) {
          Vector<String[]> v = checkSuggestedParts(viBean);
          if (v != null && v.size() > 0) {
            session.setAttribute("VendorSuggestedParts", v);
          }
        } else {
          session.removeAttribute("VendorSuggestedParts");
        }
        errorBean.setError("ITEM CHANGED SUCCESSFULLY!!!");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("ClearDups")) {
      try {
        clearDuplicates(viForm.getSupplierId(), viForm.getPartNo(), viForm.getVendorPartNo());
        errorBean.setError("DUPLICATES CLEARED SUCCESSFULLY!!!");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("DeleteItem")) {
      try {
        VendorItemBean viBean = new VendorItemBean();
        fillBean(viForm, viBean);
        viBean.deleteItem();
        session.removeAttribute("VendorSuggestedParts");
        errorBean.setError("ITEM DELETED SUCCESSFULLY!!!");
      } catch (Exception e) {
        logger.error(e);
        errorBean.setError(e.getMessage());
      }
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("ClearForm")) {
      viForm.resetForm();
      session.removeAttribute("VIBean");
      session.removeAttribute("VendorSuggestedParts");
      forwardPage = "VendorItem";
    } else if (buttonClicked.equals("ReturnToMain")) {
      viForm.resetForm();
      session.removeAttribute("VIBean");
      session.removeAttribute("SupplierIdForItem");
      session.removeAttribute("VendorSuggestedParts");
      forwardPage = "MainMenu";
    } else {
      forwardPage = "Login";
    }

    if (errorBean.getError() != null) {
      session.setAttribute("VendorItemError", errorBean);
    } else {
      session.removeAttribute("VendorItemError");
    }

    return (mapping.findForward(forwardPage));

  }

  public void fillForm(VendorItemForm viForm, VendorItemBean viBean) {
    viForm.setPartNo(viBean.getPartNo());
    viForm.setVendorPartNo(viBean.getVendorPartNo());
    viForm.setItemDesc1(viBean.getItemDesc1());
    viForm.setItemDesc2(viBean.getItemDesc2());
    viForm.setPlNo(viBean.getPlNo());
    viForm.setOemNo(viBean.getOemNo());
    viForm.setSellingRate(viBean.getSellingRate() + "");
    viForm.setNoOfPieces(viBean.getNoOfPieces() + "");
    viForm.setItemSize(viBean.getItemSize() + "");
    viForm.setItemSizeUnits(viBean.getItemSizeUnits());
  }

  public void fillBean(VendorItemForm viForm, VendorItemBean viBean) {
    viBean.setItemDesc1(viForm.getItemDesc1());
    viBean.setItemDesc2(viForm.getItemDesc2());
    double itemSize = 0.0;
    try {
      itemSize = Double.parseDouble(viForm.getItemSize());
    } catch (Exception e) {
      logger.error(e);
      itemSize = 0;
    }
    viBean.setItemSize(itemSize);
    viBean.setItemSizeUnits(viForm.getItemSizeUnits());
    int noOfPieces = 0;
    try {
      noOfPieces = Integer.parseInt(viForm.getNoOfPieces());
    } catch (Exception e) {
      logger.error(e);
      noOfPieces = 0;
    }
    viBean.setNoOfPieces(noOfPieces);
    viBean.setOemNo(viForm.getOemNo());
    viBean.setPartNo(viForm.getPartNo());
    viBean.setPlNo(viForm.getPlNo());
    double sellingRate = 0.0;
    try {
      sellingRate = Double.parseDouble(viForm.getSellingRate());
    } catch (Exception e) {
      logger.error(e);
      sellingRate = 0;
    }
    viBean.setSellingRate(sellingRate);
    int supId = 0;
    try {
      supId = Integer.parseInt(viForm.getSupplierId());
    } catch (Exception e) {
      logger.error(e);
      supId = 0;
    }
    viBean.setSupplierId(supId);
    viBean.setVendorPartNo(viForm.getVendorPartNo());
  }

  public Vector<String[]> checkSuggestedParts(VendorItemBean viBean) {
    Vector<String[]> v = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      if (viBean.getOemNo() != null && !viBean.getOemNo().trim().equals("")) {
        ResultSet rs =
            stmt.executeQuery("Select PartNo, ItemDesc1, ItemDesc2, OemNo from VendorItems Where SupplierId!="
                + viBean.getSupplierId() + " And OemNo='" + viBean.getOemNo() + "' And PartNo!='' ");
        while (rs.next()) {
          String pNo = rs.getString(1);
          String str1 = rs.getString(2);
          if (str1 == null) {
            str1 = "";
          } else {
            str1 = str1.trim();
          }
          String str2 = rs.getString(3);
          if (str2 == null) {
            str2 = "";
          } else {
            str2 = str2.trim();
          }
          String oemNo = rs.getString(4);
          if (oemNo == null) {
            oemNo = "";
          } else {
            oemNo = oemNo.trim();
          }

          PartsBean part = null;
          if (pNo != null && !pNo.trim().equals("")) {
            part = PartsBean.getPart(pNo, con);
          }
          if (part != null) {
            if (v == null) {
              v = new Vector<String[]>();
            }
            String st1 = pNo.trim();
            String st2 = oemNo.trim();
            String st3 =
                MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  " + part.getYear()
                    + "  " + part.getPartDescription();
            String st4 = str1.trim() + "   " + str2.trim();
            String[] arr = new String[4];
            arr[0] = st1;
            arr[1] = st2;
            arr[2] = st3;
            arr[3] = st4;

            v.add(arr);

          }
        }
      }
      if (viBean.getPlNo() != null && !viBean.getPlNo().trim().equals("")) {
        ResultSet rs1 =
            stmt.executeQuery("Select PartNo, ItemDesc1, ItemDesc2, PlNo from VendorItems Where SupplierId!="
                + viBean.getSupplierId() + " And PlNo='" + viBean.getPlNo() + "' And PartNo!='' ");
        while (rs1.next()) {
          String pNo = rs1.getString(1);
          String str1 = rs1.getString(2);
          if (str1 == null) {
            str1 = "";
          } else {
            str1 = str1.trim();
          }
          String str2 = rs1.getString(3);
          if (str2 == null) {
            str2 = "";
          } else {
            str2 = str2.trim();
          }
          String plNo = rs1.getString(4);
          if (plNo == null) {
            plNo = "";
          } else {
            plNo = plNo.trim();
          }

          PartsBean part = null;
          if (pNo != null && !pNo.trim().equals("")) {
            part = PartsBean.getPart(pNo, con);
          }
          if (part != null) {
            if (v == null) {
              v = new Vector<String[]>();
            }
            String st1 = pNo.trim();
            String st2 = plNo.trim();
            String st3 =
                MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  " + part.getYear()
                    + "  " + part.getPartDescription();
            String st4 = str1.trim() + "   " + str2.trim();
            String[] arr = new String[4];
            arr[0] = st1;
            arr[1] = st2;
            arr[2] = st3;
            arr[3] = st4;

            v.add(arr);

          }
        }
      }
      if ((v == null || v.size() == 0)
          && (viBean.getOemNo() != null && !viBean.getOemNo().trim().equals(""))) {
        String halfOem = viBean.getOemNo().substring(0, 5);
        ResultSet rs2 =
            stmt.executeQuery("Select PartNo, ItemDesc1, ItemDesc2, OemNo from VendorItems Where SupplierId!="
                + viBean.getSupplierId() + " And OemNo like '" + halfOem + "%'  ");
        while (rs2.next()) {
          String pNo = rs2.getString(1);
          String str1 = rs2.getString(2);
          if (str1 == null) {
            str1 = "";
          } else {
            str1 = str1.trim();
          }
          String str2 = rs2.getString(3);
          if (str2 == null) {
            str2 = "";
          } else {
            str2 = str2.trim();
          }
          String oemNo = rs2.getString(4);
          if (oemNo == null) {
            oemNo = "";
          } else {
            oemNo = oemNo.trim();
          }
          String rip1 = ripDashes(viBean.getOemNo());
          String rip2 = ripDashes(oemNo);

          if (!rip1.equalsIgnoreCase(rip2)) {
            continue;
          }

          PartsBean part = null;
          if (pNo != null && !pNo.trim().equals("")) {
            part = PartsBean.getPart(pNo, con);
          }
          if (part != null) {
            if (v == null) {
              v = new Vector<String[]>();
            }
            String st1 = pNo.trim();
            String st2 = oemNo.trim();
            String st3 =
                MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  " + part.getYear()
                    + "  " + part.getPartDescription();
            String st4 = str1.trim() + "   " + str2.trim();
            String[] arr = new String[4];
            arr[0] = st1;
            arr[1] = st2;
            arr[2] = st3;
            arr[3] = st4;

            v.add(arr);

          }
        }
      }
      if ((v == null || v.size() == 0)
          && (viBean.getOemNo() != null && !viBean.getOemNo().trim().equals(""))) {
        String halfOem = viBean.getOemNo().substring(0, 4);
        ResultSet rs2 =
            stmt.executeQuery("Select PartNo, ItemDesc1, ItemDesc2, OemNo from VendorItems Where SupplierId!="
                + viBean.getSupplierId() + " And OemNo like '" + halfOem + "%'  ");
        while (rs2.next()) {
          String pNo = rs2.getString(1);
          String str1 = rs2.getString(2);
          if (str1 == null) {
            str1 = "";
          } else {
            str1 = str1.trim();
          }
          String str2 = rs2.getString(3);
          if (str2 == null) {
            str2 = "";
          } else {
            str2 = str2.trim();
          }
          String oemNo = rs2.getString(4);
          if (oemNo == null) {
            oemNo = "";
          } else {
            oemNo = oemNo.trim();
          }
          String rip1 = ripDashes(viBean.getOemNo());
          String rip2 = ripDashes(oemNo);

          if (!rip1.equalsIgnoreCase(rip2)) {
            continue;
          }

          PartsBean part = null;
          if (pNo != null && !pNo.trim().equals("")) {
            part = PartsBean.getPart(pNo, con);
          }
          if (part != null) {
            if (v == null) {
              v = new Vector<String[]>();
            }
            String st1 = pNo.trim();
            String st2 = oemNo.trim();
            String st3 =
                MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  " + part.getYear()
                    + "  " + part.getPartDescription();
            String st4 = str1.trim() + "   " + str2.trim();
            String[] arr = new String[4];
            arr[0] = st1;
            arr[1] = st2;
            arr[2] = st3;
            arr[3] = st4;

            v.add(arr);

          }
        }
        rs2.close();
      }
      if ((v == null || v.size() == 0)
          && (viBean.getOemNo() != null && !viBean.getOemNo().trim().equals(""))) {
        String halfOem = viBean.getOemNo().substring(0, 3);
        ResultSet rs2 =
            stmt.executeQuery("Select PartNo, ItemDesc1, ItemDesc2, OemNo from VendorItems Where SupplierId!="
                + viBean.getSupplierId() + " And OemNo like '" + halfOem + "%'  ");
        while (rs2.next()) {
          String pNo = rs2.getString(1);
          String str1 = rs2.getString(2);
          if (str1 == null) {
            str1 = "";
          } else {
            str1 = str1.trim();
          }
          String str2 = rs2.getString(3);
          if (str2 == null) {
            str2 = "";
          } else {
            str2 = str2.trim();
          }
          String oemNo = rs2.getString(4);
          if (oemNo == null) {
            oemNo = "";
          } else {
            oemNo = oemNo.trim();
          }
          String rip1 = ripDashes(viBean.getOemNo());
          String rip2 = ripDashes(oemNo);

          if (!rip1.equalsIgnoreCase(rip2)) {
            continue;
          }

          PartsBean part = null;
          if (pNo != null && !pNo.trim().equals("")) {
            part = PartsBean.getPart(pNo, con);
          }
          if (part != null) {
            if (v == null) {
              v = new Vector<String[]>();
            }
            String st1 = pNo.trim();
            String st2 = oemNo.trim();
            String st3 =
                MakeModelBean.getMakeModelName(part.getMakeModelCode()) + "  " + part.getYear()
                    + "  " + part.getPartDescription();
            String st4 = str1.trim() + "   " + str2.trim();
            String[] arr = new String[4];
            arr[0] = st1;
            arr[1] = st2;
            arr[2] = st3;
            arr[3] = st4;

            v.add(arr);

          }
        }
        rs2.close();
      }

      stmt.close();
      con.close();
    } catch (Exception e) {
      logger.error(e);
    }

    return v;

  }

  public String ripDashes(String oemNo) {
    String rippedOem = oemNo;

    while (rippedOem.trim().indexOf("-") != -1) {
      rippedOem =
          rippedOem.substring(0, rippedOem.indexOf("-"))
              + rippedOem.substring(rippedOem.indexOf("-") + 1);
    }
    while (rippedOem.trim().indexOf(" ") != -1) {
      rippedOem =
          rippedOem.substring(0, rippedOem.indexOf(" "))
              + rippedOem.substring(rippedOem.indexOf(" ") + 1);
    }
    return rippedOem;

  }

  public void clearDuplicates(String supplierId, String partNo, String vendorPartNo)
      throws Exception {
    int supId = 0;
    try {
      supId = Integer.parseInt(supplierId);
    } catch (Exception e) {
      logger.error(e);
      throw new Exception("CHECK THE SUPPLIER ID");
    }
    if (supId == 0) {
      throw new Exception("CHECK THE SUPPLIER ID");
    }

    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = con.createStatement();
    try {
      ResultSet rs =
          stmt.executeQuery("Select * from VendorItems Where SupplierId=" + supId + " and PartNo='"
              + partNo + "' and VendorPartNo like '" + vendorPartNo + "%'");
      VendorItemBean viBean = new VendorItemBean();
      viBean.setSupplierId(supId);
      viBean.setPartNo(partNo);
      viBean.setVendorPartNo(vendorPartNo);
      viBean.setItemDesc1("");
      viBean.setItemDesc2("");
      viBean.setPlNo("");
      viBean.setOemNo("");
      viBean.setSellingRate(0.0);
      viBean.setNoOfPieces(0);
      viBean.setItemSize(0.0);
      viBean.setItemSizeUnits("");
      boolean partNoChanged = false;
      while (rs.next()) {
        String vPartNo = rs.getString("VendorPartNo");
        if (vPartNo.length() > viBean.getVendorPartNo().length()) {
          // logger.error("Size is more");
          partNoChanged = true;
          viBean.setVendorPartNo(vPartNo);
        }
        // logger.error("--" + vPartNo + "--");
        String itemDesc1 = rs.getString("ItemDesc1");
        String itemDesc2 = rs.getString("ItemDesc2");
        String plNo = rs.getString("PlNo");
        String oemNo = rs.getString("OemNo");
        double sellRate = rs.getDouble("SellingRate");
        int noPieces = rs.getInt("NoOfPieces");
        double itemSize = rs.getDouble("ItemSize");
        if (viBean.getItemDesc1().trim().equals("") && !itemDesc1.trim().equals("")) {
          viBean.setItemDesc1(itemDesc1);
        }
        if (viBean.getItemDesc2().trim().equals("") && !itemDesc2.trim().equals("")) {
          viBean.setItemDesc2(itemDesc2);
        }
        if (viBean.getPlNo().trim().equals("") && !plNo.trim().equals("")) {
          viBean.setPlNo(plNo);
        }
        if (viBean.getOemNo().trim().equals("") && !oemNo.trim().equals("")) {
          viBean.setOemNo(oemNo);
        }
        if (viBean.getSellingRate() == 0 && sellRate != 0) {
          viBean.setSellingRate(sellRate);
        }
        if (viBean.getNoOfPieces() == 0 && noPieces != 0) {
          viBean.setNoOfPieces(noPieces);
        }
        if (viBean.getItemSize() == 0 && itemSize != 0) {
          viBean.setItemSize(itemSize);
        }
      }
      if (viBean.getNoOfPieces() == 0) {
        viBean.setNoOfPieces(1);
      }

      if (partNoChanged) {
        // logger.error("Size is more 2 ");
        viBean.deleteItem();
      } else {
        viBean.deleteItem();
        viBean.addNewItem();
      }

      rs.close();
      stmt.close();
      con.close();

    } catch (Exception e) {
      logger.error(e);
      throw new Exception(e.getMessage());
    }

  }

}
