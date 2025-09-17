package com.houkunlin.system.common.aop.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 被标记文件下载资源类的具体数据内容
 *
 * @author HouKunLin
 */
@Getter
@AllArgsConstructor
public class DownloadFileModelMetadata implements Serializable {
    /**
     * 文件名
     */
    private final String filename;
    /**
     * 文件源。AOP中需要再次进一步判断此值的类型做后续处理
     */
    private final Object source;

    public Object getSource() {
        if (source == null) {
            return DownloadFileAspect.EMPTY_BYTE_ARRAY;
        }
        return source;
    }
}
