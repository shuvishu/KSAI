package ksai.core.util

import ksai.math.DifferentiableMultivariateFunction
import ksai.util.NumericFunctions
import org.scalatest.{Matchers, WordSpec}

class NumericFunctionsTest extends WordSpec with Matchers {

  "NumericFunctions" should {

    "be able to test minimisation with 5 args (L-BFGS)" in {
      println("L-BFGS")

      val func = new DifferentiableMultivariateFunction {
        override def f(x: Array[Double]): Double = {
          (1 to x.length).filter(_ % 2 != 0).foldLeft(0.0){(sum, j) =>
            val t1 = 1E0 - x(j -1)
            val t2 = 1E1 * (x(j) - x(j - 1) * x(j - 1))
            sum + t1 * t1 + t2 * t2
          }
        }

        /**
          * Compute the value and gradient of the function at x.
          */
        override def f(x: Array[Double], gradient: Array[Double]): Double = {
          (1 to x.length).filter(_ % 2 != 0).foldLeft(0.0){(sum, j) =>
            val t1 = 1E0 - x(j - 1)
            val t2 = 1E1 * (x(j) - x(j - 1) * x(j - 1))
            gradient(j + 1 - 1) = 2E1 * t2
            gradient(j - 1) = -2E0 * (x(j - 1) * gradient(j - 1 + 1) + t1)
            sum + t1 * t1 + t2 * t2
          }
        }
      }

      val x = new Array[Double](100)
      for (j <- 1 to x.length by 2) {
        x(j - 1) = -1.2E0
        x(j + 1 - 1) = 1E0
      }

      val result =  NumericFunctions.min(func, 5, x, 0.0001, 200)
      result shouldEqual 3.2760183604E-14 +- 1E-15
    }

    "be able to test minimization with 4 args (BFGS)" in {
      println("BFGS")

      val func = new DifferentiableMultivariateFunction {
        override def f(x: Array[Double]): Double = {
          (1 to x.length).filter(_ % 2 != 0).foldLeft(0.0){(sum, j) =>
            val t1 = 1E0 - x(j -1)
            val t2 = 1E1 * (x(j) - x(j - 1) * x(j - 1))
            sum + t1 * t1 + t2 * t2
          }
        }

        /**
          * Compute the value and gradient of the function at x.
          */
        override def f(x: Array[Double], gradient: Array[Double]): Double = {
          (1 to x.length).filter(_ % 2 != 0).foldLeft(0.0){(sum, j) =>
            val t1 = 1E0 - x(j - 1)
            val t2 = 1E1 * (x(j) - x(j - 1) * x(j - 1))
            gradient(j + 1 - 1) = 2E1 * t2
            gradient(j - 1) = -2E0 * (x(j - 1) * gradient(j - 1 + 1) + t1)
            sum + t1 * t1 + t2 * t2
          }
        }
      }

      val x = new Array[Double](100)
      for (j <- 1 to x.length by 2) {
        x(j - 1) = -1.2E0
        x(j + 1 - 1) = 1E0
      }

      val result =  NumericFunctions.min(func, x, 0.0001, 200)
      result shouldEqual 2.95793E-10 +- 1E-15
    }

  }

}
