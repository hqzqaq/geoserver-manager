# geoserver-manager

geoserver api操作库，基于 [geosolutions-it/geoserver-manager](https://github.com/geosolutions-it/geoserver-manager)

在其基础上增加了一些api的调用，以及方便springboot的调用

1. springboot引入此包

   ```yaml
   geoserver:
     #  是否开启配置
     isOpen: true
     url: http://[ip]:[port]/geoserver
     userName: username
     password: password
   ```