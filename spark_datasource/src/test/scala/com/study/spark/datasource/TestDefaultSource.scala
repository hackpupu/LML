package com.study.spark.datasource

import org.apache.spark.sql.SparkSession

/**
  * Created by dltkr on 2017-01-13.
  */
object TestDefaultSource {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("study").master("local[*]").getOrCreate()
    val df = spark.read.format("com.study.spark.datasource").load()
    df.printSchema()
    df.show()
    println(df.count())
    df.collect.foreach(println)
  }
}