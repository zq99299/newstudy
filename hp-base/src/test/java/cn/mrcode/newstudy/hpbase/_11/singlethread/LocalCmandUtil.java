package cn.mrcode.newstudy.hpbase._11.singlethread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Leader.us
 * @author wuzhihui
 */
public class LocalCmandUtil {

    public static String callCmdAndgetResult(String cmd) {
        StringBuilder result = new StringBuilder();
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s"));// 创建进程管理实例
            Process process = pb.start();// 启动进程
            InputStream is = process.getInputStream(); // 获得输入流
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {// 循环读取数据
                result.append(line);
            }
            is.close();
            isr.close();
            br.close();
            // 等待该进程终止
            process.waitFor();
        } catch (Exception e) {// 捕获异常
            result.append(e.toString());
        }
        return result.toString();

    }

}
