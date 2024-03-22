package com.ruoyi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableFeignClients
public class RuoYiApplication {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    /**
     * RestTemplate实例化 实现通过RestTemplate的远程调用
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return restTemplateBuilder.build();
    }

    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        try {
            ConfigurableApplicationContext application = SpringApplication.run(RuoYiApplication.class, args);

            Environment env = application.getEnvironment();
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path="/";
            if(null!=env.getProperty("server.servlet.context-path")){
                path = env.getProperty("server.servlet.context-path");
            }
            String pid = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("PID");

            log.info("(♥◠‿◠)ﾉﾞ  cmall启动成功   ლ(´ڡ`ლ)ﾞ" +
                    "\n----------------------------------------------------------\n\t" +
                    "cmall入口程序启动成功  Access URLs:\n\t" +
                    "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                    "External: \thttp://" + ip + ":" + port + path + "\n\t" +
                    "运行时进程PID: \t"+pid+"\n"+
                    "----------------------------------------------------------");
        } catch (UnknownHostException e) {
            System.out.println("未知主机,启动异常");
        }
    }
}
