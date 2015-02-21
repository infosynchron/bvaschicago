package com.bvas.beans;

public class InvoiceDetailsOurPrice {
	private Integer invoicenumber;
	private String partno;
	private Double ourprice;
	public Integer getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(Integer invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public String getPartno() {
		return partno;
	}
	public void setPartno(String partno) {
		this.partno = partno;
	}
	public Double getOurprice() {
		return ourprice;
	}
	public void setOurprice(Double ourprice) {
		this.ourprice = ourprice;
	}
}
