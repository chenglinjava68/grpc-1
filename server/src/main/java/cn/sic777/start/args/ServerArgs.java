package cn.sic777.start.args;

/**
 * Created by Zhengzhenxie on 2017/9/8.
 */
public class ServerArgs {
    public static String CONFIG_PATH;

    /**
     * 初始化参数
     *
     * @param args
     */
    public static void load(String args[]) {
        for (int ix = 0, len = args.length; ix < len; ix++) {
            String param = args[ix];
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            switch (key.trim()) {
                case "-config":
                    CONFIG_PATH = value;
                    break;
            }

        }
    }
}
