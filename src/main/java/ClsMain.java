import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClsMain {

    public static void main(String[] args) throws IOException {
        testGenerateClass();
        testReadClass();
    }

    private static void testReadClass() throws IOException {
        FileInputStream is = new FileInputStream(new File("out/production/classes/ClassToRead.class"));

        ClassReader reader = new ClassReader(is);

        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5) {

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                return Utils.logProxy(new AnnotationVisitor(Opcodes.ASM5) {
                    @Override
                    public void visit(String name, Object value) {
                        super.visit(name, value);
                    }

                    @Override
                    public AnnotationVisitor visitArray(String name) {
                        return super.visitArray(name);
                    }
                });
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return Utils.logProxy(new MethodVisitor(Opcodes.ASM5) {

                    @Override
                    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
                        super.visitLocalVariable(name, descriptor, signature, start, end, index);
                    }

                    @Override
                    public void visitInsn(int opcode) {
                        super.visitInsn(opcode);
                    }

                    @Override
                    public void visitLdcInsn(Object value) {
                        super.visitLdcInsn(value);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }

                    @Override
                    public void visitParameter(String name, int access) {
                        super.visitParameter(name, access);
                    }
                });
            }
        };

        reader.accept(visitor, 0);
        is.close();
    }

    /**
     * 生成class
     */
    private static void testGenerateClass() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, writer) {
        };
        String className = "com.demo.GeneratedClass".replace('.', '/');
        cv.visit(50, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                "init", "()V", null, null);

        mv.visitCode();

        String[] classes = {"ClassA", "ClassB"};
        for (String clazz : classes) {
            mv.visitLdcInsn("arg1");
            mv.visitLdcInsn("arg2");
            mv.visitInsn("ClassA".equals(clazz) ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, clazz.replace('.', '/'),
                    "init",
                    "(Ljava/lang/String;Ljava/lang/String;Z)V",
                    false);
        }
        mv.visitMaxs(0, 0);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
        cv.visitEnd();

        File dest = new File("out", className + ".class");
        dest.getParentFile().mkdirs();
        try {
            new FileOutputStream(dest).write(writer.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
