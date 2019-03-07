package com.backwards.adt

import scala.language.implicitConversions
import shapeless._
import shapeless.ops.record.Merger

trait Merge[A, B] {
  def apply(a: A, b: B): A
}

object Merge {
  implicit def adtMerge[A, B, RA <: HList, RB <: HList](
    implicit aGen: LabelledGeneric.Aux[A, RA],
    bGen: LabelledGeneric.Aux[B, RB],
    merger: Merger.Aux[RA, RB, RA]
  ): Merge[A, B] =
    (a: A, b: B) => aGen from merger(aGen to a, bGen to b)


  trait Ops[A] {
    def merge[B](b: B)(implicit merge: Merge[A, B]): A
  }

  object ops {
    implicit def toMerge[A](a: A): Ops[A] = new Ops[A] {
      def merge[B](b: B)(implicit merge: Merge[A, B]): A = merge(a, b)
    }
  }
}