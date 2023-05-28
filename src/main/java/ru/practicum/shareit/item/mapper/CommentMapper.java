package ru.practicum.shareit.item.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Component
public class CommentMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CommentMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(LocalDateTime.ofInstant(Instant.ofEpochSecond(comment.getCreated()),
                TimeZone.getDefault().toZoneId()));
        commentDto.setCreated(commentDto.getCreated().plusHours(
                TimeUnit.SECONDS.toHours(ZonedDateTime.now().getOffset().getTotalSeconds()))
        );
        commentDto.setCreated(commentDto.getCreated().plusSeconds(10));
        return commentDto;
    }

    public Comment convertToComment(CommentInputDto commentInputDto) {
        return modelMapper.map(commentInputDto, Comment.class);
    }

}
