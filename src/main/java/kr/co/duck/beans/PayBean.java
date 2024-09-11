package kr.co.duck.beans;

public class PayBean {

	private int pay_id;
	private String subscribe_date;
	private String expire_date;
	private boolean iscontinue;
	private int pay_price;
	
	public int getPay_id() {
		return pay_id;
	}
	public void setPay_id(int pay_id) {
		this.pay_id = pay_id;
	}
	public String getSubscribe_date() {
		return subscribe_date;
	}
	public void setSubscribe_date(String subscribe_date) {
		this.subscribe_date = subscribe_date;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public boolean isIscontinue() {
		return iscontinue;
	}
	public void setIscontinue(boolean iscontinue) {
		this.iscontinue = iscontinue;
	}
	public int getPay_price() {
		return pay_price;
	}
	public void setPay_price(int pay_price) {
		this.pay_price = pay_price;
	}
	
	
	
}
