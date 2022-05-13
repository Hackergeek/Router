package com.skyward.router.gradle

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class RouterMappingByteCodeBuilder implements Opcodes {
    public static final String CLASS_NAME = 'com/skyward/router/mapping/generated/RouterMapping'

    static byte[] get(Set<String> allMappingNames) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        MethodVisitor methodVisitor;
        // 创建一个类
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER,
                CLASS_NAME, null, "java/lang/Object", null)
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "<init>", "()V", null, null)
        // 开始生成构造方法的字节码
        methodVisitor.visitCode()
        methodVisitor.visitVarInsn(ALOAD, 0);
        // 调用父类的构造方法
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object",
                "<init>", "()V", false)
        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()

        // 创建get方法
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "get",
                "()Ljava/util/Map;",
                "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;", null)
        methodVisitor.visitCode()
        methodVisitor.visitTypeInsn(NEW, "java/util/HashMap")
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap",
                "<init>", "()V", false)
        methodVisitor.visitVarInsn(ASTORE, 0)
        allMappingNames.each {
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitMethodInsn(INVOKESTATIC, "$RouterMappingCollector.PACKAGE_NAME$it", "get",
                    "()Ljava/util/Map;", false)
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map",
                    "putAll", "(Ljava/util/Map;)V", true)
        }
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitInsn(ARETURN)
        methodVisitor.visitMaxs(2, 1)
        methodVisitor.visitEnd()

        return classWriter.toByteArray()
    }
}