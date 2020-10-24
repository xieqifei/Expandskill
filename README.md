# Expandskill
王者外挂工具


# 1：思路

ADB全称安卓设备调试工具，是谷歌官方提供帮助安卓开发者调试自身应用的一个轻量级工具包，总大小只有几兆。通过shell指令可以实现对安装了安卓系统的手机进行控制，包括开关机，开关应用程序，控制屏幕、音量、相机等操作。利用ADB控制屏幕的功能，可以通过程序来实现快速换装、婉儿一键上天等操作。由于ADB是一种模拟人在屏幕上操作的工具，不会修改王者荣耀的内部文件，也不用root手机，所有是不会被王者荣耀检测到，也不会被封号。只需要简单配置让安装了ADB的电脑连接手机，即可实现程序控制王者角色。因为ADB支持通过TCP/IP协议，可以让电脑脱离数据线远程调试手机。

**那么这个外挂完整的工作过程是什么样的呢？**

1. 在电脑上安装ADB工具，并将其目录放到系统环境或用户环境变量中。开启手机的USB调试功能，通过ADB远程调试功能连接手机。

2. 编写一个安卓应用程序，程序使用安卓的悬浮窗功能。在王者荣耀面板上层悬浮一个按钮。点击按钮，发送一个http的get请求。

3. 在安装了ADB的电脑上安装一个http服务器用于接收悬浮按钮发送的get请求。简单的http服务器是基于python内自带的http server。

4. http服务器接收到请求后，执行已经写好的ADB指令，控制手机，实现婉儿一键上天。

看看使用效果吧！

<div style="position: relative; width: 100%; height: 0; padding-bottom: 75%;">
    <iframe src="//player.bilibili.com/player.html?aid=415048053&bvid=BV18V411y7rw&cid=249226194&page=1&high_quality=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="position: absolute; width: 100%; height: 100%; left: 0; top: 0;">
    </iframe>
</div>

# 2：ADB工具及调试手机

## 2.1 下载ADB

