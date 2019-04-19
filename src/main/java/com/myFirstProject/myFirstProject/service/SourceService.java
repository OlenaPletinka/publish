package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.SourceReq;
import com.myFirstProject.myFirstProject.dto.SourceResp;

public interface SourceService {
    Long saveSource(SourceReq sourceReq);
    SourceResp getById(Long id);
    void deleteById(Long id);
    SourceResp update(SourceReq sourceReq);
}
