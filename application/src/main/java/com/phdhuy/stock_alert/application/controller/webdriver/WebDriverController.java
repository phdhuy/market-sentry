package com.phdhuy.stock_alert.application.controller.webdriver;

import com.phdhuy.stock_alert.application.dto.response.webdriver.WebDriverAliveResponse;
import com.phdhuy.stock_alert.shared.config.WebDriverConfig;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/webdriver")
@RequiredArgsConstructor
@Tag(name = "Webdriver APIs")
public class WebDriverController {

  private final WebDriverConfig webDriverConfig;

  @GetMapping("/status")
  public ResponseEntity<ResponseDataAPI> checkWebDriverStatus() {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            WebDriverAliveResponse.builder().isAlive(webDriverConfig.isWebDriverAlive()).build()));
  }

  @PostMapping("/stop")
  public ResponseEntity<ResponseDataAPI> stopWebDriver() {
    webDriverConfig.quitWebDriver();
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}
