package Backend.controller;

import Backend.config.JwtUtils;
import Backend.dto.LoginUser;
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
import java.util.List;
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
//
//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//    String jwt = jwtUtils.generateJwtToken(authentication);
//    List<String> roles = userDetails.getAuthorities().stream()
//            .map(item -> item.getAuthority())
//            .collect(Collectors.toList());
    List<String> roles = new ArrayList<>();
    roles.add("user");
    return ResponseEntity.ok(new JwtResponse("234l2ioj45", 35123441L, "thanh123", "ngocthanh123@gmail.com", roles));
  }
}