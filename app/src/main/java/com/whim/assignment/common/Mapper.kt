package com.whim.assignment.common

interface Mapper<R, D> {
    fun mapFrom(type: R): D
}