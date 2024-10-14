package com.phdhuy.stock_alert.application.controller.auth;

import com.phdhuy.stock_alert.domain.auth.ports.inbound.CreateTokenUseCase;
import com.phdhuy.stock_alert.domain.user.ports.inbound.CreateUserUseCase;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.BadRequestException;
import com.phdhuy.stock_alert.shared.exception.UnauthorizedException;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import com.phdhuy.stock_alert.application.request.auth.RefreshTokenRequest;
import com.phdhuy.stock_alert.application.request.auth.SignInRequest;
import com.phdhuy.stock_alert.application.request.auth.SignUpRequest;
import com.phdhuy.stock_alert.infrastructure.security.domain.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth APIs")
public class AuthController {

  private final CreateUserUseCase createUserUseCase;

  private final CreateTokenUseCase createTokenUseCase;

  private final AuthenticationManager authenticationManager;

  @PostMapping("/sign-up")
  public ResponseEntity<ResponseDataAPI> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            createUserUseCase.createUser(
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getConfirmPassword())));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<ResponseDataAPI> signIn(@Valid @RequestBody SignInRequest signInRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getEmail().toLowerCase(), signInRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

      return ResponseEntity.ok(
          ResponseDataAPI.successWithoutMeta(
              createTokenUseCase.createToken(userPrincipal.getId())));
    } catch (BadCredentialsException e) {
      throw new BadRequestException(MessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
    } catch (InternalAuthenticationServiceException e) {
      throw new UnauthorizedException(e.getMessage());
    } catch (DisabledException e) {
      throw new UnauthorizedException(MessageConstant.EMAIL_IS_NOT_VERIFIED);
    } catch (AuthenticationException e) {
      throw new UnauthorizedException(MessageConstant.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<ResponseDataAPI> refreshToken(
      @Valid @RequestBody RefreshTokenRequest refreshToken) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(createTokenUseCase.refreshToken(refreshToken.getRefreshToken())));
  }
}
