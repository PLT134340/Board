package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import study.board.entity.User;
import study.board.repository.UserRepository;
import study.board.service.dto.UserForm;
import study.board.service.dto.UserUpdateForm;
import study.board.service.dto.UserInform;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long join(UserForm form) {
        validateDuplicateUsername(form.getUsername());
        User user = new User(form.getUsername());
        userRepository.save(user);
        return user.getId();
    }

    @Transactional(readOnly = true)
    public void validateDuplicateUsername(String username) {
        if(userRepository.existsByUsername(username))
            throw new IllegalStateException("already exists username");
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such user"));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("no such user"));
    }

    @Transactional(readOnly = true)
    public List<UserInform> findAll() {
       return userRepository.findAll()
               .stream()
               .map(user -> new UserInform(user))
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserInform findOne(Long id) {
        return new UserInform(findById(id));
    }

    public void modify(UserUpdateForm userUpdateForm, Long userId) {
        User user = findById(userId);
        user.updateUsername(userUpdateForm.getNewUsername());
    }

    public void withdraw(Long id) {
        userRepository.deleteById(id);
    }

}
