package com.houkunlin.system.common.aop.bean;

import com.houkunlin.system.common.aop.annotation.DownloadFileModel;
import com.houkunlin.system.common.aop.annotation.DownloadFileName;
import com.houkunlin.system.common.aop.annotation.DownloadFileObject;
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
