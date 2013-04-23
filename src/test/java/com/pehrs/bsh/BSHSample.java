package com.pehrs.bsh;

import bsh.EvalError;
import bsh.Interpreter;

public class BSHSample {

	/**
	 * @param args
	 * @throws EvalError 
	 */
	public static void main(String[] args) throws EvalError {
		Interpreter interpreter = new Interpreter();  // Construct an interpreter
		interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());
		
		Object val = interpreter.eval("return com.pehrs.json.model.Grade.toGrade(\"i1\", 8);");
		System.out.println("val="+val);
	}

}
