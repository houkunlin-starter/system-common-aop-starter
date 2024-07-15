package com.houkunlin.system.common.aop.bean;

import com.houkunlin.system.common.aop.DownloadFileModel;
import com.houkunlin.system.common.aop.DownloadFileName;
import com.houkunlin.system.common.aop.DownloadFileObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@DownloadFileModel
@Data
@AllArgsConstructor
public class DownloadFileBean {
    @DownloadFileName
    private String filename;
    @DownloadFileObject
    private Object file;
}
