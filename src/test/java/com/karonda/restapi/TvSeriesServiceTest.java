package com.karonda.restapi;

import com.karonda.restapi.dao.TvCharacterDao;
import com.karonda.restapi.dao.TvSeriesDao;
import com.karonda.restapi.pojo.TvSeries;
import com.karonda.restapi.service.TvSeriesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TvSeriesServiceTest {

    @MockBean
    TvSeriesDao tvSeriesDao;
    @MockBean
    TvCharacterDao tvCharacterDao;

    @Autowired
    TvSeriesService tvSeriesService;
    
    @Test
    public void testGetAll() {

        List<TvSeries> list = new ArrayList<>();
        TvSeries ts = new TvSeries();
        ts.setName("POI");
        list.add(ts);
        
        // 告诉 mock 当执行 getAll 方法时，返回上面创建的 list
        Mockito.when(tvSeriesDao.getAll()).thenReturn(list);

        List<TvSeries> result = tvSeriesService.getAllTvSeries();

        Assert.assertTrue(result.size() == list.size());
        Assert.assertTrue("POI".equals(result.get(0).getName()));   
    }
    
    @Test
    public void testGetOne() {
        //根据不同的传入参数，被 mock 的 bean 返回不同的数据的例子
        String newName = "Person Of Interest";
        BitSet mockExecuted = new BitSet();
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                TvSeries bean = (TvSeries) args[0];
                //传入的值，应该和下面调用 tvSeriesService.updateTvSeries() 方法时的参数中的值相同
                Assert.assertEquals(newName, bean.getName());
                mockExecuted.set(0);
                return 1;
            }
        }).when(tvSeriesDao).update(any(TvSeries.class));
        
        TvSeries ts = new TvSeries();
        ts.setName(newName);
        ts.setId(111);
        
        tvSeriesService.updateTvSeries(ts);
        Assert.assertTrue(mockExecuted.get(0));
    }
}
