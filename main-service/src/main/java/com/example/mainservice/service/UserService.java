package com.example.mainservice.service;

import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.mapper.UserMapper;
import com.example.mainservice.model.NewUserRequest;
import com.example.mainservice.model.UserDto;
import com.example.mainservice.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto addUser(NewUserRequest newUserRequest) {
        try {
            return userMapper.mapFromUser(userRepository.save(userMapper.mapToUser(newUserRequest)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

    public List<UserDto> getUser(List<Long> ids, int from, int size) {
        Pageable pageableWithSort = PageRequest.of(from, size);
        if (ids == null || ids.isEmpty()) return userRepository.findAll(pageableWithSort).getContent().stream()
                .map(userMapper::mapFromUser).collect(Collectors.toList());
        return userRepository.
                findAllByIdIn(ids, pageableWithSort)
                .stream().map(userMapper::mapFromUser)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow();
        userRepository.deleteById(userId);
    }
}
