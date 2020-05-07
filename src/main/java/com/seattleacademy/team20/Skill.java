package com.seattleacademy.team20;

public class Skill {

	private int id;
	private int categoryId;
	private String name;
	private int score;

	public Skill(int id, int categoryId, String name, int score) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}