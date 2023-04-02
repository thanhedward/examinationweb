package Backend.controller;

import Backend.config.JwtUtils;
import Backend.dto.LoginUser;
import Backend.entity.User;
import Backend.payload.JwtResponse;
import Backend.service.UserDetailsImpl;
import Backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

  private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  JwtUtils jwtUtils;
  private AuthenticationManager authenticationManager;

  private UserService userService;

  @Autowired
  public AuthenticationController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserService userService) {
    this.jwtUtils = jwtUtils;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  @PostMapping("/signin")
  @Transactional
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginUser loginUser) {

    String username = loginUser.getUsername();
    Optional<User> user= userService.getUserByUsername(username);
    if(!user.isPresent()){
      return ResponseEntity.badRequest().build();
    }
    else if(user.get().isDeleted()==true){
      return ResponseEntity.badRequest().build();
    }

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    User userLog = userService.getUserByUsername(userDetails.getUsername()).get();
    userLog.setLastLoginDate(new Date());
    userService.updateUser(userLog);
    logger.warn(userLog.toString());
    if (userLog.isDeleted() == true) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));

//        List<String> roles = new ArrayList<>();
//    roles.add("ROLE_ADMIN");
//    roles.add("ROLE_STUDENT");
//    roles.add("ROLE_LECTURER");
//    return ResponseEntity.ok(new JwtResponse("234l2ioj45", 35123441L, "thanhtam28ss", "ngocthanh123@gmail.com", roles));
  }
}