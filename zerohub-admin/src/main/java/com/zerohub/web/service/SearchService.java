package com.zerohub.web.service;


import com.zerohub.web.domain.FileInfoDTO;

import java.io.IOException;
import java.util.List;

/**
 * es 查询
 *
 * @author zhangyu
 * @date 2022/10/10 10:13
 */
public interface SearchService {

    /**
     * 根据全文内容进行详尽搜索
     *
     * @return
     */
    List<FileInfoDTO> searchByContent(String content) throws IOException;

    /**
     * 根据文章标题进行模糊搜索
     *
     * @return
     */
    List<FileInfoDTO> searchByFilename(String filename) throws IOException;

    List<FileInfoDTO> searchBySomething(String something) throws IOException;
}
