package kr.co.duck.social;

import lombok.Getter;

@Getter
public class KakaoPayRedayDto {

    private String name;
    private String totalPrice;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
    
    
    
    
    
    

}
