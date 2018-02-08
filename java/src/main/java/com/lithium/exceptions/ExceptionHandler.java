package com.lithium.exceptions;

/**
 * A class to allow exceptions to be handled
 * in commonly-defined ways.
 * 
 * @author Luke Stevens
 *
 */
public class ExceptionHandler {
	
	public static interface ExceptionLogic{
		public void invoke() throws Exception;
	}
	
	/**
	 * Any exceptions thrown within this logic block 
	 * will be re-thrown as runtime exceptions and
	 * will terminate the program. This is not advised
	 * to be used in live code.
	 */
	public static void doOrDie(ExceptionLogic logic){
		try {
			logic.invoke();
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Any checked exceptions thrown within this logic block
	 * will simply be ignored. Runtime exceptions, however, 
	 * will be rethrown.
	 */
	public static void ignore(ExceptionLogic logic){
		try{
			logic.invoke();
		} catch(Exception e){
	        if (e instanceof RuntimeException){
	            throw (RuntimeException) e;
	        }
		}
	}
	
	/**
	 * Any exceptions (including runtime) thrown within this logic block
	 * will simply be ignored.
	 */
	public static void ignoreAll(ExceptionLogic logic){
		try{
			logic.invoke();
		} catch(Exception e){}
	}
	
	/**
	 * Any exceptions thrown within this logic block
	 * will have their stack traces printed to STDOUT
	 */
	public static void print(ExceptionLogic logic){
		try{
			logic.invoke();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * If any exceptions are thrown from within this logic
	 * block, the exception message will be returned as a
	 * String.
	 */
	public static String asString(ExceptionLogic logic){
		try{
			logic.invoke();
		} catch(Exception e){
			return e.getMessage();
		}
		return null;
	}

}
