package sec.project.domain;
// account
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long> {

    @Column(unique = true)
    private String username;
    private String password;
    private boolean isAdmin;

    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
    	return posts;
    }

    public void setPosts(List<Post> posts) {
    	this.posts = posts;
    }
    
    public void setAdmin() {
        this.isAdmin = true;
    }
    
    public void removeAdmin() {
        this.isAdmin = false;
    }
    
    public boolean isAdmin() {
        return this.isAdmin;
    }
    
    public String getRole() {
        return isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
    }

}
