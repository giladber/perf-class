package iaf.perf.course.day3;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import sun.misc.Unsafe;

public class BrokenUnsafe 
{

	private static final Unsafe UNSAFE;
	
	static
    {
		//retrieve an instance of sun.misc.Unsafe
        try
        {
        	PrivilegedExceptionAction<Unsafe> action = () -> {
            	Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            };
            
            UNSAFE = AccessController.doPrivileged(action);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }
	
	/**
	 * Don't do this, ever.
	 * 
	 * If you really wish to find the size of an object, use the JOL (Java Object Layout)
	 * library.
	 * 
	 * @param object Object whose size we supposedly return
	 * @return Supposed size of the input object
	 */
	public static long sizeOf(Object object) {
		int addressOfKlassInObjectHeader = UNSAFE.getInt(object, 4L);
		long nativeAddressOfKlass = normalize(addressOfKlassInObjectHeader);
		long addressOfLayoutHelper = nativeAddressOfKlass + 12L;
		return UNSAFE.getAddress(addressOfLayoutHelper);
	}
	
	//wat
	public static long normalize(int value) {
		if(value >= 0) return value;
		return (~0L >>> 32) & value;
	}
	
}
