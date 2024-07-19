[![Maven Central](https://img.shields.io/maven-central/v/com.houkunlin/system-common-aop-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.houkunlin%22%20AND%20a:%22system-common-aop-starter%22)
[![Java CI with Gradle](https://github.com/houkunlin-starter/system-common-aop-starter/actions/workflows/gradle.yml/badge.svg)](https://github.com/houkunlin-starter/system-common-aop-starter/actions/workflows/gradle.yml)

## 常用的 AOP 注解功能

### 防止重复提交 `PreventRepeatSubmit`

| 参数       | 默认值           | 说明                     |
|----------|---------------|------------------------|
| key      | `""`          | 在某些个特殊业务场景下二次分类的键名     |
| interval | 5             | 间隔时间（单位：秒），小于此时间视为重复提交 |
| message  | 不允许重复提交，请稍候再试 | 提示消息                   |

### 请求限流 `RequestRateLimiter`

| 参数        | 默认值                   | 说明                                                                                                                                                       |
|-----------|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| key       | `""`                  | 在某些个特殊业务场景下二次分类的键名                                                                                                                                       |
| interval  | 60                    | 限流时间，单位：秒                                                                                                                                                |
| limit     | 120                   | 限流次数                                                                                                                                                     |
| useLock   | false                 | 是否使用 Redis 锁。未使用 Redis 锁的情况下，遇到并发场景时实际访问量可能会超出 `limit` 值（具体会超出多少由多种因素决定：服务器性能、网络IO速度等等）。使用锁时可以限制最大访问次数为 `limit`，请求并发数可能会降低，但是会相对均匀的允许请求通过（能拿到锁才有机会成功访问）。 |
| message   | 访问量太多，服务器繁忙，请稍候再试     | 提示消息                                                                                                                                                     |
| limitType | `LimitType.DEFAULT`   | 限流类型： `DEFAULT` `IP` `USER`                                                                                                                              |
| method    | `LimitMethod.METHOD1` | 限流方法： `METHOD1` `METHOD2` `METHOD3`                                                                                                                      |

#### `LimitType`

| 枚举      | 说明                                      |
|---------|-----------------------------------------|
| DEFAULT | 默认策略全局限流                                |
| IP      | 根据请求者IP进行限流                             |
| USER    | 根据请求用户进行限流，使用请求头 `Authorization` 作为用户参数 |

#### `LimitMethod`

| 枚举      | 说明                                         |
|---------|--------------------------------------------|
| METHOD1 | 基于时间范围内记录请求日志的数量统计。使用 Redis ZSet 实现        |
| METHOD2 | 基于滑动窗口的请求数量统计（时间分片统计请求数量）。使用 Redis ZSet 实现 |
| METHOD3 | 基于滑动窗口的请求数量统计（时间分片统计请求数量）。使用 Redis Hash 实现 |

### Excel导出下载 `DownloadExcel`

需要在自己的项目中引入 `EasyExcel` 依赖（例如：`implementation "com.alibaba:easyexcel:4.0.1"`），本工具不包含该依赖。

| 参数               | 默认值                                                                   | 说明                                  |
|------------------|-----------------------------------------------------------------------|-------------------------------------|
| filename         |                                                                       | 文件下载名称                              |
| contentType      | `"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"` | 文件下载头内容                             |
| excelType        | `ExcelTypeEnum.XLSX`                                                  | 详情请查阅 `EasyExcel` 文档                |
| sheetName        | `Sheet1`                                                              | Excel 表格的工作簿名称。详情请查阅 `EasyExcel` 文档 |
| inMemory         | false                                                                 | 详情请查阅 `EasyExcel` 文档                |
| dataClass        | `Object.class`                                                        | 数据的类型对象。详情请查阅 `EasyExcel` 文档        |
| charset          | `"UTF-8"`                                                             | 详情请查阅 `EasyExcel` 文档                |
| password         | `""`                                                                  | 详情请查阅 `EasyExcel` 文档                |
| withTemplate     | `""`                                                                  | 详情请查阅 `EasyExcel` 文档                |
| use1904windowing | false                                                                 | 详情请查阅 `EasyExcel` 文档                |
| useDefaultStyle  | true                                                                  | 详情请查阅 `EasyExcel` 文档                |
| needHead         | true                                                                  | 详情请查阅 `EasyExcel` 文档                |

文件名参数支持 SpEL 表达式（模板）语法，参考如下：

- `@DownloadExcel(filename = "Excel 导出 - #{result.size} 条数据")`
- `@DownloadExcel(filename = "Excel 导出 - #{@dateBean.now()}")`

#### `DownloadExcel#withTemplate` 参数说明

需要自行实现 `DownloadPoiHandler` 接口对象并注入Bean，如果未找到则默认一个实现对象，该默认的对象只支持从 `classpath`
中读取文件，默认用法参考如下：

- `@DownloadExcel(filename = "Excel 导出", withTemplate = "classpath:template.xlsx")`

```java

@Bean
@ConditionalOnMissingBean
public DownloadPoiHandler downloadPoiHandler() {
    return new DownloadPoiHandler() {
        private static final Logger logger = LoggerFactory.getLogger(DownloadPoiHandler.class);

        @Override
        public InputStream getTemplate(@NonNull String templateName) throws IOException {
            if (templateName.isBlank()) {
                return null;
            }
            InputStream inputStream = ClassPathUtil.getResourceAsStream(templateName);
            if (inputStream == null) {
                logger.warn("使用默认的 Excel/Word 模板处理器，不支持读取 ClassPath 之外的文件模板，需要自行实现 DownloadPoiHandler 接口功能。当前读取模板：{}", templateName);
            }
            return inputStream;
        }
    };
}
```

可自行实现 `DownloadPoiHandler` 接口来支持如下场景使用：

- `@DownloadExcel(filename = "Excel 导出", withTemplate = "file:./upload/template.xlsx")`
- `@DownloadExcel(filename = "Excel 导出", withTemplate = "oss:/upload/template.xlsx")`

### Word导出下载 `DownloadWord`

需要在自己的项目中引入 `POI-TL` 依赖（例如：`implementation "com.deepoove:poi-tl:1.12.2"`），本工具不包含该依赖。

| 参数           | 默认值                                                                         | 说明                                          |
|--------------|-----------------------------------------------------------------------------|---------------------------------------------|
| filename     |                                                                             | 文件下载名称                                      |
| contentType  | `"application/vnd.openxmlformats-officedocument.wordprocessingml.document"` | 文件下载头内容                                     |
| withTemplate |                                                                             | Word模板文档，参考 `DownloadExcel#withTemplate` 用法 |

文件名参数支持 SpEL 表达式（模板）语法，参考如下：

- `@DownloadWord(filename = "用户信息 - #{result.name}", withTemplate = "classpath:template.xlsx")`
- `@DownloadWord(filename = "用户合同文档 - #{result.name}", withTemplate = "classpath:template.xlsx")`
- `@DownloadWord(filename = "用户数据报告 - #{@dateBean.now()}", withTemplate = "classpath:template.xlsx")`

### 文件下载 `DownloadFile`

| 参数             | 默认值                          | 说明                                   |
|----------------|------------------------------|--------------------------------------|
| filename       | `""`                         | 单个文件名称，或者压缩包名称                       |
| contentType    | `"application/octet-stream"` | 下载文件的文件内容类型                          |
| source         | `""`                         | 下载资源来源，参考 `DownloadFile#source` 参数说明 |
| forceCompress  | false                        | 强制打包压缩                               |
| compressFormat | `"zip"`                      | 打包压缩类型                               |

文件名参数支持 SpEL 表达式（模板）语法，参考如下：

- `@DownloadFile(filename = "某某文件 - #{result.name}")`
- `@DownloadFile(filename = "某某文件 - #{@dateBean.now()}")`

#### `DownloadFile#source` 参数说明

需要自行实现 `DownloadFileHandler` 接口对象并注入Bean，如果未找到则默认一个实现对象，该默认的对象只支持从 `classpath`
中读取文件，默认用法参考如下：

- `@DownloadFile(source = "classpath:template.xlsx")`

```java

@Bean
@ConditionalOnMissingBean
public DownloadFileHandler downloadFileHandler() {
    return new DownloadFileHandler() {
        private static final Logger logger = LoggerFactory.getLogger(DownloadFileHandler.class);

        @Override
        public InputStream getFileInputStream(@NonNull String filename) throws IOException {
            if (filename.isBlank()) {
                return null;
            }
            InputStream inputStream = ClassPathUtil.getResourceAsStream(filename);
            if (inputStream == null) {
                logger.warn("使用默认的文件下载处理器，不支持读取 ClassPath 之外的文件，需要自行实现 DownloadFileHandler 接口功能。当前读取文件：{}", filename);
            }
            return inputStream;
        }
    };
}
```

可自行实现 `DownloadFileHandler` 接口来支持如下场景使用：

- `@DownloadFile(source = "file:./upload/template.xlsx")`
- `@DownloadFile(source = "oss:/upload/template.xlsx")`
- `@DownloadFile(source = "http://127.0.0.1/template.xlsx")`
- `@DownloadFile(source = "https://127.0.0.1/template.xlsx")`
