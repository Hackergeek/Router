package com.skyward.router.gradle

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("apply RouterPlugin")

        if (project.plugins.hasPlugin(AppPlugin)) {
            AppExtension appExtension = project.extensions.getByType(AppExtension)
            Transform transform = new RouterMappingTransform()
            appExtension.registerTransform(transform)
        }
        /*Transform transform = new RouterMappingTransform()
        project.android.registerTransform(transform)*/
        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", project.rootProject.projectDir.absolutePath)
            }
        }
        project.clean.doFirst {
            File file = new File(project.rootProject.projectDir, "router_mapping")
            if (file.exists()) {
                file.deleteDir()
            }
        }
        if (!project.plugins.hasPlugin(AppPlugin)) {
            return
        }
        project.getExtensions().create("router", RouterExtension)

        project.afterEvaluate {
            RouterExtension extension = project["router"]
            println("wikiDir=" + extension.wikiDir)
            project.tasks.findAll { task ->
                task.name.startsWith("compile") && task.name.endsWith("JavaWithJavac")
            }.each { task ->
                task.doLast {
                    File routerMappingDir =
                            new File(project.rootProject.projectDir,
                                    "router_mapping")

                    if (!routerMappingDir.exists()) {
                        return
                    }

                    File[] allChildFiles = routerMappingDir.listFiles()

                    if (allChildFiles.length < 1) {
                        return
                    }

                    StringBuilder markdownBuilder = new StringBuilder()

                    markdownBuilder.append("# 页面文档\n\n")
                    allChildFiles.each { child ->
                        if (child.name.endsWith(".json")) {
                            JsonSlurper jsonSlurper = new JsonSlurper()
                            def content = jsonSlurper.parse(child)

                            content.each { innerContent ->
                                def url = innerContent['url']
                                def description = innerContent['description']
                                def realPath = innerContent['realPath']
                                markdownBuilder.append("## $description \n")
                                markdownBuilder.append("- url: $url \n")
                                markdownBuilder.append("- realPath: $realPath \n\n")

                            }


                        }

                    }
                    File wikiFileDir = new File(extension.wikiDir)
                    if (!wikiFileDir.exists()) {
                        wikiFileDir.mkdir()
                    }
                    File wikiFile = new File(wikiFileDir, "页面文档.md")
                    if (wikiFile.exists()) {
                        wikiFile.delete()
                    }
                    wikiFile.write(markdownBuilder.toString())
                }
            }
        }
    }
}