package kr.co.duck.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.duck.mapper.QuizMapper;

@Repository
public class QuizDao{
	
	@Autowired
	private QuizMapper quizMapper;
	//
	
}
