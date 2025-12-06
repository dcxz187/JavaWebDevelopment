用idea开发一个java web网站，该网站包括一个可以运行的servlet和一个网页及相关的图片素材。
运行并访问该网站，可以显示网页如样例图1所示。
要求：

1、使用提供的素材图片完成设计。

2、鼠标移动到导航栏的链接上的时候，文字样式发生变化（颜色，大小均可以）

3、编写javascript程序，完成如下功能：
    当点击资源动态中的资源检索关键词的查询按钮时，如果文本框中未输入任何字符，需提示。
    如文本框中输入了文字，则以异步的方式访问（局部刷新网页）网站的servlet,并显示检索信息结果在恰当的位置，检索信息的数据和结构请自行设计。

4、检索文字历史可以保存，下次打开网页的时候，可以看到。

5、补充要求1：css、js分别为独立文件，和网页代码分离，采用外部引用链接到图书馆网页

6、补充要求2：全程手搓，不得采用任何第三方的前端CSS和Javascrip框架

7、不要用附件，直接提交网页源代码和css，js代码，servlet程序的源代码到下面的答题框中。

设计一个html框架，最顶部为图片campus/02/imgs/logo.jpg，紧接着下面是图片campus/02/imgs/bgdanghang3.png，这张图片应该与上一张图片左右对齐，紧接着下面是从左到右依次是图片campus/02/imgs/bgc1.jpg、campus/02/imgs/bgc2.jpg、campus/02/imgs/bgc3.jpg、campus/02/imgs/bgc4.jpg、campus/02/imgs/bgc5.jpg、campus/02/imgs/bggonggao.jpg，每张图之间有一定间隔，这六张图片高度对齐，最左边图片对齐上方图片的最左边，最右边图片对齐上方图片的最右边，紧接着下面是从左到右是图片campus/02/imgs/bggundong.jpg、campus/02/imgs/bgziyuan.jpg，第一张图片的宽度上方前五张图片所占的宽度一样，紧接着下面是图片campus/02/imgs/bgshujuku.jpg，这张图片的宽度与上方图片的宽度对齐，紧接着下面是图片campus/02/imgs/bgcp.jpg，最底部是一个版权文字信息，请按照我上面的描述完成一个html排版