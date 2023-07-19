package study.board.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.User;
import study.board.repository.UserRepository;
import study.board.service.dto.UserForm;
import study.board.service.dto.UserUpdateForm;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class UserServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void before() {
        UserForm userForm = new UserForm();
        userForm.setUsername("kim");
        userService.join(userForm);
    }


    @Test
    void join() {
        UserForm userForm = new UserForm();
        userForm.setUsername("Lee");
        Long userId = userService.join(userForm);

        em.flush();
        em.clear();
        User user = userService.findById(userId);

        Assertions.assertThat(user.getUsername()).isEqualTo(userForm.getUsername());
    }

    @Test
    void duplicatedUsername() {
        UserForm form = new UserForm();
        form.setUsername("kim");

        Assertions.assertThatThrownBy(() -> userService.join(form))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists username");
    }

    @Test
    void modify() {
        User kim = userService.findByUsername("kim");

        UserUpdateForm form = new UserUpdateForm();
        form.setNewUsername("lee");
        userService.modify(form, kim.getId());

        User lee = userService.findByUsername("lee");
        Assertions.assertThat(kim.getId()).isEqualTo(lee.getId());
    }

    @Test
    void delete() {
        User kim = userService.findByUsername("kim");

        userService.withdraw(kim.getId());

        Assertions.assertThatThrownBy(() -> userService.findByUsername("kim"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }
}