package com.xtsoft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import com.xtsoft.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.xtsoft.kernel.util.CharPool;
import com.xtsoft.kernel.util.StreamUtil;
import com.xtsoft.kernel.util.StringPool;



public class FileUtil {

	public static byte[] getBytes(InputStream is) throws IOException {
		return getBytes(is, -1);
	}

	public static byte[] getBytes(InputStream inputStream, int bufferSize) throws IOException {

		return getBytes(inputStream, bufferSize, true);
	}

	public static byte[] getBytes(InputStream inputStream, int bufferSize, boolean cleanUpStream) throws IOException {

		if (inputStream == null) {
			return null;
		}

		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream = new UnsyncByteArrayOutputStream();

		StreamUtil.transfer(inputStream, unsyncByteArrayOutputStream, bufferSize, cleanUpStream);

		return unsyncByteArrayOutputStream.toByteArray();
	}

	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}

		int pos = fileName.lastIndexOf(CharPool.PERIOD);

		if (pos > 0) {
			return fileName.substring(pos + 1, fileName.length()).toLowerCase();
		} else {
			return StringPool.BLANK;
		}
	}

}