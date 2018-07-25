/**
 * 
 */
package com.moons.xst.track.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.moons.xst.track.AppConst;

/**
 * ZIP压缩类
 * 
 * @author LKZ
 * 
 */
public class ZipUtils {

	/**
	 * 压缩文件夹
	 * 
	 * @param srcFilePath
	 *            将压缩的文件夹路径
	 * @param zipFilePath
	 *            zip文件路径
	 * @throws Exception
	 */
	public static boolean zipFolder(String srcFilePath, String zipFilePath)
			throws Exception {

		FileOutputStream fs = new FileOutputStream(zipFilePath);
		// 创建Zip包
		ZipOutputStream outZip = new java.util.zip.ZipOutputStream(fs);		
		// 打开要输出的文件
		java.io.File file = new File(srcFilePath);
		// 压缩
		boolean bet = zipFiles(file.getPath(), outZip);
		// 完成,关闭		
		outZip.finish();
		outZip.close();
		fs.close();
		return bet;
	}

	private static void zipFiles(String folderPath, String filePath,
			ZipOutputStream zipOut) throws Exception {
		if (zipOut == null) {
			return;
		}
		java.io.File file = new java.io.File(folderPath + filePath);
		// 判断是不是文件
		if (file.isFile()) {
			java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(
					filePath);
			java.io.FileInputStream inputStream = new java.io.FileInputStream(
					file);
			zipOut.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[100000];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}
			inputStream.close();
			zipOut.closeEntry();
		} else {
			// 文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();
			// 如果没有子文件, 则添加进去即可
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(filePath + File.separator);
				zipOut.putNextEntry(zipEntry);
				zipOut.closeEntry();
			}
			// 如果有子文件, 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				zipFiles(folderPath, filePath + java.io.File.separator
						+ fileList[i], zipOut);
			}
		}
	}

	private static boolean zipFiles(String folderPath, ZipOutputStream zipOut) {
		try {

			if (zipOut == null) {
				return false;
			}
			java.io.File file = new java.io.File(folderPath);
			// 文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();
			// 如果没有子文件
			if (fileList.length <= 0) {
				return false;
			}
			// 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(
						fileList[i]);
				java.io.FileInputStream inputStream = new java.io.FileInputStream(
						file + File.separator + fileList[i]);
				zipOut.putNextEntry(zipEntry);
				int len;
				byte[] buffer = new byte[100000];
				while ((len = inputStream.read(buffer)) != -1) {
					zipOut.write(buffer, 0, len);
				}
				inputStream.close();
				zipOut.closeEntry();
			}
			//zipOut.close();
			return true;
		} catch (Exception e) {
			try {
				FileUtils.SaveToFile(AppConst.XSTBasePath(), "log.txt", "\n\n\n----------"+DateTimeHelper.GetDateTimeNow()+"----------"+e.toString(), true);
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			return false;
		}
	}

	public static void zip(String inputFileName,String zipFileName) throws Exception {
      zip(zipFileName, new File(inputFileName));
	}
	
	private static void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.close();
    }
	
	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
           File[] fl = f.listFiles();
           out.putNextEntry(new ZipEntry(base + "/"));
           base = base.length() == 0 ? "" : base + "/";
           for (int i = 0; i < fl.length; i++) {
           zip(out, fl[i], base + fl[i].getName());
         }
        }else {
           out.putNextEntry(new ZipEntry(base));
           FileInputStream in = new FileInputStream(f);
           int b;
           while ( (b = in.read()) != -1) {
            out.write(b);
         }
         in.close();
       }
    }
	
}
