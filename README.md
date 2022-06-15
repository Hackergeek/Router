# Router
一款简单的路由框架

## 实现步骤

### 1.标记页面

使用注解标记页面

### 2. 收集页面

![image-20220614224612125](/Users/skyward/WorkSpace/AndroidProjects/Router/images/APT工作原理.png)

使用注解处理器处理注解信息，生成mapping_timeInMills.json文件和RouterMapping_timeInMills类。

其中mapping_timeInMills.json文件内容模板如下：

```json
[
  {
    "url": "router://page-home",
    "description": "应用主页",
    "realPath": "com.skyward.router.MainActivity"
  },
  {
    "url": "router://page-kotlin",
    "description": "Kotlin页面",
    "realPath": "com.skyward.router.KotlinActivity"
  },
  {
    "url": "router://skyward/profile",
    "description": "个人信息",
    "realPath": "com.skyward.router.ProfileActivity"
  }
]
```

RouterMapping_timeInMills类内容模板如下：

```java
package com.skyward.router.mapping;

import java.util.HashMap;
import java.util.Map;

public class RouterMapping_1655216516998 {

    public static Map<String, String> get() {

        Map<String, String> mapping = new HashMap<>();

        mapping.put("router://page-home", "com.skyward.router.MainActivity");
        mapping.put("router://page-kotlin", "com.skyward.router.KotlinActivity");
        mapping.put("router://skyward/profile", "com.skyward.router.ProfileActivity");
        return mapping;
    }
}
```

### 3. 生成文档

编写gradle插件，在compileFlavorJavaWithJavac这个任务执行完成后（因为在javac执行完成之后，注解处理器已经处理完成注解信息并生成mapping_timeInMills.json文件），遍历之前所有Module生成的mapping_timeInMills.json文件，生成Markdown文档。

### 4. 注册映射

在gradle插件中注册transform，在transform过程中遍历所有Module生成的RouterMapping_xxx类通过ASM生成一个RouterMapping类。

### 5. 打开页面

创建Router工具类初始化RouterMapping类，实现路由功能
