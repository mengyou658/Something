package com.moons.xst.track.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: HTML相关的正则表达式工具类
 * Description: 包括过滤HTML标记，转换HTML标记，替换特定HTML标记
 * Copyright: Copyright (c) 2006
 * @author gaojun
 * @version 1.0
 * @createtime 2014-9-16
 */
public class HtmlRegexpUtils {
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签

	private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

	public HtmlRegexpUtils() {}

	/**
	 * 基本功能：替换标记以正常显示
	 * @param input
	 * @return String
	 */
	public String replaceTag(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * 基本功能：判断标记是否存在
	 * @param input
	 * @return boolean
	 */
	public boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 基本功能：过滤所有以"<"开头以">"结尾的标签
	 * @param str
	 * @return String
	 */
	public static String filterHtml(String str) {
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 基本功能：过滤指定标签
	 * @param str
	 * @param tag 指定标签
	 * @return String
	 */
	public static String fiterHtmlTag(String str, String tag) {
		String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
		Pattern pattern = Pattern.compile(regxp);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 基本功能：替换指定的标签
	 * @param str
	 * @param beforeTag 要替换的标签
	 * @param tagAttrib 要替换的标签属性值
	 * @param startTag 新标签开始标记
	 * @param endTag 新标签结束标记
	 * @return String
	 * @如：替换img标签的src属性值为[img]属性值[/img]
	 */
	public static String replaceHtmlTag(String str, String beforeTag,
			String tagAttrib, String startTag, String endTag) {
		String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
		String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
		Pattern patternForTag = Pattern.compile(regxpForTag);
		Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
		Matcher matcherForTag = patternForTag.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		while (result) {
			StringBuffer sbreplace = new StringBuffer();
			Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag
					.group(1));
			if (matcherForAttrib.find()) {
				matcherForAttrib.appendReplacement(sbreplace, startTag
						+ matcherForAttrib.group(1) + endTag);
			}
			matcherForTag.appendReplacement(sb, sbreplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 从URL中获取文件名（供文件下载用）
	 * @author LKZ
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
        String filename = "";
        boolean isok = false;
        // 从UrlConnection中获取文件名称
        try {
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            if (conn == null) {
                return null;
            }
            Map<String, List<String>> hf = conn.getHeaderFields();
            if (hf == null) {
                return null;
            }
            Set<String> key = hf.keySet();
            if (key == null) {
                return null;
            }
            for (String skey : key) {
                List<String> values = hf.get(skey);
                for (String value : values) {
                    String result;
                    try {
                        result = new String(value.getBytes("ISO-8859-1"), "GBK");
                        int location = result.indexOf("filename");
                        if (location >= 0) {
                            result = result.substring(location
                                    + "filename".length());
                            filename = result
                                    .substring(result.indexOf("=") + 1);
                            isok = true;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }// ISO-8859-1 UTF-8 gb2312
                }
                if (isok) {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 从路径中获取
        if (filename == null || "".equals(filename)) {
            filename = url.substring(url.lastIndexOf("/") + 1);
        }
        return filename;
    }
}
