package com.skyward.router.gradle
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils

import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class RouterMappingTransform extends Transform {

    @Override
    String getName() {
        return this.getClass().simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        RouterMappingCollector collector = new RouterMappingCollector()
        transformInvocation.inputs.each {
            it.directoryInputs.each { directoryInput ->
                def destinationDir = transformInvocation.getOutputProvider()
                        .getContentLocation(directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                collector.collect(directoryInput.file)
                FileUtils.copyDirectory(directoryInput.file, destinationDir)
            }
            it.jarInputs.each { jarInput->
                def destination = transformInvocation.getOutputProvider()
                        .getContentLocation(jarInput.name, jarInput.contentTypes,
                                jarInput.scopes, Format.JAR)
                collector.collectFromJarFile(jarInput.file)
                FileUtils.copyFile(jarInput.file, destination)
            }
        }
        println("all mapping class name = " + collector.getMappingClassName())
        def mappingJarFile = transformInvocation.getOutputProvider()
                .getContentLocation("router_mapping", getOutputTypes(),
                        getScopes(), Format.JAR)
        println("mappingJarFile = $mappingJarFile")
        if (!mappingJarFile.getParentFile().exists()) {
            mappingJarFile.getParentFile().mkdir()
        }
        if (mappingJarFile.exists()) {
            mappingJarFile.delete()
        }
        FileOutputStream fileOutputStream = new FileOutputStream(mappingJarFile)
        JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream)
        ZipEntry zipEntry = new ZipEntry(RouterMappingByteCodeBuilder.CLASS_NAME + ".class")
        jarOutputStream.putNextEntry(zipEntry)
        jarOutputStream.write(RouterMappingByteCodeBuilder.get(collector.getMappingClassName()))
        jarOutputStream.closeEntry()
        jarOutputStream.close()
        fileOutputStream.close()
    }
}