package com.charlieworld.housing

import monix.execution.Scheduler

trait AppSuite {
  implicit protected val s: Scheduler
}
