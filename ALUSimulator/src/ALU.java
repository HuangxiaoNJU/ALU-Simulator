/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author 151250060_黄潇
 *
 */

public class ALU {
	/**
	 * 与门（二元）
	 */
	private char and(char c1, char c2) {
		if(c1 == '1' && c2 == '1')
			return '1';
		return '0';
	}
	/**
	 * 与门（三元）
	 */
	private char and(char c1, char c2, char c3) {
		return and(and(c1, c2), c3);
	}
	/**
	 * 与门（四元）
	 */
	private char and(char c1, char c2, char c3, char c4) {
		return and(and(c1, c2, c3), c4);
	}
	/**
	 * 与门（五元）
	 */
	private char and(char c1, char c2, char c3, char c4, char c5) {
		return and(and(c1, c2, c3, c4), c5);
	}
	
	/**
	 * 或门（二元）
	 */
	private char or(char c1, char c2) {
		if(c1 == '0' && c2 == '0')
			return '0';
		return '1';
	}
	/**
	 * 或门（三元）
	 */
	private char or(char c1, char c2, char c3) {
		return or(or(c1, c2), c3);
	}
	/**
	 * 或门（四元）
	 */
	private char or(char c1, char c2, char c3, char c4) {
		return or(or(c1, c2, c3), c4);
	}
	/**
	 * 或门（五元）
	 */
	private char or(char c1, char c2, char c3, char c4, char c5) {
		return or(or(c1, c2, c3, c4), c5);
	}
	
	/**
	 * 非门
	 */
	private char not(char c) {
		return (char)('0' + '1' - c);
	}
	
