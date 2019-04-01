##### 硬件&软件环境
1. 阿里云服务器
> ![服务器配置](https://upload-images.jianshu.io/upload_images/1839165-19d75e62091cadda.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)
2. zk版本
 > zookeeper-3.4.13

### 踩坑
0. 本地测试代码连接服务器超时
 > 测试代码&Error
![demo](https://upload-images.jianshu.io/upload_images/1839165-7ea032fe9ab1b5ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1080)
> ECS默认关闭入网端口，配置安全组规则，开放端口解决问题
![ECS](https://upload-images.jianshu.io/upload_images/1839165-b57719ec65f8ceb2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![成功](https://upload-images.jianshu.io/upload_images/1839165-56c7104592b1c402.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