在网上下载ADB工具包：[下载地址](https://developer.android.com/studio/releases/platform-tools)（需要连接外网）

解压后，获得一个名为`platform-tools`的工具包，包内含有`adb.exe`程序，即为安卓调试工具。

## 2.2 设置环境

在windows中搜索`huanjing`打开环境变量面板。

![](https://i.loli.net/2020/10/24/RqTVla3Mpu89tnP.png)

依次点击`环境变量`，点击系统变量下的名为`Path`的一行，点击`编辑`.

![](https://i.loli.net/2020/10/24/NoKyh4bwWqzMSFj.png)

点击新建，将`platform-tools`文件夹的完整路径添加到变量中，例：我的工具包存放在`C:\Users\9752\.AndroidStudio3.5\platform-tools`

点击`确认`，然后关闭此窗口。

在windows中搜索`cmd`，打开终端窗口，输入`adb`回车。

![](https://i.loli.net/2020/10/24/ortdFpacx3NUZqK.png)

出现上文，则配置成功。

## 2.3 连接手机

**打开手机开发者模式**

通常是手机设置中，找到版本信息或者内核信息，然后点击七下，在设置中就会出现开发者模式了。具体方法可以自行谷歌。

打开开发者模式的`USB调试`

通过**原装**数据线连接电脑，**有的非原装数据线只能充电不能传输数据**。

连接电脑后，需要在手机上允许此电脑调试，最好一律允许此电脑调试。

在终端中输入`adb devices`查看是否连接上了手机。

## 2.4 开启远程调试

> 也可不用远程，直接使用数据线也是可以的。

让手机和电脑处在相同的WiFi下

电脑通过数据线连接手机后，终端输入

```shell
adb tcpip 5555
```

拔掉数据线。在WiFi设置里，查看手机的局域网ip地址，通常是`192.168.xxx.xxx`，或者`10.xxx.xxx.xxx`

修改为你的手机ip地址，连接手机：

```shell
adb connect 192.168.xxx.xxx:5555
```

## 2.5 主要用到的ADB指令

```shell
// 模拟点击，屏幕上横坐标纵坐标分别为100 120的位置
// 要查看具体坐标值，可以打开开发者选项->指针位置
adb shell input tap 100 120

// 模拟滑动，从位置（0,1000）滑动到(800,600)
adb shell input swipe 0 1000 800 600

// 模拟长按，在位置（100,200）长按500毫秒
adb shell input swipe 100 200 100 200 500// 开启adb服务
```

其中位置信息，需要在手机上获取。

在**开发者模式**中打开**显示指针**

通过按住屏幕，就能实时显示按压住的位置的x轴和y轴信息。

此时已经可以尝试用ADB指令控制屏幕了。

## 2.6 控制婉儿上天

不同的手机，需要自己寻找合适的坐标点来实现婉儿的技能释放。

```shell
向前移动：abd shell input swipe 450 1105 544 983 600
向后移动：adb shell input swipe 450 1105 217 1400 800
普工：adb shell input tap 2726 1226
一技能：adb shell input swipe 2290 1267 2748 790 100
二技能：adb shell input swipe 2451 1010 2793 670 100
三技能：adb shell input swipe 2721 860 2721 860 200

向前直线上天：adb shell "input swipe 2451 1010 2793 670 100&&input swipe 2721 860 2721 860 200&&input swipe 450 1105 544 983 600&&input tap 2726 1226&&input swipe 2290 1267 2748 790 100&&input swipe 450 1105 743 781 1000"
原地上天：adb shell "input swipe 2451 1010 2793 670 100&&input swipe 2721 860 2721 860 200&&input swipe 450 1105 544 983 600&&input tap 2726 1226&&input swipe 2290 1267 2100 1376 100&&input swipe 450 1105 217 1400 800&&input swipe 450 1105 777 1150 500"
```

## 2.7 一键换装

```shell
打开商店： adb shell input tap 2873 102
装备： adb shell input tap 2173 1278
出售： adb shell input tap 2525 1153
关闭商店： adb shell input tap 2624 167
购买预选：adb shell input tap 2832 243
完整操作链：adb shell "input tap 2873 102&&input tap 2173 1278&&input tap 2525 1153&&input tap 2624 167&&input tap 2832 243"
```

# 3：搭建http服务器

在电脑上安装好python环境。

新建一个`takeoff.py`文件。编辑文件：

```python
import os
from http.server import BaseHTTPRequestHandler
from urllib import parse

class GetHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        parsed_path = parse.urlparse(self.path)
        query = parse.parse_qs(parsed_path.query)
        if query['skill'][0] == '1':
            os.system('adb shell "input swipe 2451 1010 2793 670 100&&input swipe 2721 860 2721 860 200&&input swipe 450 1105 544 983 600&&input tap 2726 1226&&input swipe 2290 1267 2748 790 100&&input swipe 450 1105 743 781 1000"')
            message = '执行直线起飞指令'
        elif query['skill'][0] == '2':
            os.system('adb shell "input swipe 2451 1010 2793 670 100&&input swipe 2721 860 2721 860 200&&input swipe 450 1105 544 983 600&&input tap 2726 1226&&input swipe 2290 1267 2100 1376 100&&input swipe 450 1105 217 1400 800&&input swipe 450 1105 777 1150 500"')
            message = '执行原地起飞指令'
        elif query['skill'][0] == '3':
            os.system('adb shell "input tap 2873 102&&input tap 2173 1278&&input tap 2525 1153&&input tap 2624 167&&input tap 2832 243"')
            message = '执行快速换装指令'
        else:
            message = '无指令运行'
        self.send_response(200)
        self.send_header('Content-Type',
                         'text/plain; charset=utf-8')
        self.end_headers()
        self.wfile.write(message.encode('utf-8'))

if __name__ == '__main__':
    from http.server import HTTPServer
    server = HTTPServer(('0.0.0.0', 9797), GetHandler)
    print('服务器启动成功, 使用<Ctrl-C> 来停止。。')
    server.serve_forever()
```

需要将`os.system()`中的**内容替换为自己**的。

> 修改上面的程序很重要，因为不同分辨率手机，需要的adb指令是不一样的，如果不修改，基本上不可能会实现理想的操作，除非你跟我用的相同的手机，三星S10+。

通过python运行这个文件就已经建立好了http服务器。

```shell
python3 filepath/takeoff.py
#filepath修改为takeoff.py的路径。比如C:/Users/999/Desktop/takeoff.py
#显示，服务器启动成功, 使用<Ctrl-C> 来停止。。则启动成功
```

查看电脑的局域网ip地址，终端输入

```shell
ipconfig
```

通常是`192.168.xxx.xxx`，或者`10.xxx.xxx.xxx`

在电脑浏览器中，访问：`192.168.xxx.xxx:9797/?skill=1`就能实现直线上天了。skill =2和skill=3分别对应原地上天和一键换装。

此时，如果在手机浏览器上访问`192.168.xxx.xxx:9797/?skill=1`，应该是访问不通的，因为windows自带的防火墙，是不允许局域网下的其他设备访问未开放的端口的。你需要**关闭局域网防火墙**。

> 关闭防火墙是存在一定风险的，如果你用的公共场所的WiFi，我建议不要关闭。如果是家庭WiFi，记得在使用完后再次开启防火墙。

windows搜索`fanghuoqiang`注意不要用汉字搜索，用拼音。打开**防火墙和网络保护**

如果是家庭WiFi，则点击专用网络。

![](https://i.loli.net/2020/10/24/wJDbeTHg36ntWzm.png)

将防火墙**关闭**。

此时在手机浏览器上也能访问到http服务器了。

# 4：王者局内悬浮按钮

新建一个安卓项目，开发一个悬浮按钮，在王者内点击按钮，就能给http服务器发送请求，然后服务器通过adb再返过来控制局内角色。

开发新项目，我直接选择站在巨人肩膀上。https://github.com/dongzhong/TestForFloatingWindow 直接对这个仓库里提供的悬浮窗功能进行修改。。关键程序如下：

```java
//FloatingButtonService2.java
private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            button2 = new Button(getApplicationContext());
            button2.setText("装");//按键上的字，表示按下这个按钮一键换装
            button2.setBackgroundColor(Color.argb(120,0,255,0));
            windowManager.addView(button2, layoutParams);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send();
                }
            });
            button2.setOnTouchListener(new FloatingOnTouchListener());

        }
    }
    private void send() {
        //开启线程，发送get请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                //value对应http服务器接收的技能
                //在http服务器中，设置的，skill=1，直线上天，skill=2，原地上天，skill=3，换装
                char value = '3';
                //这里修改为http服务器地址。
                String path = "http://192.168.0.109:9797/?skill="+value;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = conn.getInputStream();
                    conn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
```

这只是其中一个一键换装的按钮，另外还需要修改一键上天的按钮。在`FloatingButtonService.java`中，两个程序，都相似。。修改完后，直接编译，就可以使用了。

完整的项目地址：https://github.com/xieqifei/Expandskill

安卓项目，可以直接通过git克隆

```shell
git clone https://github.com/xieqifei/Expandskill.git
```

修改程序后，直接编译即可。

# 5：参考资料

《[android adb常用命令收集](https://www.jianshu.com/p/85066854c9e6)》

《[Android adb远程调试](https://blog.csdn.net/u012785382/article/details/79171782)》

《[http.server — 实现 Web 服务器的基础类](https://learnku.com/docs/pymotw/httpserver-base-classes-for-implementing-web-servers/3436)》

《[Android HTTP请求方式:HttpURLConnection](https://www.runoob.com/w3cnote/android-tutorial-httpurlconnection.html)》

《[Android悬浮窗的实现](https://blog.csdn.net/dongzhong1990/article/details/80512706)》
