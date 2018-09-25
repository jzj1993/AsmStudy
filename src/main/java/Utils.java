import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class Utils {

    @SuppressWarnings("unchecked")
    public static <T> T logProxy(T object) {
        Class<T> clazz = (Class<T>) object.getClass();
        return mock(clazz, withSettings().defaultAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                System.out.println(toInstanceName(clazz) + "." + invocation.getMethod().getName() + arrayToString(invocation.getArguments()));
                return invocation.callRealMethod();
            }
        }));
    }

    public static String arrayToString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "()";

        StringBuilder b = new StringBuilder();
        b.append('(');
        for (int i = 0; ; i++) {
            b.append(objToString(a[i]));
            if (i == iMax)
                return b.append(')').toString();
            b.append(", ");
        }
    }

    private static String objToString(Object obj) {
        if (obj instanceof String) {
            return '"' + (String) obj + '"';
        }
        return String.valueOf(obj);
    }

    private static String toInstanceName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (className.length() == 0) {
            //it's an anonymous class, let's get name from the parent
            className = clazz.getSuperclass().getSimpleName();
        }
        //lower case first letter
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
