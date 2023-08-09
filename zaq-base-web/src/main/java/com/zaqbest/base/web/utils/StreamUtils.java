    /*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.web.utils;

import java.io.*;

/**
 * ***************************************************************************
 * 创建时间 : 2016年8月18日
 * 实现功能 : 流工具类
 * 作者 : yanping.shi
 * 版本 : v0.0.1
 * -----------------------------------------------------------------------------
 * 修改记录:
 * 日 期 版本 修改人 修改内容
 * 2016年8月18日 v0.0.1 yanping.shi 创建
 * ***************************************************************************
 */
public class StreamUtils {

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        String string = null;
        int count = 0;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = null;
        try {
            string = new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) {
        String string = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = null;
        try {
            string = new String(outStream.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] StringTObyte(String in) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(StringTOInputStream(in));
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    /**
     * 将byte数组转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream byteTOInputStream(byte[] in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * 将byte数组转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String byteTOString(byte[] in) {

        InputStream is = null;
        try {
            is = byteTOInputStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InputStreamTOString(is, "UTF-8");
    }

    /**
     * 将byte数组转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String getString(String in) {

        String is = null;
        try {
            is = byteTOString(StringTObyte(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    // InputStream 转换成byte[]
    public byte[] getBytes(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[BUFFER_SIZE];
        int len = 0;

        while ((len = is.read(b, 0, BUFFER_SIZE)) != -1) {
            baos.write(b, 0, len);
        }

        baos.flush();

        byte[] bytes = baos.toByteArray();

        System.out.println(new String(bytes));

        return bytes;
    }

    /**
     * 根据文件路径创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param path
     * @return
     */
    public static FileInputStream getFileInputStream(String filepath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param path
     * @return
     */
    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param file
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return
     */
    public static FileOutputStream getFileOutputStream(File file, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileOutputStream;
    }

    /**
     * 根据文件路径创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param path
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return
     */
    public static FileOutputStream getFileOutputStream(String filepath, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filepath, append);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileOutputStream;
    }

    public static ByteArrayOutputStream getByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    /**
     * ====================================================================
     * 功 能： 将文件转化为byte[]
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年4月27日 v0.0.1 yanping.shi 创建
     * ====================================================================
     */
    public static byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;

    }

    /**
     * ====================================================================
     * 功 能： 将文件转化为byte[]
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年4月27日 v0.0.1 yanping.shi 创建
     * ====================================================================
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;

    }

    /**
     * ====================================================================
     * 功 能： 根据byte数组，生成文件
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年4月27日 v0.0.1 yanping.shi 创建
     * ====================================================================
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() || !dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

}
