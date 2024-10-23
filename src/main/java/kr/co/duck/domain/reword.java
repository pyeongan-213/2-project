/*
 * package kr.co.duck.domain;
 * 
 * import javax.persistence.Column; import javax.persistence.Entity; import
 * javax.persistence.FetchType; import javax.persistence.GeneratedValue; import
 * javax.persistence.GenerationType; import javax.persistence.Id; import
 * javax.persistence.ManyToOne;
 * 
 * // 기능: 리워드 엔티티
 * 
 * @Entity public class reword {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long rewardId;
 * 
 * @Column(nullable = false) private String rewardName;
 * 
 * @ManyToOne(fetch = FetchType.LAZY) private Member member;
 * 
 * // 기본 생성자 public MEMBER_GAME_STATS() { }
 * 
 * // 모든 필드를 포함하는 생성자 public MEMBER_GAME_STATS(Long rewardId, String rewardName,
 * Member member) { this.rewardId = rewardId; this.rewardName = rewardName;
 * this.member = member; }
 * 
 * // 생성자 (rewardName, member를 이용) public MEMBER_GAME_STATS(String rewardName,
 * Member member) { this.rewardName = rewardName; this.member = member; }
 * 
 * // Getter 메서드 public Long getRewardId() { return rewardId; }
 * 
 * public String getRewardName() { return rewardName; }
 * 
 * public Member getMember() { return member; }
 * 
 * @Override public String toString() { return "Reward{" + "rewardId=" +
 * rewardId + ", rewardName='" + rewardName + '\'' + ", member=" + member + '}';
 * } }
 */