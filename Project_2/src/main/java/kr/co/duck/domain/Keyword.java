package kr.co.duck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// 기능: 퀴즈 카테고리와 키워드 엔티티
@Entity
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long keywordId;

	@Column(nullable = false)
	private String category;

	@Column(nullable = false)
	private String word;

	// 기본 생성자
	public Keyword() {
	}

	// 모든 필드를 포함하는 생성자
	public Keyword(Long keywordId, String category, String word) {
		this.keywordId = keywordId;
		this.category = category;
		this.word = word;
	}

	// Getter 메서드
	public Long getKeywordId() {
		return keywordId;
	}

	public String getCategory() {
		return category;
	}

	public String getWord() {
		return word;
	}

	@Override
	public String toString() {
		return "Keyword{" + "keywordId=" + keywordId + ", category='" + category + '\'' + ", word='" + word + '\''
				+ '}';
	}
}
