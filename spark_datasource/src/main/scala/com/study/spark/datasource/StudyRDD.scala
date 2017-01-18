package com.study.spark.datasource

import org.apache.spark.{Partition, TaskContext}
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by dltkr on 2017-01-13.
  * RDD[Row]를 상속받아 compute, getPartitions를 구현.
  * compute 메소드에서 데이터를 Row단위로 읽어올 수 있도록 Iterator를 생성해 준다.
  */
class StudyRDD(sqlContext: SQLContext, schema: StructType) extends RDD[Row](sqlContext.sparkContext, deps=Nil) {
  @DeveloperApi
  override def compute(split: Partition, context: TaskContext): Iterator[Row] = new StudyReader(context, schema, split)

  // 학습을 위한 용도이므로 2개의 파티션이 있다고 가정한다.
  // 각 Executor에 파티션이 하나씩 할당된다. 여기서는 파티션을 2개 만들어 주므로, 동시에 동작할 수 있는 Executor의 수는 2개이다.
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
