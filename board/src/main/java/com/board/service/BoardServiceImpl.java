package com.board.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.board.dao.BoardDAO;
import com.board.domain.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	 private BoardDAO dao;
	
	@Override
	public List<BoardVO> list() throws Exception {
		
		return dao.list();
	}

	@Override
	public void write(BoardVO vo) throws Exception {
		
		dao.write(vo);
	}

	@Override
	public BoardVO view(int bno) throws Exception {
		
		return dao.view(bno);
	}

	@Override
	public void modify(BoardVO vo) throws Exception {
		
		dao.modify(vo);
	}
	
	// 게시물 삭제
	public void delete(int bno) throws Exception {
		
		dao.delete(bno);
	}
	
	// 게시물 총 개수
	@Override
	public int count() throws Exception {
		
		 return dao.count();
	}
	
	// 게시물 목록 + 페이징
	@Override
	public List<BoardVO> listPage(int displayPost, int postNum) throws Exception {
		
		return dao.listPage(displayPost, postNum);
	}
}
