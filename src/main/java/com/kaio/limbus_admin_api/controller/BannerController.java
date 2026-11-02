package com.kaio.limbus_admin_api.controller;

import com.kaio.limbus_admin_api.dto.BannerRequest;
import com.kaio.limbus_admin_api.dto.BannerResponse;
import com.kaio.limbus_admin_api.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<BannerResponse> create(@RequestBody @Valid BannerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.create(req));
    }

    @GetMapping
    public List<BannerResponse> all() {
        return bannerService.all();
    }

    @GetMapping("/{id}")
    public BannerResponse findId(@PathVariable Long id) {
        return bannerService.findId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerResponse> update(@PathVariable Long id, @RequestBody @Valid BannerRequest req) {
        return ResponseEntity.ok(bannerService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.ok().build();
    }
}
