package kr.co.duck.social;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KaKaoReadyResponse {

    // 결제 고유번호
    private String tid;
    // 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력화면 Redirect URL (카카오 측 제공)
    private String next_redirect_pc_url;
    
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		System.out.println(tid);
		this.tid = tid;
	}
	public String getNext_redirect_pc_url() {
		return next_redirect_pc_url;
	}
	public void setNext_redirect_pc_url(String next_redirect_pc_url) {
		this.next_redirect_pc_url = next_redirect_pc_url;
	}

}
