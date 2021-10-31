package gameinfoweb.gameinfo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import gameinfoweb.gameinfo.excludeannotation.AnnotationExclusionStrategy;
import gameinfoweb.gameinfo.model.Board;
import gameinfoweb.gameinfo.repository.BoardRepository;
import gameinfoweb.gameinfo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 20) Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() / 5 * 5 + 1);
        int endPage = Math.min(boards.getTotalPages(), startPage + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id, Authentication authentication) {
        if (id == null) {
            model.addAttribute("board", new Board());
        } else {
            try {
                Board board = boardRepository.findById(id).orElseThrow();
                String bn = board.getUser().getUsername();
                String n = authentication.getName();
                if (!n.equals(bn)) {
                    return "redirect:/board/form";
                }
                model.addAttribute("board", board);
            }catch (Exception e){
                return "redirect:/board/form";
            }
        }
        return "board/form";
    }

    @PostMapping(value = "/prepost", produces = "application/json; charset=utf8")
    @ResponseBody
    public JsonObject prepost(@Valid Board board, BindingResult bindingResult, Authentication authentication){
        JsonObject jsonObject = new JsonObject();
        try {
            if(board.getId()!=null) {
                Board b = boardRepository.findById(board.getId()).orElseThrow();
                String bn = b.getUser().getUsername();
                String n = authentication.getName();
                if (!n.equals(bn)) {
                    throw new SecurityException("Different username");
                }
            }
            if(bindingResult.hasErrors()){
                throw new Exception("Too long or too short");
            }
        }catch (Exception e){
            jsonObject.addProperty("responseCode", "error");
            return jsonObject;
        }
        Board savedBoard = boardService.save(authentication.getName(),board);
        jsonObject.addProperty("responseCode", "success");
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        jsonObject.addProperty("savedBoard", gson.toJson(savedBoard));
        return jsonObject;
    }

    @PostMapping("/form")
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
        if(bindingResult.hasErrors()){
            return "board/form";
        }
        //새로 만들때
        if(board.getId() == null) {
            boardService.save(authentication.getName(),board);
        }
        //수정할때
        else{
            try {
                Board b = boardRepository.findById(board.getId()).orElseThrow();
                String bn = b.getUser().getUsername();
                String n = authentication.getName();
                if (!n.equals(bn)) {
                    throw new SecurityException("Different username");
                }
                boardService.save(authentication.getName(), board);
            }catch (Exception e){
                return "board/form";
            }
        }
        return "redirect:/board/list";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Long id){
        Board board = boardRepository.findById(id).orElse(null);
        model.addAttribute("board", board);
        return "board/view";
    }
}
