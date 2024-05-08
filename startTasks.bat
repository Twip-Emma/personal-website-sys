@echo off
start "RedisServer" /D "D:\work\jar\redis7" cmd /k "redis-server redis.windows.conf"
start "PersonalWebsiteSysConsul" /D "D:\work\SpringCloud学习\personal-website-sys-consul" cmd /k "run.bat"
pause