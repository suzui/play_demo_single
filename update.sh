#!/bin/bash
ssh demo << remotessh;
cd /data/demo;
sudo -S -u demo git pull << EOF;
demopassword
EOF;
play stop;
play start;
exit;  
remotessh; 
