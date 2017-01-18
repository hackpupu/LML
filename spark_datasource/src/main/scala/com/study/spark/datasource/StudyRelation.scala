package com.study.spark.datasource

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by dltkr on 2017-01-13.
  * BaseRelation을 상속받아 스키마 정보를 생성할 수 있도록 schema를 오버라이드
  * TableScan을 상속받아 buildScan을 구현 (데이터소스로부터 데이터를 읽을 수 있도록 RDD[Row]를 구현해야 한다)
  */
class StudyRelation(parameters: Map[String, String])(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan {

  override def schema: StructType = {
    // 학습을 위한 용도이므로, 그냥 정해진 스키마를 생성한다. 실제로는 테이블로부터 스키마를 받아온다든지, 추측하는 등의 작업이 이루어짐
    val fields: Array[StructField] = new Array[StructField](3)
    fields.update(0, new StructField("field1", StringType))
    fields.update(1, new StructField("field2", StringType))
    fields.update(2, new StructField("field2", StringType))
    new StructType(fields.asInstanceOf[Array[StructField]])
  }

  // RDD[Row]의 구현체인 StudyRDD를 만든다.
  override def buildScan(): RDD[Row] = new StudyRDD(sqlContext, schema)
}
