package com.kaio.limbus_admin_api.controller;

import com.kaio.limbus_admin_api.dto.EgoRequest;
import com.kaio.limbus_admin_api.dto.EgoResponse;
import com.kaio.limbus_admin_api.service.EgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("egos")
@RequiredArgsConstructor
public class EgoController {

    private final EgoService egoService;

    @PostMapping
    public ResponseEntity<EgoResponse> create(@RequestBody @Valid EgoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(egoService.create(req));
    }

    @GetMapping
    public List<EgoResponse> all() {
        return egoService.all();
    }

    @GetMapping("/{id}")
    public EgoResponse findId(@PathVariable Long id) {
        return egoService.findId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EgoResponse> update(@PathVariable Long id, @RequestBody @Valid EgoRequest req) {
        return ResponseEntity.ok(egoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        egoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
