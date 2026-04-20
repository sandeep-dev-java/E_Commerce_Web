package Com.E_Commerce.Project.Security.Services;

import Com.E_Commerce.Project.Repository.UserRepository;
import Com.E_Commerce.Project.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDeatilsServiceImpl implements UserDetailsService {
   @Autowired
   UserRepository userRepository;


   @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= userRepository.findByUserName(username).
               orElseThrow
               (()-> new UsernameNotFoundException("userNotFound"));

       return UserDetailsImpl.build(user);
    }
}
