package sec.project.repository;

import java.util.List;
import sec.project.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Account;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAccount(Account account);

}
