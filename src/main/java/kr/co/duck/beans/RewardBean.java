package kr.co.duck.beans;

import java.util.ArrayList;
import java.util.List;

public class RewardBean {
	private List<String> reward_list = new ArrayList<>();
	
	// 기본 생성자
	public RewardBean() {
	}
	

	// Getter
	public List<String> getReward_list() {
		return reward_list;
	}

	// Setter
	public void addReward(String reward) {
		this.reward_list.add(reward);
	}

	@Override
	public String toString() {
		return "RewardBean [reward_list=" + reward_list + "]";
	}
}
