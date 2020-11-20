package com.pentasecurity.edge.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.pentasecurity.edge.model.DataInfo;
import com.pentasecurity.edge.model.response.DataUseApiResponse;
import com.pentasecurity.edge.util.DataUtil;
import com.pentasecurity.edge.util.HttpUtil;

@Service
public class EdgeDataService
{
	Logger logger = LoggerFactory.getLogger("mainLogger");

    @Value("${edge.device-id}")
    private String deviceId;
    @Value("${edge.node-list}")
    private String nodeList;

    private String[] nodes = null;
    private String recentDataId = null;
    private int recentNodeNo = 0;

    @PostConstruct
    public void init() {
    	if ( !StringUtils.isEmpty(nodeList) ) {
        	nodes = nodeList.split(",");
    	} else {
    		nodes = new String[0];
    	}
    }

	public void createData() {
		try {
        	if ( nodes.length > 0 ) {
        		logger.debug(String.format("%10s %10s %5s %10s", deviceId, "create", "data", ""));

    			String data = DataUtil.make(100);
    			DataInfo dataInfo = new DataInfo(deviceId, data);

    			int nodeNo = (int)Math.floor(Math.random()*nodes.length);
        		String node = nodes[nodeNo];

        		HttpUtil.post(node+"/api/edge/upload", dataInfo.toJson());

        		recentDataId = dataInfo.getDataId();
        		recentNodeNo = nodeNo;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadData() {
		try {
			String node = nodes[(int)Math.floor(Math.random()*nodes.length)];
			HashMap<String, String> requestBody = new HashMap<String, String>();
    		Gson gson = new Gson();

    		requestBody.put("deviceId", deviceId);

        	String resonseBody = HttpUtil.post(node+"/api/edge/download", gson.toJson(requestBody));
        	DataUseApiResponse response = DataUseApiResponse.fromJson(resonseBody, DataUseApiResponse.class);

        	logger.debug(String.format("%10s %10s %5s %10s", deviceId, "download", "data", response.toJson()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteData() {
		try {
        	if ( !StringUtils.isEmpty(recentDataId) ) {
        		logger.debug(String.format("%10s %10s %5s %10s", deviceId, "delete", "data", ""));

    			DataInfo dataInfo = new DataInfo(recentDataId, deviceId, "");

        		String node = nodes[recentNodeNo];

        		HttpUtil.post(node+"/api/edge/delete", dataInfo.toJson());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}