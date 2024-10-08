package daring.web.controller;

import daring.web.domain.User;
import daring.web.dto.BoardCreateRequest;
import daring.web.service.UploadImageService;
import daring.web.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import daring.web.dto.BoardDto;
import daring.web.service.BoardService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("board")    // /board 경로로 들어오는 경우 아래의 Method들로 분기될 수 있도록 설정
public class BoardController {
    private BoardService boardService;
    private final UserService userService;
    private final UploadImageService uploadImageService;

    // 게시판

    // 게시글 목록
    // list 경로로 GET 메서드 요청이 들어올 경우 list 메서드와 맵핑시킴
    // list 경로에 요청 파라미터가 있을 경우 (?page=1), 그에 따른 페이지네이션을 수행함.

    @GetMapping({"", "/list"})
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum, Authentication auth) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("auth", auth);

        return "board/list";
    }

    // 글쓰는 페이지

    @GetMapping("/post")
    public String write(Authentication auth) {
        if (auth == null) {
            return "redirect:/board/list";
        }

        return "board/write";
    }

    // 글을 쓴 뒤 POST 메서드로 글 쓴 내용을 DB에 저장
    // 그 후에는 /list 경로로 리디렉션해준다.

    @PostMapping("/post")
    public String write(@ModelAttribute BoardCreateRequest req,
                        Authentication auth, Model model) throws IOException {
        boardService.savePost(req, auth.getName(), auth);
        return "redirect:/board/list";
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model, Authentication auth) {
        System.out.println("test111111111111111111111");
        BoardDto boardDTO = boardService.getPost(no);
        if(auth.getName()!=null){   //그냥 if말고 예외처리 해야함
            User loginUser = userService.getLoginUserByLoginId(auth.getName());
            model.addAttribute("user", loginUser);
        }
        else
            model.addAttribute("user", null);
        model.addAttribute("boardDto", boardDTO);
        System.out.println("test5555555555555");

        //model.addAttribute("imageSrc", uploadImageService.getFullPath(boardDTO.getUploadImage().getSavedFilename()));
        System.out.println("test666666666666666");
        return "board/detail";
    }

    // 게시물 수정 페이지이며, {no}로 페이지 넘버를 받는다.

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/update";
    }

    // 위는 GET 메서드이며, PUT 메서드를 이용해 게시물 수정한 부분에 대해 적용

    @PutMapping("/post/edit/{no}")
    public String update(@PathVariable("no") Long boardId,
                         @ModelAttribute BoardDto dto, Model model) throws IOException {
        boardService.editPost(boardId, dto);

        return "redirect:/board/list";
    }

    // 게시물 삭제는 deletePost 메서드를 사용하여 간단하게 삭제할 수 있다.

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect:/board/list";
    }

    // 검색
    // keyword를 view로부터 전달 받고
    // Service로부터 받은 boardDtoList를 model의 attribute로 전달해준다.

    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list";
    }

    //@ResponseBody
    @GetMapping("/images/{filename}")
    public String showImage(@PathVariable("filename") String filename, Model model) throws MalformedURLException {
        model.addAttribute("imageSrc", uploadImageService.getFullPath(filename));
        return "redirect:/board/list";
        //return new UrlResource("file:" + uploadImageService.getFullPath(filename));
    }

    @GetMapping("/images/download/{boardId}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable Long boardId) throws MalformedURLException {
        return uploadImageService.downloadImage(boardId);
    }
}
