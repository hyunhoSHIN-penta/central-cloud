package com.pentasecurity.edge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class DataInfo extends BaseModel {
	String dataId;
    String dataFormat;
    String deviceId;
    long createTime;
    String data;
}