package com.ssafy.passit.certification.repository;

import com.ssafy.passit.certification.dto.Certification;
import com.ssafy.passit.certification.dto.CertificationProblemCount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CertificationRepository {

    String findCertificationNameByCertId(Long certId);

    List<Certification> findAllCertifications();

    List<CertificationProblemCount> findAllCertificationProblemCount();

    Long findCertificationCount();
}
