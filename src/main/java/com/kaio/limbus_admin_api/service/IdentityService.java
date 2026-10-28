package com.kaio.limbus_admin_api.service;

import com.kaio.limbus_admin_api.dto.IdentityRequest;
import com.kaio.limbus_admin_api.dto.IdentityResponse;
import com.kaio.limbus_admin_api.entity.Identity;
import com.kaio.limbus_admin_api.entity.Sinner;
import com.kaio.limbus_admin_api.expection.ResourceNotFoundException;
import com.kaio.limbus_admin_api.repository.IdentityRepository;
import com.kaio.limbus_admin_api.repository.SinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final IdentityRepository identityRepository;
    private final SinnerRepository sinnerRepository;

    public IdentityResponse create(IdentityRequest req) {
        Sinner sinner = sinnerRepository.findById(req.sinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sinner " + req.sinnerId() + " não encontrado"));

        Identity identity = new Identity();
        identity.setName(req.name());
        identity.setRarity(req.rarity());
        identity.setSinner(sinner);
        identity.setUptie1ArtUrl(req.uptie1ArtUrl());
        identity.setUptie3ArtUrl(req.uptie3ArtUrl());
        identity.setUptieCutscene(req.uptieCutscene());
        identity.setObtainmentVideoUrl(req.obtainmentVideoUrl());

        return IdentityResponse.from(identityRepository.save(identity));
    }

    public List<IdentityResponse> all() {
        return identityRepository.findAll().stream()
                .map(IdentityResponse::from)
                .toList();
    }

    public IdentityResponse findId(Long id) {
       Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));
       return IdentityResponse.from(identity);
    }

    public IdentityResponse update(Long id, IdentityRequest req) {
        Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));
        Sinner sinner = sinnerRepository.findById(req.sinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sinner " + req.sinnerId() + " não encontrado"));

        identity.setName(req.name());
        identity.setRarity(req.rarity());
        identity.setSinner(sinner);
        identity.setUptie1ArtUrl(req.uptie1ArtUrl());
        identity.setUptie3ArtUrl(req.uptie3ArtUrl());
        identity.setUptieCutscene(req.uptieCutscene());
        identity.setObtainmentVideoUrl(req.obtainmentVideoUrl());

        return IdentityResponse.from(identityRepository.save(identity));
    }

    public void delete(Long id) {
        Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));

        identityRepository.delete(identity);
    }
}
