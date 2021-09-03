package com.bit.controller;

import org.springframework.ui.Model;

import com.bit.service.GalleryService;

//네비바 DB데이터 각 페이지마다 호출
public class CommonMethod {
	
	public static void getCategoryAndGalname(Model model, GalleryService gServ) {
		//카테고리를 스트링배열로 넣어서 붙여준다.
		String[] category = gServ.getCategory();
		
		model.addAttribute("category", category);
		model.addAttribute("sports", gServ.getGalname("스포츠"));
		model.addAttribute("broadcast", gServ.getGalname("연예&방송"));
		model.addAttribute("games", gServ.getGalname("게임"));
		model.addAttribute("hobbies", gServ.getGalname("취미생활"));
		model.addAttribute("edu", gServ.getGalname("교육&금융&IT"));
		model.addAttribute("trip", gServ.getGalname("여행&음식&생물"));
	}
}