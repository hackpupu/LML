package com.study.spark.datasource

import org.apache.spark.{Partition, TaskContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

/**
  * Created by dltkr on 2017-01-13.
  * 데이터를 한 Row씩 읽어올 수 있는 Reader. Iterator를 구현하면 된다.
  */
class StudyReader(context: TaskContext, schema: StructType, split: Partition) extends Iterator[Row] {
  private[this] var counter: Int = 0

  // Task가 완료되면 마지막에 close를 호출하도록 한다.
  if(context != null) {
    context.addTaskCompletionListener(context => close())
  }

  // 100개의 Row가 있다고 가정
  override def hasNext: Boolean = {
    if(counter < 100) {
      true
    } else {
      false
    }
  }

  // 1개의 Row씩 가져온다.
  override def next(): Row = {
    if(!hasNext) {
      throw new NoSuchElementException("End of stream")
    }
    counter += 1
    Row(split.index + " field1 " + counter, "field2 " + counter, "field3: " + counter)
  }

  // close해야 할 객체가 있다면 여기서 close한다.
  def close() = println("closed")
}
