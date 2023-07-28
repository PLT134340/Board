package study.board.service.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    TITLE("제목"), CONTENT("내용"), USERNAME("작성자");

    private final String typeName;

}
