package cn.mrcode.newstudy.hpbase._12.myniorector;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2018/06/15     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2018/6/15 13:52
 * @date 2018/6/15 13:52
 * @since 1.0.0
 */
public class TelnetHandler extends MyIOHandler {
    public TelnetHandler(SelectionKey sk) throws IOException {
        super(sk);
    }

    @Override
    protected void onConnected() throws IOException {
        this.write(("1: find keyword in files\r\n2: quit" +
                "\r\nTelnet>").getBytes());
    }
}
