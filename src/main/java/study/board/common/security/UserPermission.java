package study.board.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPermission {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String value;
}
