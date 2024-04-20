f4 进入project setting
选择 Artifacts，添加JAR, 选择 from module...

再在工具栏选择 Build-> Build Artifacts..

参考：https://cloud.tencent.com/developer/article/1898130


gradle:
```kotlin
tasks.jar {
manifest {
attributes["Main-Class"] = "TestJarKt"
}

    // 下方的依赖打包可能会有重复文件，设置排除掉重复文件
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // 将依赖一起打包进jar
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}
```

