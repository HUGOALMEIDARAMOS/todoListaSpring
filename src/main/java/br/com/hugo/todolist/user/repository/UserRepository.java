package br.com.hugo.todolist.user.repository;

import br.com.hugo.todolist.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);
}
