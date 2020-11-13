package com.pentasecurity.edge.controller;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pentasecurity.edge.model.DataHistory;
import com.pentasecurity.edge.model.DataInfoAndHistory;
import com.pentasecurity.edge.model.entity.History;
import com.pentasecurity.edge.model.entity.Master;
import com.pentasecurity.edge.model.response.ApiResponse;
import com.pentasecurity.edge.repository.HistoryRepository;
import com.pentasecurity.edge.repository.MasterRepository;

@Controller
@RequestMapping("/api/gw")
public class ApiController {
    Logger logger = LoggerFactory.getLogger("mainLogger");

    @Value("${edge.storage-path}")
    private String storagePath;

    @Autowired
    MasterRepository masterRepository;
    @Autowired
    HistoryRepository historyRepository;

    @PostMapping("/upload")
    @ResponseBody
    public ApiResponse upload(@RequestBody DataInfoAndHistory dataInfo) {
    	ApiResponse apiResponse = new ApiResponse(-99, "error");

    	try {
    		Master master = new Master(dataInfo);
    		History history = new History(dataInfo);
    		masterRepository.save(master);
    		historyRepository.save(history);

    		File file = new File(storagePath+"/"+dataInfo.getDataId()+".data");

			FileUtils.forceMkdirParent(file);
			FileUtils.writeStringToFile(file, dataInfo.getData(), "UTF-8");

        	apiResponse.setCode(0);
        	apiResponse.setMessage("OK");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return apiResponse;
    }

    @PostMapping("/history")
    @ResponseBody
    public ApiResponse history(@RequestBody DataHistory dataHistory) {
    	ApiResponse apiResponse = new ApiResponse(-99, "error");

    	try {
    		History history = new History(dataHistory);
    		historyRepository.save(history);

        	apiResponse.setCode(0);
        	apiResponse.setMessage("OK");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return apiResponse;
    }
}