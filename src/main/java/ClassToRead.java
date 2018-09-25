import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ClassToRead.AnnotationA(value = 15, classes = {ClassToRead.ClassA.class, ClassToRead.ClassB.class})
public class ClassToRead {

    public static void init() {
        int a = 100;
        int b = 200;
        int c = a + b + 300;
        System.out.print("test");
        ClassA.set("A0", "B0", "C0", true);
        ClassA.set("A1", "B1", "C1", true);
        System.out.print("test");
        ClassA.set("A2", "B2", "C2", true);
        ClassA.set("A3", "B3", "C3", true);
    }

    public static class ClassA {
        public static void set(String a, String b, String c, boolean d) {

        }
    }

    public static class ClassB {

    }

    @Retention(RetentionPolicy.CLASS)
    public @interface AnnotationA {

        int value();

        Class[] classes() default {};
    }
}
