package com.ppsoft1991.chatViewTool;

public class GlobalConfig {
    public static boolean isLinux = true;
    public static String VERSION = "1.0";
    public static String RESOURCE = "/com/ppsoft1991/chatViewTool/";

    static {
        if ((System.getProperty("os.name")).toLowerCase().startsWith("win")){
            isLinux = false;
        }
    }

}
