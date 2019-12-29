package sec.project.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Post;
import sec.project.repository.AccountRepository;
import sec.project.repository.PostRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        // Creating two accounts here for testing
        Account account = new Account();
        account.setUsername("blank");
        account.setPassword(encoder.encode("pass"));
        accountRepository.save(account);
        
        account = new Account();
        account.setUsername("dash");
        account.setPassword(encoder.encode("word"));
        accountRepository.save(account);
        
        account = new Account();
        account.setUsername("admin");
        account.setPassword(encoder.encode("admin"));
        account.setAdmin();
        accountRepository.save(account);
        
        Post post = new Post();
        post.setTitle("Hello World!");
        post.setContent("keep calm & hack this page !");
        post.setAccount(accountRepository.findByUsername("blank"));
        postRepository.save(post);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority(account.getRole())));
    }
}
