package com.phdhuy.stock_alert.application.controller.user;

import com.phdhuy.stock_alert.application.mapper.UserDTOMapper;
import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.ports.inbound.FindUserByIdUseCase;
import com.phdhuy.stock_alert.infrastructure.security.domain.UserPrincipal;
import com.phdhuy.stock_alert.shared.annotation.CurrentUser;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User APIs")
public class UserController {

  private final FindUserByIdUseCase findUserByIdUseCase;

  private final UserDTOMapper userDTOMapper;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyInfo(@CurrentUser UserPrincipal userPrincipal) {
    User user = findUserByIdUseCase.findById(userPrincipal.getId());
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(userDTOMapper.toUserInfoResponse(user)));
  }
}
