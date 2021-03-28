/**
 * This class takes a mathmatical expression in infix notation and converts the expression to
 * Postfix notation. Currently this class supports a given table of unique functions,
 * @see	isFunction().
 *
 * @author Nicholas Boren
 * @version %I%, %G%
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfixToPostFix {
	private List<String> parsedExpression;
	
	public static void main(String[] args) {
		InfixToPostFix expression = new InfixToPostFix("5x^2 / sin(x)");
		System.out.println(expression.infixToPostFix());
	}
	
	/**
	 * Constructs a single instance of the InfixToPostFix object.
	 * 
	 * @param expression - The expression that will be converted.
	 */
	public InfixToPostFix(String expression) {
		if (expression == null || expression.isEmpty()) {
			throw new IllegalArgumentException("Please Enter a Non Empty Output");
		}
		
		parsedExpression = parseExpression(expression);
	}

	/**
	 * Takes in a parsed Expression as a list where each element of the list represents:
	 * a operand, operator, and a function. Uses the Shunting-yard algorithm to return the given
	 * expression in Postfix form.
	 * 
	 * @param parsedExpression - The parsed Expression being transformed.
	 * @return - The given expression in Postfix from.
	 */
	public String infixToPostFix() {
		Stack<String> operator = new Stack<String>();
		String postFixExpression = "";
		
		while (!parsedExpression.isEmpty()) {
			String curr = parsedExpression.remove(0);
			if (isFunction(curr)) {
				operator.push(curr);
			} else if (isOperand(curr.charAt(0))) {
				postFixExpression += curr + " ";
			} else if (isOperator(curr.charAt(0))) {
				while (!operator.empty() && !curr.equals("(") && precedenceChecker(operator.peek().charAt(0), curr.charAt(0))) {
					postFixExpression += operator.pop() + " ";
				}
				
				operator.push(curr);
				
			} else if (curr.equals("(")) {
				operator.push(curr);
			} else if (curr.equals(")")) {
				while (!operator.empty() && !operator.peek().equals("(")) {
					postFixExpression += operator.pop() + " ";
				}
				
				if (operator.peek().equals("(")) {
					operator.pop();
				}				
				
				if (isFunction(operator.peek())) {
					postFixExpression += operator.pop() + " ";
				}
			}
		}
				
		while (!operator.empty()) {
			postFixExpression += operator.peek() + " ";
			operator.pop();
		}
		
		return postFixExpression;
	}
	
	/**
	 * Takes in a mathematical expression and splits the expression into different chunks each time
	 * a new type of character is found. These types being Numbers, Variables, and Special Characters.
	 * 
	 * @param s - The expression currently being worked with.
	 * @return - The parsed expression split into different chunks.
	 */
	private List<String> parseExpression(String s) {
		List<String> output = new ArrayList<>();
		Matcher match = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+|[+-/*^()]").matcher(s);
		while (match.find()) {
			output.add(match.group());
		}
		
		return output;
	}
	
	/**
	 * Checks if the given token is a pre-defined function
	 * 
	 * @param s - The current token being looked at.
	 * @return - If our token is a function.
	 */
	private static boolean isFunction(String s) {
		if (s.equalsIgnoreCase("sin")) {
			return true;
		} else if (s.equalsIgnoreCase("cos")) {
			return true;
		} else if (s.equalsIgnoreCase("tan")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the given token is a valid number or variable.
	 * 
	 * @param c - The current token being looked at.
	 * @return - If our token is a valid number or variable.
	 */
	private static boolean isOperand(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		
		if (c >= 'a' && c <= 'z') {
			return true;
		}
		
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Checks if the given token is a valid operating symbol.
	 * 
	 * @param c - The current token being worked with.
	 * @return - If our token is a valid operating symbol.
	 */
	public static boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the current weight the operator being looked at based on the rules of PEMDAS.
	 * 
	 * @param operator - The current operator being worked with.
	 * @return - The precedence of the operator.
	 */
	private static int getOperatorWeight(char operator) {
		int weight = -1;
		
		switch (operator) {
			case '+':
			case '-':
				weight = 1;
				break;
				
			case '*':
			case '/':
				weight = 2;
				break;
			
			case '^':
				weight = 3;
				break;
		}
		
		return weight;
	}
	
	/**
	 * Compares two different operators and returns if the first operators has greater or less 
	 * precedence than the other operator.
	 * 
	 * @param operatorOne - The first operator being worked with.
	 * @param operatorTwo - The other operator being worked with.
	 * @return - The result if the first operator has higher or less precedence than the other operator.
	 */
	public static boolean precedenceChecker(char operatorOne, char operatorTwo) {
		int oneWeight = getOperatorWeight(operatorOne);
		int twoWeight = getOperatorWeight(operatorTwo);
		
		if (oneWeight == twoWeight) {
			if (isRightAssociative(operatorOne)) {
				return false;
			} else {
				return true;
			}
		} else {
			return oneWeight > twoWeight;
		}
	}
	
	/**
	 * Returns the result if our operator is a exponent or not.
	 * 
	 * @param operator - The operator being looked at
	 * @return - The result if our operator is a exponent or not.
	 */
	private static boolean isRightAssociative(char operator) {
		return (operator == '^');
	}
}
