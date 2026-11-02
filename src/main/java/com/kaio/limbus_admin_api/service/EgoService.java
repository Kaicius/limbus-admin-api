package com.kaio.limbus_admin_api.service;

import com.kaio.limbus_admin_api.dto.EgoRequest;
import com.kaio.limbus_admin_api.dto.EgoResponse;
import com.kaio.limbus_admin_api.entity.Ego;
import com.kaio.limbus_admin_api.entity.Sinner;
import com.kaio.limbus_admin_api.expection.ResourceNotFoundException;
import com.kaio.limbus_admin_api.repository.EgoRepository;
import com.kaio.limbus_admin_api.repository.SinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EgoService {

    private final EgoRepository egoRepository;
    private final SinnerRepository sinnerRepository;

    public EgoResponse create(EgoRequest req) {
        Sinner sinner = sinnerRepository.findById(req.sinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sinner " + req.sinnerId() + " não encontrado"));

        Ego ego = new Ego();
        ego.setName(req.name());
        ego.setSinner(sinner);
        ego.setArtUrl(req.artUrl());
        ego.setObtainmentVideoUrl(req.obtainmentVideoUrl());

        return EgoResponse.from(egoRepository.save(ego));
    }

    public List<EgoResponse> all() {
        return egoRepository.findAll().stream()
                .map(EgoResponse::from)
                .toList();
    }

    public EgoResponse findId(Long id) {
        Ego ego = egoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));
        return EgoResponse.from(ego);
    }

    public EgoResponse update(Long id, EgoRequest req) {
        Ego ego = egoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));
        Sinner sinner = sinnerRepository.findById(req.sinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sinner " + req.sinnerId() + " não encontrado"));

        ego.setName(req.name());
        ego.setSinner(sinner);
        ego.setArtUrl(req.artUrl());
        ego.setObtainmentVideoUrl(req.obtainmentVideoUrl());

        return EgoResponse.from(egoRepository.save(ego));
    }

    public void delete(Long id) {
        Ego ego = egoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identity " + id + " não encontrada"));

        egoRepository.delete(ego);
    }
}
