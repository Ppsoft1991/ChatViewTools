package com.ppsoft1991.chatViewTool.utils;

import com.ppsoft1991.chatViewTool.GlobalConfig;
import com.ppsoft1991.chatViewTool.controller.ChatController;
import com.ppsoft1991.chatViewTool.mapper.microMsg.HeaderIconMapper;
import com.ppsoft1991.chatViewTool.mapper.msg.MsgMapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static HashMap<String,String> WXID_MAP = new HashMap<>();
    public static HashMap<String, String> WXHEADER_MAP = new HashMap<>();
    public static String SELF_WXID;
    public static String SELF_WX_HEAD;

    public static void put(String key, HashMap<String, String> from, HashMap<String, String> to){
        if (from.containsKey(key)){
            to.put(key, from.get(key));
        }
    }

    public static String BLACK_Header = String.valueOf(CommonUtils.class.getResource("/com/ppsoft1991/chatViewTool/ui/black.png"));

    private static final Pattern pattern = Pattern.compile("\\b(.*)?\u001a");

    public static String getWxid(byte[] b){
        Matcher matcher = pattern.matcher(new String(b, StandardCharsets.UTF_8));
        if (matcher.find()) {
            String wxid = matcher.group(1);
            if (wxid!=null){
                return wxid;
            }
        }
        return "";
    }

    public static String getWxName(String wxid){
        String wxName = WXID_MAP.get(wxid);
        if (wxName!=null){
            return wxName;
        }else {
            return wxid;
        }
    }

    public static ImageView selfImage(){
        return ChatController.setImage(SELF_WX_HEAD);
    }

    public static byte[] readByte(ByteBuffer bf, int length) {
        byte[] b = new byte[length];
        b = Arrays.copyOfRange(bf.array(), bf.position(), bf.position() + length);
        bf.position(bf.position() + length);
        return b;
    }

    public static void deleteFile(String f){
        File file = new File(f);
        if (file.exists()){
            boolean delete = file.delete();
            if (delete){
                System.out.println("[+] DeleteFile "+f);
            }else {
                System.out.println("[-] Error,文件可能被占用了! DeleteFile "+f);
            }
        }
    }

    public static byte[] readFromJarToByte(String path) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream fs = CommonUtils.class.getResourceAsStream("/" + path);
        byte[] temp = new byte[4096];
        int len = 0;
        while ((len = fs.read(temp)) != -1) {
            bos.write(temp, 0, len);
        }
        fs.close();
        byte[] data = bos.toByteArray();
        bos.close();
        return data;
    }

    public static Image readImage(String path) throws Exception{
        return new Image(new ByteArrayInputStream(readFromJarToByte(path)));
    }


    public static byte[] getWechatKey(String inKeyPath) {
        try {
            Charset charset = detectEncoding(inKeyPath);
            FileInputStream fi = new FileInputStream(inKeyPath);
            byte[] b = new byte[fi.available()];
            fi.read(b);
            fi.close();

            String fileContent = new String(removeBOM(b, charset),charset);
            return hexStringToByteArray(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public static byte[] hexStringToByteArray(String hexString) {
        // 去掉字符串中的空格和0x前缀
        hexString = hexString.replaceAll("0x", "").replaceAll(",", "").replaceAll("\\s+", "");

        // 计算字节数组的长度
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        // 将每两个字符转换为一个字节
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }

        return byteArray;
    }

    private static byte[] removeBOM(byte[] b, Charset charset) {
        if (charset.name().equals("UTF-8") && b.length >= 3
                && b[0] == (byte) 0xEF && b[1] == (byte) 0xBB && b[2] == (byte) 0xBF) {
            return Arrays.copyOfRange(b, 3, b.length);
        } else if (charset.name().equals("UTF-16LE") && b.length >= 2
                && b[0] == (byte) 0xFF && b[1] == (byte) 0xFE) {
            return Arrays.copyOfRange(b, 2, b.length);
        } else if (charset.name().equals("UTF-16BE") && b.length >= 2
                && b[0] == (byte) 0xFE && b[1] == (byte) 0xFF) {
            return Arrays.copyOfRange(b, 2, b.length);
        }
        return b;
    }

    private static Charset detectEncoding(String filePath) throws IOException {
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(filePath);

        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        fis.close();
        return encoding != null ? Charset.forName(encoding) : Charset.defaultCharset();
    }

    public static String byteArray2Hex(final byte[] hash) {
        final Formatter formatter = new Formatter();
        for (final byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] byteMerge(byte[] b1, byte[] b2) {
        byte[] b3 = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, b3, 0, b1.length);
        System.arraycopy(b2, 0, b3, b1.length, b2.length);
        return b3;
    }

    public static String parseChatStr(String type, MsgMapper data) throws SQLException {
        switch (type){
            case "1":
                return  data.getStrContent();
            case "3":
                return "[图片暂时无法展示]";
            case "49":
                return "[文件暂时无法展示]";
            case "50":
                return "[语音电话]";
            case "47":
                return "[emoji]";
            case "43":
                return "[视频暂时无法展示]";
            case "34":
                return "[wave]"+data.getMsgSvrId();
            case "10000":
                return "[系统消息]->"+data.getStrContent();
            default:
                return "";
        }
    }

    public static String getSilkPath(){
        if (GlobalConfig.isLinux){
            return ((new File("")).getAbsoluteFile()).getAbsolutePath()+ "/voice";
        }else {
            return ((new File("")).getAbsoluteFile()).getAbsolutePath()+ "\\voice.exe";
        }
    }

    public static byte[] getChatSound(String hex) throws Exception {
        return base64Decode(new String(runCmd(getSilkPath(),hex)));
    }

    public static byte[] runCmd(String env, String cmd) throws Exception {
        String[] c = {env, cmd};
        Process p = Runtime.getRuntime().exec(c);
        byte[] b = bufferToByte(p.getInputStream());
        byte[] b1 = bufferToByte(p.getErrorStream());
        return byteMerge(b, b1);
    }

    public static byte[] base64Decode(String bs) {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }
        return value;
    }

    public static String base64Encode(byte[] bs) {
        Class base64;
        String value = "";
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", null).invoke(base64, null);
            value = (String) Encoder.getClass().getMethod("encodeToString", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = (String) Encoder.getClass().getMethod("encode", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }
        return value.replaceAll("[\\s*\t\n\r]", "");
    }
    private static byte[] bufferToByte(InputStream stream) throws IOException {
        int _byte;
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        while ((_byte = stream.read()) != -1) {
            stream1.write(_byte);
        }
        stream1.close();
        return stream1.toByteArray();
    }

    public static String getChatMedia(String absolutePath, String msgSvrId) throws Exception {
        final String sql = "select * from Media where Media.Reserved0 = '"+msgSvrId+"'";
        final ResultSet data = CommonFunc.executeSql(absolutePath + "/MediaMSG_ALL.db", sql);
        if (data.next()){
            String imgPath;
            if (GlobalConfig.isLinux) {
                imgPath = absolutePath + "/.wavcache/" + msgSvrId + ".wav";
            }else {
                imgPath = absolutePath + "\\.wavcache\\" + msgSvrId + ".wav";
            }
            if (!Files.exists(Paths.get(imgPath))) {
                if (!(new File(imgPath).getParentFile().exists())){
                    (new File(imgPath).getParentFile()).mkdirs();
                }
                System.out.println("生成语音文件...");
                byte[] buf = getChatSound("0x" + byteArray2Hex(data.getBytes("Buf")));
                FileOutputStream stream = new FileOutputStream(imgPath);
                stream.write(buf);
                stream.flush();
                stream.close();
            }
            return imgPath;
        }
        return null;
    }

    public static URL getResource(String resource) {
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        URL res = GlobalConfig.class.getResource(GlobalConfig.RESOURCE + resource);
        if (res == null){
            // log.error("getResource error URL:{}", resource);
            res = GlobalConfig.class.getResource(String.format("/%s", resource));
        }
        assert res != null;
        // log.debug("getResource URL:{}, ClassName:{}", res, Config.class.getName());
        return res;
    }

    public static InputStream resource(String resource) {
        if (new File(resource).exists()) {
            try {
                return new FileInputStream(resource);
            } catch (Exception FileNotFoundException) {
            }
        } else {
            if (resource.startsWith("/")) {
                resource = resource.substring(0, resource.length() - 1);
            }
            return CommonUtils.class.getResourceAsStream(GlobalConfig.RESOURCE + resource);
        }
        return null;
    }
}
