package com.csbaic.edatope.file.service;

import com.csbaic.edatope.file.model.FileChunk;

import java.io.IOException;

/**
 * 文件写入策略
 */
public interface ChunkWriteStrategy {

    /**
     * 写入文件
     * @param path 文件路径
     * @param chunk 块数据
     * @return
     */
    int write(String path, FileChunk chunk) throws IOException;
}
