package sec.project.controller;
// Main controller
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.repository.AccountRepository;

@Controller
public class MainViewController {

    @Autowired
    private AccountRepository accountRepository;
    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/main";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginMapping(Principal principal) {
        return "login";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
        }
        return "signup";
    }

    @RequestMapping(value = "/newpost", method = RequestMethod.GET)
    public String newPostMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
        }
        return "newPost";
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editPostMapping() {
        return "editPost";
    }
    
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public String changeMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
            return "change";
        }
        return "redirect:/main";
    }
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
}
