package com.ppsoft1991.chatViewTool.utils;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class WechatDb {
    private static final int IV_SIZE = 16;
    private static final int DEFAULT_PAGE_SIZE = 4096;
    private static final byte[] PE_HEADER = "SQLite format 3\0".getBytes(StandardCharsets.US_ASCII);

    private final byte[] salt = new byte[16];
    private final byte[] mac_salt = new byte[16];
    private final byte[] WECHAT_KEY;
    private final byte[] keyBytes;
    private final Mac HmacSHA1;
    private final byte[] iV = new byte[16];
    private final String inputFile;
    private final String outputFile;

    public WechatDb(String dbPass, String dbFile) throws Exception {
        System.out.println("[+] 开始解密: " + dbFile);
        WECHAT_KEY = CommonUtils.getWechatKey(dbPass);
        inputFile = dbFile;
        outputFile = dbFile + "_dec.db";

        try (FileInputStream in = new FileInputStream(dbFile)) {
            in.read(salt);
            for (int i = 0; i < mac_salt.length; ++i) {
                mac_salt[i] = (byte) (salt[i] ^ 0x3A);
            }

            PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA1Digest());
            generator.init(WECHAT_KEY, salt, 64000);
            keyBytes = ((KeyParameter) generator.generateDerivedParameters(256)).getKey();

            PBEParametersGenerator generatorSha1 = new PKCS5S2ParametersGenerator(new SHA1Digest());
            generatorSha1.init(keyBytes, mac_salt, 2);
            byte[] ctxCode = ((KeyParameter) generatorSha1.generateDerivedParameters(256)).getKey();
            SecretKeySpec signingKey = new SecretKeySpec(ctxCode, "HmacSHA1");
            HmacSHA1 = Mac.getInstance("HmacSHA1");
            HmacSHA1.init(signingKey);
        }

        File outputF = new File(outputFile);
        if (outputF.exists()) {
            System.out.println("[+] 删除原先的解密文件" + outputFile);
            outputF.delete();
        }
    }

    private Cipher initCipher(byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec decKey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, decKey, new IvParameterSpec(iv));
        return cipher;
    }

    public void decryptDb() throws Exception {
        byte[] page = new byte[DEFAULT_PAGE_SIZE];
        byte[] data1 = new byte[4032];
        byte[] padding = new byte[48];

        try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(Paths.get(inputFile)));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile, true))) {

            in.skip(16); // skip salt

            // 读取第一页数据
            readFully(in, data1);
            readFully(in, padding);
            System.arraycopy(padding, 0, iV, 0, iV.length);

            // 解密第一页数据
            Cipher cipher = initCipher(iV);
            data1 = cipher.doFinal(data1);
            out.write(PE_HEADER);
            out.write(data1);
            out.write(padding);

            // 处理后续页面
            while (in.read(page) != -1) {
                byte[] data2 = Arrays.copyOfRange(page, 0, 4048);
                System.arraycopy(page, 4048, iV, 0, iV.length);

                // 解密后续页面数据
                cipher = initCipher(iV);
                data2 = cipher.doFinal(data2);
                byte[] pagePadding = Arrays.copyOfRange(page, 4048, 4096);

                out.write(data2);
                out.write(pagePadding);
            }
        }
    }

    private void readFully(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int offset = 0;
        while (offset < buffer.length && (bytesRead = in.read(buffer, offset, buffer.length - offset)) != -1) {
            offset += bytesRead;
        }
        if (offset < buffer.length) {
            throw new EOFException("Reached end of stream before reading fully.");
        }
    }
}
