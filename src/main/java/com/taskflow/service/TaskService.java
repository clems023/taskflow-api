package com.taskflow.service;

import com.taskflow.dto.CreateTaskRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.dto.UpdateTaskRequest;
import com.taskflow.entity.BoardColumn;
import com.taskflow.entity.Project;
import com.taskflow.entity.Task;
import com.taskflow.entity.TaskPriority;
import com.taskflow.entity.User;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.mapper.EntityMapper;
import com.taskflow.repository.BoardColumnRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final EntityMapper mapper;

    @Transactional(readOnly = true)
    public List<TaskResponse> findByProject(UUID projectId) {
        projectService.getAccessibleProject(projectId);
        return taskRepository.findByProjectIdOrderByPositionAsc(projectId).stream()
                .map(mapper::toTaskResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(UUID projectId, UUID taskId) {
        Task task = getAccessibleTask(projectId, taskId);
        return mapper.toTaskResponse(task);
    }

    @Transactional
    public TaskResponse create(UUID projectId, CreateTaskRequest request) {
        Project project = projectService.getAccessibleProject(projectId);

        BoardColumn column = columnRepository.findById(request.columnId())
                .orElseThrow(() -> new ResourceNotFoundException("Column not found"));

        if (!column.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Column not found in this project");
        }

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        }

        int nextPosition = taskRepository.findByColumnIdOrderByPositionAsc(column.getId()).size();

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM)
                .position(nextPosition)
                .dueDate(request.dueDate())
                .project(project)
                .column(column)
                .assignee(assignee)
                .build();

        return mapper.toTaskResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse update(UUID projectId, UUID taskId, UpdateTaskRequest request) {
        Task task = getAccessibleTask(projectId, taskId);

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }
        if (request.position() != null) {
            task.setPosition(request.position());
        }
        if (request.columnId() != null) {
            BoardColumn column = columnRepository.findById(request.columnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Column not found"));
            if (!column.getProject().getId().equals(projectId)) {
                throw new ResourceNotFoundException("Column not found in this project");
            }
            task.setColumn(column);
        }
        if (request.assigneeId() != null) {
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return mapper.toTaskResponse(taskRepository.save(task));
    }

    @Transactional
    public void delete(UUID projectId, UUID taskId) {
        Task task = getAccessibleTask(projectId, taskId);
        taskRepository.delete(task);
    }

    Task getAccessibleTask(UUID projectId, UUID taskId) {
        projectService.getAccessibleProject(projectId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task not found in this project");
        }

        return task;
    }
}
