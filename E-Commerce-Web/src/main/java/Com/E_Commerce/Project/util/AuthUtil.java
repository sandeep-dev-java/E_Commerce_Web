package Com.E_Commerce.Project.util;

import Com.E_Commerce.Project.Repository.UserRepository;
import Com.E_Commerce.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthUtil {
    @Autowired
    UserRepository userRepository;
    public  String loggedInEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserName(authentication.getName()).
                orElseThrow(()
                ->new UsernameNotFoundException("user Not Found Exception"));
        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserName(authentication.getName()).
                orElseThrow(()-> new UsernameNotFoundException("username Not Found "));

        return  user;
    }
    public  String loggedInUserName(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUserName(authentication.getName()).
                orElseThrow(()-> new UsernameNotFoundException("User Not Found Exception"));
        return  user.getUserName();
    }
}
