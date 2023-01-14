

### Appium 入门 (Python)

### 零、前置

#### 0、adb

- client端，在电脑上，负责发送adb命令
- daemon守护进程，在手机上，负责接收和执行adb命令
- server端，在电脑上，负责管理client和daemon之间的通信

#### 1、获取包名、界面名

此命令在nova8上不可用（windows下`grep`替换为`findstr`）

`adb shell dumpsys window windows | grep mFocusedApp`

nova8可用

`adb shell dumpsys window windows | grep topApp`

#### 2、文件传输

发送文件到手机

`adb push pcFilePath mobileDicPath`

从手机里拉文件

`adb pull mobileFilePath pcDicPath`

3、获取app启动时间

QQ: `adb shell am start -W com.tencent.mobileqq/.activity.SplashActivity`

Tim: `adb shell am start -W com.tencent.tim/com.tencent.mobileqq.activity.SplashActivity`



### 一、Appium 起步

#### 0、安装

* `Appium-Desktop`
* ``Appium-Inspector` (也可用sdk/tools里的`UIAutomatorViewer`代替)
* Python (依赖库：`pip3 install Appium-Python-Client`)

#### 1、demo代码&执行流程

* 我们写的 python 代码会访问本机的 appium 服务器，并获取 driver 对象
* appium 会将我们的 driver 对象调用的方法转化成 post 请求，提交给appium服务器
* appium 通过接收到的 post 请求发送给手机，再由手机进行执行

打开设置页demo代码

```python
import time
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy

capabilities = dict(
    # 设备信息
    platformName='Android',
    deviceName='nova',
    udid='CTVVB21326008664',
    # 起始页面
    appPackage='com.android.settings',
    appActivity='.HWSettings',
)

appium_server_url = 'http://localhost:4723/wd/hub'

driver = webdriver.Remote(appium_server_url, capabilities)
print("webdriver.Remote")

time.sleep(3)
print("time.sleep(3) end")

el = driver.find_element(by=AppiumBy.XPATH, value='//*[@text="桌面和壁纸"]')
el.click()
print("桌面和壁纸.click()")

time.sleep(10)
```

#### 2、app常用方法：

* 打开应用 `driver.start_activity`
* 获取包名 `driver.current_package`
* 获取界面名`driver.current_activity`
* 关闭app `driver.close+app`
* 关闭驱动&所关联的app `driver.quit`
* 安装 `driver.install_app`
* 卸载 `driver.remove_app`
* 是否已经安装 `driver.is_app_installed`
* 放到后台再回到前台（模拟热启动）`driver.background_app`

#### 3、元素查找（等待）

##### 3.0 查找

* find_element_by_id 传参 resource-id
* find_element_by_class_name 传参 class
*  find_element_by_xpath 传参 xpath表达式

##### 3.1 等待

* 隐式等待：driver.implicitly_wait 
* 显式等待
    * 1.导包 `from selenium.webdriver.support.wait import WebDriverWait`
    * 2.创建 WebDriverWait 对象
    * 3.调用 WebDriverWait 对象的 until 方法

* 区别

    * 作用域
        * 显式等待为单个元素有效，隐式为全局元素

    * 方法
        * 显式等待方法封装在 WebDriverWait 类中，而隐式等待则直接通过 driver 实例化对象调用

    * sleep 
        * 是固定死一个时间。 元素等待可以让元素出来的第一时间进行操作。sleep 可能造成不必要的浪费。

##### 3.2 输入

- send_keys("要输入的文字")

    - 注意默认输入中文是有问题的，需要在连接手机的参数中多加两行代码

        ```python
        dic['unicodeKeyboard'] = True
        dic['resetKeyboard'] = True
        ```

#### 4、滑动和拖拽事件

##### 4.0 swipe 滑动屏幕

* driver.swipe(起始x坐标, 起始y坐标, 结束x坐标, 结束y坐标, 持续时间ms)
* 特点：参数是坐标点；持续时间短，惯性大；持续时间长，惯性小

##### 4.1 scroll 滑动屏幕

* driver.scroll(起始元素, 结束元素)
* 特点：参数是元素；没有持续时间，有惯性

##### 4.2 drag_and_drop 滑动屏幕

* driver.drag_and_drop(起始元素, 结束元素)
* 特点：参数是元素；没有持续时间，没有惯性

#### 5、高级手势 TouchAction

##### 5.0 基本手势

* 轻敲 `TouchAction(driver).tap(element=None, x=None, y=None).perform()`
* 按下 `TouchAction(driver).press(element=None, x=None, y=None).perform()`
* 抬起 `TouchAction(driver).press(element=None, x=None, y=None).release().perform()`
* 等待 `TouchAction(driver).wait(ms=0).perform()`
* 长按 `TouchAction(driver).long_press(el=None, x=None, y=None, duration=1000).perform()`
* 移动 `TouchAction(driver).move_to(el=None, x=None, y=None).perform()`

##### 5.1 组合举例

```python
TouchAction(driver).press(x=246, y=857).move_to(x=721, y=867).move_to(x=1200,
y=851).move_to(x=1200, y=1329).move_to(x=724, y=1329).move_to(x=246,
y=1329).move_to(x=718, y=1815).release().perform()
```

![image-20230114151905152](https://cdn.jsdelivr.net/gh/lugf027/pictures/imgs/image-20230114151905152.png)

#### 6、补充手机操作一些API

* 获取手机分辨率 `driver.get_window_size()`
    * 滑动时，预估滑动距离会用得到

* 截图 `driver.get_screenshot_as_file(os.getcwd() + os.sep + './screen.png')`

* 获取手机网络 `driver.network_connection`

* 设置手机网络 `driver.set_network_connection(connectionType)`

    * ```python
        def set_network_connection(self, connection_type: int) -> int:
            """Sets the network connection type. Android only.
            Possible values:
                +--------------------+------+------+---------------+
                | Value (Alias)      | Data | Wifi | Airplane Mode |
                +====================+======+======+===============+
                | 0 (None)           | 0    | 0    | 0             |
                +--------------------+------+------+---------------+
                | 1 (Airplane Mode)  | 0    | 0    | 1             |
                +--------------------+------+------+---------------+
                | 2 (Wifi only)      | 0    | 1    | 0             |
                +--------------------+------+------+---------------+
                | 4 (Data only)      | 1    | 0    | 0             |
                +--------------------+------+------+---------------+
                | 6 (All network on) | 1    | 1    | 0             |
                +--------------------+------+------+---------------+
            These are available through the enumeration `appium.webdriver.ConnectionType`
            Args:
                connection_type: a member of the enum `appium.webdriver.ConnectionType`
            Return:
                int: Set network connection type
            """
            data = {'parameters': {'type': connection_type}}
            return self.execute(Command.SET_NETWORK_CONNECTION, data)['value']
        ```

* 发送按键到设备 `driver.press_keycode(keycode, metastate=None)`
    * 值参考：https://www.cnblogs.com/findyou/p/5614178.html （search key: `android keycode`）

* 打开通知栏 `driver.open_notifications()`后可根据滑动或返回(`driver.press_keycode(4)`)关闭