package com.karonda.restapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
		// 如果 spring boot 的配置有问题，会报错
		// 所以，如果没有任何测试用例时，写这么个空的也是好过没有
		// 如果有了其他有具体内容的测试用例，这个空测试用例就没存在的必要了
	}

}
