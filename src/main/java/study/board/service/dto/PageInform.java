package study.board.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import study.board.entity.Post;
import study.board.service.enumeration.SearchType;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class PageInform {
    List<PostSummaryInform> posts;
    int page;
    int total;
    int start;
    int end;
    SearchType type;
    String keyword;

    public PageInform(Page<Post> page, SearchType type, String keyword) {
        this.page = page.getNumber();
        this.total = page.getTotalPages();
        this.posts = page.getContent()
                .stream()
                .map(post -> new PostSummaryInform(post))
                .collect(Collectors.toList());

        int size = page.getSize();
        this.start = this.page - this.page % size;
        if (start + size < total) {
            this.end = start + size - 1;
        } else if (total - 1 > 0) {
            this.end = total - 1;
        } else {
            this.end = start;
        }

        this.type = type;
        this.keyword = keyword;
    }
}
