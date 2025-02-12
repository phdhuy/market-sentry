package com.phdhuy.stock_alert.application.controller.notification;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import com.phdhuy.stock_alert.domain.notification.port.inbound.NotificationUseCase;
import com.phdhuy.stock_alert.infrastructure.security.domain.UserPrincipal;
import com.phdhuy.stock_alert.shared.annotation.CurrentUser;
import com.phdhuy.stock_alert.shared.payload.general.PageInfo;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import com.phdhuy.stock_alert.shared.utils.PagingUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification APIs")
public class NotificationController {

  private final NotificationUseCase notificationUseCase;

  @GetMapping("/count-unread")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> countUnreadNotification(
      @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            notificationUseCase.countUnreadNotification(userPrincipal.getId())));
  }

  @PostMapping("/{notificationId}/mark-read")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> markReadNotification(
      @CurrentUser UserPrincipal userPrincipal, @PathVariable UUID notificationId) {
    notificationUseCase.markReadNotification(notificationId, userPrincipal.getId());
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  @GetMapping()
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyNotification(
      @RequestParam(name = "sort", defaultValue = "createdAt") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging,
      @CurrentUser UserPrincipal userPrincipal) {
    Pageable pageable = PagingUtils.makePageRequestWithSnakeCase(sortBy, order, page, paging);

    Page<Notification> notifications =
        notificationUseCase.getMyNotification(pageable, userPrincipal.getId());

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1,
            notifications.getTotalPages(),
            notifications.getTotalElements());

    return ResponseEntity.ok(
        ResponseDataAPI.success(notifications.getContent().stream().toList(), pageInfo));
  }

  @GetMapping("/{notificationId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getDetailNotification(
      @PathVariable UUID notificationId, @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            notificationUseCase.getDetailNotification(notificationId, userPrincipal.getId())));
  }
}
