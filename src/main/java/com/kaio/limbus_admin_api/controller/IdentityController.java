package com.kaio.limbus_admin_api.controller;

import com.kaio.limbus_admin_api.dto.IdentityRequest;
import com.kaio.limbus_admin_api.dto.IdentityResponse;
import com.kaio.limbus_admin_api.service.IdentityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class IdentityController {

    private final IdentityService identityService;

    @PostMapping
    public ResponseEntity<IdentityResponse> create(@RequestBody @Valid IdentityRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(identityService.create(req));
    }

    @GetMapping
    public List<IdentityResponse> all() {
        return identityService.all();
    }

    @GetMapping("/{id}")
    public IdentityResponse findId(@PathVariable Long id) {
        return identityService.findId(id);
    }
}
