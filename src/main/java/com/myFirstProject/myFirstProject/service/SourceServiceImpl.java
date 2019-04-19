package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterServiceImpl;
import com.myFirstProject.myFirstProject.converter.RespConverterServiceImpl;
import com.myFirstProject.myFirstProject.dto.SourceReq;
import com.myFirstProject.myFirstProject.dto.SourceResp;
import com.myFirstProject.myFirstProject.exception.SourceNotExistException;
import com.myFirstProject.myFirstProject.exception.SourceNotFoundException;
import com.myFirstProject.myFirstProject.model.Source;
import com.myFirstProject.myFirstProject.repository.SourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SourceServiceImpl implements SourceService{

    private Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Autowired
    private ReqConverterServiceImpl reqConverterService;

    @Autowired
    private RespConverterServiceImpl respConverterService;

    @Autowired
    private SourceRepository sourceRepository;

    @Transactional
    @Override
    public Long saveSource(SourceReq sourceReq) {
        logger.info("Get source to update {}", sourceReq);
        Source source = reqConverterService.convert(sourceReq);
        sourceRepository.save(source);
        Long id = source.getId();
        logger.info("Source saved with id {}", id);

        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public SourceResp getById(Long id) {
        Optional <Source> findById = sourceRepository.findById(id);
        return findById.map(source -> {
            SourceResp sourceResp = respConverterService.convertSourceToResp(findById.get());
            return sourceResp;
        }).orElseThrow(() -> new SourceNotFoundException(String.format("Source with id %d is not found", id)));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (sourceRepository.existsById(id)){
            logger.info("Delete category with id {}", id);
            sourceRepository.deleteById(id);
            logger.info("Source with id {} was delete ", id);
        }


    }

    @Transactional
    @Override
    public SourceResp update(SourceReq sourceReq) {
        if (sourceRepository.existsById(sourceReq.getId())){
            logger.info("Update source {}", sourceReq);
            Source source = reqConverterService.convert(sourceReq);
            Source savedSource = (Source) sourceRepository.save(source);
            Long id = savedSource.getId();
            logger.info("Source with id {} was update", id);

            return respConverterService.convertSourceToResp(savedSource);

        }else {
            throw new SourceNotExistException(String.format("Source with id %d don't exist", sourceReq.getId()));
        }

    }
}
