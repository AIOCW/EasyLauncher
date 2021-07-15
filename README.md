# EasyLauncher
及其简单的Android桌面

[APP 下载地址](https://github.com/AIOCW/EasyLauncher/releases)

## 视频介绍
![演示视频](https://ps.aiocw.com/easylauncher/0.mp4)
## 图形介绍
![图一](https://ps.aiocw.com/easylauncher/1.jpg)
![图二](https://ps.aiocw.com/easylauncher/2.jpg)
![图三](https://ps.aiocw.com/easylauncher/3.jpg)


# 结构介绍
分为对整体包结构介绍，对类的简要介绍
## common
大概是一些公共类
## desktop
桌面相关的类与代码
## extendfun  
额外功能，例如桌面记事提示、文件转存
 
## 数据结构介绍
账户（账户名，账户号，账户金额）

金额、消费类型（支出、收入）、使用账户、消费详情、日期、时间

 
# 引用的额外库


    implementation 'androidx.recyclerview:recyclerview:1.1.0'

    implementation 'com.squareup.okhttp3:okhttp:4.4.0'

    implementation 'com.github.ybq:Android-SpinKit:1.4.0'
    implementation 'com.google.android.material:material:1.0.0'

    implementation 'de.hdodenhof:circleimageview:3.1.0'

    // https://mvnrepository.com/artifact/commons-net/commons-net
    implementation group: 'commons-net', name: 'commons-net', version: '3.6'
这个是给来获取webdav的

    implementation 'com.paul623.wdsyncer:wdsyncer:0.0.3'