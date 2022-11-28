package util;

import java.io.Serializable;

import javax.ejb.Singleton;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LoggingInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@AroundInvoke
	public Object methodInterceptor(InvocationContext ctx) throws Exception {
		Object callToMethod = null;
		System.out.println("**************** Intercepting call to method: " + ctx.getTarget().getClass() + "." + ctx.getMethod().getName() + "()");
		
		try {
			callToMethod = ctx.proceed();
		} catch (Exception e) {
			throw e;
		} finally {
			System.out.println("**************** Leaving method: " + ctx.getTarget().getClass() + "." + ctx.getMethod().getName() + "()");
		}
		return callToMethod;
	}
}
