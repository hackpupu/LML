package com.study.spark.datasource

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

/**
  * Created by dltkr on 2017-01-13.
  * Spark가 패키지명을 통해 Datasource를 찾아서 사용할 수 있도록 Defaultsource 구현
  * RelationProvider를 상속받아 createRelationd을 구현
  * DataSourceRegister를 구현하게 되면 Full package명이 아닌 짧은 이름을 사용할 수 있다
  */
class DefaultSource extends RelationProvider with DataSourceRegister {
  override def shortName(): String = "datasource"
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = new StudyRelation(parameters)(sqlContext)
}