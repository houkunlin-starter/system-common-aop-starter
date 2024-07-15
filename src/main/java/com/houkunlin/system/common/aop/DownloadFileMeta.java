package com.houkunlin.system.common.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息数据
 *
 * @author HouKunLin
 */
@Data
@AllArgsConstructor
public class DownloadFileMeta implements Serializable {
    /**
     * 文件名
     */
    private final String filename;
    /**
     * 文件源
     */
    private final Object source;
}
