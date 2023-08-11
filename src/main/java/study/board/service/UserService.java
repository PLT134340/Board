package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.User;
import study.board.repository.UserRepository;
import study.board.service.dto.user.UserCreateForm;
import study.board.service.dto.user.UserUpdateForm;
import study.board.service.dto.user.UserInform;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Long join(UserCreateForm form) {
        validateDuplicateUsername(form.getUsername());
        User user = userRepository.save(new User(form.getUsername(), passwordEncoder.encode(form.getPassword())));
        return user.getId();
    }

    @Transactional(readOnly = true)
    public void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username))
            throw new IllegalArgumentException("already exists username");
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
    public List<UserInform> findAllToUserInform() {
       return userRepository.findAll()
               .stream()
               .map(user -> new UserInform(user))
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserInform toUserInform(Long id) {
        return new UserInform(findById(id));
    }

    @Transactional(readOnly = true)
    public UserUpdateForm toUserUpdateForm(Long id) {
        return new UserUpdateForm(findById(id));
    }

    public void modify(UserUpdateForm form, Long userId) {
        validateDuplicateUsernameExcludeSelf(form.getUsername(), userId);
        User user = findById(userId);
        user.update(form.getUsername(), passwordEncoder.encode(form.getPassword()));
    }

    @Transactional(readOnly = true)
    public void validateDuplicateUsernameExcludeSelf(String username, Long id) {
        if (userRepository.existsByUsername(username) && !findByUsername(username).getId().equals(id))
            throw new IllegalArgumentException("already exists username");
    }

    public void withdraw(Long id) {
        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("no such user");
        }

        userRepository.deleteById(id);
    }

}
