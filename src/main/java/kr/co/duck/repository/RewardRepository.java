/*
 * package kr.co.duck.repository;
 * 
 * import java.util.List;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import kr.co.duck.domain.Member; import kr.co.duck.domain.MEMBER_GAME_STATS;
 * 
 * // 기능: 리워드 정보 레포지토리 public interface RewardRepository extends
 * JpaRepository<MEMBER_GAME_STATS, Long> { List<MEMBER_GAME_STATS>
 * findByMember(Member member);
 * 
 * @Transactional void deleteAllByMember(Member member);
 * 
 * boolean existsByMember(Member member); }
 */