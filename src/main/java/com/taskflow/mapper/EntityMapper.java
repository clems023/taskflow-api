package com.taskflow.mapper;

import com.taskflow.dto.*;
import com.taskflow.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityMapper {

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public BoardColumnResponse toBoardColumnResponse(BoardColumn column) {
        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition(),
                column.getCreatedAt()
        );
    }

    public CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                toUserResponse(comment.getAuthor()),
                comment.getCreatedAt()
        );
    }

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getPosition(),
                task.getDueDate(),
                task.getProject().getId(),
                task.getColumn().getId(),
                task.getAssignee() != null ? toUserResponse(task.getAssignee()) : null,
                task.getComments().stream().map(this::toCommentResponse).toList(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public ProjectResponse toProjectResponse(Project project) {
        List<UserResponse> members = project.getMembers().stream()
                .map(this::toUserResponse)
                .toList();
        List<BoardColumnResponse> columns = project.getColumns().stream()
                .map(this::toBoardColumnResponse)
                .toList();

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                toUserResponse(project.getOwner()),
                members,
                columns,
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
