package kr.co.duck.social;

import lombok.Getter;



@Getter
public class KakaoPayRedayDto {

    private String name;
    private String totalPrice;
    private Long memberId; //사용자 Id 필드 추가
    
    
    
    
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
   public Long getMemberId() {
      return memberId;
   }
   public void setMemberId(Long memberId) {
      this.memberId = memberId;
   }
    
    
    
    
    
    

}
