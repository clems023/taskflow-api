package com.taskflow.service;

import com.taskflow.dto.CommentResponse;
import com.taskflow.dto.CreateCommentRequest;
import com.taskflow.entity.Comment;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.mapper.EntityMapper;
import com.taskflow.repository.CommentRepository;
import com.taskflow.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Transactional(readOnly = true)
    public List<CommentResponse> findByTask(UUID projectId, UUID taskId) {
        taskService.getAccessibleTask(projectId, taskId);
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(mapper::toCommentResponse)
                .toList();
    }

    @Transactional
    public CommentResponse create(UUID projectId, UUID taskId, CreateCommentRequest request) {
        Task task = taskService.getAccessibleTask(projectId, taskId);
        User author = securityUtils.getCurrentUser();

        Comment comment = Comment.builder()
                .content(request.content())
                .task(task)
                .author(author)
                .build();

        return mapper.toCommentResponse(commentRepository.save(comment));
    }
}
