package gameinfoweb.gameinfo.controller;

import gameinfoweb.gameinfo.model.Board;
import gameinfoweb.gameinfo.model.Image;
import gameinfoweb.gameinfo.repository.BoardRepository;
import gameinfoweb.gameinfo.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api")
class BoardAPIController {

    @Value("${image-folder-path}")
    private String imgfolderpath;

    @Autowired
    private BoardRepository repository;

    @Autowired
    private ImageRepository imageRepository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false,defaultValue = "") String title,
                    @RequestParam(required = false,defaultValue = "") String content) {
        if(StringUtils.isEmpty(title)&&StringUtils.isEmpty(content)) {
            return repository.findAll();
        }else {
            return repository.findByTitleOrContent(title,content);
        }
    }
    // end::get-aggregate-root[]

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return repository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return repository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return repository.save(newBoard);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        try {
            List<Image> images = repository.findById(id).orElse(null).getImages();
            images.forEach(image -> {
                String fileRoot = imgfolderpath;
                File targetFile = new File(fileRoot + image.getFileName());
                FileUtils.deleteQuietly(targetFile);
                imageRepository.delete(image);
            });
            repository.deleteById(id);

        }catch(NullPointerException e){

        }

    }
}