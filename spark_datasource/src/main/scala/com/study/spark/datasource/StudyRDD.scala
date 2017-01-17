package com.study.spark.datasource

import org.apache.spark.{Partition, TaskContext}
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by dltkr on 2017-01-13.
  */
class StudyRDD(sqlContext: SQLContext, schema: StructType) extends RDD[Row](sqlContext.sparkContext, deps=Nil) {
  @DeveloperApi
  override def compute(split: Partition, context: TaskContext): Iterator[Row] = new StudyReader(context, schema, split)

  override protected def getPartitions: Array[Partition] = {
    val arr: Array[Partition] = new Array[Partition](2)
    arr.update(0, new Partition() {
      override def index: Int = 0
    })
    arr.update(1, new Partition() {
      override def index: Int = 1
    })
    arr
  }
}
