package study.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.User;
import study.board.repository.UserRepository;
import study.board.service.dto.UserCreateForm;
import study.board.service.dto.UserInform;
import study.board.service.dto.UserUpdateForm;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void joinSuccess() {
        // given
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setUsername("kim");

        User user = new User(userCreateForm.getUsername());
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        Long userId = userService.join(userCreateForm);

        // then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("닉네임 중복으로 회원가입 실패")
    void joinFail() {
        // given
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setUsername("kim");

        given(userRepository.existsByUsername(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> userService.join(userCreateForm))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists username");
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findByIdSuccess() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        User findUser = userService.findById(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("id로 조회 실패")
    void findByIdFail() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }

    @Test
    @DisplayName("username으로 조회 성공")
    void findByUsernameSuccess() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        // when
        User findUser = userService.findByUsername(user.getUsername());

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("username으로 조회 실패")
    void findByUsernameFail() {
        // given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.findByUsername("kim"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }

    @Test
    @DisplayName("전체 조회 후 dto로 반환")
    void findAll() {
        // given
        List<User> users = List.of(new User("kim"));
        ReflectionTestUtils.setField(users.get(0), "id", 1L);

        given(userRepository.findAll()).willReturn(users);

        // when
        List<UserInform> userInforms = userService.findAllToUserInform();

        // then
        assertThat(users.size()).isEqualTo(userInforms.size());
        assertThat(userInforms.get(0).getId()).isEqualTo(users.get(0).getId());
        assertThat(userInforms.get(0).getUsername()).isEqualTo(users.get(0).getUsername());
    }

    @Test
    @DisplayName("dto로 반환 성공")
    void toUserInformSuccess() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserInform userInform = userService.toUserInform(user.getId());

        // then
        assertThat(userInform.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("dto로 반환 실패")
    void toUserInformFail() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.toUserInform(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }

    @Test
    @DisplayName("닉네임 수정 성공")
    void modifySuccess() {
        // given

        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        UserUpdateForm form = new UserUpdateForm(user);
        form.setUsername("lee");

        given(userRepository.existsByUsername(anyString())).willReturn(user.getUsername().equals(form.getUsername()));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userService.modify(form, user.getId());

        // then
        assertThat(user.getUsername()).isEqualTo(form.getUsername());
    }

    @Test
    @DisplayName("중복으로 닉네임 수정 실패")
    void modifyFail() {
        // given
        User user = new User("kim");
        ReflectionTestUtils.setField(user, "id", 1L);

        UserUpdateForm form = new UserUpdateForm(user);
        form.setUsername("kim");

        given(userRepository.existsByUsername(anyString())).willReturn(user.getUsername().equals(form.getUsername()));

        // when
        // then
        assertThatThrownBy(() -> userService.modify(form, user.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists username");
    }

}