	/**
	 * 异或门
	 */
	private char xor(char c1, char c2) {
		return or(and(not(c1), c2), and(c1, not(c2)));
	}

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {
		int decimal = Integer.parseInt(number);
		String binary = "";
		// 十进制正整数转换为二进制原码整数
		int num = Math.abs(decimal);
		while(num > 0) {
			binary = num % 2 == 0 ? '0' + binary : '1' + binary;
			num = num >> 1;
		}
		// 符号扩展
		for (int i = binary.length(); i < length; i++)
			binary = '0' + binary;
		// 如果十进制整数大于等于0，则其二进制补码与原码相同，否则取反加1
		return decimal >= 0 ? binary : oneAdder(negation(binary)).substring(1);
	}
	
	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		String result = "";
		double decimal = Double.parseDouble(number);
		// 结果加上符号位，取十进制浮点数绝对值
		result += decimal >= 0 ? "0" : "1";
		decimal = Math.abs(decimal);
		// 0
		if(decimal == 0) {
			// 所有位全部置为0
			for (int i = 0; i < eLength + sLength; i++)
				result += '0';
			return result;
		}
		// 正（负）无穷
		if(decimal > (2 - Math.pow(2, -sLength)) * Math.pow(2, Math.pow(2, eLength - 1) - 1)) {
			// 指数全部置为1
			for (int i = 0; i < eLength; i++)
				result += '1';
			// 尾数全部置为0
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// 非规格化数表示
		if(decimal < Math.pow(2, 2 - Math.pow(2, eLength - 1))) {
			// 指数全部置为0
			for (int i = 0; i < eLength; i++)
				result += '0';
			decimal *= Math.pow(2, Math.pow(2, eLength - 1) - 2);
			for (int i = 0; i < sLength; i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
			return result;
		}
		// 规格化表示
		// 将十进制浮点数整数部分化为二进制整数
		// 十进制正整数N化为二进制后二进制数的长度为 log2(N+1) = ln(N+1)/ln2 的向上取整
		String intDecimal = integerRepresentation("" + (int)decimal, (int)Math.ceil(Math.log((int)decimal + 1)/Math.log(2)));
		// 十进制浮点数取其小数部分
		decimal = decimal - (int)decimal;
		// 如果整数部分大于0
		if(intDecimal.length() > 0) {
			// 计算指数
			int exponent = intDecimal.length() - 1 + (int)Math.pow(2, eLength - 1) - 1;
			result += this.integerRepresentation("" + exponent, eLength);
			// 计算尾数
			result += intDecimal.equals("1") ? "" : intDecimal.substring(1, intDecimal.length());
			for (int i = 0; i <= sLength - intDecimal.length(); i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
		}
		// 如果整数部分为0
		else {
			// 计算指数
			int exponent = (int)Math.pow(2, eLength - 1) - 1;
			do {
				decimal *= 2;
				exponent --;
			} while(decimal < 1);
			result += this.integerRepresentation("" + exponent, eLength);
			// 计算尾数
			decimal -= 1;
			for (int i = 0; i < sLength; i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
		}
		return result;
	}
	
	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {
		if(length == 32)
			return this.floatRepresentation(number, 8, 23);
		
		return this.floatRepresentation(number, 11, 52);
	}
	
	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		int decimal = 0;
		if(operand.charAt(0) == '1')
			decimal -= Math.pow(2, operand.length() - 1);
		for (int i = 1; i < operand.length(); i++) {
			if(operand.charAt(i) == '1')
				decimal += Math.pow(2, operand.length() - 1 - i);
		}
		return "" + decimal;
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		int sign = operand.charAt(0) == '0' ? 1 : -1;
		int exponent = 0;
		double fraction = 0;
		// 计算指数
		for (int i = 1; i <= eLength; i++) {
			if(operand.charAt(i) == '1')
				exponent += Math.pow(2, eLength - i);
		}
		// 计算分数（0.f）
		for (int i = eLength + 1; i <= eLength + sLength; i++) {
			if(operand.charAt(i) == '1')
				fraction += Math.pow(2, eLength - i);
		}
		// 指数全为0
		if(exponent == 0) {
			if(fraction == 0)
				return "0";
			else {
				double result = sign * fraction * Math.pow(2, exponent + 2 - Math.pow(2, eLength - 1));
				return String.valueOf(result);
			}
		}
		// 指数全为1
		if(exponent == Math.pow(2, eLength) - 1) {
			if(fraction == 0)
				return  sign > 0 ? "+Inf" : "-Inf";
			else
				return "NaN";
		}
		// 一般情形
		double result = sign * (fraction + 1) * Math.pow(2, exponent + 1 - Math.pow(2, eLength - 1));
		// 判断浮点数是否为整数
		if((int)result == result)
			return "" + (int)result;
		return "" + result;
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		char[] binary = operand.toCharArray();
		for (int i = 0; i < binary.length; i++)
			binary[i] = not(binary[i]);
		return new String(binary);
	}
	
	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {
		char[] binary = operand.toCharArray();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < binary.length - 1; j++)
				binary[j] = binary[j + 1]; 
			binary[binary.length - 1] = '0';
		}
		return new String(binary);
	}
	
	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {
		char[] binary = operand.toCharArray();
		for (int i = 0; i < n; i++) {
			for (int j = binary.length - 1; j > 0; j--)
				binary[j] = binary[j - 1];
			// 右移后左边补0
			binary[0] = '0';
		}
		return new String(binary);
	}
	
	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		char[] binary = operand.toCharArray();
		// 操作数符号
		char sign = binary[0];
		for (int i = 0; i < n; i++) {
			for (int j = binary.length - 1; j > 0; j--)
				binary[j] = binary[j - 1];
			// 右移后左边补符号位
			binary[0] = sign;
		}
		return new String(binary);
	}
	
	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {
		char s, nextC;
		// Si = Xi ^ Yi ^ Ci
		s = xor(xor(x, y), c);
		// Ci+1 = Xi & Ci | Yi & Ci | Xi & Yi
		nextC = or(and(x, c), and(y, c), and(x, y));
		return "" + nextC + s;
	}
	
	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		char[] X = operand1.toCharArray();
		char[] Y = operand2.toCharArray();
		char[] P = new char[5];
		char[] G = new char[5];
 		char[] C = new char[5];
 		C[0] = c;
 		// 计算Pi、Gi
		for (int i = 1; i <= 4; i++) {
			P[i] = or(X[4 - i], Y[4 - i]);
			G[i] = and(X[4 - i], Y[4 - i]);
		}
		// 计算出所有Ci
		C[1] = or(G[1], and(P[1], C[0]));
		C[2] = or(G[2], and(P[2], G[1]), and(P[2], P[1], C[0]));
		C[3] = or(G[3], and(P[3], G[2]), and(P[3], P[2], G[1]), and(P[3], P[2], P[1], C[0]));
		C[4] = or(G[4], and(P[4], G[3]), and(P[4], P[3], G[2]), and(P[4], P[3], P[2], G[1]), and(P[4], P[3], P[2], P[1], C[0]));
		String result = "" + C[4];
		// 计算Si
		for (int i = 4; i > 0; i--)
			result += fullAdder(X[4 - i], Y[4 - i], C[i - 1]).charAt(1);
		return result;
	}
	
	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		char[] x = operand.toCharArray();
		char[] result = new char[operand.length()];
		char nextC = '1';
		for (int i = result.length - 1; i >= 0; i--) {
			result[i] = xor(x[i], nextC);
			nextC = and(x[i], nextC);
		}
		// 计算是否溢出
		char Xn = x[0];
		char Yn = '0';
		char Sn = result[0];
		char overflow = or(and(Xn, Yn, not(Sn)), and(not(Xn), not(Yn), Sn));
		return overflow + new String(result);
	}
	
	/**
	 * 符号扩展（私有方法）
	 * @param operand 二进制补码表示的操作数
	 * @param length 符号扩展后的总长度
	 * @return operand符号扩展到总长度为length后的结果
	 */
	private String signExtention (String operand, int length) {
		char sign = operand.charAt(0);
		for (int i = operand.length() + 1; i <= length; i++)
			operand = sign + operand;
		return operand;
	}
	
	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		operand1 = signExtention(operand1, length);
		operand2 = signExtention(operand2, length);
		String result = "";
		char nextC = c;
		for (int i = length; i >= 4; i -= 4) {
			String temp = claAdder(operand1.substring(i - 4, i), operand2.substring(i - 4, i), nextC);
			result = temp.substring(1, 5) + result;
			nextC = temp.charAt(0);
		}
		// 计算是否溢出 overflow = Xn Yn !Sn + !Xn !Yn Sn
		char Xn = operand1.charAt(0);
		char Yn = operand2.charAt(0);
		char Sn = result.charAt(0);
		char overflow = or(and(Xn, Yn, not(Sn)), and(not(Xn), not(Yn), Sn));
		return overflow + result;
	}
	
	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		return adder(operand1, oneAdder(negation(operand2)).substring(1), '0', length);
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		String X = signExtention(operand1, length);
		String Y = signExtention(operand2, length);
		String result = Y + '0';
		for (int i = 0; i < length; i++)
			result = '0' + result;
		for (int i = 0; i < length; i++) {
			// Yn - Yn+1 = 1
			if(result.charAt(result.length() - 1) - result.charAt(result.length() - 2) == 1) {
				String add = integerAddition(result.substring(0, length), X, length).substring(1);
				result = add + result.substring(length);
			}
			// Yn - Yn+1 = -1
			if(result.charAt(result.length() - 1) - result.charAt(result.length() - 2) == -1) {
				String minus = integerSubtraction(result.substring(0, length), X, length).substring(1);
				result = minus + result.substring(length);
			}
			// 算数右移
			result = ariRightShift(result, 1);
		}
		// 去掉最后一位辅助位
		result = result.substring(0, length << 1);
		// 判断溢出
		char sign = result.charAt(0);
		for (int i = 1; i <= length; i++) {
			if(result.charAt(i) != sign)
				return '1' + result.substring(length);
		}
		return '0' + result.substring(length);
	}
	
	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		String dividend = signExtention(operand1, length);
		String divisor = signExtention(operand2, length);
		String result = "";
		// 被除数为0
