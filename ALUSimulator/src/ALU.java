/**
 * ģ��ALU���������͸���������������
 * @author 151250060_����
 *
 */

public class ALU {
	/**
	 * ���ţ���Ԫ��
	 */
	private char and(char c1, char c2) {
		if(c1 == '1' && c2 == '1')
			return '1';
		return '0';
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char and(char c1, char c2, char c3) {
		return and(and(c1, c2), c3);
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char and(char c1, char c2, char c3, char c4) {
		return and(and(c1, c2, c3), c4);
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char and(char c1, char c2, char c3, char c4, char c5) {
		return and(and(c1, c2, c3, c4), c5);
	}
	
	/**
	 * ���ţ���Ԫ��
	 */
	private char or(char c1, char c2) {
		if(c1 == '0' && c2 == '0')
			return '0';
		return '1';
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char or(char c1, char c2, char c3) {
		return or(or(c1, c2), c3);
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char or(char c1, char c2, char c3, char c4) {
		return or(or(c1, c2, c3), c4);
	}
	/**
	 * ���ţ���Ԫ��
	 */
	private char or(char c1, char c2, char c3, char c4, char c5) {
		return or(or(c1, c2, c3, c4), c5);
	}
	
	/**
	 * ����
	 */
	private char not(char c) {
		return (char)('0' + '1' - c);
	}
	
	/**
	 * �����
	 */
	private char xor(char c1, char c2) {
		return or(and(not(c1), c2), and(c1, not(c2)));
	}

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		int decimal = Integer.parseInt(number);
		String binary = "";
		// ʮ����������ת��Ϊ������ԭ������
		int num = Math.abs(decimal);
		while(num > 0) {
			binary = num % 2 == 0 ? '0' + binary : '1' + binary;
			num = num >> 1;
		}
		// ������չ
		for (int i = binary.length(); i < length; i++)
			binary = '0' + binary;
		// ���ʮ�����������ڵ���0����������Ʋ�����ԭ����ͬ������ȡ����1
		return decimal >= 0 ? binary : oneAdder(negation(binary)).substring(1);
	}
	
	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		String result = "";
		double decimal = Double.parseDouble(number);
		// ������Ϸ���λ��ȡʮ���Ƹ���������ֵ
		result += decimal >= 0 ? "0" : "1";
		decimal = Math.abs(decimal);
		// 0
		if(decimal == 0) {
			// ����λȫ����Ϊ0
			for (int i = 0; i < eLength + sLength; i++)
				result += '0';
			return result;
		}
		// ������������
		if(decimal > (2 - Math.pow(2, -sLength)) * Math.pow(2, Math.pow(2, eLength - 1) - 1)) {
			// ָ��ȫ����Ϊ1
			for (int i = 0; i < eLength; i++)
				result += '1';
			// β��ȫ����Ϊ0
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// �ǹ������ʾ
		if(decimal < Math.pow(2, 2 - Math.pow(2, eLength - 1))) {
			// ָ��ȫ����Ϊ0
			for (int i = 0; i < eLength; i++)
				result += '0';
			decimal *= Math.pow(2, Math.pow(2, eLength - 1) - 2);
			for (int i = 0; i < sLength; i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
			return result;
		}
		// ��񻯱�ʾ
		// ��ʮ���Ƹ������������ֻ�Ϊ����������
		// ʮ����������N��Ϊ�����ƺ���������ĳ���Ϊ log2(N+1) = ln(N+1)/ln2 ������ȡ��
		String intDecimal = integerRepresentation("" + (int)decimal, (int)Math.ceil(Math.log((int)decimal + 1)/Math.log(2)));
		// ʮ���Ƹ�����ȡ��С������
		decimal = decimal - (int)decimal;
		// ����������ִ���0
		if(intDecimal.length() > 0) {
			// ����ָ��
			int exponent = intDecimal.length() - 1 + (int)Math.pow(2, eLength - 1) - 1;
			result += this.integerRepresentation("" + exponent, eLength);
			// ����β��
			result += intDecimal.equals("1") ? "" : intDecimal.substring(1, intDecimal.length());
			for (int i = 0; i <= sLength - intDecimal.length(); i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
		}
		// �����������Ϊ0
		else {
			// ����ָ��
			int exponent = (int)Math.pow(2, eLength - 1) - 1;
			do {
				decimal *= 2;
				exponent --;
			} while(decimal < 1);
			result += this.integerRepresentation("" + exponent, eLength);
			// ����β��
			decimal -= 1;
			for (int i = 0; i < sLength; i++) {
				result += decimal * 2 >= 1 ? '1' : '0';
				decimal = decimal * 2 >= 1 ? decimal * 2 - 1 : decimal * 2;
			}
		}
		return result;
	}
	
	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		if(length == 32)
			return this.floatRepresentation(number, 8, 23);
		
		return this.floatRepresentation(number, 11, 52);
	}
	
	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
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
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		int sign = operand.charAt(0) == '0' ? 1 : -1;
		int exponent = 0;
		double fraction = 0;
		// ����ָ��
		for (int i = 1; i <= eLength; i++) {
			if(operand.charAt(i) == '1')
				exponent += Math.pow(2, eLength - i);
		}
		// ���������0.f��
		for (int i = eLength + 1; i <= eLength + sLength; i++) {
			if(operand.charAt(i) == '1')
				fraction += Math.pow(2, eLength - i);
		}
		// ָ��ȫΪ0
		if(exponent == 0) {
			if(fraction == 0)
				return "0";
			else {
				double result = sign * fraction * Math.pow(2, exponent + 2 - Math.pow(2, eLength - 1));
				return String.valueOf(result);
			}
		}
		// ָ��ȫΪ1
		if(exponent == Math.pow(2, eLength) - 1) {
			if(fraction == 0)
				return  sign > 0 ? "+Inf" : "-Inf";
			else
				return "NaN";
		}
		// һ������
		double result = sign * (fraction + 1) * Math.pow(2, exponent + 1 - Math.pow(2, eLength - 1));
		// �жϸ������Ƿ�Ϊ����
		if((int)result == result)
			return "" + (int)result;
		return "" + result;
	}
	
	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		char[] binary = operand.toCharArray();
		for (int i = 0; i < binary.length; i++)
			binary[i] = not(binary[i]);
		return new String(binary);
	}
	
	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
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
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		char[] binary = operand.toCharArray();
		for (int i = 0; i < n; i++) {
			for (int j = binary.length - 1; j > 0; j--)
				binary[j] = binary[j - 1];
			// ���ƺ���߲�0
			binary[0] = '0';
		}
		return new String(binary);
	}
	
	/**
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		char[] binary = operand.toCharArray();
		// ����������
		char sign = binary[0];
		for (int i = 0; i < n; i++) {
			for (int j = binary.length - 1; j > 0; j--)
				binary[j] = binary[j - 1];
			// ���ƺ���߲�����λ
			binary[0] = sign;
		}
		return new String(binary);
	}
	
	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
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
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		char[] X = operand1.toCharArray();
		char[] Y = operand2.toCharArray();
		char[] P = new char[5];
		char[] G = new char[5];
 		char[] C = new char[5];
 		C[0] = c;
 		// ����Pi��Gi
		for (int i = 1; i <= 4; i++) {
			P[i] = or(X[4 - i], Y[4 - i]);
			G[i] = and(X[4 - i], Y[4 - i]);
		}
		// ���������Ci
		C[1] = or(G[1], and(P[1], C[0]));
		C[2] = or(G[2], and(P[2], G[1]), and(P[2], P[1], C[0]));
		C[3] = or(G[3], and(P[3], G[2]), and(P[3], P[2], G[1]), and(P[3], P[2], P[1], C[0]));
		C[4] = or(G[4], and(P[4], G[3]), and(P[4], P[3], G[2]), and(P[4], P[3], P[2], G[1]), and(P[4], P[3], P[2], P[1], C[0]));
		String result = "" + C[4];
		// ����Si
		for (int i = 4; i > 0; i--)
			result += fullAdder(X[4 - i], Y[4 - i], C[i - 1]).charAt(1);
		return result;
	}
	
	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		char[] x = operand.toCharArray();
		char[] result = new char[operand.length()];
		char nextC = '1';
		for (int i = result.length - 1; i >= 0; i--) {
			result[i] = xor(x[i], nextC);
			nextC = and(x[i], nextC);
		}
		// �����Ƿ����
		char Xn = x[0];
		char Yn = '0';
		char Sn = result[0];
		char overflow = or(and(Xn, Yn, not(Sn)), and(not(Xn), not(Yn), Sn));
		return overflow + new String(result);
	}
	
	/**
	 * ������չ��˽�з�����
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @param length ������չ����ܳ���
	 * @return operand������չ���ܳ���Ϊlength��Ľ��
	 */
	private String signExtention (String operand, int length) {
		char sign = operand.charAt(0);
		for (int i = operand.length() + 1; i <= length; i++)
			operand = sign + operand;
		return operand;
	}
	
	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
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
		// �����Ƿ���� overflow = Xn Yn !Sn + !Xn !Yn Sn
		char Xn = operand1.charAt(0);
		char Yn = operand2.charAt(0);
		char Sn = result.charAt(0);
		char overflow = or(and(Xn, Yn, not(Sn)), and(not(Xn), not(Yn), Sn));
		return overflow + result;
	}
	
	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		return adder(operand1, oneAdder(negation(operand2)).substring(1), '0', length);
	}
	
	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
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
			// ��������
			result = ariRightShift(result, 1);
		}
		// ȥ�����һλ����λ
		result = result.substring(0, length << 1);
		// �ж����
		char sign = result.charAt(0);
		for (int i = 1; i <= length; i++) {
			if(result.charAt(i) != sign)
				return '1' + result.substring(length);
		}
		return '0' + result.substring(length);
	}
	
	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		String dividend = signExtention(operand1, length);
		String divisor = signExtention(operand2, length);
		String result = "";
		// ������Ϊ0
