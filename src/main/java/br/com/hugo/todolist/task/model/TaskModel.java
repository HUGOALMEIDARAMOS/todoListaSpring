package br.com.hugo.todolist.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_task")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String descricao;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAT;
    private LocalDateTime endAT;
    private String prioridade;
    private UUID idUser;
    @CreationTimestamp
    private LocalDateTime creatAT;

    public void setTitle(String title) throws  Exception {
        if(title.length() > 50){
            throw new Exception("o campo title deve conter no maximo 50 caracteres");
        }
        this.title = title;
    }


}
