package com.tenglv.gate.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {


    private static final int FILESIZE = 4 * 1024;

    public static String getCrashLogsDir(Context context) {
        return new File(context.getExternalFilesDir(""), "logs/crash").getAbsolutePath();
    }


    public static String getCropPicturesDir(Context context) {
        File file = new File(context.getExternalFilesDir(""), "cropImages");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    public static String getPictureSaveDir(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    public static String getDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }


    /**
     * 获取文件大小
     *
     * @param filePath :文件的路径
     * @return
     */
    public static String getFileSize(String filePath) {
        String fileSize = "";
        File targetFile = new File(filePath);
        if (targetFile.exists() && targetFile.isFile()) {
            long size = targetFile.length();
            fileSize = formetFileSize(size);
        }
        return fileSize;
    }

    /**
     * 格式化文件大小 * @param fileS，文件的路径
     *
     * @return 文件大小的字符串
     */
    public static String formetFileSize(long fileSize) {
        String result = "";
        if ((fileSize / 1024) >= 1) {
            fileSize /= 1024;
            if (fileSize / 1024 >= 1) {
                result = new BigDecimal(fileSize * 1.0 / 1024).setScale(2,
                        BigDecimal.ROUND_HALF_UP).floatValue()
                        + " MB";
            } else {
                result = fileSize + " kb";
            }
        } else {
            result = fileSize + " B";
        }
        return result;
    }

    /**
     * 计算视频文件内容的播放时间
     *
     * @param mPlayer   MediaPlayer类型
     * @param videoPath 文件路径
     * @return 0:0:00样式
     */
    public static String getVideoFileTime(MediaPlayer mPlayer, String videoPath) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(videoPath);
                mPlayer.prepare();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int v_time = mPlayer.getDuration() / 1000;
        if (v_time < 10) {
            return "0:0" + v_time;
        } else if (v_time < 60) {
            return "0:" + v_time;
        } else {
            return null;
        }
    }


    /**
     * 获取文件的文件类型
     *
     * @param name
     * @param regex
     * @return String
     */
    public String getFileType(String name, String regex) {
        String[] temps = name.split(regex);
        return temps[temps.length - 1];
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


    public static void clearDir(String path) {
        clearDir(new File(path));
    }

    public static void clearDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();

            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    clearDir(files[i]);
                }
            }
            file.delete();
        }

    }


    /**
     * 将InputStream写入到SD卡
     *
     * @param path
     * @param input
     * @return
     */
    public File write2SDFromInput(String path, InputStream input) {
        File file = null;
        OutputStream output = null;
        int a = 1;
        try {
            file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];
            while ((a = input.read(buffer)) != -1) {
                output.write(buffer, 0, a);
                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static long getAvailableStore(String path) {
        // 取得sdcard文件路径
        StatFs statFs = new StatFs(path);
        // 获取block的SIZE
        long blocSize = statFs.getBlockSize();
        // 可使用的Block的数目
        long availaBlock = statFs.getAvailableBlocks();
        long availableSpare = availaBlock * blocSize;

        return availableSpare;
    }

    public static String getDirSize(String Dir1, String Dir2) {
        String result = "";
        long size = 0;
        File file1 = new File(Dir1);
        if (file1 != null && file1.exists()) {
            for (File temp : file1.listFiles()) {
                if (temp != null && temp.isFile()) {
                    size += temp.length();
                }
            }
        }
        File file2 = new File(Dir2);
        if (file2 != null && file2.exists()) {
            for (File temp : file2.listFiles()) {
                if (temp != null && temp.isFile()) {
                    size += temp.length();
                }
            }
        }
        result = StringUtils.getStringBySize(size);
        return result;
    }

    public static String getDirSize(String Dir) {
        String result = "";
        long size = 0;
        File file = new File(Dir);
        if (file != null && file.exists()) {
            for (File temp : file.listFiles()) {
                if (temp != null && temp.isFile()) {
                    size += temp.length();
                }
            }
        }

        result = StringUtils.getStringBySize(size);
        return result;
    }

    /**
     * 文件复制
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(File sourceFile, File targetFile) {
        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            input = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[FILESIZE];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                inBuff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outBuff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 流复制
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copyStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[4096];
        int n;
        while ((n = in.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
        out.close();
    }


    // 用当前时间给取得的图片命名
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(date) + ".jpg";
    }


    // 用当前时间给取得的下载的安装包名
    public static String getDownloadFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return "weibo_" + dateFormat.format(date) + ".apk";
    }

    /**
     * 判断 指定路径 有没有剩余空间
     *
     * @param sizeKb
     * @return
     */
    public static boolean isAvaiableSpace(int sizeKb) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            Log.d("isAvaiableSpace ", "path=" + sdcard);
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = (blocks * blockSize) / 1024;
            Log.d("isAvaiableSpace ", "availableSpare=" + availableSpare);
            if (availableSpare > sizeKb) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

//    public static boolean writeFile2Disk(ResponseBody body, String filePath, String fileName) {
//        return writeFile2Disk(body, new File(filePath, fileName));
//    }
//
//    public static boolean writeFile2Disk(ResponseBody body, File file) {
//
//        try {
//            InputStream in = null;
//            OutputStream out = null;
//
//            try {
//
//                long fileSize = body.contentLength();
//                Logger.i("FileSize:" + fileSize);
//
//                if (fileSize <= 0) {
//                    return false;
//                }
//
//                if (file.exists() && file.isFile()) {
//                    Logger.i("file is exist!");
//                    file.delete();
//
//                }
//
//                out = new FileOutputStream(file);
//                byte[] buffer = new byte[4096];
//
//
//                long fileSizeDownloaded = 0;
//
//                in = body.byteStream();
//                while (true) {
//                    int read = in.read(buffer);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    out.write(buffer, 0, read);
//
//                    fileSizeDownloaded += read;
//                }
//                out.flush();
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//                try {
//                    if (in != null) {
//                        in.close();
//                    }
//
//                    if (out != null) {
//                        out.close();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 将字节数组保存在指定文件中
     *
     * @param pathname 需要保存的文件路径
     * @param buffer   需要被保存的内容
     * @param append   是否采用追加的方式
     * @return
     */
    public static int saveFile(String pathname, byte[] buffer, boolean append) {
        String path = null;
        pathname = pathname.replaceAll("\\\\", "/");
        path = pathname.substring(0, pathname.lastIndexOf("/"));
        File dir = new File(path);
        if (!dir.isDirectory())
            dir.mkdirs();// 创建不存在的目录
        try {
            // writeln the file to the file specified
            OutputStream bos = new FileOutputStream(pathname, append);
            bos.write(buffer);
            bos.close();
        } catch (FileNotFoundException fnfe) {
            return -1;
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }

    public static File createFile(String filename) throws Exception {
        File file = FileUtils.newFile(filename);
        if (!file.canWrite()) {
            String dirName = file.getPath();
            int i = dirName.lastIndexOf(File.separator);
            if (i > -1) {
                dirName = dirName.substring(0, i);
                File dir = FileUtils.newFile(dirName);
                dir.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    public static File newFile(String pathName) throws Exception {
        try {
            return new File(pathName).getCanonicalFile();
        } catch (IOException e) {
            throw new Exception("创建文件失败", e);
        }
    }

    /**
     * 解压缩文件
     *
     * @param inFileName  输入需要解压缩的文件的全路径
     * @param outFilePath 输出解压缩完的文件的路径
     * @return
     */
    public static void uncoilZIP(String inFileName, String outFilePath)
            throws Exception {
        File file = new File(outFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        int BUFFER = 2048;
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        ZipInputStream zip = null;
        ZipEntry entry = null;

        try {
            zip = new ZipInputStream(new FileInputStream(inFileName));
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(outFilePath + File.separator + entry.getName())
                            .mkdirs();
                    continue;
                }

                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(outFilePath
                        + File.separator + entry.getName());
                while ((count = zip.read(data, 0, BUFFER)) != -1) {
                    fos.write(data, 0, count);
                }

                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 列举某个目录下的所有文件列表（含子目录）
     *
     * @param dir
     * @return
     */
    public static List<File> listAllSubFiles(File dir) {
        List<File> ret = new ArrayList<File>();
        listAllFiles(dir, ret);
        return ret;
    }

    /**
     * 列举某个目录下的所有文件列表（含子目录）
     *
     * @param dir
     * @param ret
     * @return
     */
    public static void listAllFiles(File dir, List<File> ret) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File aFile : files) {
                if (aFile.isDirectory()) {
                    listAllFiles(aFile, ret);
                } else {
                    ret.add(aFile);
                }
            }
        } else {
            ret.add(dir);
        }
    }

    /**
     * 删除文件夹里面的所有文件以及子文件夹
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFiles(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File f : dir.listFiles()) {
            delFile(f);
        }
    }

    /**
     * 删除指定文件，如果是文件则直接删除；如果是目录，递归遍历删除所有文件再删除该目录
     *
     * @param p
     */
    public static void delFile(File p) {
        if (p.isFile()) {
            p.delete();
        }
        if (p.isDirectory()) {
            File[] files = p.listFiles();
            for (File file : files) {
                delFile(file);
            }
            p.delete();
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static boolean copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                    // temp.renameTo(arg0)
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String 如：c:/old/
     * @param newPath String 如：d:/new/
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delAllFiles(oldPath);
    }

    /**
     * 读取文件文本
     *
     * @param fileName 文件名
     * @return 文件文本 @
     */
    public static String getFileTxt(String fileName) {
        String txt = null;
        try {
            FileInputStream fs = new FileInputStream(fileName);

            byte buff[] = new byte[fs.available()];
            fs.read(buff);
            String s = new String(buff, "utf-8");
            txt = s.trim();
            fs.close();
        } catch (Exception e) {
        }
        return txt;
    }

    private static AtomicInteger atomic = new AtomicInteger(0);

    private static String getNextInteger() {
        int i = atomic.getAndIncrement();
        if (i > 98)
            atomic.set(0);
        String str = "000" + i;
        return str.substring(str.length() - 3, str.length());
    }

    public static String createFileNameByTime(String ext) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String filename = df.format(new Date());
        String suffix = getNextInteger();

        filename += suffix;
        if (!ext.startsWith(".")) {
            ext = "." + ext;
        }
        filename += ext;
        return filename;
    }

    /**
     * 功能描述：复制单个文件，如果目标文件存在，则不覆盖
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 返回： 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 功能描述：复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @param coverlay     如果目标文件已存在，是否覆盖
     * @return 返回： 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName,
                                        String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                if (!delFile(descFileName)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文件复制到指定文件夹内
     *
     * @param filePath    源文件路径
     * @param descDirName 目标文件夹
     * @retuern boolean true:成功 false:失败
     */
    public static boolean copyFileToDirectory(String filePath,
                                              String descDirName) {
        File src = new File(filePath);
        if (!src.isFile())
            return false;
        File descDir = new File(descDirName);
        if (!descDir.exists()) {
            descDir.getParentFile().mkdirs();
        }
        return copyFileCover(src.getAbsolutePath(), descDirName
                + File.separator + src.getName(), true);
    }

    /**
     * 功能描述：复制整个目录的内容，如果目标目录存在，则不覆盖
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @return 返回： 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return copyDirectoryCover(srcDirName, descDirName, true);
    }

    /**
     * 功能描述：复制整个目录的内容
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @param coverlay    如果目标目录存在，是否覆盖
     * @return 返回： 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName,
                                             String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        if (!descDirName.endsWith(File.separator)) {
            descDirName = descDirName + File.separator;
        }
        File descDir = new File(descDirName);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                if (!delFile(descDirName)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            // 创建目标目录
            if (!descDir.mkdirs()) {
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(), descDirName
                        + files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(), descDirName
                        + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            return false;
        }
        return true;

    }

    /**
     * 功能描述：删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 返回： 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 功能描述：删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 返回： 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 功能描述：删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 返回： 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        if (!dirName.endsWith(File.separator)) {
            dirName = dirName + File.separator;
        }
        File dirFile = new File(dirName);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 功能描述：创建目录
     *
     * @param descDirName 目录名,包含路径
     * @return 返回： 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        if (!descDirName.endsWith(File.separator)) {
            descDirName = descDirName + File.separator;
        }
        File descDir = new File(descDirName);
        if (descDir.exists()) {
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            return true;
        } else {
            return false;
        }

    }

}