//		if(integerTrueValue(dividend).equals("0")) {
//			for (int i = 0; i < (length << 1) + 1; i++)
//				result += '0';
//			return result;
//		}
		// 被除数不为0
		result = signExtention(dividend, length << 1);
		if(dividend.charAt(0) == divisor.charAt(0)) {
			String minus = integerSubtraction(result.substring(0, length), divisor, length).substring(1);
			result = minus + result.substring(length);
		}
		else {
			String add = integerAddition(result.substring(0, length), divisor, length).substring(1);
			result = add + result.substring(length);
		}
		char Qn = result.charAt(0) == divisor.charAt(0) ? '1' : '0';
		for (int i = 0; i < length; i++) {
			result = leftShift(result + Qn, 1).substring(0, length << 1);
			if(Qn == '1') {
				String minus = integerSubtraction(result.substring(0, length), divisor, length).substring(1);
				result = minus + result.substring(length);
			}
			else {
				String add = integerAddition(result.substring(0, length), divisor, length).substring(1);
				result = add + result.substring(length);
			}
			Qn = result.charAt(0) == divisor.charAt(0) ? '1' : '0';
		}
		String remainder = result.substring(0, length);
		String quotient = result.substring(length + 1) + Qn;
		if(quotient.charAt(0) == '1' && dividend.charAt(0) != divisor.charAt(0))
			quotient = oneAdder(quotient).substring(1);
		if(remainder.charAt(0) != dividend.charAt(0)) {
			if(dividend.charAt(0) == divisor.charAt(0))
				remainder = integerAddition(remainder, divisor, length).substring(1);
			else
				remainder = integerSubtraction(remainder, divisor, length).substring(1);
		}
		return '0' + quotient + remainder;
	}
	
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		String result = "";
		// 符号扩展
		char sign1 = operand1.charAt(0);
		String X = operand1.substring(1);
		char sign2 = operand2.charAt(0);
		String Y = operand2.substring(1);
		for (int i = 0; i <= length - operand1.length(); i++)
			X = '0' + X;
		for (int i = 0; i <= length - operand2.length(); i++)
			Y = '0' + Y;
		// 符号相同
		if(sign1 == sign2) {
			// 扩展位数
			int extention = ((length / 4) + 1) * 4;
			for (int i = length; i < extention; i++) {
				X = '0' + X;
				Y = '0' + Y;
			}
			result = integerAddition(X, Y, extention).substring(1);
			char overflow = '0';
			for (int i = 0; i < extention - length; i++)
				if(result.charAt(i) == '1') {
					overflow = '1';
					break;
				}
			return "" + overflow + sign1 + result.substring(extention - length);
		}
		// 符号相反
		else {
			// 负数取反加1
			if(sign1 == '1')
				X = oneAdder(negation(X)).substring(1);
			else
				Y = oneAdder(negation(Y)).substring(1);
			// 相加
			char nextC = '0';
			for (int i = length; i >= 4; i -= 4) {
				String temp = claAdder(X.substring(i - 4, i), Y.substring(i - 4, i), nextC);
				result = temp.substring(1, 5) + result;
				nextC = temp.charAt(0);
			}
			// 修正
			if(nextC == '1')
				return "0" + "0" + result;
			else
				return "0" + "1" + oneAdder(negation(result)).substring(1);
		}
	}
	
	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		if(floatTrueValue(operand1, eLength, sLength).equals("0"))
			return "0" + operand2;
		if(floatTrueValue(operand2, eLength, sLength).equals("0"))
			return "0" + operand1;
	    // X，Y均不等于0
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		// 计算指数与尾数
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// 尾数添加保护位
		for (int i = 0; i < gLength; i++) {
			fraction1 += '0';
			fraction2 += '0';
		}
		// 指数对齐
		int maxExponent = Math.max(exponent1, exponent2);
		if(exponent1 < exponent2)
			fraction1 = logRightShift(fraction1, exponent2 - exponent1);
		else
			fraction2 = logRightShift(fraction2, exponent1 - exponent2);
		// 判断尾数是否为0
		if(integerTrueValue(fraction1).equals("0"))
			return "0" + operand2;
		if(integerTrueValue(fraction2).equals("0"))
			return "0" + operand1;
		// 尾数相加（先将尾数和保护位扩展长度为4的倍数）
		int extention = (((1 + sLength + gLength) / 4) + 1) * 4;
		String fraction = signedAddition(sign1 + fraction1, sign2 + fraction2, extention);
		// 记录结果符号位
		char sign = fraction.charAt(1);
		// 结果去掉溢出位和符号位
		fraction = fraction.substring(2);
		// 尾数相加结果为0
		if(integerTrueValue(fraction).equals("0")) {
			String result = "";
			for (int i = 0; i < eLength + sLength + 2; i++)
				result = '0' + result;
			return result;
		}
		// 去掉相加时扩展的位数减1
		fraction = fraction.substring(extention - (1 + sLength + gLength) - 1);
		// 尾数相加后产生进位，右移尾数，指数加1
		if(fraction.charAt(0) == '1') {
			fraction = logRightShift(fraction, 1).substring(1);
			maxExponent ++;
			// 指数上溢，返回Inf
			if(maxExponent >= Math.pow(2, eLength - 1)) {
				String result = "1" + sign;
				for (int i = 0; i < eLength; i++)
					result += '1';
				for (int i = 0; i < sLength; i++)
					result += '0';
				return result;
			}
		}
		// 不产生进位
		else {
			fraction = fraction.substring(1);
		}
		// 处理尾数
		while(fraction.charAt(0) == '0') {
			fraction = leftShift(fraction, 1);
			maxExponent --;
		}
		// 指数下溢，非规格化表示
		if(maxExponent < 2 - Math.pow(2, eLength - 1)) {
			String result = "0" + sign;
			while(maxExponent < 2 - Math.pow(2, eLength - 1))
				fraction = '0' + fraction;
			for (int i = 0; i < eLength; i++)
				result += '0';
			result += fraction.substring(1, sLength + 1);
			return result;
		}
		// 规格化表示
		String result = "0" + sign + integerRepresentation("" + (maxExponent + (int)Math.pow(2, eLength - 1) - 1), eLength);
		result = result + fraction.substring(1, sLength + 1);
		return result;
	}
	
	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// 改变减数符号
		operand2 = operand2.charAt(0) == '0' ? '1' + operand2.substring(1) : '0' + operand2.substring(1);
		return floatAddition(operand1, operand2, eLength, sLength, gLength);
	}
	
	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		String result = "";
		// 两个乘数其中之一为0
		if(floatTrueValue(operand1, eLength, sLength).equals("0") || floatTrueValue(operand2, eLength, sLength).equals("0")) {
			for (int i = 0; i <= eLength + sLength + 1; i++)
				result += '0';
			return result;
		}
		// 两个乘数均不为0
		char sign = xor(operand1.charAt(0), operand2.charAt(0));
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// 指数相加
		int exponent = exponent1 + exponent2;
		// 尾数相乘（尾数长度扩展为4的倍数）
		int extention = ((1 + sLength) / 4 + 1) * 4;
		String product = integerMultiplication("0" + fraction1, "0" + fraction2, extention * 2).substring(1);
		product = product.substring((extention - sLength - 1) * 2);
		// 处理结果
		for (int i = 0; i < product.length(); i++)
			if(product.charAt(i) == '1') {
				exponent += 1 - i;
				product = product.substring(i + 1);
				break;
			}
	    // 指数上溢
		if(exponent >= Math.pow(2, eLength - 1)) {
			result = "1" + sign;
			for (int i = 0; i < eLength; i++)
				result += '1';
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// 指数下溢，非规格化表示
		if(exponent < 2 - Math.pow(2, eLength - 1)) {
			product = '1' + product;
			while(exponent < 2 - Math.pow(2, eLength - 1)) {
				product = '0' + product;
				exponent ++;
			}
			product = product.substring(1);
			result = "0" + sign;
			for (int i = 0; i < eLength; i++)
				result += '0';
			if(product.length() > sLength)
				product = product.substring(0, sLength);
			else
				for (int i = product.length(); i < sLength; i++)
					product += '0';
			result += product;
			return result;
		}
		// 规格化表示
		result = "0" + sign + integerRepresentation("" + (exponent + (int)Math.pow(2, eLength - 1) - 1), eLength);
		if(product.length() > sLength)
			product = product.substring(0, sLength);
		else
			for (int i = product.length(); i < sLength; i++)
				product += '0';
		result += product;
		return result;
	}
	
	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		String result = "";
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		char sign = xor(sign1, sign2);
		// 除数为0，返回NaN
		if(floatTrueValue(operand2, eLength, sLength).equals("0")) {
			result = "0" + sign + result;
			for (int i = 0; i < eLength + sLength; i++)
				result = '1' + result;
			return result;
		}
		// 被除数为0，返回0
		if(floatTrueValue(operand1, eLength, sLength).equals("0")) {
			for (int i = 0; i < 2 + eLength + sLength; i++)
				result = '0' + result;
			return result;
		}
		// 被除数、除数均不为0
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// 指数相减
		int exponent = exponent1 - exponent2;
		// 尾数相除（结果精确到小数点后2*(1+sLength)位，将尾数长度扩展为4的倍数）
		int extention = (((1 + sLength) * 3) / 4 + 1) * 4;
		// 符号扩展（被除数右边先扩2*(1+sLength)位0，左边再扩0，除数直接左边扩0）
		for (int i = 0; i < (sLength + 1) * 2; i++)
			fraction1 += '0';
		for (int i = (1 + sLength) * 3; i < extention; i++)
			fraction1 = '0' + fraction1;
		for (int i = 1 + sLength; i < extention; i++)
			fraction2 = '0' + fraction2;
		String quotient = integerDivision(fraction1, fraction2, extention).substring(1, extention + 1);
		// 处理结果
		for (int i = 0; i < extention; i++) {
			if(quotient.charAt(i) == '1') {
				exponent += extention - (sLength + 1) * 2 - 1 - i;
				quotient = quotient.substring(i + 1);
				break;
			}
		}
		// 指数上溢，返回Inf
		if (exponent >= Math.pow(2, eLength - 1)) {
			// 溢出位置为1
			result = "1" + sign;
			for (int i = 0; i < eLength; i++)
				result += '1';
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// 指数下溢，非规格化表示
		if(exponent < 2 - Math.pow(2, eLength - 1)) {
			quotient = '1' + quotient;
			while(exponent < 2 - Math.pow(2, eLength - 1)) {
				quotient = '0' + quotient;
				exponent ++;
			}
			// 溢出位置为0
			result = "0" + sign;
			for (int i = 0; i < eLength; i++)
				result += '0';
			result += quotient.substring(1, sLength + 1);
			return result;
		}
		// 规格化表示
		result = "0" + sign + integerRepresentation("" + (exponent + (int)Math.pow(2, eLength - 1) - 1), eLength);
		result += quotient.substring(0, sLength);
		return result;
	}
	
}

