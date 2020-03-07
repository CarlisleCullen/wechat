package cn.carlisle.official.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.carlisle.official.accounts.config.YamlFeilConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficialAccountsApplicationTests {
    @Autowired
    YamlFeilConfig config;

    @Test
    public void contextLoads() {
        System.out.println(config);
    }

}
