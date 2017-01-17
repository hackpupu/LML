package com.study.spark.datasource

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, RelationProvider}

/**
  * Created by dltkr on 2017-01-13.
  */
class DefaultSource extends RelationProvider with DataSourceRegister {
  override def shortName(): String = "datasource"
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = new StudyRelation(parameters)(sqlContext)
}