package br.com.hugo.todolist.task.controller;

import br.com.hugo.todolist.task.model.TaskModel;
import br.com.hugo.todolist.task.repository.TaskRepository;
import br.com.hugo.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class taskController {


    @Autowired
    private TaskRepository taskrepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currenDate = LocalDateTime.now();

        //valida se a data da tarefa e menor doque a data atual
        if(currenDate.isAfter(taskModel.getCreatAT()) || currenDate.isAfter(taskModel.getEndAT())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior do que a data atual");
        }

        if(taskModel.getStartAT().isAfter(taskModel.getEndAT())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menodr doq ue a data final");
        }

        var task = this.taskrepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping
    public List<TaskModel> listatask(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskrepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel,  @PathVariable UUID id, HttpServletRequest request){

        var task = this.taskrepository.findById(id).orElse(null);

        if (task == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!task.getIdUser().equals(idUser)){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullproperties(taskModel, task);
        var taskUpdate = this.taskrepository.save(task);

        return ResponseEntity.ok().body(taskUpdate);
    }


}
