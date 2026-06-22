package com.ssafy.passit.certification.service;

import com.ssafy.passit.certification.dto.Certification;
import com.ssafy.passit.certification.dto.CertificationProblemCount;
import com.ssafy.passit.certification.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicCertificationService implements CertificationService {

    private final CertificationRepository certificationRepository;

    @Override
    public String findCertificationName(Long certId) {
        return certificationRepository.findCertificationNameByCertId(certId);
    }

    @Override
    public List<Certification> findAllCertifications() {
        return certificationRepository.findAllCertifications();
    }

    @Override
    public List<CertificationProblemCount> findAllCertificationProblemCount() {
        return certificationRepository.findAllCertificationProblemCount();
    }

    @Override
    public Long findCertificationCount() {
        return certificationRepository.findCertificationCount();
    }
}
