#	qcloud-cdn-https
用于Certbot生成证书后，将PKCS8私钥转换为PKCS1私钥，并推送证书至腾讯云CDN上进行更新，以保持服务器的https证书有效。（附带更新证书、上传证书到CDN、重启Nginx脚本，可添加至Crontab中定时运行）

#	使用限制

本Java程序是用于将Certbot生成的PKCS8证书，转换为腾讯云需要的PKCS1证书，需要自行安装Certbot并配置，安装教程可参考 [使用 Certbot 为服务器安装免费的 https 证书](https://yulaiz.com/archives/2018/347)

#	使用的框架与资源

- Java代码部分

  - httpclient
  - common-io
  - owner
  - log4j

- 脚本部分

  - shell基础
  - crontab


#	安装与使用

##	1.	qcloud-cdn-https-1.0.jar

下载 [/bin/qcloud-cdn-https-1.0.jar](https://github.com/YuLaiZ/qcloud-cdn-https/blob/master/bin/qcloud-cdn-https-1.0.jar) 或者 通过源码编译 ``mvn compile package install`` 获取

下载 [/src/main/resources/config.properties](https://github.com/YuLaiZ/qcloud-cdn-https/blob/master/src/main/resources/config.properties) 与 qcloud-cdn-https-1.0.jar 存放在同级目录下

编辑 ``config.properties`` 文件：

```properties
#qcloud
#腾讯云cdn中配置的域名，需要上传几个就配置几个，以逗号分隔
qcloud.hosts=yulaiz.com, www.yulaiz.com
#腾讯云账号的API的SecretId
qcloud.secret.id=
#腾讯云账号的API的SecretKey
qcloud.secret.key=
#cert
#file.public.cert=C:\\Users\\YuLai\\Desktop\\yulaiz.com\\live\\fullchain.pem
#file.private.cert=C:\\Users\\YuLai\\Desktop\\yulaiz.com\\live\\privkey.pem
#Certbot生成的公钥路径
file.public.cert=/etc/letsencrypt/live/yulaiz.com/fullchain.pem
#Certbot生成的私钥路径
file.private.cert=/etc/letsencrypt/live/yulaiz.com/privkey.pem
#openssl
#支持windows环境与Linux环境
#windows环境需要配置openssl命令位置，与cmd命令相同
#default value 'openssl.exe'
#openssl.windows=C:\\Users\\YuLai\\Desktop\\openssl\\bin\\openssl.exe
#Linux环境需要安装openssl环境
#default value 'openssl'
#openssl.linux=openssl
```

其中腾讯云账号的API的 SecretId 和 SecretKey 需要在 [腾讯云控制台-云API密钥](https://console.cloud.tencent.com/capi) 上申请，需要注意的是，腾讯云的CDN API不支持子账户操作，只能使用主账号的 SecretId 和 SecretKey 了。

将 ``qcloud-cdn-https-1.0.jar`` 与 ``config.properties`` 上传至服务器

```shell
cd /usr
mkdir update_push_cert
#...上传文件 文件目录可自定义
```

##	2.	update_push_cert.sh

下载 [/bin/update_push_cert.sh](https://github.com/YuLaiZ/qcloud-cdn-https/blob/master/bin/update_push_cert.sh) 并编辑文件

```shell
#!/bin/bash
echo ""
echo ""
echo ""
echo "-----update_push_cert.sh-----"
echo "-----create by: YuLaiZ-----"
DATE=$(date +%Y-%m-%d)
TIME=$(date +%H:%M:%S)
echo "-----update time:"$DATE $TIME"-----"

echo "-----certbot update begin-----"
#certbot路径
/usr/certbot/certbot-auto renew --quiet
echo "-----certbot update end-----"

echo "-----qcloud cdn push begin-----"
#jar包存放路径
java -jar /usr/update_push_cert/qcloud-cdn-https-1.0.jar
echo "-----qcloud cdn push end-----"


#echo "-----nginx reload begin-----"
#仅reload本机测试失败，必须先stop再start
#/usr/local/nginx/sbin/nginx -s reload
#echo "-----nginx reload end-----"
echo "-----nginx reload begin-----"
#nginx路径
/usr/local/nginx/sbin/nginx -s stop
#nginx路径, nginx.conf路径
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
echo "-----nginx reload end-----"
#log日志在配置crontab定时任务的是会指定
echo "-----you can see log in /usr/update_push_cert/logs/upc.log-----"
echo ""
echo ""
echo ""
```

将 ``update_push_cert.sh`` 上传至服务器，并修改文件权限

```shell
cd /usr/update_push_cert
#...上传文件 文件目录可自定义
chmod 755 update_push_cert.sh
```

##	3.	Crontab定时任务配置

登录Linux输入 ``crontab -e`` 以进入任务配置模式（VIM模式）

在最下新增一条记录

```shell
0 4 * * 1 /usr/update_push_cert/update_push_cert.sh >> /usr/update_push_cert/logs/upc.log
```

上面的意思为每周一凌晨4点运行 ``/usr/update_push_cert/update_push_cert.sh`` 脚本，日志保存在 ``/usr/update_push_cert/logs/upc.log`` 中

保存后再输入 ``service crond reload`` 以重新载入配置

##	4.	测试

可以会手动执行命令 ``/usr/update_push_cert/update_push_cert.sh >> /usr/update_push_cert/logs/upc.log`` 看看是否执行正常
