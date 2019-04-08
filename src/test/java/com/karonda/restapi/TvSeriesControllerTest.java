package com.karonda.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karonda.restapi.controller.TvSeriesController;
import com.karonda.restapi.dao.TvCharacterDao;
import com.karonda.restapi.dao.TvSeriesDao;
import com.karonda.restapi.pojo.TvCharacter;
import com.karonda.restapi.pojo.TvSeries;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // 初始化一个 mvc 环境用于测试
public class TvSeriesControllerTest {

    @MockBean
    TvSeriesDao tvSeriesDao;
    @MockBean
    TvCharacterDao tvCharacterDao;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private TvSeriesController tvSeriesController;

    @Test
    public void testGetAll() throws Exception {
        List<TvSeries> list = new ArrayList<>();
        TvSeries ts = new TvSeries();
        ts.setName("POI");
        list.add(ts);

        Mockito.when(tvSeriesDao.getAll()).thenReturn(list);

        // 相当于在启动项目后，执行 GET /tvseries，被测模块是 web 控制层，因为 web 控制层会调用业务逻辑层，所以业务逻辑层也会被测试
        // 业务逻辑层调用了被 mock 出来的数据访问层桩模块。
        //如果想仅仅测试 web 控制层，（例如业务逻辑层尚未编码完毕），可以 mock 一个业务逻辑层的桩模块
        mockMvc.perform(MockMvcRequestBuilders.get("/tvseries"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("POI")));
    }
    
    @Test
    public void testAddSeries() throws Exception{
        BitSet bitSet = new BitSet(1);
        bitSet.set(0, false);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                TvSeries ts = (TvSeries) args[0];
                Assert.assertEquals(ts.getName(), "疑犯追踪");
                ts.setId(5432);
                bitSet.set(0, true);
                return 1;
            }
        }).when(tvSeriesDao).insert(Mockito.any(TvSeries.class));

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                TvCharacter tc = (TvCharacter) args[0];
                // json 中传递过来的剧中角色名字
                Assert.assertEquals(tc.getName(), "芬奇");
                Assert.assertTrue(tc.getTvSeriesId() == 5432);
                bitSet.set(0, true);
                return 1;
            }
        }).when(tvCharacterDao).insert(Mockito.any(TvCharacter.class));
        
        String jsonData = "{\"name\":\"疑犯追踪\",\"seasonCount\":5,\"originRelease\":\"2011-09-22\",\"tvCharacters\":[{\"name\":\"芬奇\"}]}";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/tvseries")
                .contentType(MediaType.APPLICATION_JSON).content(jsonData))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertTrue(bitSet.get(0));
    }
    
    @Test
    public void testFileUpload() throws Exception{
        String fileFolder = "target/files/";
        File folder = new File(fileFolder);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        // 设置 bean 里面通过 @Value 获得的数据
        ReflectionTestUtils.setField(tvSeriesController, "uploadFolder", folder.getAbsolutePath());
        
        InputStream is = getClass().getResourceAsStream("/testfileupload.jpg");
        if(is == null) {
            throw new RuntimeException("需要先在src/test/resources目录下放置一张jpg文件，名为testfileupload.jpg然后运行测试");
        }
        
        //模拟一个文件上传的请求
        MockMultipartFile imgFile = new MockMultipartFile("photo", "testfileupload.jpg", "image/jpeg", IOUtils.toByteArray(is));
        
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/tvseries/1/photos")
                        .file(imgFile))
                        .andExpect(MockMvcResultMatchers.status().isOk());
        
        //解析返回的 JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<Map<String, Object>>(){});
       
        String fileName = (String) map.get("photo");
        File f2 = new File(folder, fileName);
        //返回的文件名，应该已经保存在 filFolder 文件夹下
        Assert.assertTrue(f2.exists());
    }
}
