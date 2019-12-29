package sec.project.controller;
// account control
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Controller
public class AccountController {

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String addAccount(@RequestParam String username, @RequestParam String password) {
        Account account = new Account();
        account.setUsername(username);

        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/change", method = RequestMethod.PATCH)
    public String changePassword(Authentication auth, @RequestParam String password) {
        String name = auth.getName();
        Account account = accountRepository.findByUsername(name);
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
        return "redirect:/main";
    }
    
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable long id) {
        accountRepository.delete(id);
        return "redirect:/admin";
    }
    
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.POST)
    public String makeAdmin(@PathVariable long id) {
        Account account = accountRepository.findOne(id);
        account.setAdmin();
        accountRepository.save(account);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PATCH)
    public String removeAdmin(@PathVariable long id) {
        Account account = accountRepository.findOne(id);
        account.removeAdmin();
        accountRepository.save(account);
        return "redirect:/admin";
    }
}
