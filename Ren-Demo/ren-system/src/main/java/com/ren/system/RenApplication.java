package com.ren.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//如果每一个Mqpper接口上都标注了@Mapper注解，这里则不需要指定Mapper包所在路径
//@MapperScan("com.ren.securitydemo.mapper")
public class RenApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功   ლ(´ڡ`ლ)ﾞ \n" +
                "                 ,----.  .-._               \n" +
                "  .-.,.---.   ,-.--` , \\/==/ \\  .-._      \n" +
                " /==/  `   \\ |==|-  _.-`|==|, \\/ /, /     \n" +
                "|==|-, .=., ||==|   `.-.|==|-  \\|  |       \n" +
                "|==|   '='  /==/_ ,    /|==| ,  | -|        \n" +
                "|==|- ,   .'|==|    .-' |==| -   _ |        \n" +
                "|==|_  . ,'.|==|_  ,`-._|==|  /\\ , |       \n" +
                "/==/  /\\ ,  )==/ ,     //==/, | |- |       \n" +
                "`--`-`--`--'`--`-----`` `--`./  `--` ");
    }

}
