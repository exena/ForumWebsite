package gameinfoweb.gameinfo.service;

import gameinfoweb.gameinfo.model.Board;
import gameinfoweb.gameinfo.model.User;
import gameinfoweb.gameinfo.repository.BoardRepository;
import gameinfoweb.gameinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board){
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        return boardRepository.save(board);
    }
}
