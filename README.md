Gizwits GoKit App
==============================

    这是一款使用GizWifiSDK的Android开源代码示例APP，可以帮助开发者快速入手使用GizWifiSDK开发连接机智云的物联APP。
    
    GoKit App可作为Wi-Fi设备接入机智云的手机调试工具，功能包含了设备网络配置、用户注册登录、设备状态查询、设备远程控制等功能的参考实现，开发者可通过此开源程序学习物联网硬件相关的App实现方法。

    请注意：
    
    使用机智云开源APP之前，需要先在机智云开发平台创建您自己的产品和应用。

    开源App需要使用您申请的AppId、AppSecret以及您自己的产品ProductKey才能正常运行。

    具体申请流程请参见：http://docs.gizwits.com/hc/。

    上述信息申请好之后，在代码中请找到"your_app_id"、"your_app_secret"、"your_product_key"字符串做相应的替换。

    使用QQ帐号登录、百度或极光推送功能之前，需要您先到相应网站申请QQ登录的AppID、百度或极光推送的AppKey，在配置文件中作相应的替换。

    配置文件位置：assets/UIConfig.json


GizWifiSDK 版本号

    2.05.05.2916


功能介绍
    
    项目主要展示如何使用GizWifiSDK开发基于GAgent的智能硬件APP，用到了大部分主要SDK接口，供使用GizWifiSDK的开发者参考。主要功能如下：

    ▪	初始化SDK
    ▪   用户登录到云端
    ▪	设备配置入网
    ▪	搜索设备列表
    ▪	绑定或解绑设备
    ▪	设备登录
    ▪	设备控制


项目依赖和安装

    ▪	GizWifiSDK的jar包和支持库
        登录机智云官方网站http://gizwits.com的开发者中心，下载并解压最新版本的SDK。
        下载后，将解压后的目录拷贝到复制到 Android 项目 libs 目录即可。



GoKit硬件依赖

    GoKit项目调试，需要有调试设备的支持，您可以使用虚拟设备或者实体设备搭建调试环境。

    ▪	虚拟设备
        机智云官网提供GoKit虚拟设备的支持，链接地址：
        http://site.gizwits.com/developer/product/631/virtualdevice

    ▪	实体设备
        GoKit开发板。您可以在机智云官方网站上免费预约申请（限量10000台），申请地址：
        http://gizwits.com/zh-cn/gokit

    GoKit开发板提供MCU开源代码供智能硬件设计者参考，请去此处下载：https://github.com/gizwits/gokit-mcu



问题反馈

    您可以给机智云的技术支持人员发送邮件，反馈您在使用过程中遇到的任何问题。
    邮箱：club@gizwits.com
