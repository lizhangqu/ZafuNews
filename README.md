农大新闻网
==========

 - 数据来源：爬自http://news.zafu.edu.cn/，通过Jsoup解析网页内容，提取关键信息，支持分页，搜索等功能

 - 使用到的库。
  - CorePage，页面切换框架，见https://github.com/lizhangqu/CorePage；
  - OkHttp，网络层框架；
  - Picasso，图片加载框架；
  - Ormlite，数据持久层框架，并进行二次封装；
  - Jsoup，数据解析框架；
  - Gson，json数据解析，用于解析天气预报接口
  - 友盟统计、更新、分享等第三方sdk；


 - 特点

   - 整个App页面全部由Fragment组成，主体App（不包含第三方）只有一个Activity，作为Fragment的容器，详见博客http://blog.csdn.net/sbsujjbcy/article/details/47060211

 - 包结构

  ![https://raw.githubusercontent.com/lizhangqu/ZafuNews/version_2_0/app/picture/package.png](https://raw.githubusercontent.com/lizhangqu/ZafuNews/version_2_0/app/picture/package.png)
 
