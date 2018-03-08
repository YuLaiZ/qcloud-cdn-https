# qcloud-cdn-https
用于Certbot生成证书后，将PKCS8私钥转换为PKCS1私钥，并推送证书至腾讯云CDN上进行更新。（附带更新证书、上传证书到CDN、重启Nginx脚本，可添加至Crontab中定时运行）
