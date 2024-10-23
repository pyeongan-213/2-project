/*
 * package kr.co.duck.service;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import kr.co.duck.beans.RewardBean; import
 * kr.co.duck.domain.MEMBER_GAME_STATS; import kr.co.duck.domain.Member; import
 * kr.co.duck.domain.QuizCommand; import
 * kr.co.duck.repository.MemberGameStatsRepository;
 * 
 * @Service public class MemberGameStats {
 * 
 * private final QuizCommand quizCommand; private final
 * MemberGameStatsRepository memberGameStatsRepository;
 * 
 * // 생성자 public MemberGameStats(QuizCommand quizCommand,
 * MemberGameStatsRepository memberGameStatsRepository) {
 * 
 * this.quizCommand = quizCommand; this.memberGameStatsRepository =
 * memberGameStatsRepository; }
 * 
 * 
 * @Transactional public boolean createWinReward(Member member) {
 * MemberGameStats stats = getMemberGameStatsByMemberId(member.getMemberId());
 * // 게임 통계 정보 가져오기 if (stats.getWinNum() == 1) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("병아리", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 40) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리", member); quizCommand.saveReward(reward); return true;
 * } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리장군", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리왕", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("봉황", member); quizCommand.saveReward(reward); return true;
 * }
 * 
 * else { return false; } }
 * 
 * 
 * 
 * 
 * @Transactional public boolean createWinReward(Member member) {
 * MemberGameStats stats = getMemberGameStatsByMemberId(member.getMemberId());
 * // 게임 통계 정보 가져오기 if (stats.getWinNum() == 1) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("병아리", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 40) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리", member); quizCommand.saveReward(reward); return true;
 * } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리장군", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("오리왕", member); quizCommand.saveReward(reward); return
 * true; } else if (stats.getWinNum() == 60) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("봉황", member); quizCommand.saveReward(reward); return true;
 * }
 * 
 * else { return false; } }
 * 
 * @Transactional public boolean createLoseReward(Member member) {
 * MemberGameStats stats = getMemberGameStatsByMemberId(member.getMemberId());
 * // 게임 통계 정보 가져오기 if (stats.getLoseNum() == 1) { MEMBER_GAME_STATS reward =
 * new MEMBER_GAME_STATS("병든 병아리", member); quizCommand.saveReward(reward);
 * return true; } else if (stats.getLoseNum() == 40) { MEMBER_GAME_STATS reward
 * = new MEMBER_GAME_STATS("병든 오리", member); quizCommand.saveReward(reward);
 * return true; } else if (stats.getLoseNum() == 60) { MEMBER_GAME_STATS reward
 * = new MEMBER_GAME_STATS("그는 좋은 오리이었습니다...", member);
 * quizCommand.saveReward(reward); return true; } else { return false; } }
 * 
 * @Transactional public boolean createTotalGameReward(Member member) {
 * MemberGameStats stats = getMemberGameStatsByMemberId(member.getMemberId());
 * // 게임 통계 정보 가져오기 if (stats.getTotalGameNum() == 1) { MEMBER_GAME_STATS reward
 * = new MEMBER_GAME_STATS("게임에 중독된 오리", member);
 * quizCommand.saveReward(reward); return true; } else if
 * (stats.getTotalGameNum() == 40) { MEMBER_GAME_STATS reward = new
 * MEMBER_GAME_STATS("게임에 중독된 오리", member); quizCommand.saveReward(reward);
 * return true; } else if (stats.getTotalGameNum() == 60) { MEMBER_GAME_STATS
 * reward = new MEMBER_GAME_STATS("이제 그만 인생을 살아가세요 휴먼...", member);
 * quizCommand.saveReward(reward); return true; } else { return false; } }
 * 
 * public RewardBean allRewardList(Member member) { List<MEMBER_GAME_STATS>
 * rewardList = rewardRepository.findByMember(member); RewardBean rewardBean =
 * new RewardBean(); for (MEMBER_GAME_STATS reward : rewardList) {
 * rewardBean.addReward(reward.getRewardName()); // 메서드 이름 변경에 따라 수정 } return
 * rewardBean; }
 * 
 * // **멤버 ID로 게임 통계 정보 조회 메서드** private MemberGameStats
 * getMemberGameStatsByMemberId(int memberId) { MemberGameStats stats =
 * memberGameStatsRepository.findByMemberId(memberId); // 직접 조회 if (stats ==
 * null) { // 조회된 결과가 없을 경우 예외 처리 throw new
 * IllegalArgumentException("해당 멤버의 게임 통계를 찾을 수 없습니다: " + memberId); } return
 * stats; } }
 */