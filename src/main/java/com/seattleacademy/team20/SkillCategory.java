package com.seattleacademy.team20;

public class SkillCategory {

	private int id;
	private String category;
	private String color;
	private String borderColor;

	public SkillCategory(int id, String category, String color, String borderColor) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id = id;
		this.category = category;
		this.color = color;
		this.borderColor = borderColor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

}