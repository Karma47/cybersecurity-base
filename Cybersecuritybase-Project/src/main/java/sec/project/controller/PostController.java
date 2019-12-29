package sec.project.controller;

import java.security.Principal;
import sec.project.repository.PostRepository;
import sec.project.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String list(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("authentication", auth);
            model.addAttribute("posts", postRepository.findAll());
            String name = auth.getName();
            model.addAttribute("username", name);
            return "authMain";
        } else {
            model.addAttribute("posts", postRepository.findAll());
            return "main";
        }
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String accountMapping(Authentication auth, Model model) {
        Account account = accountRepository.findByUsername(auth.getName());
        model.addAttribute("user", account);
        model.addAttribute("username", account.getUsername());
        model.addAttribute("posts", account.getPosts());
        return "account";
    }
    
    @RequestMapping(value = "/newpost", method = RequestMethod.POST)
    public String addPost(@RequestParam String title, @RequestParam String content, Authentication authentication) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAccount(accountRepository.findByUsername(authentication.getName()));
        postRepository.save(post);
        return "redirect:/main";
    }
    
    // Returns the default edit post page
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editPost(Model model, @PathVariable long id) {
        Post post = postRepository.findOne(id);
        model.addAttribute("shouldBeChanged", post);
        return "editpost";
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String updatePost(@RequestParam String content, @PathVariable long id) {
        Post post = postRepository.findOne(id);
        post.setContent(content);
        postRepository.save(post);
        return "redirect:/account";
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.DELETE)
    public String deletePost(@PathVariable long id) {
        postRepository.delete(id);
        return "redirect:/account";
    }
    
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Authentication auth) {
        Account account = accountRepository.findByUsername(auth.getName());
        model.addAttribute("username", account.getUsername());
        model.addAttribute("users", accountRepository.findAll());
        model.addAttribute("posts", postRepository.findAll());
        return "admin";
    }
}
