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
/usr/certbot/certbot-auto renew --quiet
echo "-----certbot update end-----"

echo "-----qcloud cdn push begin-----"
java -jar /usr/update_push_cert/qcloud-cdn-https-1.0.jar
echo "-----qcloud cdn push end-----"

echo "-----nginx reload begin-----"
/usr/local/nginx/sbin/nginx -s reload
echo "-----nginx reload end-----"

echo "-----you can see log in /usr/update_push_cert/logs/upc.log-----"
echo ""
echo ""
echo ""