//		if(integerTrueValue(dividend).equals("0")) {
//			for (int i = 0; i < (length << 1) + 1; i++)
//				result += '0';
//			return result;
//		}
		// ��������Ϊ0
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
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		String result = "";
		// ������չ
		char sign1 = operand1.charAt(0);
		String X = operand1.substring(1);
		char sign2 = operand2.charAt(0);
		String Y = operand2.substring(1);
		for (int i = 0; i <= length - operand1.length(); i++)
			X = '0' + X;
		for (int i = 0; i <= length - operand2.length(); i++)
			Y = '0' + Y;
		// ������ͬ
		if(sign1 == sign2) {
			// ��չλ��
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
		// �����෴
		else {
			// ����ȡ����1
			if(sign1 == '1')
				X = oneAdder(negation(X)).substring(1);
			else
				Y = oneAdder(negation(Y)).substring(1);
			// ���
			char nextC = '0';
			for (int i = length; i >= 4; i -= 4) {
				String temp = claAdder(X.substring(i - 4, i), Y.substring(i - 4, i), nextC);
				result = temp.substring(1, 5) + result;
				nextC = temp.charAt(0);
			}
			// ����
			if(nextC == '1')
				return "0" + "0" + result;
			else
				return "0" + "1" + oneAdder(negation(result)).substring(1);
		}
	}
	
	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		if(floatTrueValue(operand1, eLength, sLength).equals("0"))
			return "0" + operand2;
		if(floatTrueValue(operand2, eLength, sLength).equals("0"))
			return "0" + operand1;
	    // X��Y��������0
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		// ����ָ����β��
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// β����ӱ���λ
		for (int i = 0; i < gLength; i++) {
			fraction1 += '0';
			fraction2 += '0';
		}
		// ָ������
		int maxExponent = Math.max(exponent1, exponent2);
		if(exponent1 < exponent2)
			fraction1 = logRightShift(fraction1, exponent2 - exponent1);
		else
			fraction2 = logRightShift(fraction2, exponent1 - exponent2);
		// �ж�β���Ƿ�Ϊ0
		if(integerTrueValue(fraction1).equals("0"))
			return "0" + operand2;
		if(integerTrueValue(fraction2).equals("0"))
			return "0" + operand1;
		// β����ӣ��Ƚ�β���ͱ���λ��չ����Ϊ4�ı�����
		int extention = (((1 + sLength + gLength) / 4) + 1) * 4;
		String fraction = signedAddition(sign1 + fraction1, sign2 + fraction2, extention);
		// ��¼�������λ
		char sign = fraction.charAt(1);
		// ���ȥ�����λ�ͷ���λ
		fraction = fraction.substring(2);
		// β����ӽ��Ϊ0
		if(integerTrueValue(fraction).equals("0")) {
			String result = "";
			for (int i = 0; i < eLength + sLength + 2; i++)
				result = '0' + result;
			return result;
		}
		// ȥ�����ʱ��չ��λ����1
		fraction = fraction.substring(extention - (1 + sLength + gLength) - 1);
		// β����Ӻ������λ������β����ָ����1
		if(fraction.charAt(0) == '1') {
			fraction = logRightShift(fraction, 1).substring(1);
			maxExponent ++;
			// ָ�����磬����Inf
			if(maxExponent >= Math.pow(2, eLength - 1)) {
				String result = "1" + sign;
				for (int i = 0; i < eLength; i++)
					result += '1';
				for (int i = 0; i < sLength; i++)
					result += '0';
				return result;
			}
		}
		// ��������λ
		else {
			fraction = fraction.substring(1);
		}
		// ����β��
		while(fraction.charAt(0) == '0') {
			fraction = leftShift(fraction, 1);
			maxExponent --;
		}
		// ָ�����磬�ǹ�񻯱�ʾ
		if(maxExponent < 2 - Math.pow(2, eLength - 1)) {
			String result = "0" + sign;
			while(maxExponent < 2 - Math.pow(2, eLength - 1))
				fraction = '0' + fraction;
			for (int i = 0; i < eLength; i++)
				result += '0';
			result += fraction.substring(1, sLength + 1);
			return result;
		}
		// ��񻯱�ʾ
		String result = "0" + sign + integerRepresentation("" + (maxExponent + (int)Math.pow(2, eLength - 1) - 1), eLength);
		result = result + fraction.substring(1, sLength + 1);
		return result;
	}
	
	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// �ı��������
		operand2 = operand2.charAt(0) == '0' ? '1' + operand2.substring(1) : '0' + operand2.substring(1);
		return floatAddition(operand1, operand2, eLength, sLength, gLength);
	}
	
	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		String result = "";
		// ������������֮һΪ0
		if(floatTrueValue(operand1, eLength, sLength).equals("0") || floatTrueValue(operand2, eLength, sLength).equals("0")) {
			for (int i = 0; i <= eLength + sLength + 1; i++)
				result += '0';
			return result;
		}
		// ������������Ϊ0
		char sign = xor(operand1.charAt(0), operand2.charAt(0));
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// ָ�����
		int exponent = exponent1 + exponent2;
		// β����ˣ�β��������չΪ4�ı�����
		int extention = ((1 + sLength) / 4 + 1) * 4;
		String product = integerMultiplication("0" + fraction1, "0" + fraction2, extention * 2).substring(1);
		product = product.substring((extention - sLength - 1) * 2);
		// ������
		for (int i = 0; i < product.length(); i++)
			if(product.charAt(i) == '1') {
				exponent += 1 - i;
				product = product.substring(i + 1);
				break;
			}
	    // ָ������
		if(exponent >= Math.pow(2, eLength - 1)) {
			result = "1" + sign;
			for (int i = 0; i < eLength; i++)
				result += '1';
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// ָ�����磬�ǹ�񻯱�ʾ
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
		// ��񻯱�ʾ
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
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		String result = "";
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		char sign = xor(sign1, sign2);
		// ����Ϊ0������NaN
		if(floatTrueValue(operand2, eLength, sLength).equals("0")) {
			result = "0" + sign + result;
			for (int i = 0; i < eLength + sLength; i++)
				result = '1' + result;
			return result;
		}
		// ������Ϊ0������0
		if(floatTrueValue(operand1, eLength, sLength).equals("0")) {
			for (int i = 0; i < 2 + eLength + sLength; i++)
				result = '0' + result;
			return result;
		}
		// ����������������Ϊ0
		int exponent1 = Integer.parseInt(integerTrueValue('0' + operand1.substring(1, eLength + 1)));
		int exponent2 = Integer.parseInt(integerTrueValue('0' + operand2.substring(1, eLength + 1)));
		String fraction1 = operand1.substring(eLength + 1);
		String fraction2 = operand2.substring(eLength + 1);
		fraction1 = exponent1 == 0 ? '0' + fraction1 : '1' + fraction1;
		exponent1 -= exponent1 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		fraction2 = exponent2 == 0 ? '0' + fraction2 : '1' + fraction2;
		exponent2 -= exponent2 == 0 ? (int)(Math.pow(2, eLength - 1) - 2) : (int)(Math.pow(2, eLength - 1) - 1);
		// ָ�����
		int exponent = exponent1 - exponent2;
		// β������������ȷ��С�����2*(1+sLength)λ����β��������չΪ4�ı�����
		int extention = (((1 + sLength) * 3) / 4 + 1) * 4;
		// ������չ���������ұ�����2*(1+sLength)λ0���������0������ֱ�������0��
		for (int i = 0; i < (sLength + 1) * 2; i++)
			fraction1 += '0';
		for (int i = (1 + sLength) * 3; i < extention; i++)
			fraction1 = '0' + fraction1;
		for (int i = 1 + sLength; i < extention; i++)
			fraction2 = '0' + fraction2;
		String quotient = integerDivision(fraction1, fraction2, extention).substring(1, extention + 1);
		// ������
		for (int i = 0; i < extention; i++) {
			if(quotient.charAt(i) == '1') {
				exponent += extention - (sLength + 1) * 2 - 1 - i;
				quotient = quotient.substring(i + 1);
				break;
			}
		}
		// ָ�����磬����Inf
		if (exponent >= Math.pow(2, eLength - 1)) {
			// ���λ��Ϊ1
			result = "1" + sign;
			for (int i = 0; i < eLength; i++)
				result += '1';
			for (int i = 0; i < sLength; i++)
				result += '0';
			return result;
		}
		// ָ�����磬�ǹ�񻯱�ʾ
		if(exponent < 2 - Math.pow(2, eLength - 1)) {
			quotient = '1' + quotient;
			while(exponent < 2 - Math.pow(2, eLength - 1)) {
				quotient = '0' + quotient;
				exponent ++;
			}
			// ���λ��Ϊ0
			result = "0" + sign;
			for (int i = 0; i < eLength; i++)
				result += '0';
			result += quotient.substring(1, sLength + 1);
			return result;
		}
		// ��񻯱�ʾ
		result = "0" + sign + integerRepresentation("" + (exponent + (int)Math.pow(2, eLength - 1) - 1), eLength);
		result += quotient.substring(0, sLength);
		return result;
	}
	
}

