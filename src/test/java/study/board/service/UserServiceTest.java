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
import study.board.service.dto.UserLoginForm;
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

    private UserCreateForm getUserCreatForm() {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setUsername("kim");
        userCreateForm.setPassword("1234");
        return userCreateForm;
    }

    private User getUser() {
        User user = new User("kim", "1234");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private UserLoginForm getUserLoginForm() {
        UserLoginForm userLoginForm = new UserLoginForm();
        userLoginForm.setUsername("kim");
        userLoginForm.setPassword("1234");
        return userLoginForm;
    }

    private UserUpdateForm getUserUpdateForm() {
        UserUpdateForm form = new UserUpdateForm();
        form.setUsername("lee");
        form.setPassword("1234");
        return form;
    }

    @Test
    @DisplayName("회원가입 성공")
    void joinSuccess() {
        // given
        UserCreateForm userCreateForm = getUserCreatForm();

        User user = getUser();

        given(userRepository.existsByUsername(user.getUsername())).willReturn(false);
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
        UserCreateForm userCreateForm = getUserCreatForm();

        given(userRepository.existsByUsername("kim"))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> userService.join(userCreateForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists username");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        // given

        UserLoginForm userLoginForm = getUserLoginForm();

        User user = new User("kim", "1234");
        ReflectionTestUtils.setField(user, "id", 1L);


        given(userRepository.findByUsername("kim"))
                .willReturn(Optional.of(user));

        // when
        Long userId = userService.login(userLoginForm);

        // then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 실패")
    void LoginFailByNotExistId() {
        // given
        UserLoginForm userLoginForm = getUserLoginForm();

        User user = getUser();

        given(userRepository.findByUsername("kim"))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> userService.login(userLoginForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }

    @Test
    @DisplayName("일치하지 않는 비밀번호로 로그인 실패")
    void LoginFailByDifferId() {
        // given
        UserLoginForm userLoginForm = getUserLoginForm();
        userLoginForm.setPassword("2345");

        User user = getUser();

        given(userRepository.findByUsername("kim"))
                .willReturn(Optional.of(user));

        // when
        // then
        assertThatThrownBy(() -> userService.login(userLoginForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not match password");
    }

    @Test
    @DisplayName("id로 조회 성공")
    void findByIdSuccess() {
        // given
        User user = getUser();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

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
        User user = getUser();

        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        // when
        User findUser = userService.findByUsername(user.getUsername());

        // then
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("username으로 조회 실패")
    void findByUsernameFail() {
        // given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());

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
        List<User> users = List.of(getUser());

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
        User user = getUser();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

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
        User user = getUser();

        UserUpdateForm form = getUserUpdateForm();

        given(userRepository.existsByUsername(form.getUsername()))
                .willReturn(user.getUsername().equals(form.getUsername()));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        userService.modify(form, user.getId());

        // then
        assertThat(user.getUsername()).isEqualTo(form.getUsername());
    }

    @Test
    @DisplayName("중복으로 닉네임 수정 실패")
    void modifyFail() {
        // given
        User user = getUser();

        UserUpdateForm form = new UserUpdateForm();
        form.setUsername("kim");

        given(userRepository.existsByUsername(form.getUsername()))
                .willReturn(user.getUsername().equals(form.getUsername()));

        // when
        // then
        assertThatThrownBy(() -> userService.modify(form, user.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists username");
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void withdrawSuccess() {
        // given
        given(userRepository.existsById(1L)).willReturn(true);

        // when
        userService.withdraw(1L);

        // then
        verify(userRepository, atLeastOnce()).deleteById(1L);
    }

    @Test
    @DisplayName("회원 삭제 실패")
    void withdrawFail() {
        // given
        given(userRepository.existsById(1L)).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> userService.withdraw(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no such user");
    }

}