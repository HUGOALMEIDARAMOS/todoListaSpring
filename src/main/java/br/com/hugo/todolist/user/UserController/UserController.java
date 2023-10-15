package br.com.hugo.todolist.user.UserController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.hugo.todolist.user.model.UserModel;
import br.com.hugo.todolist.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null){
        return  ResponseEntity.status(HttpStatus.CONFLICT).body("Username já existe");
        }
        var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHash);
        var userCreate =  this.userRepository.save(userModel);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
    }


}
