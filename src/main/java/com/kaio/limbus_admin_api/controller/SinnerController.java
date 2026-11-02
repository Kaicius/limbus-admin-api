package com.kaio.limbus_admin_api.controller;

import com.kaio.limbus_admin_api.entity.Sinner;
import com.kaio.limbus_admin_api.repository.SinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sinners")
@RequiredArgsConstructor
public class SinnerController {

    private final SinnerRepository sinnerRepository;

    @GetMapping
    public List<Sinner> findAll() {
        return sinnerRepository.findAll();
    }
}