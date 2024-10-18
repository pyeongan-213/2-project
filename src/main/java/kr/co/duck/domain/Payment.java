package kr.co.duck.domain;

import java.util.Date;

public class Payment {

    // 필드 선언 (데이터베이스 컬럼명과 동일하게 수정)
    private int PAY_ID;               // 결제 식별자
    private int MEMBER_ID;            // 멤버 식별자
    private Date SUBSCRIBEDATE;       // 구독일
    private Date EXPIREDATE;          // 만료일
    private String CONTINUESUB;       // 구독 갱신 여부 (Y/N)
    private String PRICE;             // 결제액
    private String PAYMENT;           // 결제 수단 (카드, 카카오페이 등)
    private String PAYMENT_INFO;      // 결제 수단 정보 (카드 번호 등)

    // Getters and Setters
    public int getPAY_ID() {
        return PAY_ID;
    }

    public void setPAY_ID(int PAY_ID) {
        this.PAY_ID = PAY_ID;
    }

    public int getMEMBER_ID() {
        return MEMBER_ID;
    }

    public void setMEMBER_ID(int MEMBER_ID) {
        this.MEMBER_ID = MEMBER_ID;
    }

    public Date getSUBSCRIBEDATE() {
        return SUBSCRIBEDATE;
    }

    public void setSUBSCRIBEDATE(Date SUBSCRIBEDATE) {
        this.SUBSCRIBEDATE = SUBSCRIBEDATE;
    }

    public Date getEXPIREDATE() {
        return EXPIREDATE;
    }

    public void setEXPIREDATE(Date EXPIREDATE) {
        this.EXPIREDATE = EXPIREDATE;
    }

    public String getCONTINUESUB() {
        return CONTINUESUB;
    }

    public void setCONTINUESUB(String CONTINUESUB) {
        this.CONTINUESUB = CONTINUESUB;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getPAYMENT() {
        return PAYMENT;
    }

    public void setPAYMENT(String PAYMENT) {
        this.PAYMENT = PAYMENT;
    }

    public String getPAYMENT_INFO() {
        return PAYMENT_INFO;
    }

    public void setPAYMENT_INFO(String PAYMENT_INFO) {
        this.PAYMENT_INFO = PAYMENT_INFO;
    }

}
