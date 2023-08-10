package study.board.service.dto.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import study.board.entity.Post;
import study.board.service.dto.post.PostSummaryInform;

import java.util.List;

@Getter @Setter
public class PageInform {
    List<PostSummaryInform> posts;
    int page;
    int total;
    int start;
    int end;

    public PageInform(Page<Post> page, List<PostSummaryInform> posts) {
        this.page = page.getNumber();
        this.total = page.getTotalPages();
        this.posts = posts;

        int size = page.getSize();
        this.start = this.page - this.page % size;
        if (start + size < total) {
            this.end = start + size - 1;
        } else if (total - 1 > 0) {
            this.end = total - 1;
        } else {
            this.end = start;
        }
    }
}
