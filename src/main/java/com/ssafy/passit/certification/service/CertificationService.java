package com.ssafy.passit.certification.service;

import com.ssafy.passit.certification.dto.Certification;
import com.ssafy.passit.certification.dto.CertificationProblemCount;

import java.util.List;

public interface CertificationService {

    /**
     * 자격증 ID로 해당 자격증 이름 찾음
     * @param certId
     * @return
     */
    String findCertificationName(Long certId);

    /**
     * 모든 자격증에 대한 정보 조회
     * @return
     */
    List<Certification> findAllCertifications();

    /**
     * 각 자격증별 문제 개수 구하여 리턴
     * @return
     */
    List<CertificationProblemCount> findAllCertificationProblemCount();
}
