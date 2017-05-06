package com.melonl.msexplorer.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/*
 使用Intent打开文件
 void openFile(Context par1,File par2);

 获取内置sd卡路径
 String getInnerSdcardPath(void);

 获取文件名后缀
 String getFilePrefix(File par1)

 获取文件的MINE类型
 String getMIMEType(File par1)

 删除文件或文件夹
 int deleteFile(File par1)
 文件不存在返回-1
 删除成功返回0
 代码已执行但文件未删除返回-2

 复制单个文件
 int copyAFile(File 源文件, File 目标文件)
 复制成功返回0
 源文件不存在返回-1
 源文件不可读-2
 目标文件不可写-3
 代码已执行但没有复制-4
 复制后不完整-5

 复制文件或文件夹
 int copyFile(File 源文件, File 目标文件)
 复制成功返回0
 源文件不存在返回-1
 文件夹创建失败-2

 获取目录下所有文件
 ArrayList<File> getFileList(String 路径)
 返回File类型的ArrayList

 获取文件或文件夹大小
 long getFileSize(File 目标文件)
 返回大小，单位B

 根据文件大小转化为 合适单位并带上单位 的字符串
 String getSizeStr(long 单位为字节的文件大小)
 返回自动适配单位的字符串


 */

public class FileUtil {
    private final static String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".txt", "text/plain"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".smali", "text/plain"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".js", "application/x-javascript"},
            {".json", "text/plain"},
            {".lua", "text/plain"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".avi", "video/avi"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
    };
    public static int buffSize = 131072;//缓冲区大小，默认128k，单位:b

    //传入路径获得路径下所有文件的File对象并作为List传回
    public static ArrayList<File> getFileList(String path) {
        File f = new File(path);
        File[] allFileList = f.listFiles();
        ArrayList<File> list = new ArrayList<File>(Arrays.asList(allFileList));

        return customSort(list);
    }

    public static ArrayList<File> customSort(ArrayList<File> list) {
        Collections.sort(list);//排序
        Collections.sort(list, new IgnoreCase());
        Collections.sort(list, new SortByType());//按文件类型排序
        return list;
    }

    //打开文件,自动匹配MIME类型
    public static void openFile(Context c, File file) {
        c.startActivity(new Intent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setAction(Intent.ACTION_VIEW)
                .setDataAndType(Uri.fromFile(file), getMIMEType(file)));

		/*
         Intent intent = new Intent();
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 //设置intent的Action属性
		 intent.setAction(Intent.ACTION_VIEW);
		 //获取文件file的MIME类型
		 String type = getMIMEType(file);
		 //设置intent的data和Type属性。
		 intent.setDataAndType(Uri.fromFile(file), type);
		 //跳转
		 c.startActivity(intent);
		 */

    }

    //获取文件后缀
    public static String getFileSuffix(File f) {

        String prefix = "";
        prefix = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        prefix = "." + prefix;
        return prefix;


    }

    public static String getMIMEType(File f) {
        String type = "*/*";
        String prefix = getFileSuffix(f);
        if (prefix.equals(".")) return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (prefix.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;

    }

    //删除文件或文件夹
    public static int deleteFile(File f) {
        if (!f.exists()) return -1;//文件不存在返回-1

        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (fileArray.length != 0) {
                for (File file : fileArray) {
                    if (file.isFile()) {
                        file.delete();
                    } else if (file.isDirectory()) {
                        deleteFile(file);
                    }
                    //file.delete();
                }

            } else {
                f.delete();
            }
            f.delete();

        } else if (f.isFile()) {
            f.delete();
        }

        if (!f.exists()) return 0;//已删除

        return -2;//代码已执行但是文件并未删除
    }

    //获取sd卡路径
    public static String getInnerSdcardPath() {
        return Environment.getExternalStorageDirectory().getPath();

    }

    public static String getExternalSdCardPath() {

        String parent = getInnerSdcardPath();
        parent = parent.substring(0, parent.lastIndexOf("/"));
        File f = new File(parent);
        String[] path = parent.split("/");

        File[] allFiles = new File[path.length];
        for (int i = 0; i < path.length; i++) {
            DebugUtil.printLog("for1,f:" + f.getAbsolutePath());
            allFiles[i] = f;
            f = f.getParentFile();
        }
        for (File file : allFiles) {
            if (!file.canRead()) {
                DebugUtil.printLog("can't read:" + file.getAbsolutePath());
                continue;
            }
            File[] fs = file.listFiles();
            for (File tmp : fs) {
                if (tmp.getAbsolutePath().equals(getInnerSdcardPath())) {
                    continue;
                }
                DebugUtil.printLog("for2-1,tmp:" + tmp.getAbsolutePath());
                if (new File(tmp.getAbsolutePath() + "/Android/data").exists()) {
                    DebugUtil.printLog("found extsd path:" + tmp);
                    return tmp.getAbsolutePath();
                }
            }
        }

		/*
		 for (int i=0;i < path.length;i++)
		 {
		 String str = path[i];
		 if (str.contains("storage"))
		 {
		 String ext = getInnerSdcardPath().substring(0, getInnerSdcardPath().indexOf("storage") + 7);
		 DebugUtil.printLog("ext:" + ext);
		 File[] files = new File(ext).listFiles();
		 for (File f : files)
		 {
		 DebugUtil.printLog("f:" + f.getAbsoluteFile());
		 if (f.canRead() && new File(f.getAbsolutePath() + "/Android/data").exists())
		 {
		 return f.getAbsolutePath();
		 }

		 }

		 }
		 }
		 */
        return null;
    }

    public static int copyAFile(String source, String target) {
        return copyAFile(new File(source), new File(target));
    }

    public static String getExternalStoragePathApi22() {
        Map sysInfo = System.getenv();
        //获取外置的sd卡
        String extSD = sysInfo.get("SECONDARY_STORAGE").toString();
        return extSD;
    }

    //复制单个文件
    public static int copyAFile(File source, File target) {
        if (!source.exists()) {
            return -1;//源文件不存在返回-1
        } else if (!source.canRead()) {
            return -2;//源文件不可读返回-2
        } else if (!target.getParentFile().canWrite()) {
            return -3;//目标目录不可写返回-3
        }

        //init stream
        FileInputStream fis;
        FileOutputStream fos;
        byte[] buffer;
        try {
            buffer = new byte[buffSize];
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }

            fis.close();
            fos.close();
            buffer = null;
            if (!target.exists()) {
                return -4;//未知原因文件复制失败
            } else if (source.length() != target.length()) {
                return -5;//文件复制不完整
            }


        } catch (IOException err) {
            err.printStackTrace();
        }

        return 0;//文件复制成功
    }

    //复制文件或文件夹
    public static int copyFile(File source, File target) {
        if (!source.exists()) {
            return -1;//源文件不存在返回-1
        }
        if (source.isDirectory()) {
            if (target.exists() && target.isDirectory()) {
                int result = copySeconedDirs(source, target);
                if (result != 0) {
                    return result;
                }
            } else {
                boolean result2 = target.mkdir();
                if (!result2) {
                    return -2;//文件夹创建失败，返回-2
                }
                int result = copySeconedDirs(source, target);
                if (result != 0) {
                    return result;
                }
            }
        } else {
            int result;
            //移除已存在文件
            if (target.exists()) {
                deleteFile(target);
            }
            result = copyAFile(source, target);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
	
	/*
	public static String getExternalStoragePath()
	{
		return Environment.MEDIA_MOUNTED;
	}
	*/

    //为提高效率分离的二级方法
    private static int copySeconedDirs(File source, File target) {
        if (source.isDirectory()) {
            File[] filelist = source.listFiles();
            if (filelist.length == 0) {
                return 0;//表示目录下无文件，已复制完毕
            } else {
                for (File f : filelist) {
                    File secTargetFile = new File(target.getAbsolutePath() + File.separator + f.getName());
                    if (!f.isDirectory()) {
                        if (secTargetFile.exists() && !secTargetFile.isDirectory()) {
                            secTargetFile.delete();
                        }
                        copyAFile(f, secTargetFile);
                    } else {
                        if (secTargetFile.exists() && secTargetFile.isDirectory()) {
                            copySeconedDirs(f, secTargetFile);
                        } else {
                            boolean result = secTargetFile.mkdirs();
                            if (!result) {
                                return -2;//文件夹创建失败
                            }
                            copySeconedDirs(f, secTargetFile);
                        }
                    }
                }
            }
        } else {
            return -1;//不是文件夹
        }
        return 0;
    }

    //移动文件或文件夹
    public static int moveFile(File source, File target) {
        if (!source.exists()) {
            return -1;//源文件不存在返回-1
        }
        if (source.isDirectory()) {
            if (target.exists() && target.isDirectory()) {
                int result = moveSeconedDirs(source, target);
                if (result != 0) {
                    return result;
                }
            } else {
                boolean result2 = target.mkdir();
                if (!result2) {
                    return -2;//文件夹创建失败，返回-2
                }
                int result = moveSeconedDirs(source, target);
                if (result != 0) {
                    return result;
                }
            }
        } else {
            int result = moveAFile(source, target);
            if (result != 0) {
                return result;
            }
        }
        deleteFile(source);
        return 0;
    }

    private static int moveSeconedDirs(File source, File target) {
        if (source.isDirectory()) {
            File[] filelist = source.listFiles();
            if (filelist.length == 0) {
                return 0;//表示目录下无文件，已复制完毕
            } else {
                for (File f : filelist) {
                    File secTargetFile = new File(target.getAbsolutePath() + File.separator + f.getName());
                    if (!f.isDirectory()) {
                        if (secTargetFile.exists() && !secTargetFile.isDirectory()) {
                            secTargetFile.delete();
                        }
                        moveAFile(f, secTargetFile);
                    } else {
                        if (secTargetFile.exists() && secTargetFile.isDirectory()) {
                            moveSeconedDirs(f, secTargetFile);
                        } else {
                            boolean result = secTargetFile.mkdirs();
                            if (!result) {
                                return -2;//文件夹创建失败
                            }
                            moveSeconedDirs(f, secTargetFile);
                        }
                    }
                }
            }
        } else {
            return -1;//不是文件夹
        }
        return 0;
    }

    //移动单个文件
    public static int moveAFile(File source, File target) {
        if (!source.exists()) {
            return -1;//源文件不存在返回-1
        } else if (!source.canRead()) {
            return -2;//源文件不可读返回-2
        } else if (!target.getParentFile().canWrite()) {
            return -3;//目标目录不可写返回-3
        }

        if (source.renameTo(target)) {
            deleteFile(source);
            return 0;//移动成功

        } else {
            return -4;//移动失败
        }


    }

    //获取 文件/文件夹 大小
    public static double getFileSize(File f) {
        if (!f.exists()) return -1;//文件不存在返回-1
        double size = 0;
        if (f.isDirectory()) {
            size = size + getFolderSize(f);
        } else {
            size = size + f.length();
        }
        return size;
    }

    //获取文件夹大小
    private static long getFolderSize(File f) {
        if (!f.exists()) return -1;
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                //递归
                size = size + getFolderSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }

        return size;
    }

    public static String getSizeStr(double size) {
        String sizestr = "";
        //单位处理
        if (size < 1024) {
            int i = (int) size;
            sizestr = i + "B";
        } else if (size > 1024 && size < 1024000) {

            sizestr = (size / 1024) + "";
            BigDecimal b = new BigDecimal(sizestr);
            b = b.setScale(2, BigDecimal.ROUND_HALF_UP);

            sizestr = b.toString() + "KB";
        } else if (size > 1024000 && size < 1024000000) {
            sizestr = (size / 1024000) + "";
            BigDecimal b = new BigDecimal(sizestr);
            b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
            sizestr = b.toString() + "MB";
        } else if (size > 1024000000) {
            sizestr = (size / 1024000000) + "";
            BigDecimal b = new BigDecimal(sizestr);
            b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
            sizestr = b.toString() + "GB";
        }


        return sizestr;
    }

    public static String getSizeStr(File f) {
        return getSizeStr(getFileSize(f));
    }

    public static long getFileCount(File f) {
        long count = 0;
        if (!f.isDirectory()) {
            return -1;
        }

        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                count += getFileCount(file);
            } else {
                ++count;
            }
        }
        return count;

    }

    //根据文件类型排序(文件夹或者文件)
    public static class SortByType implements Comparator {
        @Override
        public int compare(Object obj1, Object obj2) {
            File f1 = (File) obj1;
            File f2 = (File) obj2;

            if (f1.isDirectory() && f2.isDirectory()) return 0;
            if (!f1.isDirectory() && !f2.isDirectory()) return 0;

            if (f1.isDirectory()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    static class IgnoreCase implements Comparator {
        public int compare(Object s1, Object s2) {
            File f1 = (File) s1;
            File f2 = (File) s2;
            return f1.getName().compareToIgnoreCase(f2.getName());
        }

    }


}
