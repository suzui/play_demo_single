package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import play.Logger;

public class OpenIDUtils {
	public static String gen(long userID, long appID) {
		// 补位到12位
		String fillZeros = new DecimalFormat("000000").format(appID) + new DecimalFormat("000000").format(userID);
		Logger.debug("[gen]fillZeros:" + fillZeros);
		// 倒序
		String reverse = StringUtils.reverse(fillZeros);
		Logger.debug("[gen]reverse:" + reverse);
		// 转换
		int reversePart1 = Integer.parseInt(StringUtils.substring(reverse, 0, 3));
		int reversePart2 = Integer.parseInt(StringUtils.substring(reverse, 3, 6));
		int reversePart3 = Integer.parseInt(StringUtils.substring(reverse, 6, 9));
		int reversePart4 = Integer.parseInt(StringUtils.substring(reverse, 9, 12));
		Logger.debug("[gen]reversePart1:" + reversePart1);
		Logger.debug("[gen]reversePart2:" + reversePart2);
		Logger.debug("[gen]reversePart3:" + reversePart3);
		Logger.debug("[gen]reversePart4:" + reversePart4);
		int total = reversePart1 + reversePart2 + reversePart3 + reversePart4;
		Character factor1 = chars.get(total % chars.size());
		Character factor2 = chars.get((total / chars.size()) % chars.size());
		String convert = new StringBuffer().append(factor1).append(factor1)
				.append(chars.get(reversePart1 / chars.size())).append(chars.get(reversePart1 % chars.size()))
				.append(factor1).append(factor2).append(chars.get(reversePart2 / chars.size()))
				.append(chars.get(reversePart2 % chars.size())).append(factor2).append(factor1)
				.append(chars.get(reversePart3 / chars.size())).append(chars.get(reversePart3 % chars.size()))
				.append(factor2).append(factor2).append(chars.get(reversePart4 / chars.size()))
				.append(chars.get(reversePart4 % chars.size())).toString();
		Logger.debug("[gen]convert:" + convert);
		return convert;
	}

	public static long readUser(String openID) {
		// 去除冗余信息
		char[] usefulPart1 = { openID.charAt(2), openID.charAt(3) };
		char[] usefulPart2 = { openID.charAt(6), openID.charAt(7) };
		char[] usefulPart3 = { openID.charAt(10), openID.charAt(11) };
		char[] usefulPart4 = { openID.charAt(14), openID.charAt(15) };
		Logger.debug("[read]usefulPart1:" + usefulPart1[0] + usefulPart1[1]);
		Logger.debug("[read]usefulPart2:" + usefulPart2[0] + usefulPart2[1]);
		Logger.debug("[read]usefulPart3:" + usefulPart3[0] + usefulPart3[1]);
		Logger.debug("[read]usefulPart4:" + usefulPart4[0] + usefulPart4[1]);
		// 转换
		String convert = new DecimalFormat("000")
				.format(chars.indexOf(usefulPart1[0]) * chars.size() + chars.indexOf(usefulPart1[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart2[0]) * chars.size() + chars.indexOf(usefulPart2[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart3[0]) * chars.size() + chars.indexOf(usefulPart3[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart4[0]) * chars.size() + chars.indexOf(usefulPart4[1]));
		Logger.debug("[read]convert:" + convert);
		// 倒序
		String reverse = StringUtils.reverse(convert);
		Logger.debug("[read]reverse:" + reverse);
		// 读取
		long userId = Long.parseLong(StringUtils.substring(reverse, 6, 12));
		Logger.debug("[read]userId:" + userId);
		return userId;
	}

	public static long readApp(String openID) {
		// 去除冗余信息
		char[] usefulPart1 = { openID.charAt(2), openID.charAt(3) };
		char[] usefulPart2 = { openID.charAt(6), openID.charAt(7) };
		char[] usefulPart3 = { openID.charAt(10), openID.charAt(11) };
		char[] usefulPart4 = { openID.charAt(14), openID.charAt(15) };
		Logger.debug("[read]usefulPart1:" + usefulPart1[0] + usefulPart1[1]);
		Logger.debug("[read]usefulPart2:" + usefulPart2[0] + usefulPart2[1]);
		Logger.debug("[read]usefulPart3:" + usefulPart3[0] + usefulPart3[1]);
		Logger.debug("[read]usefulPart4:" + usefulPart4[0] + usefulPart4[1]);
		// 转换
		String convert = new DecimalFormat("000")
				.format(chars.indexOf(usefulPart1[0]) * chars.size() + chars.indexOf(usefulPart1[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart2[0]) * chars.size() + chars.indexOf(usefulPart2[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart3[0]) * chars.size() + chars.indexOf(usefulPart3[1]))
				+ new DecimalFormat("000")
						.format(chars.indexOf(usefulPart4[0]) * chars.size() + chars.indexOf(usefulPart4[1]));
		Logger.debug("[read]convert:" + convert);
		// 倒序
		String reverse = StringUtils.reverse(convert);
		Logger.debug("[read]reverse:" + reverse);
		// 读取
		long userId = Long.parseLong(StringUtils.substring(reverse, 0, 6));
		Logger.debug("[read]userId:" + userId);
		return userId;
	}

	private static final List<Character> chars = new ArrayList<Character>() {
		{
			for (int i = 97; i < 123; i++) {
				add((char) i);
			}
			for (int i = 65; i < 91; i++) {
				add((char) i);
			}
			for (int i = 49; i < 58; i++) {
				add((char) i);
			}
		}
	};

	public static void main(String[] args) {
	}

}
