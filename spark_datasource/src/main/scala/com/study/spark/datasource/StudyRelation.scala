package com.study.spark.datasource

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by dltkr on 2017-01-13.
  */
class StudyRelation(parameters: Map[String, String])(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan {

  override def schema: StructType = {
    val fields: Array[StructField] = new Array[StructField](3)
    fields.update(0, new StructField("field1", StringType))
    fields.update(1, new StructField("field2", StringType))
    fields.update(2, new StructField("field2", StringType))
    new StructType(fields.asInstanceOf[Array[StructField]])
  }

  override def buildScan(): RDD[Row] = new StudyRDD(sqlContext, schema)
}
