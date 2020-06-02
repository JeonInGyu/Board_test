package com.board.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.dao.BoardDAO;
import com.board.domain.BoardVO;
import com.board.service.BoardService;
import com.member.domain.MemberVO;
import com.member.service.MemberService;

@Controller
@RequestMapping(value = {"/board/*", "/member/*"})
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	 @Inject
	 private BoardService service;
	 
	 @Inject
	 MemberService memberService;
	 
	 
	 // 게시판 목록
 	@RequestMapping(value = "/list", method = RequestMethod.GET)
 	public void getList(Model model) throws Exception {
		  List<BoardVO> list = null;
		  list = service.list();
		  
		  model.addAttribute("list", list);
	 }
 	
 	// 게시물 작성
 	@RequestMapping(value = "/write", method = RequestMethod.GET)
 	public void getWirte(HttpSession session, Model model) throws Exception {
 		logger.info("get write");
 		
 		Object loginInfo = session.getAttribute("member");
 		System.out.println(loginInfo + "여기");
 		
 		if(loginInfo == null) {
  			model.addAttribute("msg", false);
  		}
 		
 	}
 	
 	// 게시물 작성
  	@RequestMapping(value = "/write", method = RequestMethod.POST)
  	public String postWirte(HttpSession session, Model model,BoardVO vo) throws Exception {
		service.write(vo);
  		
  		return "redirect:/board/list";
  	}
  	
  	// 게시물 조회
  	@RequestMapping(value = "/view", method = RequestMethod.GET)
  	public void getView(@RequestParam("bno") int bno, Model model) throws Exception {
  		BoardVO vo = service.view(bno);
  		
  		model.addAttribute("view", vo);
  	}
  	
  	// 게시물 수정
  	@RequestMapping(value = "/modify", method = RequestMethod.GET)
  	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception {
	  	 BoardVO vo = service.view(bno);
	  	   
	  	 model.addAttribute("view", vo);
  	}
  	
  	// 게시물 수정
  	@RequestMapping(value = "/modify", method = RequestMethod.POST)
  	public String postModify(BoardVO vo) throws Exception {
	  	 service.modify(vo);
	  	   
	  	 return "redirect:/board/view?bno=" + vo.getBno();
  	}
  	
  	// 게시물 삭제
  	@RequestMapping(value = "/delete", method = RequestMethod.GET)
  	public String getDelete(@RequestParam("bno") int bno) throws Exception {
	  	 service.delete(bno);  
	
	  	 return "redirect:/board/list";
  	}
  	
  	 // 게시판 목록 + 페이징 추가
 	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
 	public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
		// 게시물 총 갯수
		int count = service.count();
		
		// 한 페이지에 출력할 게시물 갯수
		int postNum = 10;
		 
		// 하단 페이징 번호 ([ 게시물 총 갯수 ÷ 한 페이지에 출력할 갯수 ]의 올림)
		int pageNum = (int)Math.ceil((double)count/postNum);
		 
		// 출력할 게시물
		int displayPost = (num - 1) * postNum;
		 
		// 한번에 표시할 페이징 번호의 갯수
		int pageNum_cnt = 10;

		// 표시되는 페이지 번호 중 마지막 번호
		int endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
		
		// 표시되는 페이지 번호 중 첫번째 번호
		int startPageNum = endPageNum - (pageNum_cnt - 1);
		 
		// 마지막 번호 재계산
		int endPageNum_tmp = (int)(Math.ceil((double)count / (double)pageNum_cnt));
		 
		if(endPageNum > endPageNum_tmp) {
			endPageNum = endPageNum_tmp;
		}
 		
		boolean prev = startPageNum == 1 ? false : true;
		boolean next = endPageNum * pageNum_cnt >= count ? false : true;
		
		// 시작 및 끝 번호
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);

		// 이전 및 다음 
		model.addAttribute("prev", prev);
		model.addAttribute("next", next);
		
		List<BoardVO> list = null; 
		list = service.listPage(displayPost, postNum);
		model.addAttribute("list", list);   
		model.addAttribute("pageNum", pageNum);
	 }
 	
 	 // 글 작성 get
 	 @RequestMapping(value = "/register", method = RequestMethod.GET)
 	 public void getRegister() throws Exception {
 		 logger.info("get register");
 	 }
 	
 	 // 글 작성 post
 	 @RequestMapping(value = "/register", method = RequestMethod.POST)
 	 public String postRegister(MemberVO vo) throws Exception {
 		 logger.info("post resister");
 		 
 		 memberService.register(vo);
 		 
 		 return "redirect:/";
 	 }
 		 
	 // 로그인
	 @RequestMapping(value = "/login", method = RequestMethod.POST)
	 public String login(MemberVO vo, RedirectAttributes rttr,HttpServletRequest req) throws Exception {
		  logger.info("post login");
		  
		  HttpSession session = req.getSession();
		  
		  MemberVO login = memberService.login(vo);
		  
		  if(login == null) {
			  rttr.addFlashAttribute("msg", false);
		  } else {
			  session.setAttribute("member", login);
		  }
		  
		  return "redirect:/";
	 }
 	
	 // 회원정보 수정
	 @RequestMapping(value = "/memberModify", method = RequestMethod.GET)
	 public void modify() throws Exception {
		 logger.info("get modify");
		 
		 
	 }
	 
	 // 회원정보 수정
	 @RequestMapping(value = "/memberModify", method = RequestMethod.POST)
	 public String postModify(HttpSession session, MemberVO vo) throws Exception {
		 logger.info("post modify");
		 
		 memberService.modify(vo);
		 
		 session.invalidate();
		 
		 return "redirect:/";
	 }
	 
	// 회원 확인
	 @ResponseBody
	 @RequestMapping(value = "/member/idCheck", method = RequestMethod.POST)
	 public String postIdCheck(HttpServletRequest req) throws Exception {
		  logger.info("post idCheck");
		  
		  String userId = req.getParameter("userId");
		  MemberVO idCheck = memberService.idCheck(userId);
		  
		  String result = "0";
		  
		  if(idCheck != null) {
			  result = "1";
		  } 
		  
		  System.out.println(result);
		  return result;
	 }
	 
}