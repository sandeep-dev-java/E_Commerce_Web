package Com.E_Commerce.Project.controller;

import Com.E_Commerce.Project.DTO.MessageResponse;
import Com.E_Commerce.Project.Repository.RoleRepository;
import Com.E_Commerce.Project.Repository.UserRepository;
import Com.E_Commerce.Project.Security.Services.UserDetailsImpl;
import Com.E_Commerce.Project.Security.jwt.JwtUtils;

import Com.E_Commerce.Project.Security.request.LoginRequest;
import Com.E_Commerce.Project.Security.request.SignupRequest;
import Com.E_Commerce.Project.Security.response.UserInfoResponse;
import Com.E_Commerce.Project.model.Role;
import Com.E_Commerce.Project.model.User;
import Com.E_Commerce.Project.model.User_Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
     @Autowired
     RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUserName(signupRequest.getUserName())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error : username is already Exist ! "));
    }
    if(userRepository.existsByEmail(signupRequest.getEmail()))
        {
        return  ResponseEntity.badRequest().body(new MessageResponse("Error : Email is already Exist ! "));
        }

        User user= new User(
                signupRequest.getEmail(),
                signupRequest.getUserName(),
                 encoder.encode(signupRequest.getPassword())
        );
       Set<String> strRoles =signupRequest.getRole();
       Set<Role> roles= new HashSet<>();
       if(strRoles==null){
        Role userRole= roleRepository.findByRole(User_Role.Role_User)
                .orElseThrow(()-> new  RuntimeException("Error : Role is Not Found ! "));
            roles.add(userRole);
       }else {
           strRoles.forEach(role -> {
               switch (role) {
                   case "admin":
                       Role adminRole = roleRepository.findByRole(User_Role.Role_Admin)
                               .orElseThrow(() -> new RuntimeException("Error : Role is Not Found ! "));
                       roles.add(adminRole);
                       break;
                   case "seller":
                       Role sellerRole = roleRepository.findByRole(User_Role.Role_Seller)
                               .orElseThrow(() -> new RuntimeException("Error : Role is Not Found ! "));
                       roles.add(sellerRole);
                       break;
                   default:
                       Role userRole = roleRepository.findByRole(User_Role.Role_User)
                               .orElseThrow(() -> new RuntimeException("Error : Role is Not Found ! "));
                       roles.add(userRole);
               }


           });

       }
        user.setRoles(roles);
        userRepository.save(user);
        return  ResponseEntity.ok().body(new MessageResponse("User successfully Registerd"));
    }
}
