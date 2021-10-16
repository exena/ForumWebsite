package gameinfoweb.gameinfo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gameinfoweb.gameinfo.model.Board;
import gameinfoweb.gameinfo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping(value = "/prepost", produces = "application/json; charset=utf8")
    @ResponseBody
    public JsonObject prepost(@Valid Board board, BindingResult bindingResult){
        JsonObject jsonObject = new JsonObject();
        if(bindingResult.hasErrors()){
            jsonObject.addProperty("responseCode", "error");
            return jsonObject;
        }
        Board savedBoard = boardRepository.save(board);
        jsonObject.addProperty("responseCode", "success");
        jsonObject.addProperty("savedBoard", new Gson().toJson(savedBoard));
        return jsonObject;
    }
    @PostMapping("/form")
    public String postForm(@Valid Board board, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "board/form";
        }
        //새로 만들때
        if(board.getId() == null) {
            boardRepository.save(board);
        }
        //수정할때
        else{
            boardRepository.save(board);
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
