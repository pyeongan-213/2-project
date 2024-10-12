package kr.co.duck.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Entity
@Getter
@Repository
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "KAKAOPAY_INFO")
public class KakaopayInfo {

    @Id
    @Column(name = "TID")
    private String tid;

    @Column(name = "SID")
    private String sid;

    public static KakaopayInfo of(String tid, String sid){
        return KakaopayInfo.builder()
                .tid(tid)
                .sid(sid)
                .build();
    }

}
