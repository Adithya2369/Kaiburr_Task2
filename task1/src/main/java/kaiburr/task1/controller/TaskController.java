package kaiburr.task1.controller;
import kaiburr.task1.model.Task;
import kaiburr.task1.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService service;

    @GetMapping
    public List<Task> getAll(@RequestParam(required = false) String id) {
        if (id == null) return service.getAll();
        return service.getById(id).map(List::of).orElse(List.of());
    }

    @PutMapping
    public Task addTask(@RequestBody Task task) { return service.add(task); }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) { service.delete(id); }

    @GetMapping("/search")
    public List<Task> search(@RequestParam String name) { return service.searchByName(name); }

    @PutMapping("/{id}/execute")
    public Task execute(@PathVariable String id) throws Exception { return service.execute(id); }
}
