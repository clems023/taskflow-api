package com.taskflow.service;

import com.taskflow.dto.CreateProjectRequest;
import com.taskflow.dto.ProjectResponse;
import com.taskflow.dto.UpdateProjectRequest;
import com.taskflow.entity.BoardColumn;
import com.taskflow.entity.Project;
import com.taskflow.entity.User;
import com.taskflow.exception.ForbiddenException;
import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.mapper.EntityMapper;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final List<String> DEFAULT_COLUMNS = List.of("To Do", "In Progress", "Done");

    private final ProjectRepository projectRepository;
    private final SecurityUtils securityUtils;
    private final EntityMapper mapper;

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAllForCurrentUser() {
        User currentUser = securityUtils.getCurrentUser();
        return projectRepository.findAllAccessibleByUser(currentUser).stream()
                .map(mapper::toProjectResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(UUID projectId) {
        Project project = getAccessibleProject(projectId);
        return mapper.toProjectResponse(project);
    }

    @Transactional
    public ProjectResponse create(CreateProjectRequest request) {
        User owner = securityUtils.getCurrentUser();

        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .owner(owner)
                .build();

        project.getMembers().add(owner);

        for (int i = 0; i < DEFAULT_COLUMNS.size(); i++) {
            BoardColumn column = BoardColumn.builder()
                    .name(DEFAULT_COLUMNS.get(i))
                    .position(i)
                    .project(project)
                    .build();
            project.getColumns().add(column);
        }

        return mapper.toProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(UUID projectId, UpdateProjectRequest request) {
        Project project = getAccessibleProject(projectId);
        ensureCanEdit(project);

        if (request.name() != null) {
            project.setName(request.name());
        }
        if (request.description() != null) {
            project.setDescription(request.description());
        }

        return mapper.toProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(UUID projectId) {
        Project project = getAccessibleProject(projectId);
        ensureIsOwner(project);
        projectRepository.delete(project);
    }

    public Project getAccessibleProject(UUID projectId) {
        User currentUser = securityUtils.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));

        if (!isOwner && !isMember) {
            throw new ForbiddenException("You do not have access to this project");
        }

        return project;
    }

    private void ensureCanEdit(Project project) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));

        if (!isOwner && !isMember) {
            throw new ForbiddenException("You cannot edit this project");
        }
    }

    private void ensureIsOwner(Project project) {
        User currentUser = securityUtils.getCurrentUser();
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the project owner can delete it");
        }
    }
}
