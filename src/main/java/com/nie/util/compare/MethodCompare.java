package com.nie.util.compare;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * Description:
 * <br> 对两个不同的对象中具有相同参数、相同返回类型、相同名称的方法的返回值进行比较
 * <br> {@link IgnoreCompare} 忽略不想比较的同名方法
 * @author: niedaoxin
 * date:   2017年3月5日 下午6:39:27   
 * @param <E>
 * @param <T>
 */
public class MethodCompare<E, T>
{
	private E e;
	private T t;
	
	public MethodCompare(E e, T t)
	{
		super();
		this.e = e;
		this.t = t;
	}
	
	/**
	 * 
	 * Description: 比较两个对象中具有相同参数、相同返回类型、相同名称的方法返回值，如果存在值不同返回true，如果所有值相同返回false
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public boolean isDiff() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method[] eMethods = e.getClass().getDeclaredMethods();
		Method[] tMethods = t.getClass().getDeclaredMethods();
		Object [] emptyArray = new Object[0];
		for(Method eMethod : eMethods)
		{
			if(!eMethod.getName().contains("get"))
				continue;
			IgnoreCompare eig = eMethod.getAnnotation(IgnoreCompare.class);
			if(eig != null)
				continue;
			for(Method tMethod : tMethods)
			{
				IgnoreCompare tig = tMethod.getAnnotation(IgnoreCompare.class);
				if(tig != null)
					continue;
				
				Class<?>[] params1 = eMethod.getParameterTypes();
				Class<?>[] params2 = tMethod.getParameterTypes();
				if (params1.length == params2.length)
				{
					for (int i = 0; i < params1.length; i++)
					{
						if (params1[i] != params2[i])
							break;
					}
				}
				else
					continue;
				
				if(eMethod.getName().equals(tMethod.getName())
						&& eMethod.getReturnType().equals(tMethod.getReturnType())
						)
				{
					Object eo = eMethod.invoke(e, emptyArray);
					Object to = tMethod.invoke(t, emptyArray);
					if(eo == null && to == null)
						return false;
					if((eo == null && to != null)
							|| (eo!=null && to == null)
							|| (!eo.equals(to)))
						return true;
						
				}
			}
		}
		return false;
	}
}
