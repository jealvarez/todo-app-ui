package org.jealvarez.todoapp.ui.controllers;

import org.jealvarez.todoapp.ui.api.client.TodoApiClient;
import org.jealvarez.todoapp.ui.domain.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/tasks")
public class TaskController extends BaseController {

    private final TodoApiClient todoApiClient;

    public TaskController(final TodoApiClient todoApiClient) {
        this.todoApiClient = todoApiClient;
    }

    @GetMapping
    public String getTasks(final Model model) {
        model.addAttribute("tasks", todoApiClient.getTasks());

        return "task/index";
    }

    @GetMapping("/new")
    public String newTask(final Model model) {
        model.addAttribute("task", Task.builder().build());

        return "task/task";
    }

    @PostMapping("/submit")
    public String submitTask(@ModelAttribute final Task task) {
        final String view = "redirect:/tasks";

        if (Objects.nonNull(task.getId())) {
            todoApiClient.updateTask(task);

            return view;
        }

        todoApiClient.createTask(task);

        return view;
    }

    @GetMapping("/delete/{id}")
    public String newTask(@PathVariable("id") final long id) {
        todoApiClient.deleteTask(id);

        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getTaskById(final Model model, @PathVariable("id") final long id) {
        model.addAttribute("task", todoApiClient.getTaskById(id));

        return "task/task";
    }

}
