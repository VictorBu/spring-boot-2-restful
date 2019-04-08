package com.karonda.restapi.controller;

import com.karonda.restapi.pojo.TvSeries;
import com.karonda.restapi.service.TvSeriesService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {
    private final Log log = LogFactory.getLog(TvSeriesController.class);

    @Value("${tutorial.uploadFolder:target/files}")
    String uploadFolder;

    @Autowired
    TvSeriesService tvSeriesService;
    
    @GetMapping
    public List<TvSeries> getAll() {
        if(log.isTraceEnabled()) {
            log.trace("getAll() ");
        }
        List<TvSeries> list = tvSeriesService.getAllTvSeries();
        
        return list;
    }
    
    @GetMapping("/{id}")
    public TvSeries getOne(@PathVariable int id){
        if(log.isTraceEnabled()) {
            log.trace("getOne " + id);
        }

        TvSeries tvSeries = tvSeriesService.getTvSeriesById(id);

        if(tvSeries == null){
            throw new ResourceNotFoundException();
        }

        return tvSeries;
    }

    @PostMapping
    public TvSeries insertOne(@Valid @RequestBody TvSeries tvSeries) {
        if(log.isTraceEnabled()) {
            log.trace("insertOne 传递进来的参数是：" + tvSeries);
        }

        tvSeriesService.addTvSeries(tvSeries);
        return tvSeries;
    }

    @PutMapping("/{id}")
    public TvSeries updateOne(@PathVariable int id, @RequestBody TvSeries tvSeries){
        if(log.isTraceEnabled()) {
            log.trace("updateOne " + id);
        }

        TvSeries ts = tvSeriesService.getTvSeriesById(id);
        if(ts == null) {
            throw new ResourceNotFoundException();
        }
        ts.setSeasonCount(tvSeries.getSeasonCount());
        ts.setName(tvSeries.getName());
        ts.setOriginRelease(tvSeries.getOriginRelease());
        tvSeriesService.updateTvSeries(ts);
        return ts;
    }
    
    /**
     * @RequestParam(value="delete_reason", required=false) String deleteReason 表示 deleteReason 参数的值
     * 来自 Request 的参数 delete_reason (等同于 request.getParameter("delete_reason")
     * 可以是 URL 中 Querystring ，也可以是 form post 里的值)
     * required=false 表示不是必须的。默认是 required=true，required=true 时，如果请求没有传递这个参数，会被返回400错误
     */
    @DeleteMapping("/{id}")
    public Map<String, String> deleteOne(@PathVariable int id, HttpServletRequest request,
                                         @RequestParam(value="delete_reason", required=false) String deleteReason) throws Exception{
        if(log.isTraceEnabled()) {
            log.trace("deleteOne " + id);
        }
        Map<String, String> result = new HashMap<>();

        TvSeries ts = tvSeriesService.getTvSeriesById(id);
        if(ts != null) {
            throw new ResourceNotFoundException();
        }else {
            tvSeriesService.deleteTvSeries(id, deleteReason);
            result.put("message", "#" + id + "被" + request.getRemoteAddr() + "删除(原因：" + deleteReason + ")");
        }

        return result;
    }
    
    /**
     * 文件上传的例子
     */
    @PostMapping(value="/{id}/photos", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> addPhoto(@PathVariable int id, @RequestParam("photo") MultipartFile imgFile) throws Exception{
        if(log.isTraceEnabled()) {
            log.trace("接受到文件 " + id + "收到文件：" + imgFile.getOriginalFilename());
        }

        //保存文件
        File folder = new File(uploadFolder);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = imgFile.getOriginalFilename();
        if(fileName.endsWith(".jpg")) {
            FileOutputStream fos = new FileOutputStream(new File(folder, fileName));
            IOUtils.copy(imgFile.getInputStream(), fos);
            fos.close();
            Map<String, String> result = new HashMap<>();
            result.put("photo", fileName);
            return result;
        }else {
            throw new RuntimeException("不支持的格式，仅支持jpg格式");
        }
    }
    
    /**
     * 返回非JSON格式（图片）格式的例子
     */
    @GetMapping(value="/{id}/icon", produces= MediaType.IMAGE_JPEG_VALUE)
    public byte[] getIcon(@PathVariable int id) throws Exception{
        if(log.isTraceEnabled()) {
            log.trace("getIcon(" + id + ")");
        }
        String iconFile = "src/test/resources/icon.jpg";
        InputStream is = new FileInputStream(iconFile);
        byte[] data = IOUtils.toByteArray(is);
        is.close();
        return data;
    }
    
}
