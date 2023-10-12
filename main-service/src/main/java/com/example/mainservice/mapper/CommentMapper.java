package com.example.mainservice.mapper;

import com.example.mainservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CommentMapper {
    private final UserMapper userMapper;

    public Comment mapToComment(CommentCreateDto commentCreateDto, Event event, User user) {
        return new Comment(null, event, user, commentCreateDto.getText(), LocalDateTime.now());
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(userMapper.mapToShortDto(comment.getUser()), comment.getCreatedOn(), comment.getText());
    }
}
