package com.seattleacademy.team20;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Controller
public class SkillController {

	private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

	@RequestMapping(value = "/skillUpload" , method = RequestMethod.GET)
	public String skillUpload(Locale locale, Model model) {
		logger.info("Welcome SkillUpload! The client locale is {}", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate );

		try {
			initialize();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		};
		List<SkillCategory> categories = selectSkillCategories();
		List<Skill> skills = selectSkills();

		uploadSkill(categories, skills);

		return "skillUpload";
	}

//	ここからタスク10

	@Autowired
	// 多分database使えるようになるもの
	private JdbcTemplate jdbcTemplate;

	// Listの宣言
	public List<SkillCategory> selectSkillCategories() {
		// sequel proで作ったテーブルからデータを取得する文字列をsqlという変数に入れている
		final String sql = "select * from skill_categories";
		// おそらくjdbaTemplateでsqlを実行している
		return jdbcTemplate.query(sql, new RowMapper<SkillCategory>() {
			// 呪文、
			public SkillCategory mapRow(ResultSet rs, int rowNum) throws SQLException{
				// SkillCategoryの中にそれぞれのデータを入れている？そのあとRowMapper<Skillcategory>に返却している？
				return new SkillCategory(rs.getInt("id"), rs.getString("category"),
						rs.getString("color"), rs.getString("border_color"));
			}
		});
	}

	// 同上
	public List<Skill> selectSkills(){
		final String sql = "select * from skills";
		return jdbcTemplate.query(sql, new RowMapper<Skill>() {
			public Skill mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new Skill(rs.getInt("id"), rs.getInt("category_id"),
						rs.getString("name"), rs.getInt("score"));
			}
		});
	}

	private FirebaseApp app;

	// SDKの初期化
	public void initialize() throws IOException {
		FileInputStream refreshToken = new FileInputStream("/Users/ruymtnw/seattle-data/portfolio-demo-mvsdm-firebase-adminsdk-13b89-f4b6b65d08.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
			    .setCredentials(GoogleCredentials.fromStream(refreshToken))
			    .setDatabaseUrl("https://portfolio-demo-mvsdm.firebaseio.com/")
			    .build();
		app = FirebaseApp.initializeApp(options, "other");
	}

	public void uploadSkill(List<SkillCategory> categories, List<Skill> skills) {
		// データの保存
		final FirebaseDatabase database = FirebaseDatabase.getInstance(app);
		DatabaseReference ref = database.getReference("skillcategories");

		// Map型のリストを作る。MapはStringで聞かれたものに対し、Object型で返すようにしている
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap;
		for(SkillCategory category : categories) {
			dataMap = new HashMap<>();
			dataMap.put("category", category.getCategory());
			dataMap.put("color", category.getColor());
			dataMap.put("borderColor", category.getBorderColor());
			// skillsのcategory_idとcategoiesのidで同じものをfilterで抽出している
			//collectでリスト化している
			dataMap.put("skills", skills.stream()
					.filter(s -> s.getCategoryId()==category.getId())
					.collect(Collectors.toList()));
			dataList.add(dataMap);
		}

		ref.setValue(dataList, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if(databaseError != null) {
					System.out.println("Data could be saved" + databaseError.getMessage());
				} else {
					System.out.println("Data save successfully.");
				}
			}
		});
	}
}