package com.study.spark.datasource

import org.apache.spark.{Partition, TaskContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

/**
  * Created by dltkr on 2017-01-13.
  */
class StudyReader(context: TaskContext, schema: StructType, split: Partition) extends Iterator[Row] {
  private[this] var counter: Int = 0

  if(context != null) {
    context.addTaskCompletionListener(context => close())
  }
  override def hasNext: Boolean = {
    if(counter < 100) {
      true
    } else {
      false
    }
  }

  override def next(): Row = {
    if(!hasNext) {
      throw new NoSuchElementException("End of stream")
    }
    counter += 1
    Row(split.index + " field1 " + counter, "field2 " + counter, "field3: " + counter)
  }

  def close() = println("closed")
}
