# Spark Datasource
Spark Datasource를 개발하는 방법 및 개발된 Datasource를 어떻게 Spark-Shell에서 사용할 수 있는지에 대해 설명합니다.

## Datasource 개발의 큰 흐름
Spark는 `RDD`라는 데이터의 집합에 스키마 정보를 결합하여, `DataFrame`(Spark 2.0부터는 Row자료형의 DataSet이지만 문서의 통일성을 위해 계속해서 DataFrame으로 표기)이라는 데이터로 관리를 하게 됩니다. DataFrame을 생성하게 되면 SQL문을 사용하여 데이터를 읽고 쓸 수 있는 장점이 있습니다. 이러한 DataFrame을 만드는 최소한의 기능으로 아래의 두 가지가 필요하게 됩니다.

* 특정 데이터 소스로부터 데이터를 읽어오는 기능 -> 읽어와서 RDD로 만들기 (Build Scan)
* 읽어온 데이터의 스키마를 정의해줘야 하는 기능 -> 스키마를 어디선가 얻어오거나, 만들어내거나, 아니면 추측하기.

조금 더 깊이 들어가 보자면, 만들어진 DataFrame을 파일에 `저장`하는 기능도 필요하고, `Column Pruning`, `Filter push down`... 등등 여러가지 기능들을 구현해 주어야 되겠지만, 최대한 이해를 돕기 위해 최소한의 소스 코드만을 작성하였습니다.

Datasource는 Spark에 정의된 인터페이스를 구현함으로써, 간단히 개발할 수 있습니다.

## Default Source
스파크는 Datasource 패키지의 이름이 주어지면, `DefaultSource`라는 클래스명을 이용하여 Datasource API에 해당 기능을 추가시켜 주는 방식을 사용하고 있습니다. (`jdbc`, `csv`, `parquet` 등등 기본적으로 포함된 패키지들은 물론 좀 더 간편한 방법으로 접근이 가능합니다)

가장 기본적으로 DefaultSource 클래스는 `RelationProvider` 인터페이스를 상속받아, `createRelation` 메소드를 구현해 줘야 합니다. RelationProvider는 사용자가 전달한 파라미터 정보들을 통해 `BaseRelation`을 생성하게 됩니다. 여기서 `SchemaRelationProvider` 인터페이스를 상속받아 메소드를 구현할 수도 있고, 해당 인터페이스를 구현하게 되면 사용자가 전달한 스키마 정보를 통해 BaseRelation을 생성합니다.

**소스코드의 `DefaultSource.scala`가 이 부분에 해당합니다.**

## Base Relation
`BaseRelation`을 굳이 한글로 번역해 보자면 `기본 관계`쯤이 될 것입니다. 이름에서 느껴지듯이 Datasource로부터 데이터를 읽어온 RDD와 스키마 정보의 관계를 맺어주는 역할을 합니다.  (실제로 데이터를 읽어오는 작업은 하지 않습니다.) 간단히 `schema`를 오버라이드하여 스키마 정보를 만들어 주면 됩니다. `sqlContext`와 `sizeInBytes`, `needConversion` 등 여러 가지를 오버라이드하여, 좀 더 강력한 기능을 구현할 수도 있지만 여기서는 예외로 합니다.

또한 `TableScan` 인터페이스를 구현해야 합니다.
`TableScan` 이라는 이름에서 느껴지듯이 데이터를 읽어오는 역할을 합니다. 해당 인터페이스를 상속받아 `buildScan` 메소드를 구현하면 되며, 해당 메소드에서 `RDD[Row]` 객체를 생성해 줍니다.

**소스코드의 `StudyRelation.scala`가 이 부분에 해당합니다.**

## RDD[Row]
Spark는 앞서 설명한 바와 같이 `RDD`라는 데이터의 집합을 사용하게 됩니다. RDD는 내용의 변경이 불가능한 자료구조이며, 실행계획을 통해 데이터를 읽어오게 됩니다. RDD를 상속받아 `compute` 등의 메소를 오버라이드하여 필요한 RDD를 개발할 수 있습니다.

**소스코드의 `StudyRDD.scala`가 이 부분에 해당합니다.**

## Iterator[Row]
Row형의 자료를 하나씩 읽어올 수 있도록 구현을 하면 됩니다. (일반적인 내용이라 상세한 설명은 하지 않겠습니다.)

**소스코드의 `StudyReader.scala`가 이 부분에 해당합니다.**

## Build
해당 패키지를 빌드하는 방법입니다. `maven`이 설치되어 있어야 합니다.
```
# cd ${PROJECT_HOME}/spark_datasource
# mvn package
# ls -rlt target/
total 80
-rw-r--r--  1 leesak  staff      1  1 18 19:23 classes.-1746931459.timestamp
-rw-r--r--  1 leesak  staff      1  1 18 19:23 test-classes.-484120298.timestamp
drwxr-xr-x  4 leesak  staff    136  1 18 19:23 classes
drwxr-xr-x  3 leesak  staff    102  1 18 19:23 test-classes
-rw-r--r--  1 leesak  staff  15796  1 18 19:23 spark-datasource-1.0-SNAPSHOT.jar
drwxr-xr-x  3 leesak  staff    102  1 18 19:23 maven-archiver
-rw-r--r--  1 leesak  staff  14335  1 18 19:23 spark-datasource-1.0-SNAPSHOT-jar-with-dependencies.jar
drwxr-xr-x  2 leesak  staff     68  1 18 19:23 archive-tmp
```

현재 프로젝트에서는 종속 패키지가 없기 때문에 위의 jar 파일 중 아무 파일이나 사용해도 무관합니다.

## Deploy
`spark`가 이미 설치되어 있다는 가정하에 작성되었습니다. Spark에 Job을 제출시 `--jars` 옵션의 경우 각 Worker로 라이브러리 파일을 전송하여 사용을 하게 됩니다. 이는 어플리케이션 별로 라이브러리가 제출되므로 비효율 적이며, 미리 모든 호스트에 라이브러리를 배포한 후 아래와 같이 spark.driver.extraClassPath, spark.executor.extraClassPath 두 가지 설정을 하는 것을 권장합니다.
```
# vi ${SPARK_HOME}/conf/spark-defaults.conf
spark.driver.extraClassPath ${PACKAGE_PATH}/spark-datasource-1.0-SNAPSHOT-jar-with-dependencies.jar
spark.executor.extraClassPath ${PACKAGE_PATH}/spark-datasource-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Example
```
val spark = SparkSession.builder.appName("study").master("local[*]").getOrCreate()
val df = spark.read.format("datasource").load()
println(df.count())
df.show()
```