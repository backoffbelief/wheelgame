生命周期绑定在generate-sources下，只要执行maven generate-sources就可以了
proto文件按照maven的规范放在src/main/proto文件夹下，最终生成的java文件在src/main/gen-java下

有自动生成的文件就要有自动删除