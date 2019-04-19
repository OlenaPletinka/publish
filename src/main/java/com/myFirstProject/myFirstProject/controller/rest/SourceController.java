package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.SourceReq;
import com.myFirstProject.myFirstProject.dto.SourceResp;
import com.myFirstProject.myFirstProject.exception.SourceNotValidForSaveException;
import com.myFirstProject.myFirstProject.exception.SourceNotValidForUpdateException;
import com.myFirstProject.myFirstProject.repository.SourceRepository;
import com.myFirstProject.myFirstProject.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/source/")
public class SourceController {
    @Autowired
    private SourceService sourceService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveSource (@RequestBody SourceReq sourceReq) throws SourceNotValidForSaveException, JsonProcessingException {
        sourceValidForSave(sourceReq);
        Long id = sourceService.saveSource(sourceReq);
        SourceResp sourceResp = new SourceResp();
        sourceResp.setId(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(sourceResp));

    }

    private void sourceValidForSave(SourceReq sourceReq) throws SourceNotValidForSaveException {
        if(sourceReq.getId()!=null||sourceReq.getName()==null||sourceReq.getName().isEmpty()){
            throw new SourceNotValidForSaveException("Source  is not valid for save");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById (@PathVariable Long id) throws JsonProcessingException {
        SourceResp sourceResp = sourceService.getById(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(sourceResp));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteById (@PathVariable Long id){
        sourceService.deleteById(id);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update (@RequestBody SourceReq sourceReq) throws JsonProcessingException {
        sourceValidForUpdate(sourceReq);
        SourceResp sourceResp = sourceService.update(sourceReq);

        return ResponseEntity.ok(objectMapper.writeValueAsString(sourceResp));
    }

    private void sourceValidForUpdate(SourceReq sourceReq) {
        if(sourceReq.getId()==null||sourceReq.getName()==null||sourceReq.getName().isEmpty()){
            throw new SourceNotValidForUpdateException("Source  is not valid for update");
        }
    }

}
