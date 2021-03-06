package com.pentasecurity.edge.model.response;

import java.util.ArrayList;

import com.pentasecurity.edge.model.DataInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class DataUseApiResponse extends ApiResponse {
	public DataUseApiResponse(int code, String message) {
		super(code, message);
		data = new ArrayList<DataInfo>();
	}

	ArrayList<DataInfo> data;
}